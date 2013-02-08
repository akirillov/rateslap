package org.awsm.rsclient.client;

/**
 * Created by: akirillov
 * Date: 2/5/13
 */
public enum ServiceMethods {
    APPANNIE_GET_GAMES_STATS("getGamesStats");


    private String methodName;

    ServiceMethods(String s) {
        this.methodName = s;
    }

    @Override
    public String toString() {
        return methodName;
    }
}
