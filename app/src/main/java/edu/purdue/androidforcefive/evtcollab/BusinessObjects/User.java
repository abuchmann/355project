package edu.purdue.androidforcefive.evtcollab.BusinessObjects;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Interfaces.IBusinessObject;
import edu.purdue.androidforcefive.evtcollab.DataCollections.UserCollection;

/**
 * Created by abuchmann on 20.11.2015.
 */
public class User implements IBusinessObject{
    private int id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String eMail;

    public User() {

    }

    public User(int id, String firstName, String lastName, String userName, String password, String eMail) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
    }

    public User(String firstName, String lastName, String userName, String password, String eMail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", eMail='" + eMail + '\'' +
                '}';
    }

    @Override
    public void save() {
        if(id == 0) {
            // It's a new user, we have create it on the Api
            UserCollection.getInstance().addUser(this);
        }
        else {
            // It's an existing user, update it!
            UserCollection.getInstance().updateUser(this);
        }


    }

    @Override
    public void destroy() {
        UserCollection.getInstance().destroyUser(this);
    }
}
