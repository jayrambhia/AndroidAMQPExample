package com.fenchtose.amqptest;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.fenchtose.amqptest.message.IncomingMessageEvent;
import com.fenchtose.amqptest.message.SimpleMessage;
import com.fenchtose.amqptest.message.SimpleMessageAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.event.EventBus;


public class MainActivity extends ActionBarActivity {

    private RecyclerView recyclerView;
    private SimpleMessageAdapter adapter;

    private EventBus eventBus;
    private Intent receiverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventBus = EventBus.getDefault();
        eventBus.register(this);

        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SimpleMessageAdapter(this, R.layout.simple_message_object_layout, new ArrayList<SimpleMessage>());
        recyclerView.setAdapter(adapter);

        receiverService = new Intent(this, ReceiverService.class);
        startService(receiverService);

        try {
            JSONObject jsondata = new JSONObject();
            jsondata.put("message", getResources().getString(R.string.big_note_text));
            jsondata.put("channel", "Douglas Adams Podcast");
            jsondata.put("timestamp", new Date());

            adapter.insert(new SimpleMessage(jsondata), 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (eventBus != null) {
            eventBus.unregister(this);
        }

        if (receiverService != null) {
            stopService(receiverService);
        }

        super.onDestroy();
    }

    public void onEvent(IncomingMessageEvent event) {
        String message = event.getMessage();
        try {
            JSONObject jsonObject = new JSONObject(message);
            adapter.insert(new SimpleMessage(jsonObject), 0);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
