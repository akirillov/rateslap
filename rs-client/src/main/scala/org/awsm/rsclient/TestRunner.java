package org.awsm.rsclient;

import org.awsm.rsclient.client.RateSlapClient;
import org.awsm.rsclient.exception.ConnectionException;
import org.awsm.rsclient.exception.InvalidRequestParameter;
import org.awsm.rsclient.exception.ResponseException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by: akirillov
 * Date: 2/7/13
 */
public class TestRunner {
    public static void main(String ... args) throws ResponseException, ConnectionException {
        try {
            RateSlapClient client = new RateSlapClient("http://127.0.0.1:9000/rpc.json");
                                   /* application = "Cut the Rope",
        store = "appstore",
        rankType = "ranks",
        dates = List("2012-01-01", "2012-02-02"),
        countries = Set("United States", "Russia"),
        authObject = AuthObject("user", "secret")*/
            client.getAppAnnieGamesStats("Cut the Rope", "appstore", "ranks", new ArrayList<String>(Arrays.asList("2012-01-01", "2012-01-02")), new HashSet<String>(Arrays.asList("United States", "Russia")),"", "");

        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidRequestParameter invalidRequestParameter) {
            invalidRequestParameter.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
}
