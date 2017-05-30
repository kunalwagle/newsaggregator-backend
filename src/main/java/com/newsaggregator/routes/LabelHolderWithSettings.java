package com.newsaggregator.routes;

import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.server.ClusterHolder;
import com.newsaggregator.server.LabelHolder;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 15/05/2017.
 */
public class LabelHolderWithSettings {

    private Comparator<ClusterHolder> byLastPublished = (left, right) -> {

        ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));

        SimpleDateFormat formatter = simpleDateFormatThreadLocal.get();
        try {

            Date leftDate = formatter.parse(left.getLastPublished().replaceAll("Z$", "+0000"));
            Date rightDate = formatter.parse(right.getLastPublished().replaceAll("Z$", "+0000"));

            return leftDate.compareTo(rightDate);

        } catch (ParseException e) {
            Logger.getLogger(getClass()).error("Error in last published", e);
        }
        return 1;
    };
    private LabelHolder labelHolder;
    private boolean digests;
    private List<String> sources;

    public LabelHolderWithSettings(LabelHolder labelHolder, boolean digests, List<String> sources) {
        this.labelHolder = distinct(labelHolder);
        this.labelHolder.setClusters(this.labelHolder.getClusters().stream().filter(c -> c.getSummaryMap().size() > 0).collect(Collectors.toList()));
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
        newClusterHolders = newClusterHolders.stream().sorted(byLastPublished.reversed()).collect(Collectors.toList());
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

    public void setLabelHolder(LabelHolder labelHolder) {
        this.labelHolder = labelHolder;
    }

    public boolean isDigests() {
        return digests;
    }

    public void setDigests(boolean digests) {
        this.digests = digests;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }
}
