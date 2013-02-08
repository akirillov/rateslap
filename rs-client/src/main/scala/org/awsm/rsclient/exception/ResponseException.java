package org.awsm.rsclient.exception;

/**
 * Created by: akirillov
 * Date: 2/8/13
 */
public class ResponseException extends Exception{
    public ResponseException() {
    }

    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
