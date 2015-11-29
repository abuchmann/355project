package edu.purdue.androidforcefive.evtcollab.BusinessObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import edu.purdue.androidforcefive.evtcollab.DataCollections.AnnotationCollection;

/**
 * Created by abuchmann on 20.11.2015.
 */
public class Annotation extends SuperItem {

    protected String message;
    protected Date createdAt;
    protected int authorId;
    protected int eventId;

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" );

    

    public Annotation() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int postId) {
        this.eventId = postId;
    }

    @Override
    public void save() {
        if (id == 0) {
            // It's a new user, we have create it on the Api
            AnnotationCollection.getInstance().addAnnotation(this);
        } else {
            // It's an existing user, update it!
            AnnotationCollection.getInstance().updateAnnotation(this);
        }


    }

    @Override
    public void destroy() {
        AnnotationCollection.getInstance().destroyAnnotation(this);
    }

    @Override
    public boolean contentEquals(Object other) {
        if (this.message.equals(((Annotation) other).getMessage()) &&
               // this.createdAt.equals(((Annotation) other).getCreatedAt()) &&
                this.authorId == ((Annotation) other).getAuthorId() &&
                this.eventId == ((Annotation) other).getEventId())
            return true;
        return false;
    }

    @Override
    public void applyJsonObject(JSONObject object) {
        try {
            if (object.has("id"))
                this.id = object.getInt("id");
            this.message = object.getString("message");
            this.authorId = object.getInt("author_id");
            this.eventId = object.getInt("event_id");
            this.createdAt = dateFormat.parse(object.getString("created_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //[{"id":1,"title":"Dinner","location":"KNOY","description":"Just a test annotation","starttime":"2015-12-01T18:30:00.000Z","endtime":"2015-12-01T23:30:00.000Z","timezone":null,"allday":null,"url":"http://192.168.109.128:3000/annotations/1.json"}]

    }

    @Override
    public JSONObject getItemAsJsonObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("message", message);
            json.put("author_id", authorId);
            json.put("event_id", eventId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
