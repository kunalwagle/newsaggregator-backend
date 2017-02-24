package com.newsaggregator.base;

import java.util.List;

/**
 * Created by kunalwagle on 24/02/2017.
 */
public class CandidateLabel {

    private String label;
    private List<Outlink> outlinks;

    public CandidateLabel(String label, List<Outlink> outlinks) {
        this.label = label;
        this.outlinks = outlinks;
    }
}
