package com.newsaggregator.routes;

import com.newsaggregator.server.LabelHolder;

import java.util.List;

/**
 * Created by kunalwagle on 15/05/2017.
 */
public class LabelHolderWithSettings {

    private LabelHolder labelHolder;
    private boolean digests;
    private List<String> sources;

    public LabelHolderWithSettings(LabelHolder labelHolder, boolean digests, List<String> sources) {
        this.labelHolder = labelHolder;
        this.digests = digests;
        this.sources = sources;
    }

    public LabelHolder getLabelHolder() {
        return labelHolder;
    }

    public boolean isDigests() {
        return digests;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setLabelHolder(LabelHolder labelHolder) {
        this.labelHolder = labelHolder;
    }

    public void setDigests(boolean digests) {
        this.digests = digests;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }
}
