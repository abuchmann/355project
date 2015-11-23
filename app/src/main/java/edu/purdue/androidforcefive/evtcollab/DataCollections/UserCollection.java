package edu.purdue.androidforcefive.evtcollab.DataCollections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.User;
import edu.purdue.androidforcefive.evtcollab.DataAccess.SuperDataAccess;
import edu.purdue.androidforcefive.evtcollab.DataAccess.Enums.RestMethod;
import edu.purdue.androidforcefive.evtcollab.DataAccess.Interfaces.IAsyncResponse;
import edu.purdue.androidforcefive.evtcollab.DataAccess.RestCommand;

/**
 * Created by abuchmann on 21.11.2015.
 */
public class UserCollection implements IAsyncResponse<RestCommand> {
    private static UserCollection instance;

    private List<User> users;
    private List<IDataCollection> delegates = new ArrayList<>();
    // private final SuperDataAccess asyncRestAccess;
    private RestCommand currentCommand;


    private String ApiUrl = "http://192.168.109.128:3000/api/";
    private int ApiVersion = 1;

    public static UserCollection getInstance() {
        if (instance == null) {
            instance = new UserCollection();
        }
        return instance;
    }

    /**
     * Private constructor as this class is a singleton
     */
    private UserCollection() {
        //asyncRestAccess = new SuperDataAccess(this);
    }

    public List<User> getUsers() {
        //System.out.println(users.size());
        return users;

    }

    public User getUser(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public void addUser(User user) {
        JSONObject userJson = new JSONObject();
        try {
            userJson.put("firstname", user.getFirstName());
            userJson.put("lastname", user.getLastName());
            userJson.put("email", user.geteMail());
            userJson.put("username", user.getUserName());
            userJson.put("password", user.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SuperDataAccess superDataAccess1 = new SuperDataAccess(this);
        superDataAccess1.execute(new RestCommand(RestMethod.CREATE, ApiUrl + "v" + ApiVersion + "/users", userJson.toString()));
    }


    public void initializeUsers() {
        users = new ArrayList<>();
        refreshUsers();
    }

    public void refreshUsers() {
        SuperDataAccess superDataAccess = new SuperDataAccess(this);
        superDataAccess.execute(new RestCommand(RestMethod.INDEX, ApiUrl + "v" + ApiVersion + "/users"));
    }

    public void destroyUser(User user) {
        SuperDataAccess superDataAccess = new SuperDataAccess(this);
        RestCommand restCommand = new RestCommand(RestMethod.DELETE, ApiUrl + "v" + ApiVersion + "/users/" + user.getId());
        restCommand.setResult(user);
        superDataAccess.execute(restCommand);
    }

    public void updateUser(User user) {
        JSONObject userJson = new JSONObject();
        try {
            userJson.put("firstname", user.getFirstName());
            userJson.put("lastname", user.getLastName());
            userJson.put("email", user.geteMail());
            userJson.put("username", user.getUserName());
            userJson.put("password", user.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SuperDataAccess superDataAccess = new SuperDataAccess(this);
        RestCommand restCommand = new RestCommand(RestMethod.UPDATE, ApiUrl + "v" + ApiVersion + "/users/" + user.getId(),  userJson.toString());
        superDataAccess.execute(restCommand);
    }

    public void addListener(IDataCollection delegate) {
        delegates.add(delegate);
    }

    private void notifyListeners() {
        for (IDataCollection delegate : delegates) {
            delegate.onCollectionChanged();
        }
    }

    @Override
    public void onTaskCompleted(RestCommand restCommand) {
        switch (restCommand.getRestMethod()) {
            case INDEX:
                try {
                    JSONArray usersJson = new JSONArray((String) restCommand.getResult());
                    for (int i = 0; i < usersJson.length(); i++) {
                        JSONObject e = usersJson.getJSONObject(i);
                        users.add(new User(e.getInt("id"),
                                e.getString("firstname"),
                                e.getString("lastname"),
                                e.getString("username"),
                                e.getString("password"),
                                e.getString("email")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case CREATE:
                try {
                    JSONObject newUserJson = new JSONObject((String) restCommand.getResult());
                    users.add(new User(newUserJson.getInt("id"),
                            newUserJson.getString("firstname"),
                            newUserJson.getString("lastname"),
                            newUserJson.getString("username"),
                            newUserJson.getString("password"),
                            newUserJson.getString("email")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println("User added!");
                break;
            case DELETE:
                if (restCommand.getStatusCode() == 204) {
                    users.remove(restCommand.getResult());
                }
                break;
            case UPDATE:
                try {
                    JSONObject updatedUserJson = new JSONObject((String) restCommand.getResult());

                    for(User user : users) {
                        if(user.getId() == updatedUserJson.getInt("id")) {
                            user.setFirstName(updatedUserJson.getString("firstname"));
                            user.setLastName(updatedUserJson.getString("lastname"));
                            user.setUserName(updatedUserJson.getString("username"));
                            user.setPassword(updatedUserJson.getString("password"));
                            user.seteMail(updatedUserJson.getString("email"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


        }
        notifyListeners();

    }



}
