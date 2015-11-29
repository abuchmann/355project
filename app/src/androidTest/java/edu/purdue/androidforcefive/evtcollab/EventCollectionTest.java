package edu.purdue.androidforcefive.evtcollab;

import android.test.InstrumentationTestCase;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Event;
import edu.purdue.androidforcefive.evtcollab.DataCollections.Interfaces.IDataCollectionChanged;
import edu.purdue.androidforcefive.evtcollab.DataCollections.EventCollection;


/**
 * Created by abuchmann on 22.11.2015.
 */
public class EventCollectionTest extends InstrumentationTestCase implements IDataCollectionChanged {
    //CountDownLatch signal = new CountDownLatch(1);
    Semaphore semaphore = new Semaphore(1);
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" );
    private static final int NUMBER_OF_TESTS = 2; // count your tests here
    private static int sTestsRun = 0;

    @Test
    public void testGetEvents() throws Exception {
        EventCollection.getInstance().addListener(this);
        EventCollection.getInstance().initializeEvents();
        semaphore.tryAcquire(5, TimeUnit.SECONDS);

        assertNotNull(EventCollection.getInstance().getEvents());
    }

    @Test
    public void testAddAndRemoveEvent() throws InterruptedException {
        Event testEvent = new Event(){{
            associatedCalendar = 1;
            title = "TestEvent";
            description = "this is a test event";
            author = 1;
            location = "house of the 1000 suns";
            try {
                startTime = dateFormat.parse("2015-12-02T18:30:00.000Z");
                endTime = dateFormat.parse("2015-12-02T22:30:00.000Z");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            isAllDay = false;   
        }};

        EventCollection.getInstance().addListener(this);
        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        EventCollection.getInstance().initializeEvents();

        // Let's wait for the events to be added and the semaphore to be released;
        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        EventCollection.getInstance().addEvent(testEvent);

        // Let's wait for the event to be added and the semaphore to be released
        semaphore.tryAcquire(10, TimeUnit.SECONDS);


        for (Event event : EventCollection.getInstance().getEvents()) {
            System.out.println(event.getId() + " " + event.getTitle());
            if (event.contentEquals(testEvent) &&
                    event.getId() != 0) {
                testEvent = event;
            }
        }
        assertTrue(testEvent.getId() != 0);
        System.out.println("Semaphore status: " + semaphore.availablePermits());

        testEvent.setDescription("Lord");
        testEvent.save();

        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        for (Event event : EventCollection.getInstance().getEvents()) {
            System.out.println(     event.getId() + " " + event.getTitle());
            if (event.getId() == testEvent.getId()) {
                assertEquals(event.getDescription(), "Lord");
            }
        }

        testEvent.destroy();
        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        assertFalse(EventCollection.getInstance().getEvents().contains(testEvent));
    }

    @Override
    public void onCollectionChanged() {
        semaphore.release();
        System.out.println("Semaphore released. Status: " + semaphore.availablePermits());
    }
}
