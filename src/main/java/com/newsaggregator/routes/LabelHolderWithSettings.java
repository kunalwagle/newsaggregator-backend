package com.newsaggregator.routes;

import com.newsaggregator.server.ClusterString;
import com.newsaggregator.server.LabelHolder;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by kunalwagle on 15/05/2017.
 */
public class LabelHolderWithSettings {

    private Comparator<ClusterString> byLastPublished = (left, right) -> {

        ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));

        SimpleDateFormat formatter = simpleDateFormatThreadLocal.get();
        try {

            int ldidx = left.getLastPublished().indexOf(".");
            int rdidx = right.getLastPublished().indexOf(".");

            String ld = left.getLastPublished();
            String rd = right.getLastPublished();

            if (ldidx != -1) {
                ld = ld.substring(0, ldidx).concat("+0000");
            } else {
                ld = ld.replaceAll("Z$", "+0000");
            }
            if (rdidx != -1) {
                rd = rd.substring(0, rdidx).concat("+0000");
            } else {
                rd = rd.replaceAll("Z$", "+0000");
            }

            ld = ld.replace("+00:00", "+0000");
            rd = rd.replace("+00:00", "+0000");


            Date leftDate = formatter.parse(ld);
            Date rightDate = formatter.parse(rd);

            return leftDate.compareTo(rightDate);

        } catch (ParseException e) {
            Logger.getLogger(getClass()).error("Error in last published", e);
        }
        return 1;
    };
    private LabelHolder labelHolder;
    private boolean digests;
    private List<String> sources;

    public LabelHolderWithSettings(LabelHolder labelHolder, int page, boolean digests, List<String> sources) {
        this.labelHolder = labelHolder;//distinct(labelHolder, page);
        this.digests = digests;
        this.sources = sources;
    }

    private LabelHolder distinct(LabelHolder labelHolder, int page) {
        List<ClusterString> clusterHolders = labelHolder.getClusters();
//        List<ClusterString> newClusterHolders = new ArrayList<>();
//        for (ClusterString clusterHolder : clusterHolders) {
//            if (!clusterExists(newClusterHolders, clusterHolder)) {
//                newClusterHolders.add(clusterHolder);
//            }
        int start = Math.max(clusterHolders.size() - 10 * page, 0);
        int end = clusterHolders.size() - 10 * page + 10;
        clusterHolders = clusterHolders.subList(start, end);
//        try {
//            clusterHolders = clusterHolders.stream().sorted(byLastPublished.reversed()).collect(Collectors.toList());
//        } catch (Exception e) {
//            Logger.getLogger(getClass()).error("Had an issue parsing dates", e);
//        }
        labelHolder.setClusters(clusterHolders);
        return labelHolder;
    }

    private boolean clusterExists(List<ClusterString> clusters, ClusterString otherCluster) {
        return clusters.stream().anyMatch(c -> c.getId().equals(otherCluster.getId()));
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
