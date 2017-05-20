package com.newsaggregator.server.jobs;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.Utils;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.base.Topic;
import com.newsaggregator.db.Articles;
import com.newsaggregator.db.Topics;
import com.newsaggregator.ml.labelling.TopicLabelling;
import com.newsaggregator.ml.modelling.TopicModelling;
import com.newsaggregator.server.ArticleFetch;
import com.newsaggregator.server.LabelHolder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunalwagle on 21/02/2017.
 */
public class ArticleFetchRunnable implements Runnable {

    @Override
    public void run() {
        Logger logger = Logger.getLogger(getClass());

        List<OutletArticle> articleList = new ArrayList<>();

        MongoDatabase db = Utils.getDatabase();
        Articles articleManager = new Articles(db);
        Topics topics = new Topics(db);

        try {

            logger.info("Starting fetch");


            articleList = ArticleFetch.fetchArticles();
            List<OutletArticle> allArticles = articleManager.getAllArticles();
            logger.info("Starting filter");
            articleList.removeIf(art -> allArticles.stream().anyMatch(a -> a.getArticleUrl().equals(art.getArticleUrl())));
            articleList.forEach(outletArticle -> outletArticle.setLabelled(false));
            logger.info("Starting save");

            if (articleList.size() > 0) {
                articleManager.saveArticles(articleList);
            }

        } catch (Exception e) {
            logger.error("Caught an exception in ArticleFetchRunnable", e);
        }


        try {

            TopicModelling modelling = new TopicModelling();

            for (OutletArticle article : articleList) {
                if (article.getBody() == null || article.getBody().length() == 0) {
                    article.setLabelled(true);
                    articleManager.updateArticles(Lists.newArrayList(article));
                    continue;
                }

                Topic topic = modelling.getModel(article);
                List<String> topicLabels = TopicLabelling.generateTopicLabel(topic, article);

                if (topicLabels != null) {
                    for (String topicLabel : topicLabels) {
                        try {
                            LabelHolder labelHolder = topics.getTopic(topicLabel);
                            if (labelHolder == null) {
                                labelHolder = new LabelHolder(topicLabel);
                            }
                            labelHolder.addArticle(article);
                            labelHolder.setNeedsClustering(true);
                            article.setLabelled(true);
                            topics.saveTopic(labelHolder);
                            articleManager.updateArticles(Lists.newArrayList(article));
                        } catch (Exception e) {
                            logger.error("An error in the saving part of a topic label. Moving on", e);
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Caught an exception labelling new articles", e);
        }

//        try {
//            logger.info("Fetching articles");
//            MongoDatabase db = Utils.getDatabase();
//            Articles articleManager = new Articles(db);
//            Topics topicManager = new Topics(db);
//            Summaries summaryManager = new Summaries(db);
//            List<OutletArticle> articleList = ArticleFetch.fetchArticles();
//            logger.info("Getting database articles");
//            List<OutletArticle> allArticles = articleManager.getAllArticles();
//            logger.info("Getting database topics");
//            List<OutletArticle> finalAllArticles = allArticles;
//            articleList.removeIf(art -> finalAllArticles.stream().anyMatch(a -> a.getArticleUrl().equals(art.getArticleUrl())));
//            List<LabelHolder> topicLabels = topicManager.getAllTopics();
//            Map<String, LabelHolder> topicLabelMap = new HashMap<>();
//
//            for (LabelHolder holder : topicLabels) {
//                topicLabelMap.put(holder.getLabel(), holder);
//            }
//
//            logger.info("Starting topic modelling and labelling");
//
//            try {
//                TopicModelling topicModelling = new TopicModelling();
//                if (allArticles.size() < 150) {
//                    allArticles = articleList;
//                }
//                topicModelling.trainTopics(allArticles);
//                for (OutletArticle article : articleList) {
//                    try {
//                        int count = articleList.indexOf(article) + 1;
//                        logger.info("Modelling and labelling article " + count + " out of " + articleList.size());
//                        Topic topic = topicModelling.getModel(article);
//                        List<String> labels = TopicLabelling.generateTopicLabel(topic, article);
//                        for (String topicLabel : labels) {
//                            if (topicLabelMap.containsKey(topicLabel)) {
//                                topicLabelMap.get(topicLabel).addArticle(article);
//                            } else {
//                                LabelHolder newLabelHolder = new LabelHolder(topicLabel);
//                                newLabelHolder.addArticle(article);
//                                topicLabelMap.put(topicLabel, newLabelHolder);
//                            }
//                        }
//                    } catch (Exception e) {
//                        logger.error("An error", e);
//                        e.printStackTrace();
//                    }
//                }
//                logger.info("Completed topic labelling.");
//                logger.info("Starting clustering and summarising");
//
//                int counter = 1;
//                List<ClusterHolder> clusterHolderList = new ArrayList<>();
//                List<ClusterHolder> databaseClusters = summaryManager.getAllClusters();
//                for (Map.Entry<String, LabelHolder> topicLabel : topicLabelMap.entrySet()) {
//                    logger.info("Clustering topic " + counter + " out of " + topicLabelMap.size());
//                    List<OutletArticle> articles = topicLabel.getValue().getArticles();
//                    Clusterer clusterer = new Clusterer(articles);
//                    List<Cluster<ArticleVector>> clusters = clusterer.cluster();
//                    for (Cluster<ArticleVector> cluster : clusters) {
//                        try {
//                            List<OutletArticle> clusterArticles = cluster.getClusterItems().stream().map(ArticleVector::getArticle).collect(Collectors.toList());
//                            if (databaseClusters.stream().noneMatch(c -> c.sameCluster(clusterArticles))) {
//                                if (clusterHolderList.stream().anyMatch(c -> c.sameCluster(clusterArticles))) {
//                                    ClusterHolder clusterHolder = clusterHolderList.stream().filter(c -> c.sameCluster(clusterArticles)).findFirst().get();
//                                    clusterHolder.addLabel(topicLabel.getKey());
//                                } else {
//                                    if (!topicLabel.getValue().clusterExists(cluster)) {
//                                        ClusterHolder clusterHolder = new ClusterHolder(clusterArticles);
//                                        clusterHolder.addLabel(topicLabel.getKey());
//                                        clusterHolderList.add(clusterHolder);
//                                    }
//                                }
//                            }
//                        } catch (Exception e) {
//                            logger.error("Adding cluster error", e);
//                        }
//                    }
//                    counter++;
//                }
//
//                counter = 1;
//                for (ClusterHolder clusterHolder : clusterHolderList) {
//                    try {
//                        logger.info("Summarising cluster " + counter + " out of " + clusterHolderList.size());
//                        Set<OutletArticle> clusterArticles = new HashSet<>(clusterHolder.getArticles());
//                        Set<Set<OutletArticle>> permutations = Sets.powerSet(clusterArticles).stream().filter(set -> set.size() > 0).collect(Collectors.toSet());
//                        List<Extractive> extractives = permutations.stream().map(permutation -> new Extractive(new ArrayList<>(permutation))).collect(Collectors.toList());
//                        List<Summary> summaries = extractives.parallelStream().map(Extractive::summarise).collect(Collectors.toList());
//                        clusterHolder.setSummary(summaries.stream().map(Summary::getNodes).collect(Collectors.toList()));
//                        for (String topicLabel : clusterHolder.getLabels()) {
//                            LabelHolder labelHolder = topicLabelMap.get(topicLabel);
//                            labelHolder.addCluster(clusterHolder);
//                        }
//                    } catch (Exception e) {
//                        logger.error("Summarisation error", e);
//                    }
//                    counter++;
//                }
//                logger.info("Writing Articles");
//                articleManager.saveArticles(articleList);
//                logger.info("Articles saved");
//                logger.info("Writing Summaries");
//                summaryManager.saveSummaries(clusterHolderList);
//                logger.info("Summaries saved");
//                logger.info("Writing Topics");
//                topicManager.saveTopics(topicLabelMap.values());
//                logger.info("Topics saved");
//            } catch (Exception e) {
//                Utils.sendExceptionEmail(e);
//                logger.error("Error. Email sent", e);
//            }
//        } catch (Exception e) {
//            logger.error("An error", e);
//        }
    }

}
