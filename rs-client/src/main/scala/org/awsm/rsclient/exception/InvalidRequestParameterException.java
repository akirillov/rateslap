package org.awsm.rsclient.exception;

/**
 * Created by: akirillov
 * Date: 2/7/13
 */
public class InvalidRequestParameterException extends Exception{
    public InvalidRequestParameterException() {
    }

    public InvalidRequestParameterException(String message) {
        super(message);
    }

    public InvalidRequestParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
