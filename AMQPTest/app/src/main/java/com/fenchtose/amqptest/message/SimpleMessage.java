package com.fenchtose.amqptest.message;

import com.fenchtose.amqptest.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Jay Rambhia on 07/03/15.
 */
public class SimpleMessage {

    private String message;
    private Date timestamp;
    private String channel;

    public SimpleMessage(JSONObject jsondata) throws JSONException {
        this.message = jsondata.getString("message");
        this.channel = jsondata.getString("channel");
        String timestampStr = jsondata.getString("timestamp");
        this.timestamp = DateUtils.getDate(timestampStr);
    }

    private SimpleMessage(String message, String channel, Date timestamp) {
        this.message = message;
        this.channel = channel;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
