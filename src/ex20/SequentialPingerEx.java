package ex20;

/*
 * Code taken from 
 * http://crunchify.com/how-to-get-ping-status-of-any-http-end-point-in-java/
 */
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SequentialPingerEx implements Callable<String> {
    private String url;
    
    private SequentialPingerEx(String url) {
        this.url = url;
    }


    public static void main(String args[]) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        long start = System.nanoTime();

        List<Future<String>> list = new ArrayList<Future<String>>();

        String[] hostList = {"http://crunchify.com", "http://yahoo.com",
            "http://www.ebay.com", "http://google.com",
            "http://www.example.co", "https://paypal.com",
            "http://bing.com/", "http://techcrunch.com/",
            "http://mashable.com/", "http://thenextweb.com/",
            "http://wordpress.com/", "http://cphbusiness.dk/",
            "http://example.com/", "http://sjsu.edu/",
            "http://ebay.co.uk/", "http://google.co.uk/",
            "http://www.wikipedia.org/",
            "http://dr.dk", "http://pol.dk", "https://www.google.dk",
            "http://phoronix.com", "http://www.webupd8.org/",
            "https://studypoint-plaul.rhcloud.com/", "http://stackoverflow.com",
            "http://docs.oracle.com", "https://fronter.com",
            "http://imgur.com/", "http://www.imagemagick.org"
        };
        
        
        for (int i = 0; i < hostList.length; i++) {
            Future<String> future = executor.submit(new SequentialPingerEx(hostList[i]));
            //add Future to the list, we can get return value using Future
            list.add(future);

        }
        for (Future<String> fut : list) {
                System.out.println(fut.get());
        }
        //shut down the executor service now
        executor.shutdown();
        long slut = System.nanoTime();
        System.out.println(slut - start);
    }

    @Override
    public String call() throws Exception {
        String result = "Error";
        try {
            URL siteURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) siteURL
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();
            if (code == 200) {
                result = "Green";
            }
            if (code == 301) {
                result = "Redirect";
            }
        } catch (Exception e) {
            result = "->Red<-";
        }
        return url + ": " + result;
    }

}
