package com.java.masteringthreads.virtualthreads.renderer;

import java.io.IOException;

public class SearchTerm {
    private final String url;
    private final BasicRenderer renderer;

    public SearchTerm(String url, BasicRenderer renderer) {
        this.url = url;
        this.renderer = renderer;
    }

    public SearchResult searchOnPartnerSite() throws IOException {
        return searchOnPartnerSite(0);
    }

    public SearchResult searchOnPartnerSite(int randomLatency) throws IOException {
        System.out.println("searchOnPartnerSite() started " + Thread.currentThread() + " for " + url);
        int i = Integer.parseInt(url.substring(6));
        BasicRenderer.sleepSilently(renderer.searchOnPartnerSiteDelay(), randomLatency);
        System.out.println("searchOnPartnerSite() done " + Thread.currentThread() + " for " + url);
        if (Math.random() < renderer.probabilityOfFailure())
            throw new IOException("Network error");
        return new SearchResult(i);
    }
}
