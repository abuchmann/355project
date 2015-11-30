package edu.purdue.androidforcefive.evtcollab;

import android.test.InstrumentationTestCase;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.User;

/**
 * Created by abuchmann on 29.11.2015.
 */
public class UserTest extends InstrumentationTestCase {
    public void testContentEquals() {
        User testUser1 = new User() {{
            firstName = "firstName";
            lastName = "lastName";
            userName = "username";
            password = "password";
            eMail = "email@email.ema";
        }};

        User testUser2 = new User() {{
            firstName = "firstName";
            lastName = "lastName";
            userName = "username";
            password = "password";
            eMail = "email@email.ema";
        }};

        assertTrue(testUser1.contentEquals(testUser2));

        testUser1.addPermissionToAccessCalendar(1);
        assertFalse(testUser1.contentEquals(testUser2));


        testUser2.addPermissionToAccessCalendar(1);
        assertTrue(testUser1.contentEquals(testUser2));
    }


}
