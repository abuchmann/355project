package edu.purdue.androidforcefive.evtcollab.DataCollections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Calendar;
import edu.purdue.androidforcefive.evtcollab.DataAccess.AsyncApiAccess;
import edu.purdue.androidforcefive.evtcollab.DataAccess.Enums.RestMethod;
import edu.purdue.androidforcefive.evtcollab.DataAccess.Interfaces.IAsyncResponse;
import edu.purdue.androidforcefive.evtcollab.DataAccess.RestCommand;
import edu.purdue.androidforcefive.evtcollab.DataCollections.Interfaces.IDataCollectionChanged;

/**
 * Created by abuchmann on 21.11.2015.
 */
public class CalendarCollection implements IAsyncResponse<RestCommand> {
    private static CalendarCollection instance;

    private List<Calendar> calendars;
    private List<IDataCollectionChanged> delegates = new ArrayList<>();
    // private final AsyncApiAccess asyncRestAccess;
    private RestCommand currentCommand;


    // private String ApiUrl = "http://192.168.109.128:3000/api/";
    private String ApiUrl = "http://agglo.mooo.com:4000/api/";

    private int ApiVersion = 1;

    public static CalendarCollection getInstance() {
        if (instance == null) {
            instance = new CalendarCollection();
        }
        return instance;
    }

    /**
     * Private constructor as this class is a singleton
     */
    private CalendarCollection() {
        //asyncRestAccess = new AsyncApiAccess(this);
    }

    public List<Calendar> getCalendars() {
        //System.out.println(calendars.size());
        return calendars;

    }

    public Calendar getCalendar(int id) {
        for (Calendar calendar : calendars) {
            if (calendar.getId() == id) {
                return calendar;
            }
        }
        return null;
    }

    public void addCalendar(Calendar calendar) {
        AsyncApiAccess asyncApiAccess1 = new AsyncApiAccess(this);
        asyncApiAccess1.execute(new RestCommand(RestMethod.CREATE, ApiUrl + "v" + ApiVersion + "/calendars", calendar.getItemAsJsonObject().toString()));
    }


    public void initializeCalendars() {
        calendars = new ArrayList<>();
        refreshCalendars();
    }

    public void refreshCalendars() {
        AsyncApiAccess asyncApiAccess = new AsyncApiAccess(this);
        asyncApiAccess.execute(new RestCommand(RestMethod.INDEX, ApiUrl + "v" + ApiVersion + "/calendars"));
    }

    public void destroyCalendar(Calendar calendar) {
        AsyncApiAccess asyncApiAccess = new AsyncApiAccess(this);
        RestCommand restCommand = new RestCommand(RestMethod.DELETE, ApiUrl + "v" + ApiVersion + "/calendars/" + calendar.getId());
        restCommand.setResult(calendar);
        asyncApiAccess.execute(restCommand);
    }

    public void updateCalendar(Calendar calendar) {
        AsyncApiAccess asyncApiAccess = new AsyncApiAccess(this);
        RestCommand restCommand = new RestCommand(RestMethod.UPDATE, ApiUrl + "v" + ApiVersion + "/calendars/" + calendar.getId(),  calendar.getItemAsJsonObject().toString());
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
                    JSONArray calendarsJson = new JSONArray((String) restCommand.getResult());
                    for (int i = 0; i < calendarsJson.length(); i++) {
                        Calendar itemToAdd = new Calendar();
                        itemToAdd.applyJsonObject(calendarsJson.getJSONObject(i));
                        calendars.add(itemToAdd);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case CREATE:
                try {
                    JSONObject newCalendarJson = new JSONObject((String) restCommand.getResult());
                    Calendar itemToAdd = new Calendar();
                    itemToAdd.applyJsonObject(newCalendarJson);
                    calendars.add(itemToAdd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println("Calendar added!");
                break;
            case DELETE:
                if (restCommand.getStatusCode() == 204) {
                    calendars.remove(restCommand.getResult());
                }
                break;
            case UPDATE:
                try {
                    JSONObject updatedCalendarJson = new JSONObject((String) restCommand.getResult());

                    for(Calendar calendar : calendars) {
                        if(calendar.getId() == updatedCalendarJson.getInt("id")) {
                            calendar.applyJsonObject(updatedCalendarJson);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


        }
        notifyListeners();

    }



}
