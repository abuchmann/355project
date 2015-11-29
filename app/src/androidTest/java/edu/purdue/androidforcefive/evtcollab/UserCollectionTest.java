package edu.purdue.androidforcefive.evtcollab;

import android.test.InstrumentationTestCase;

import org.junit.Test;


import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.User;
import edu.purdue.androidforcefive.evtcollab.DataCollections.Interfaces.IDataCollectionChanged;
import edu.purdue.androidforcefive.evtcollab.DataCollections.UserCollection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


/**
 * Created by abuchmann on 22.11.2015.
 */
public class UserCollectionTest extends InstrumentationTestCase implements IDataCollectionChanged {
    //CountDownLatch signal = new CountDownLatch(1);
    Semaphore semaphore = new Semaphore(1);
    private static final int NUMBER_OF_TESTS = 2; // count your tests here
    private static int sTestsRun = 0;

    @Test
    public void testGetUsers() throws Exception {
        UserCollection.getInstance().addListener(this);
        UserCollection.getInstance().initializeUsers();
        semaphore.tryAcquire(5, TimeUnit.SECONDS);

        assertNotNull(UserCollection.getInstance().getUsers());
    }

    @Test
    public void testAddAndRemoveUser() throws InterruptedException {
        User testUser = new User("Paul", "Breitner", "pbreitner", "testpw", "paul@breitner.de");

        UserCollection.getInstance().addListener(this);
        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        UserCollection.getInstance().initializeUsers();

        // Let's wait for the users to be added and the semaphore to be released;
        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        UserCollection.getInstance().addUser(testUser);

        // Let's wait for the user to be added and the semaphore to be released
        semaphore.tryAcquire(10, TimeUnit.SECONDS);


        for (User user : UserCollection.getInstance().getUsers()) {
            System.out.println(user.getId() + " " + user.getLastName());
            if (user.getFirstName().equals(testUser.getFirstName()) &&
                    user.getLastName().equals(testUser.getLastName()) &&
                    user.getUserName().equals(testUser.getUserName()) &&
                    user.getPassword().equals(testUser.getPassword()) &&
                    user.geteMail().equals(testUser.geteMail()) &&
                    user.getId() != 0) {
                testUser = user;
            }
        }
        assertTrue(testUser.getId() != 0);
        System.out.println("Semaphore status: " + semaphore.availablePermits());

        testUser.setFirstName("Lord");
        testUser.save();

        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        for (User user : UserCollection.getInstance().getUsers()) {
            System.out.println(user.getId() + " " + user.getLastName());
            if (user.getId() == testUser.getId()) {
                assertEquals(user.getFirstName(), "Lord");
            }
        }

        testUser.destroy();
        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        assertFalse(UserCollection.getInstance().getUsers().contains(testUser));
    }

    @Override
    public void onCollectionChanged() {
        semaphore.release();
        System.out.println("Semaphore released. Status: " + semaphore.availablePermits());
    }
}
