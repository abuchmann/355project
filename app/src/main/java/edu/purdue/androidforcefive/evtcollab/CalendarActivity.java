package edu.purdue.androidforcefive.evtcollab;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Date;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Event;
import edu.purdue.androidforcefive.evtcollab.Controller.LogonController;
import edu.purdue.androidforcefive.evtcollab.DataCollections.EventCollection;
import edu.purdue.androidforcefive.evtcollab.DataCollections.Interfaces.IDataCollectionChanged;

public class CalendarActivity extends AppCompatActivity implements IDataCollectionChanged{

    private CalendarView calendarView;
    private EventDataAdapter eventArrayAdapter;
    private Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        this.getActionBar();

        EventCollection.getInstance().addListener(this);
        EventCollection.getInstance().initializeEvents();


        calendarView = (CalendarView) findViewById(R.id.calendarView);
//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
//
//            }
//        });





        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void setCurrentEvents() {

    }

    @Override
    public void onCollectionChanged() {
        eventArrayAdapter = new EventDataAdapter(this,EventCollection.getInstance().getEvents().toArray(new Event[0]));
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CalendarActivity.this, eventArrayAdapter.getItem(position).getId() + " " + eventArrayAdapter.getItem(position).getTitle() , Toast.LENGTH_SHORT).show();
                Intent eventDisplay = new Intent(getApplicationContext(), EventDisplayActivity.class);
                Event test = eventArrayAdapter.getItem(position);
                eventDisplay.putExtra("event", eventArrayAdapter.getItem(position));
                startActivity(eventDisplay);
            }
        });
        listView.setAdapter(eventArrayAdapter);
    }
}
