package edu.purdue.androidforcefive.evtcollab.DataCollections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Event;
import edu.purdue.androidforcefive.evtcollab.BusinessObjects.SuperItem;
import edu.purdue.androidforcefive.evtcollab.DataAccess.AsyncApiAccess;
import edu.purdue.androidforcefive.evtcollab.DataAccess.Enums.RestMethod;
import edu.purdue.androidforcefive.evtcollab.DataAccess.Interfaces.IAsyncResponse;
import edu.purdue.androidforcefive.evtcollab.DataAccess.RestCommand;
import edu.purdue.androidforcefive.evtcollab.DataCollections.Interfaces.IDataCollectionChanged;

/**
 * Created by abuchmann on 21.11.2015.
 */
public class EventCollection implements IAsyncResponse<RestCommand> {
    private static EventCollection instance;

    private List<Event> events;
    private List<IDataCollectionChanged> delegates = new ArrayList<>();
    // private final AsyncApiAccess asyncRestAccess;
    private RestCommand currentCommand;


    // private String ApiUrl = "http://192.168.109.128:3000/api/";
    private String ApiUrl = "http://agglo.mooo.com:4000/api/";

    private int ApiVersion = 1;

    public static EventCollection getInstance() {
        if (instance == null) {
            instance = new EventCollection();
        }
        return instance;
    }

    /**
     * Private constructor as this class is a singleton
     */
    private EventCollection() {
        //asyncRestAccess = new AsyncApiAccess(this);
    }

    public List<Event> getEvents() {
        //System.out.println(events.size());
        return events;

    }

    public Event getEvent(int id) {
        for (Event event : events) {
            if (event.getId() == id) {
                return event;
            }
        }
        return null;
    }

    public void addEvent(Event event) {
        AsyncApiAccess asyncApiAccess1 = new AsyncApiAccess(this);
        asyncApiAccess1.execute(new RestCommand(RestMethod.CREATE, ApiUrl + "v" + ApiVersion + "/events", event.getItemAsJsonObject().toString()));
    }


    public void initializeEvents() {
        events = new ArrayList<>();
        refreshEvents();
    }

    public void refreshEvents() {
        AsyncApiAccess asyncApiAccess = new AsyncApiAccess(this);
        asyncApiAccess.execute(new RestCommand(RestMethod.INDEX, ApiUrl + "v" + ApiVersion + "/events"));
    }

    public void destroyEvent(Event event) {
        AsyncApiAccess asyncApiAccess = new AsyncApiAccess(this);
        RestCommand restCommand = new RestCommand(RestMethod.DELETE, ApiUrl + "v" + ApiVersion + "/events/" + event.getId());
        restCommand.setResult(event);
        asyncApiAccess.execute(restCommand);
    }

    public void updateEvent(Event event) {

        AsyncApiAccess asyncApiAccess = new AsyncApiAccess(this);
        RestCommand restCommand = new RestCommand(RestMethod.UPDATE, ApiUrl + "v" + ApiVersion + "/events/" + event.getId(),  event.getItemAsJsonObject().toString());
        asyncApiAccess.execute(restCommand);
    }

    public void addListener(IDataCollectionChanged delegate) {
        delegates.add(delegate);
    }

    private void notifyListeners() {
        for (IDataCollectionChanged delegate : delegates) {
            delegate.onCollectionChanged();
        }
    }

    @Override
    public void onTaskCompleted(RestCommand restCommand) {
        switch (restCommand.getRestMethod()) {
            case INDEX:
                try {
                    JSONArray eventsJson = new JSONArray((String) restCommand.getResult());
                    for (int i = 0; i < eventsJson.length(); i++) {
                        JSONObject e = eventsJson.getJSONObject(i);
                        Event itemToAdd = new Event();
                        itemToAdd.applyJsonObject(e);
                        events.add(itemToAdd);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case CREATE:
                try {
                    JSONObject newEventJson = new JSONObject((String) restCommand.getResult());
                    Event itemToAdd = new Event();
                    itemToAdd.applyJsonObject(newEventJson);
                    events.add(itemToAdd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println("Event added!");
                break;
            case DELETE:
                if (restCommand.getStatusCode() == 204) {
                    events.remove(restCommand.getResult());
                }
                break;
            case UPDATE:
                try {
                    JSONObject updatedEventJson = new JSONObject((String) restCommand.getResult());

                    for(Event event : events) {
                        if(event.getId() == updatedEventJson.getInt("id")) {
                            event.applyJsonObject(updatedEventJson);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


        }
        notifyListeners();

    }



}
