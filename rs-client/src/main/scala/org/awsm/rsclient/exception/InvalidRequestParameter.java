package org.awsm.rsclient.exception;

/**
 * Created by: akirillov
 * Date: 2/7/13
 */
public class InvalidRequestParameter extends Exception{
    public InvalidRequestParameter() {
    }

    public InvalidRequestParameter(String message) {
        super(message);
    }

    public InvalidRequestParameter(String message, Throwable cause) {
        super(message, cause);
    }
}
