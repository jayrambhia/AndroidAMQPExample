package com.fenchtose.amqptest.message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fenchtose.amqptest.R;
import com.fenchtose.amqptest.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jay Rambhia on 07/03/15.
 */
public class SimpleMessageAdapter extends RecyclerView.Adapter<SimpleMessageAdapter.ViewHolder> {

    private ArrayList<SimpleMessage> items;
    private int layout;
    private Context context;

    public SimpleMessageAdapter(Context context, int layout, @NonNull ArrayList<SimpleMessage> items) {
        this.items = items;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public SimpleMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        SimpleMessage simpleMessage = items.get(i);
        viewHolder.channelView.setText(simpleMessage.getChannel());
        viewHolder.messageView.setText(simpleMessage.getMessage());

        // date
        Date messageStamp = simpleMessage.getTimestamp();
        if (messageStamp != null) {
            long diff = DateUtils.getCurrentDiff(messageStamp, TimeUnit.MINUTES);
            if (diff < 5) {
                viewHolder.timeView.setText("Just Now");
            } else if (diff >= 5 && diff < 60) {
                viewHolder.timeView.setText(String.valueOf(diff) + " mins");
            } else if (diff > 60 && diff < 1440) {
                viewHolder.timeView.setText(String.valueOf(diff/60) + " hrs");
            } else if (diff >= 1440 && diff < 2880) {
                viewHolder.timeView.setText("Yesterday");
            } else {
                String time_text = DateUtils.getDateString(messageStamp, "d-MMM-yyyy");
                viewHolder.timeView.setText(time_text);
            }
        } else {
            viewHolder.timeView.setText("Long Ago");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public boolean insert(SimpleMessage simpleMessage, int index) {
        if (index < 0 || index > items.size()) {
            return false;
        }

        items.add(index, simpleMessage);
        return true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView messageView;
        public TextView timeView;
        public TextView channelView;

        public ViewHolder(View itemView) {
            super(itemView);

            messageView = (TextView)itemView.findViewById(R.id.message_textview);
            timeView = (TextView)itemView.findViewById(R.id.time_textview);
            channelView = (TextView)itemView.findViewById(R.id.channel_textview);
        }
    }

}
