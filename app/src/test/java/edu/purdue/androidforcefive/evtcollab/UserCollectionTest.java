package edu.purdue.androidforcefive.evtcollab;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import edu.purdue.androidforcefive.evtcollab.DataCollections.IDataCollection;
import edu.purdue.androidforcefive.evtcollab.DataCollections.UserCollection;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class UserCollectionTest implements IDataCollection {
//    @Test
//    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
//    }

    @Test
    public void testGetUsers() throws Exception {
        signal = new CountDownLatch(1);
        UserCollection.getInstance().addListener(this);
        UserCollection.getInstance().initializeUsers();
        signal.await(5, TimeUnit.SECONDS);

        assertNotNull(UserCollection.getInstance().getUsers());
    }
    CountDownLatch signal = new CountDownLatch(1);
    Semaphore testAndRemoveUserSemaphore = new Semaphore(1);

    @Override
    public void onCollectionChanged() {
        System.out.println("Notified!");
        signal.countDown();
        testAndRemoveUserSemaphore.release();
        System.out.println("Semaphore released. Status: " + testAndRemoveUserSemaphore.availablePermits());
    }
}