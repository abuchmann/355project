package edu.purdue.androidforcefive.evtcollab.DataAccess.Interfaces;

/**
 * Created by abuchmann on 21.11.2015.
 */
public interface IAsyncResponse<T> {
    void onTaskCompleted(T result);
}
