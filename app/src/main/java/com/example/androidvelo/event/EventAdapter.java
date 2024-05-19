package com.example.androidvelo.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.androidvelo.R;
import com.example.androidvelo.event.Event;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    private Context mContext;
    private List<Event> mEventList;

    public EventAdapter(Context context, List<Event> eventList) {
        super(context, 0, eventList);
        mContext = context;
        mEventList = eventList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.item_event, parent, false);
        }

        Event currentEvent = mEventList.get(position);

        TextView nameTextView = listItemView.findViewById(R.id.name_text_view);
        nameTextView.setText(currentEvent.getNameEvent());

        TextView distanceTextView = listItemView.findViewById(R.id.distance_text_view);
        distanceTextView.setText(currentEvent.getDistance() + " km");

        return listItemView;
    }
}
