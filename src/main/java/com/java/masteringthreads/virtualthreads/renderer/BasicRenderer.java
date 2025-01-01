package com.java.masteringthreads.virtualthreads.renderer;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public abstract class BasicRenderer {
    public abstract void renderPage(HttpRequest request) throws Exception;

    protected double probabilityOfFailure() {
        return 0.0;
    }

    protected int scanForSearchTermsDelay() {
        return 50;
    }

    protected int advertisingLinksDelay() {
        return 200;
    }

    protected int collateResultsDelay() {
        return 200;
    }

    protected int searchOnPartnerSiteDelay() {
        return 200;
    }

    protected final List<SearchTerm> scanForSearchTerms(HttpRequest request) {
        System.out.println("scanForSearchTerms() started " + Thread.currentThread());
        List<SearchTerm> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            result.add(new SearchTerm("step1-" + i, this));
            sleepSilently(scanForSearchTermsDelay() / 10);
        }
        System.out.println("scanForSearchTerms() done " + Thread.currentThread());
        return result;
    }

    protected final Void createAdvertisingLinks(HttpRequest request) {
        System.out.println("createAdvertisingLinks() started " + Thread.currentThread());
        sleepSilently(advertisingLinksDelay());
        System.out.println("createAdvertisingLinks() done " + Thread.currentThread());
        return null;
    }

    protected final int collateResult(SearchResult result) {
        System.out.println("collateResult() started " + Thread.currentThread() + " for id=" + result.id());
        sleepSilently(collateResultsDelay());
        System.out.println("collateResult() done " + Thread.currentThread() + " for id=" + result.id());
        return result.id();
    }

    static void sleepSilently(int millis) {
        sleepSilently(millis, 0);
    }

    static void sleepSilently(int millis, int randomDelta) {
        try {
            if (randomDelta > 0) millis += ThreadLocalRandom.current().nextInt(randomDelta);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CancellationException("interrupted");
        }
    }

    public static final int CPU_POOL_SIZE = 4;
    public static final int IO_POOL_SIZE = 4;


    public static ExecutorService renderPool() {
        return Executors.newFixedThreadPool(CPU_POOL_SIZE, new ThreadFactory() {
            private final ThreadFactory factory = Executors.defaultThreadFactory();

            public Thread newThread(Runnable r) {
                Thread thread = factory.newThread(r);
                thread.setName("RenderPool-" + thread.getName());
                return thread;
            }
        });
    }

    public static ExecutorService downloadPool() {
        return Executors.newFixedThreadPool(IO_POOL_SIZE, new ThreadFactory() {
            private final ThreadFactory factory = Executors.defaultThreadFactory();

            public Thread newThread(Runnable r) {
                Thread thread = factory.newThread(r);
                thread.setName("DownloadPool-" + thread.getName());
                return thread;
            }
        });
    }
}
