package edu.purdue.androidforcefive.evtcollab;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Event;
import edu.purdue.androidforcefive.evtcollab.Controller.LogonController;
import edu.purdue.androidforcefive.evtcollab.DataCollections.EventCollection;
import edu.purdue.androidforcefive.evtcollab.DataCollections.Interfaces.IDataCollectionChanged;

public class EventCreateActivity extends AppCompatActivity implements IDataCollectionChanged {

    Calendar calendarEventStart = Calendar.getInstance();
    Calendar calendarEventEnd = Calendar.getInstance();
    EditText txtStartDate, txtEndDate, txtStartTime, txtEndTime, txtTitle, txtDescription, txtLocation;
    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        EventCollection.getInstance().addListener(this);
        txtStartDate = (EditText) findViewById(R.id.txtStartDate);
        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EventCreateActivity.this, startDatePicker, calendarEventStart.get(Calendar.YEAR), calendarEventStart.get(Calendar.MONTH), calendarEventStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtEndDate = (EditText) findViewById(R.id.txtEndDate);
        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EventCreateActivity.this, endDatePicker, calendarEventEnd.get(Calendar.YEAR), calendarEventEnd.get(Calendar.MONTH), calendarEventEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtStartTime = (EditText) findViewById(R.id.txtStartTime);
        txtStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(EventCreateActivity.this, startTimePicker, calendarEventStart.get(Calendar.HOUR), calendarEventStart.get(Calendar.MINUTE), false).show();
            }
        });

        txtEndTime = (EditText) findViewById(R.id.txtEndTime);
        txtEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(EventCreateActivity.this, endTimePicker, calendarEventEnd.get(Calendar.HOUR), calendarEventEnd.get(Calendar.MINUTE), false).show();
            }
        });

        txtTitle = (EditText) findViewById(R.id.txtEventTitle);
        txtDescription = (EditText) findViewById(R.id.txtEventDescription);
        txtLocation = (EditText) findViewById(R.id.txtEventLocation);

        btnSave = (Button) findViewById(R.id.btnCreateEvent);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSave();
            }
        });


    }

    private boolean checkAndSave() {
        if(txtTitle.getText().equals("")) {
            Toast.makeText(EventCreateActivity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(txtLocation.getText().equals("")) {
            Toast.makeText(EventCreateActivity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtStartTime.getText().equals("") &&
                txtStartDate.getText().equals("")) {
            Toast.makeText(EventCreateActivity.this, "Please enter a start time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtEndTime.getText().equals("") &&
                txtEndDate.getText().equals("")) {
            Toast.makeText(EventCreateActivity.this, "Please enter a end time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(calendarEventStart.getTimeInMillis() > calendarEventEnd.getTimeInMillis()) {
            Toast.makeText(EventCreateActivity.this, "Start time is after end time, please change", Toast.LENGTH_SHORT).show();
            return false;
        }



        Event event = new Event() {{
            title = txtTitle.getText().toString();
            description = txtDescription.getText().toString();
            location = txtLocation.getText().toString();
            startTime = calendarEventStart.getTime();
            endTime = calendarEventEnd.getTime();
            author = LogonController.getInstance().getLoggedInUser().getId();
            associatedCalendar = 1;
        }};

        EventCollection.getInstance().addEvent(event);

        return true;
    }

    TimePickerDialog.OnTimeSetListener startTimePicker = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendarEventStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendarEventStart.set(Calendar.MINUTE, minute);

            txtStartTime.setText(calendarEventStart.get(Calendar.HOUR) + ":" + calendarEventStart.get(Calendar.MINUTE) + " " + ((hourOfDay < 12) ? "AM" : "PM"));

        }
    };

    TimePickerDialog.OnTimeSetListener endTimePicker = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendarEventEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendarEventEnd.set(Calendar.MINUTE, minute);

            txtEndTime.setText(calendarEventEnd.get(Calendar.HOUR) + ":" + calendarEventEnd.get(Calendar.MINUTE) + " " + ((hourOfDay < 12) ? "AM" : "PM"));

        }
    };

    DatePickerDialog.OnDateSetListener endDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendarEventEnd.set(Calendar.YEAR, year);
            calendarEventEnd.set(Calendar.MONTH, monthOfYear);
            calendarEventEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            txtEndDate.setText(sdf.format(calendarEventEnd.getTime()));
        }
    };

    DatePickerDialog.OnDateSetListener startDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendarEventStart.set(Calendar.YEAR, year);
            calendarEventStart.set(Calendar.MONTH, monthOfYear);
            calendarEventStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            txtStartDate.setText(monthOfYear + "/" + dayOfMonth + "/" + year);

            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            txtStartDate.setText(sdf.format(calendarEventStart.getTime()));
        }
    };


    @Override
    public void onCollectionChanged() {
        finish();
    }
}
