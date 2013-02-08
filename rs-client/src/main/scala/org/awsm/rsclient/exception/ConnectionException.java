package org.awsm.rsclient.exception;

/**
 * Created by: akirillov
 * Date: 2/7/13
 */
public class ConnectionException extends Exception{
    public ConnectionException() {
    }

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
