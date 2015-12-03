package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;


/**
 * Created by Dmytro_Kovalskyi on 03.12.2015.
 */
public class Speeker {

    public static void main(String[] args) throws Exception {
        String url = createLink();//"http://www.google.com/search?q=httpClient";

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);

        System.out.println("Response Code : "
            + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
            new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result);
    }

    private static String createLink(){
        String link = "http://api.microsofttranslator.com/V2/Ajax.svc/Translate" +
            "?appId=" + URLEncoder.encode("Liberty") + "YMQOiV+mxV6a2V5vyMtjV0BbOSdIEOx+luArs99kUk0=" +
            "&from=en"+
            "&to=uk" +
            "&text=" + URLEncoder.encode("Hello world");
        return link;
    }
}
