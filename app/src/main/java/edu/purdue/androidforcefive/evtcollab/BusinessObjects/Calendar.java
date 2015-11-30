package edu.purdue.androidforcefive.evtcollab.BusinessObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.androidforcefive.evtcollab.DataCollections.CalendarCollection;

/**
 * Created by abuchmann on 23.11.2015.
 */
public class Calendar extends SuperItem {
    protected String name;
    protected String description;
    protected int ownerId;
    protected List<Integer> members = new ArrayList<>();


    public Calendar(int id, String name, String description, int ownerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
    }

    public Calendar(String name, String description, int ownerId) {
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
    }

    public Calendar() {

    }

    public int getId() {
        return id;
    }

    @Override
    public void applyJsonObject(JSONObject object) {
        try {
            if (object.has("id"))
                this.id = object.getInt("id");
            this.name = object.getString("name");
            this.description = object.getString("description");
            this.ownerId = object.getInt("owner_id");
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
        JSONObject calendarJson = new JSONObject();
        JSONArray membersJson = new JSONArray();
        try {
            for (int member : members) {
                membersJson.put(new JSONObject().put("id", member));
            }
            calendarJson.put("users", membersJson);
            calendarJson.put("name", name);
            calendarJson.put("description", description);
            calendarJson.put("owner_id", ownerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return calendarJson;
    }

    public void addMember(int id) {
        members.add(id);
    }

    public void removeMember(int id) {
        members.remove(new Integer(id));
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

    public List<Integer> getMembers() {
        return members;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
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
        if (this.name.equals(((Calendar) other).getName()) &&
                this.description.equals(((Calendar) other).getDescription()) &&
                this.ownerId == ((Calendar) other).getOwnerId() &&
                this.members.size() == ((Calendar) other).getMembers().size()) {
            for (int member : members) {
                if (!((Calendar) other).getMembers().contains(member)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
