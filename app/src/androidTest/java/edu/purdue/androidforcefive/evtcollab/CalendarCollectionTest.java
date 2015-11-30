package edu.purdue.androidforcefive.evtcollab;

import android.test.InstrumentationTestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Calendar;
import edu.purdue.androidforcefive.evtcollab.Controller.LogonController;
import edu.purdue.androidforcefive.evtcollab.DataCollections.Interfaces.IDataCollectionChanged;
import edu.purdue.androidforcefive.evtcollab.DataCollections.CalendarCollection;


/**
 * Created by abuchmann on 22.11.2015.
 */
public class CalendarCollectionTest extends InstrumentationTestCase implements IDataCollectionChanged {
    //CountDownLatch signal = new CountDownLatch(1);
    Semaphore semaphore = new Semaphore(1);

    @Test
    public void testGetCalendars() throws Exception {
//        LogonController.getInstance().login("jsaibene", "testpassword");
//        System.out.println(LogonController.getInstance().getToken());
        CalendarCollection.getInstance().addListener(this);
        CalendarCollection.getInstance().initializeCalendars();
        semaphore.tryAcquire(60, TimeUnit.SECONDS);
        List<Calendar> list = CalendarCollection.getInstance().getCalendars();
        assertNotNull(CalendarCollection.getInstance().getCalendars());
    }

    @Test
    public void testAddAndRemoveCalendar() throws InterruptedException {
        LogonController.getInstance().login("abuchmann", "testpassword");
        System.out.println(LogonController.getInstance().getToken());
        Calendar testCalendar = new Calendar() {{
            name = "Shared Testcalendar";
            description = "This is the description of the shared calendar";
            ownerId = 2;
        }};

        testCalendar.addMember(1);

        CalendarCollection.getInstance().addListener(this);
        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        CalendarCollection.getInstance().initializeCalendars();

        // Let's wait for the calendars to be added and the semaphore to be released;
        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        CalendarCollection.getInstance().addCalendar(testCalendar);

        // Let's wait for the calendar to be added and the semaphore to be released
        semaphore.tryAcquire(10, TimeUnit.SECONDS);


        for (Calendar calendar : CalendarCollection.getInstance().getCalendars()) {
            System.out.println(calendar.getId() + " " + calendar.getName());
            if (calendar.contentEquals(testCalendar) &&
                    calendar.getId() != 0) {
                testCalendar = calendar;
            }
        }
        assertTrue(testCalendar.getId() != 0);
        System.out.println("Semaphore status: " + semaphore.availablePermits());

        testCalendar.setDescription("Lord");
        testCalendar.removeMember(1);
        testCalendar.save();

        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        for (Calendar calendar : CalendarCollection.getInstance().getCalendars()) {
            System.out.println(calendar.getId() + " " + calendar.getName());
            if (calendar.getId() == testCalendar.getId()) {
                assertTrue(calendar.contentEquals(testCalendar));
            }
        }

        testCalendar.destroy();
        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        assertFalse(CalendarCollection.getInstance().getCalendars().contains(testCalendar));
    }

    @Override
    public void onCollectionChanged() {
        semaphore.release();
        System.out.println("Semaphore released. Status: " + semaphore.availablePermits());
    }
}
