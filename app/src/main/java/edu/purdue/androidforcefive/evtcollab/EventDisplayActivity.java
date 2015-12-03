package edu.purdue.androidforcefive.evtcollab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Event;

public class EventDisplayActivity extends AppCompatActivity {

    private Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);
        event =(Event) getIntent().getSerializableExtra("event");

        ((TextView) findViewById(R.id.TitleText)).setText(event.getTitle());

        ((TextView) findViewById(R.id.DescriptionText)).setText(event.getDescription());

        ((TextView) findViewById(R.id.LocationText)).setText(event.getLocation());

        ((TextView) findViewById(R.id.TextStartTime)).setText(event.getStartTime().toString());

        ((TextView) findViewById(R.id.TextEndTime)).setText(event.getEndTime().toString());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void test() {

    }

}
