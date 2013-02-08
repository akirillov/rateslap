package org.awsm.rsclient.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.awsm.rsclient.JavaToScalaConverter;
import org.awsm.rsclient.JsonBuilder;
import org.awsm.rsclient.exception.ConnectionException;
import org.awsm.rsclient.exception.InvalidRequestParameter;
import org.awsm.rsclient.exception.ResponseException;
import org.awsm.rsclient.json.RSRequest;
import org.awsm.rsclient.json.RSResponse;
import org.awsm.rscommons.StatsRequest;
import org.awsm.rscommons.StatsResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * Created by: akirillov
 * Date: 2/5/13
 */
public class RateSlapClient {
    private URL url;

    public RateSlapClient(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    private StatsResponse getAppAnnieGamesStats(RSRequest request){
        // String req = JsonBuilder.buildJSONRequest(request, ServiceMethods.APPANNIE_GET_GAMES_STATS.toString);





        return null;
    }

    private StatsResponse getResponse(RSRequest request) throws ConnectionException, ResponseException {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        try{
            HttpPost postRequest = new HttpPost(url.toURI());

            StringEntity input = new StringEntity(JsonBuilder.buildJSONRequest(request));
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse httpResponse = httpClient.execute(postRequest);

            if (httpResponse.getStatusLine().getStatusCode() != 200)
                throw new ConnectionException("Failed : HTTP error code : " + httpResponse.getStatusLine().getStatusCode());

            BufferedReader reader = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));


            StringBuilder builder = new StringBuilder();
            String aux = "";

            while ((aux = reader.readLine()) != null) {
                builder.append(aux);
            }


            RSResponse response = JsonBuilder.buildResponseFromJSON(builder.toString());

            if(response.error()!=null) throw new ResponseException("Error occurred during interaction with server: "+response.error());
            else return response.result();

        } catch (UnsupportedEncodingException e) {
            throw new ConnectionException("Error occurred during connection.", e);
        } catch (ClientProtocolException e) {
            throw new ConnectionException("Error occurred during connection.", e);
        } catch (IOException e) {
            throw new ConnectionException("Error occurred during connection.", e);
        } catch (URISyntaxException e) {
            throw new ConnectionException("Error occurred during connection.", e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    private void validate(String ... args) throws InvalidRequestParameter {
        for(String arg: args){
            if((arg == null)||("".equals(arg))) throw new InvalidRequestParameter("One of the method parameters is null or empty. No payload has been created.");
        }
    }

    public StatsResponse getAppAnnieGamesStats(String application, String store, String rankType, List<String> dates, Set<String> countries, String username, String password) throws InvalidRequestParameter, ResponseException, ConnectionException {
        validate(application, store,rankType, username, password);
        if(dates.isEmpty()) throw new InvalidRequestParameter("Dates are empty. No payload has been created.");
        if(countries.isEmpty()) throw new InvalidRequestParameter("Countries are empty. No payload has been created.");

        RSRequest request = new RSRequest("2.0", ServiceMethods.APPANNIE_GET_GAMES_STATS.toString(),
                new StatsRequest(application, store, rankType,
                        JavaToScalaConverter.convertList(dates),
                        JavaToScalaConverter.convertSet(countries),
                        org.awsm.rscommons.AuthObject.create(username, password)), System.currentTimeMillis()+"");

        return getResponse(request);
    }


    //TODO: 1. javadoc 2. override getAppAnnieGamesStats(...) to work with Calendar 3. Write some tests 4. create github README


}
