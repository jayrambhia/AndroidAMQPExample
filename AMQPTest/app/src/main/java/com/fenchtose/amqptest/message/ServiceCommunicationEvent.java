package com.fenchtose.amqptest.message;

/**
 * Created by Jay Rambhia on 07/03/15.
 */
public class ServiceCommunicationEvent {

    public String message;

    public ServiceCommunicationEvent(String message) {
        this.message = message;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
