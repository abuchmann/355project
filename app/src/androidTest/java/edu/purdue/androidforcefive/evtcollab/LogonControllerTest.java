package edu.purdue.androidforcefive.evtcollab;

import android.test.InstrumentationTestCase;

import edu.purdue.androidforcefive.evtcollab.Controller.LogonController;

/**
 * Created by abuchmann on 29.11.2015.
 */
public class LogonControllerTest extends InstrumentationTestCase {
    public void testLogin()
    {
        LogonController.getInstance().login("abuchmann", "testpassword");
        //System.out.println(LogonController.getInstance().getToken());
    }
}
