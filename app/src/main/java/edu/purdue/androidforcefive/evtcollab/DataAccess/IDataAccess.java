package edu.purdue.androidforcefive.evtcollab.DataAccess;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by abuchmann on 20.11.2015.
 */
public interface IDataAccess<T> {
    List<T> getItems() throws MalformedURLException;
    T getItem(int id);
    T createItem(T item);
    T updateItem(T item);
    int removeItem(T item);
}
