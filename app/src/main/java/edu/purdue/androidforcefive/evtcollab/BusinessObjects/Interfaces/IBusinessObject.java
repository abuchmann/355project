package edu.purdue.androidforcefive.evtcollab.BusinessObjects.Interfaces;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.SuperItem;

/**
 * Created by abuchmann on 21.11.2015.
 */
public interface IBusinessObject<T> {
    void save();
    void destroy();
    boolean contentEquals(T other);
}
