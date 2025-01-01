package com.java.masteringthreads.virtualthreads;

import com.java.masteringthreads.virtualthreads.renderer.BasicRenderer;
import com.java.masteringthreads.virtualthreads.renderer.SearchTerm;

import java.net.http.HttpRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

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
