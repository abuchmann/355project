package edu.purdue.androidforcefive.evtcollab.BusinessObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Interfaces.IBusinessObject;
import edu.purdue.androidforcefive.evtcollab.DataCollections.CalendarCollection;

/**
 * Created by abuchmann on 23.11.2015.
 */
public class Calendar extends SuperItem {
    private String name;
    private String description;
    private List<Integer> members = new ArrayList<>();

    public Calendar(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Calendar(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Calendar() {

    }

    public int getId() {
        return id;
    }

    @Override
    public void applyJsonObject(JSONObject object) {
        try {
            this.setId(object.getInt("id"));
            this.setName(object.getString("name"));
            this.setDescription(object.getString("description"));
            JSONArray users = object.getJSONArray("users");
            for (int i = 0; i < users.length(); i++) {
                this.addMember(users.getJSONObject(i).getInt("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject getItemAsJsonObject() {
        return null;
    }

    public void addMember(int id) {
        members.add(id);
    }

    public void removeMember(int id) {
        members.remove(id);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void save() {
        if (id == 0) {
            // It's a new user, we have create it on the Api
            CalendarCollection.getInstance().addCalendar(this);
        } else {
            // It's an existing user, update it!
            CalendarCollection.getInstance().updateCalendar(this);
        }

    }

    @Override
    public void destroy() {
        CalendarCollection.getInstance().destroyCalendar(this);
    }

    @Override
    public boolean contentEquals(Object other) {
        return false;
    }
}
