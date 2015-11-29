package edu.purdue.androidforcefive.evtcollab.BusinessObjects;

import android.text.format.Time;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import edu.purdue.androidforcefive.evtcollab.DataCollections.EventCollection;

/**
 * Created by abuchmann on 20.11.2015.
 */
public class Event extends SuperItem {

    protected int associatedCalendar;
    protected String title;
    protected String description;
    protected String location;
    protected Date startTime;
    protected Date endTime;
    protected boolean isAllDay;
    protected int author;

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" );


    public Event(int associatedCalendar, String title, String description, int author, String location, Date startTime, Date endTime, TimeZone timeZone, boolean isAllDay) {
        this.associatedCalendar = associatedCalendar;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAllDay = isAllDay;
        this.author = author;
    }

    public Event(int id, int associatedCalendar, String title, String description, int author, String location, Date startTime, Date endTime, TimeZone timeZone, boolean isAllDay) {
        this.id = id;
        this.associatedCalendar = associatedCalendar;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAllDay = isAllDay;
        this.author = author;
    }

    public Event() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    public void setIsAllDay(boolean isAllDay) {
        this.isAllDay = isAllDay;
    }

    public int getAssociatedCalendar() {
        return associatedCalendar;
    }

    public void setAssociatedCalendar(int associatedCalendar) {
        this.associatedCalendar = associatedCalendar;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    @Override
    public void save() {
        if (id == 0) {
            // It's a new user, we have create it on the Api
            EventCollection.getInstance().addEvent(this);
        } else {
            // It's an existing user, update it!
            EventCollection.getInstance().updateEvent(this);
        }


    }

    @Override
    public void destroy() {
        EventCollection.getInstance().destroyEvent(this);
    }

    @Override
    public boolean contentEquals(Object other) {
        if (this.title.equals(((Event) other).getTitle()) &&
                this.associatedCalendar == ((Event) other).getAssociatedCalendar() &&
                this.author == ((Event) other).getAuthor() &&
                this.location.equals(((Event) other).getLocation()) &&
                this.description.equals(((Event) other).getDescription()) &&
                this.startTime.equals(((Event) other).getStartTime()) &&
                this.endTime.equals(((Event) other).getEndTime()) &&
                this.isAllDay == ((Event) other).isAllDay())
            return true;
        return false;
    }

    @Override
    public void applyJsonObject(JSONObject object) {
        try {
            if (object.has("id"))
                this.id = object.getInt("id");
            this.associatedCalendar = object.getInt("calendar_id");
            this.author = object.getInt("owner_id");
            this.title = object.getString("title");
            this.location = object.getString("location");
            this.description = object.getString("description");
            this.startTime = dateFormat.parse(object.getString("starttime"));
            this.endTime = dateFormat.parse(object.getString("endtime"));
            this.isAllDay = !object.isNull("allday") && object.getBoolean("allday");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //[{"id":1,"title":"Dinner","location":"KNOY","description":"Just a test event","starttime":"2015-12-01T18:30:00.000Z","endtime":"2015-12-01T23:30:00.000Z","timezone":null,"allday":null,"url":"http://192.168.109.128:3000/events/1.json"}]

    }

    @Override
    public JSONObject getItemAsJsonObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("calendar_id", associatedCalendar);
            json.put("owner_id", author);
            json.put("title", title);
            json.put("location", location);
            json.put("description", description);
            json.put("starttime", dateFormat.format(startTime));
            json.put("endtime", dateFormat.format(endTime));
            json.put("allDay", isAllDay);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
