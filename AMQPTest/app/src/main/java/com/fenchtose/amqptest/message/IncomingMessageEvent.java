package com.fenchtose.amqptest.message;

/**
 * Created by Jay Rambhia on 07/03/15.
 */
public class IncomingMessageEvent {

    private String message;

    public IncomingMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
