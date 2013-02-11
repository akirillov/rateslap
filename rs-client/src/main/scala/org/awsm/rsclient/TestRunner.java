package org.awsm.rsclient;

import org.awsm.rsclient.client.RateSlapClient;
import org.awsm.rsclient.exception.ConnectionException;
import org.awsm.rsclient.exception.InvalidRequestParameterException;
import org.awsm.rsclient.exception.ResponseException;
import org.awsm.rscommons.StatsResponse;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by: akirillov
 * Date: 2/7/13
 */
public class TestRunner {
    public static void main(String ... args) throws ResponseException, ConnectionException, ParseException {
        try {
            RateSlapClient client = new RateSlapClient("http://89.175.19.202:9000/rpc.json");

            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
            StatsResponse respo = client.getAppAnnieGamesStats("Cut the Rope", "appstore", "ranks", Arrays.asList(format.parse("2012-01-01"), format.parse("2012-01-02")), new HashSet<String>(Arrays.asList("United States", "Russia")), "user", "secret");

            System.out.println(respo);
            
        } catch (InvalidRequestParameterException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }
}
