package com.fenchtose.amqptest;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.fenchtose.amqptest.message.IncomingMessageEvent;
import com.fenchtose.amqptest.message.ServiceCommunicationEvent;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class ReceiverService extends Service {

    private static final String HOSTNAME = "192.168.1.16"; // Your local/remote server address
    private static final String EXCHANGE_NAME = "group_messages";
    private static final String TAG = "ReceiverService";

    private ArrayList<String> subscribedChannels;
    private Thread subscribeThread;
    private ConnectionFactory factory;
    private Handler incomingMessageHandler;

    private EventBus eventBus;

    public ReceiverService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate called");

        eventBus = EventBus.getDefault();

        incomingMessageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("json_message");
                deliverMessage(message);
                // do something here
            }
        };

        subscribedChannels = new ArrayList<String>();
        subscribedChannels.add("channel_2");
        subscribedChannels.add("channel_3");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand called");

        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }

        if (factory == null) {
            setupConnectionFactory();
            setupSubscription(incomingMessageHandler);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy called");
        eventBus.unregister(this);
        subscribeThread.interrupt();
        super.onDestroy();
    }

    private void setupConnectionFactory() {
        factory = new ConnectionFactory();
        factory.setHost(HOSTNAME);
        factory.setUsername("android");
        factory.setPassword("android");
    }

    private void setupSubscription(final Handler handler) {
        if (subscribeThread != null) {
            subscribeThread.interrupt();
            subscribeThread.start();
            return;
        }

        subscribeThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while(true) {
                    try {
                        Connection connection = factory.newConnection();
                        Channel channel = connection.createChannel();
                        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

                        String queueName = channel.queueDeclare().getQueue();

                        // bind queue to channels
                        for(int i=0; i<subscribedChannels.size(); i++) {
                            channel.queueBind(queueName, EXCHANGE_NAME, subscribedChannels.get(i));
                        }

                        QueueingConsumer consumer = new QueueingConsumer(channel);
                        channel.basicConsume(queueName, true, consumer);

                        while(true) {
                            try {
                                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                                String routingKey = delivery.getEnvelope().getRoutingKey();
                                String message = new String(delivery.getBody());

                                Log.i(TAG, "[r] " + message);

                                Message msg = handler.obtainMessage();
                                Bundle bundle = new Bundle();
                                bundle.putString("json_message", message);
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            } catch(InterruptedException ie) {
                                ie.printStackTrace();
                                return;
                            }
                        }
                    } catch(Exception e) {
                        if (e.getClass().equals(InterruptedException.class)) {
                            Log.e(TAG, "thread interrupted");
                            break;
                        }

                        Log.e(TAG, "connection broke");
                        e.printStackTrace();

                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e1) {
                            break;
                        }
                    }
                }
            }
        });

        subscribeThread.start();
    }

    private void deliverMessage(String message) {
        // deliver to activity or store in database
        eventBus.post(new IncomingMessageEvent(message));
    }

    public void onEvent(ServiceCommunicationEvent event) {
        // do nothing
    }
}
