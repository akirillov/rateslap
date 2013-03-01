package org.awsm.rsclient;

import org.awsm.rsclient.client.RateSlapClient;
import org.awsm.rsclient.exception.ConnectionException;
import org.awsm.rsclient.exception.InvalidRequestParameterException;
import org.awsm.rsclient.exception.ResponseException;
import org.awsm.rscommons.StatsResponse;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by: akirillov
 * Date: 2/7/13
 */
public class TestRunner {
    public static void main(String ... args) throws ResponseException, ConnectionException, ParseException {
        try {
            RateSlapClient client = new RateSlapClient("http://localhost:9000/rpc.json");

            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
            String base = "2012-03-";

            List<Date> dates = new ArrayList<Date>();
            
            for(int i=0; i<31; i++){
                dates.add(format.parse(base+(i<10?"0"+i:i)));
            }
            
            
            StatsResponse respo = client.getAppAnnieGamesStats("Cut the Rope", "appstore", "ranks", dates, new HashSet<String>(Arrays.asList("United States", "Russia")), "user", "secret");

            System.out.println(respo);

        } catch (InvalidRequestParameterException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }



}
