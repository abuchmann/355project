package edu.purdue.androidforcefive.evtcollab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Event;

/**
 * Created by abuchmann on 01.12.2015.
 */
public class EventDataAdapter extends ArrayAdapter<Event> {
    private final Context context;
    private Event[] events;

    public EventDataAdapter(Context context, Event[] events) {
        super(context, -1, events);
        this.context = context;
        this.events = events;
    }

    public EventDataAdapter(Context context) {
        super(context, -1);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_event_list, parent, false);
        TextView textViewFirstLine = (TextView) rowView.findViewById(R.id.firstLine);
        TextView textViewSecondLine = (TextView) rowView.findViewById(R.id.secondLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textViewFirstLine.setText(events[position].getTitle());
        textViewSecondLine.setText(events[position].getStartTime().toString());
        // change the icon for Windows and iPhone
        Event e = events[position];
        //imageView.setImageResource(R.drawable.ok);

        return rowView;
        //return super.getView(position, convertView, parent);
    }

    public void addEvents(Event[] items) {
        super.addAll(items);
        events = items;
    }
}
