package org.awsm.rsclient.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.awsm.rsclient.JavaToScalaConverter;
import org.awsm.rsclient.JsonBuilder;
import org.awsm.rsclient.exception.ConnectionException;
import org.awsm.rsclient.exception.InvalidRequestParameterException;
import org.awsm.rsclient.exception.ResponseException;
import org.awsm.rsclient.json.RSRequest;
import org.awsm.rsclient.json.RSResponse;
import org.awsm.rscommons.StatsRequest;
import org.awsm.rscommons.StatsResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Main RateSlap Service Client providing Java API for interaction with service.
 */
public class RateSlapClient {
    private URI uri;

    /**
     * Single constructor initializing URI when invoked
     * @param URI - String representation of URI
     * @throws URISyntaxException - thrown in case of incorrect URI
     */
    public RateSlapClient(String URI) throws URISyntaxException {
        this.uri = new URI(URI);
    }


    /**
     * Main method encapsulating client-server interaction logic.
     * @param request - {@link RSRequest} object representing JSON-RPC request wrapper
     * @return {@link org.awsm.rscommons.StatsResponse} unwrapped stats response object
     * @throws ConnectionException
     * @throws ResponseException
     */
    private StatsResponse getResponse(RSRequest request) throws ConnectionException, ResponseException {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        try{
            HttpPost postRequest = new HttpPost(uri);

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
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**
     * Simple null or empty value validation method
     * @param args - {@link String} args to be checked
     * @throws org.awsm.rsclient.exception.InvalidRequestParameterException in case of null or empty argument
     */
    private void validate(String ... args) throws InvalidRequestParameterException {
        for(String arg: args){
            if((arg == null)||("".equals(arg))) throw new InvalidRequestParameterException("One of the method parameters is null or empty. No payload has been created.");
        }
    }

    /**
     * Method encapsulates creation of {@link org.awsm.rscommons.StatsRequest} and sends it to {@link #getResponse(org.awsm.rsclient.json.RSRequest)}
     * @param application - application name
     * @param store - store name
     * @param rankType - type of rank. For now the only possible rankType is "ranks"
     * @param dates - {@link List<String>} of dates
     * @param countries - {@link Set<String>} of countries
     * @param username - AppAnnie username
     * @param password - AppAnnie password
     * @param o - {@link Object} o is necessary because of collection's generics type erasure to distinguish this method from {@link #getAppAnnieGamesStats(String, String, String, java.util.List, java.util.Set, String, String)}. Object o is never used.
     * @return {@link org.awsm.rscommons.StatsResponse} object
     * @throws org.awsm.rsclient.exception.InvalidRequestParameterException
     * @throws ResponseException
     * @throws ConnectionException
     */
    private StatsResponse getAppAnnieGamesStats(String application, String store, String rankType, List<String> dates, Set<String> countries, String username, String password, Object o) throws InvalidRequestParameterException, ResponseException, ConnectionException {
        validate(application, store,rankType, username, password);
        if(dates.isEmpty()) throw new InvalidRequestParameterException("Dates are empty. No payload has been created.");
        if(countries.isEmpty()) throw new InvalidRequestParameterException("Countries are empty. No payload has been created.");

        RSRequest request = new RSRequest("2.0", ServiceMethods.APPANNIE_GET_GAMES_STATS.toString(),
                new StatsRequest(application, store, rankType,
                        JavaToScalaConverter.convertList(dates),
                        JavaToScalaConverter.convertSet(countries),
                        org.awsm.rscommons.AuthObject.create(username, password)), System.currentTimeMillis()+"");

        return getResponse(request);
    }


    /**
     * Overloaded public method for getting stats. Invokes private {@link #getAppAnnieGamesStats(String, String, String, java.util.List, java.util.Set, String, String, Object o)} inside
     * @param application - application name
     * @param store - store name
     * @param rankType - type of rank. For now the only possible rankType is "ranks"
     * @param dates - {@link List<Date>} of dates
     * @param countries - {@link Set<String>} of countries
     * @param username - AppAnnie username
     * @param password - AppAnnie password
     * @return {@link org.awsm.rscommons.StatsResponse} object
     * @throws org.awsm.rsclient.exception.InvalidRequestParameterException
     * @throws ResponseException
     * @throws ConnectionException
     */
    public StatsResponse getAppAnnieGamesStats(String application, String store, String rankType, List<Date> dates, Set<String> countries, String username, String password) throws ResponseException, InvalidRequestParameterException, ConnectionException {
        List<String> stringDates = new ArrayList<String>(dates.size());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        for(Date date: dates){
            stringDates.add(format.format(date));
        }

        return getAppAnnieGamesStats(application, store, rankType, stringDates, countries, username, password, null);
    }


    /**
     * Overloaded alternative for {@link #getAppAnnieGamesStats(String, String, String, java.util.List, java.util.Set, String, String)} with single date
     */
    public StatsResponse getAppAnnieGamesStats(String application, String store, String rankType, Date date, Set<String> countries, String username, String password) throws InvalidRequestParameterException, ResponseException, ConnectionException {
        return getAppAnnieGamesStats(application, store, rankType, Arrays.asList(date), countries, username, password);
    }

    /**
     * Overloaded alternative for {@link #getAppAnnieGamesStats(String, String, String, java.util.List, java.util.Set, String, String)} with single country
     */
    public StatsResponse getAppAnnieGamesStats(String application, String store, String rankType, List<Date> dates, String country, String username, String password) throws InvalidRequestParameterException, ResponseException, ConnectionException {
        return getAppAnnieGamesStats(application, store, rankType, dates, new HashSet<String>(Arrays.asList(country)), username, password);
    }

    /**
     * Overloaded alternative for {@link #getAppAnnieGamesStats(String, String, String, java.util.List, java.util.Set, String, String)} with single date and single country
     */
    public StatsResponse getAppAnnieGamesStats(String application, String store, String rankType, Date date, String country, String username, String password) throws InvalidRequestParameterException, ResponseException, ConnectionException {
        return getAppAnnieGamesStats(application, store, rankType, Arrays.asList(date), new HashSet<String>(Arrays.asList(country)), username, password);
    }
}
