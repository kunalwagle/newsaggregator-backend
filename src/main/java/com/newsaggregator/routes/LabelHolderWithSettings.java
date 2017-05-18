package com.newsaggregator.routes;

import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.server.ClusterHolder;
import com.newsaggregator.server.LabelHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 15/05/2017.
 */
public class LabelHolderWithSettings {

    private LabelHolder labelHolder;
    private boolean digests;
    private List<String> sources;

    public LabelHolderWithSettings(LabelHolder labelHolder, boolean digests, List<String> sources) {
        this.labelHolder = distinct(labelHolder);
        this.digests = digests;
        this.sources = sources;
    }

    private LabelHolder distinct(LabelHolder labelHolder) {
        List<ClusterHolder> clusterHolders = labelHolder.getClusters();
        List<ClusterHolder> newClusterHolders = new ArrayList<>();
        for (ClusterHolder clusterHolder : clusterHolders) {
            if (!clusterExists(newClusterHolders, clusterHolder)) {
                newClusterHolders.add(clusterHolder);
            }
        }
        labelHolder.setClusters(newClusterHolders);
        return labelHolder;
    }

    private boolean clusterExists(List<ClusterHolder> clusters, ClusterHolder otherCluster) {
        List<OutletArticle> otherArticles = otherCluster.getArticles().stream().filter(Objects::nonNull).collect(Collectors.toList());
        return clusters.stream().anyMatch(ch -> ch.sameCluster(otherArticles));
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
