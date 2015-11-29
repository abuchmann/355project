package edu.purdue.androidforcefive.evtcollab.BusinessObjects;

import org.json.JSONObject;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Interfaces.IBusinessObject;

/**
 * Created by abuchmann on 24.11.2015.
 */
public abstract class SuperItem implements IBusinessObject {
    protected int id;

    public int getId() {
        return id;
    }

    public abstract void applyJsonObject(JSONObject object);
    public abstract JSONObject getItemAsJsonObject();
   // public abstract boolean contentEquals(SuperItem other);
}
