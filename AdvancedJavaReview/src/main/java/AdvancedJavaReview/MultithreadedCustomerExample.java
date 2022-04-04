package AdvancedJavaReview;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultithreadedCustomerExample {

    public static void main(String[] args) throws InterruptedException {
        /*
        Threads: independent execution of code
            Means: multiple threads can run concurrently
            What's it good for:
                good for large computations if we have multiple processors and can
                split the problem up.

                Or, for cases where, even with 1 processor, there is a lot of waiting/IO/network
                such that we can run other tasks while we wait for that to complete.

        The first way to make a thread:
            extend thread class
            override the run() method.
            Does executing run() on an object that extends thread run a new Thread?
            No we need to call start(), which will call run() by itself
            does anyone actually use this anymore?
            No.  Because there are better ways.

            Better ways:
                using an executorService, we have more control over threads
                with this method, we use objects that implement runnable and callable interfaces

                It's actually more efficient because we can implement executorService in such a way
                that wwe create a pool of reusable threads at startup rather than recreating threads

         */
        CustomerAccount account = new CustomerAccount(1, 500);
        ExecutorService es = Executors.newFixedThreadPool(2);
        CustomerPurchaseThread thread1 = new CustomerPurchaseThread(1, 500, 100, account);
        CustomerPurchaseThread thread2 = new CustomerPurchaseThread(1, 500, 200, account);
        es.submit(thread1);
        es.submit(thread2);

        es.shutdown();
        es.awaitTermination(100, TimeUnit.MICROSECONDS);

        Thread.sleep(1000);

        System.out.println("Customer " + account.getCustomerId() + " now has balance of " +
                account.getBalance());

        /*
            Race condition: Threads have unpredictable behavior that results from not knowing what threads execute in what
            order or when they will be preempted (execution paused by processor
            after the thread has run enough)

            To solve this:
            we 'lock' sensitive code such that only a single thread can access certain code at a time
            in java: using the synchronized keyword on a method or synchronized block

            problems:
                When we introduce locking code,
                we may introduce a situation where  two threads are waiting for each other to complete
                This is called a deadlock: neither thread may proceed because they are stuck waiting
            Additional engineering cost:
                To avoid problems related to unpredictability, we have to put in significant engineering
                effort to ensure that those problems do not occur: because race conditions and deadlocking
                are both fundamentally rare, and difficult to produce events
         */

        /*
            How come we can modify objects in a thread?
                each thread contains its own stack, but all threads share the heap

            stack:
                primitive variables,
                callstack (the stack of called methods)
            Heap:
                Is where objects in memory are stored (memory references for objects point to here),
                Source of the string pool
            Garbage Collection: something that Java in particular is good at,
            Objects which the JRE knows will no longer be used and will be removed from memory
                (meaning we have lost the memory reference to it)
         */
    }
}
