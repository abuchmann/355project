package edu.purdue.androidforcefive.evtcollab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.User;
import edu.purdue.androidforcefive.evtcollab.DataCollections.Interfaces.IDataCollectionChanged;
import edu.purdue.androidforcefive.evtcollab.DataCollections.UserCollection;


public class MainActivity extends AppCompatActivity implements IDataCollectionChanged {

    private TextView textView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
            Add yourself as a listener, that means after every change of the collection, you get notified via the onCollectionChanged method
         */
        UserCollection.getInstance().addListener(this);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.btnDownload);
        textView = (TextView) findViewById(R.id.textView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserCollection.getInstance().initializeUsers();
            }
        });


        Button btn2 = (Button) findViewById(R.id.btnAdd);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User() {{
                    firstName = "test";
                    lastName = "test";
                    userName = "testUsername";
                    password = "testPassword";
                    eMail = "email";
                }};
                user.save();

            }
        });

        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (User user : UserCollection.getInstance().getUsers()) {
                    if(user.getUserName().equals("testUsername"))
                        user.destroy();
                }
            }
        });

        Button btnStartCalendar = (Button) findViewById(R.id.btnStartCalendar);
        btnStartCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calendarView = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(calendarView);

            }
        });


    }

    /*Test comment*/

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

    @Override
    public void onCollectionChanged() {
        String temp = "";
        for (User user : UserCollection.getInstance().getUsers()) {
            temp += user.getId() + " " + user.getFirstName() + " " + user.getLastName() + "\n";
        }
        textView.setText(temp);
    }
}