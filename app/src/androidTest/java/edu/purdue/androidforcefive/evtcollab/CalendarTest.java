package edu.purdue.androidforcefive.evtcollab;

import android.test.InstrumentationTestCase;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Calendar;

/**
 * Created by abuchmann on 29.11.2015.
 */
public class CalendarTest extends InstrumentationTestCase {
    public void testContentEquals() {
        Calendar testCalendar1 = new Calendar() {{
            name = "Shared Testcalendar";
            description = "This is the description of the shared calendarEventStart";
        }};

        Calendar testCalendar2 = new Calendar() {{
            name = "Shared Testcalendar";
            description = "This is the description of the shared calendarEventStart";
        }};

        assertTrue(testCalendar1.contentEquals(testCalendar2));

        testCalendar1.addMember(1);
        assertFalse(testCalendar1.contentEquals(testCalendar2));

        testCalendar2.addMember(2);
        assertFalse(testCalendar1.contentEquals(testCalendar2));

        testCalendar2.addMember(1);
        assertFalse(testCalendar1.contentEquals(testCalendar2));

        testCalendar2.removeMember(2);
        assertTrue(testCalendar1.contentEquals(testCalendar2));
    }


}
