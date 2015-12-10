package edu.purdue.androidforcefive.evtcollab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Annotation;
import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Event;
import edu.purdue.androidforcefive.evtcollab.BusinessObjects.User;
import edu.purdue.androidforcefive.evtcollab.DataCollections.AnnotationCollection;
import edu.purdue.androidforcefive.evtcollab.DataCollections.EventCollection;
import edu.purdue.androidforcefive.evtcollab.DataCollections.Interfaces.IDataCollectionChanged;
import edu.purdue.androidforcefive.evtcollab.DataCollections.UserCollection;

public class EventDisplayActivity extends AppCompatActivity implements IDataCollectionChanged {

    private Event event;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);
        AnnotationCollection.getInstance().addListener(this);
        AnnotationCollection.getInstance().initializeAnnotations();
        UserCollection.getInstance().addListener(this);
        UserCollection.getInstance().initializeUsers();

        event = (Event) getIntent().getSerializableExtra("event");
        ((TextView) findViewById(R.id.TitleText)).setText(event.getTitle());
        ((TextView) findViewById(R.id.DescriptionText)).setText(event.getDescription());
        ((TextView) findViewById(R.id.LocationText)).setText(event.getLocation());
        ((TextView) findViewById(R.id.TextStartTime)).setText(dateFormat.format(event.getStartTime()));
        ((TextView) findViewById(R.id.TextEndTime)).setText(dateFormat.format(event.getEndTime()));

        Button sendAnnotation = (Button) findViewById(R.id.btnAnnotationSend);
        final EditText txtAnnotation = (EditText) findViewById(R.id.txtAddAnnotation);
        sendAnnotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtAnnotation.getText().equals(""))
                AnnotationCollection.getInstance().addAnnotation(
                        new Annotation() {{
                            message = txtAnnotation.getText().toString();
                            authorId = 1;
                            eventId = event.getId();
                        }}
                );
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onCollectionChanged() {
        if((AnnotationCollection.getInstance().getAnnotations().size()>0) && UserCollection.getInstance().getUsers().size() > 0) {
            List<Annotation> annotationsToDisplay = new ArrayList<>();
            for(Annotation annotation : AnnotationCollection.getInstance().getAnnotations()) {
                if(annotation.getEventId() == event.getId()) {
                    for(User user : UserCollection.getInstance().getUsers()) {
                        if(user.getId() == annotation.getAuthorId())
                            annotation.setAuthor(user.getUserName());
                    }
                    annotationsToDisplay.add(annotation);
                }
            }
            AnnotationDataAdapter annotationDataAdapter = new AnnotationDataAdapter(this, annotationsToDisplay.toArray(new Annotation[0]));
            ListView list = (ListView) findViewById(R.id.listView2);
            list.setAdapter(annotationDataAdapter);
            ((ScrollView) findViewById(R.id.scrollView2)).post(new Runnable() {
                @Override
                public void run() {
                    ((ScrollView) findViewById(R.id.scrollView2)).fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    }
}
