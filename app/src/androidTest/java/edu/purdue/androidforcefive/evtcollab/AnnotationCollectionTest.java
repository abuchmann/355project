package edu.purdue.androidforcefive.evtcollab;

import android.test.InstrumentationTestCase;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.Annotation;
import edu.purdue.androidforcefive.evtcollab.DataCollections.AnnotationCollection;
import edu.purdue.androidforcefive.evtcollab.DataCollections.Interfaces.IDataCollectionChanged;


/**
 * Created by abuchmann on 22.11.2015.
 */
public class AnnotationCollectionTest extends InstrumentationTestCase implements IDataCollectionChanged {
    //CountDownLatch signal = new CountDownLatch(1);
    Semaphore semaphore = new Semaphore(1);
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" );
    private static final int NUMBER_OF_TESTS = 2; // count your tests here
    private static int sTestsRun = 0;

    @Test
    public void testGetAnnotations() throws Exception {
        AnnotationCollection.getInstance().addListener(this);
        AnnotationCollection.getInstance().initializeAnnotations();
        semaphore.tryAcquire(5, TimeUnit.SECONDS);

        assertNotNull(AnnotationCollection.getInstance().getAnnotations());
    }

    @Test
    public void testAddAndRemoveAnnotation() throws InterruptedException {
        Annotation testAnnotation = new Annotation(){{
            message = "TestAnnotation";
            eventId = 1;
            authorId = 2;
        }};

        AnnotationCollection.getInstance().addListener(this);
        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        AnnotationCollection.getInstance().initializeAnnotations();

        // Let's wait for the annotations to be added and the semaphore to be released;
        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        AnnotationCollection.getInstance().addAnnotation(testAnnotation);

        // Let's wait for the annotation to be added and the semaphore to be released
        semaphore.tryAcquire(10, TimeUnit.SECONDS);


        for (Annotation annotation : AnnotationCollection.getInstance().getAnnotations()) {
            System.out.println(annotation.getId() + " " + annotation.getMessage());
            if (annotation.contentEquals(testAnnotation) &&
                    annotation.getId() != 0) {
                testAnnotation = annotation;
            }
        }
        assertTrue(testAnnotation.getId() != 0);
        System.out.println("Semaphore status: " + semaphore.availablePermits());

        testAnnotation.setMessage("Lord");
        testAnnotation.save();

        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        for (Annotation annotation : AnnotationCollection.getInstance().getAnnotations()) {
            System.out.println(annotation.getId() + " " + annotation.getMessage());
            if (annotation.getId() == testAnnotation.getId()) {
                assertEquals(annotation.getMessage(), "Lord");
            }
        }

        testAnnotation.destroy();
        semaphore.tryAcquire(10, TimeUnit.SECONDS);
        assertFalse(AnnotationCollection.getInstance().getAnnotations().contains(testAnnotation));
    }

    @Override
    public void onCollectionChanged() {
        semaphore.release();
        System.out.println("Semaphore released. Status: " + semaphore.availablePermits());
    }
}
