package com.java.masteringthreads.virtualthreads;

import com.java.masteringthreads.virtualthreads.renderer.BasicRenderer;
import com.java.masteringthreads.virtualthreads.renderer.SearchTerm;

import java.net.http.HttpRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;

// Run the code and observe how long it takes to complete. It should
//  be approximately 500 ms. Now change the probabilityOfFailure() method to
//  return 0.3 and run it again. What do we observe?
// on failure of one task the other task continue to execute and are not shutdown
// this can lead to wastage of resources
//  Change to use StructuredTaskScope.ShutdownOnFailure. Nested tasks
//  are not an issue with structured tasks. Once we have submitted all the
//  tasks, we need to join and to then throw the exception if any of the
//  tasks failed. Run it with a probabilityOfFailure of 0.0 and 0.3 and observe
//  the differences.
public class RendererVirtualThreads extends BasicRenderer {

    @Override
    protected double probabilityOfFailure() {
        return 0.3;
    }

    @Override
    public void renderPage(HttpRequest request) throws Exception {
        Collection<Future<?>> futures = new ConcurrentLinkedQueue<>();
        try (var service = Executors.
                newVirtualThreadPerTaskExecutor()) {
            futures.add(service.submit(() ->
                    createAdvertisingLinks(request))); // 3
            futures.add(service.submit(() -> {
                List<SearchTerm> terms =
                        scanForSearchTerms(request); // 1
                try (var subService = Executors.
                        newVirtualThreadPerTaskExecutor()) {
                    terms.forEach(term ->
                            futures.add(subService.submit(() ->
                                    collateResult(// 2 & 4
                                            term.searchOnPartnerSite()))));
                }
            }));
        }
        System.out.println("done");
        for (Future<?> future : futures) {
            future.get();
        }

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            scope.fork(() -> createAdvertisingLinks(request));
            scope.fork(() -> {
                List<SearchTerm> searchTerms = scanForSearchTerms(request);
                searchTerms.forEach(term -> {
                    scope.fork(() -> collateResult(term.searchOnPartnerSite()));
                });
                return null;
            });
            scope.join();
            System.out.println("done");
            scope.throwIfFailed();
        }
    }

    public static void main(String[] args) throws Exception {
        Instant startTime = Instant.now();
        new RendererVirtualThreads().renderPage(null);
        System.out.println("Time taken : " + Duration.between(startTime, Instant.now()).toMillis());
    }
}
