package com.newsaggregator;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.newsaggregator.base.TopicLabel;
import com.newsaggregator.db.Topics;
import com.newsaggregator.ml.labelling.TopicLabelling;
import com.newsaggregator.routes.RouterApplication;
import org.restlet.Component;
import org.restlet.data.Protocol;

import java.util.List;


public class Main {

    public static void main(String[] args) throws Exception {

        Component component = new Component();

        component.getServers().add(Protocol.HTTP, 8182);

        component.getDefaultHost().attach("/api", new RouterApplication());

        component.start();


        try {

            //CandidateLabel label = Wikipedia.getOutlinksAndCategories("Moose_Jaw_Civic_Centre");

            // System.out.println(label);

//            TaskService scheduleManager = new TaskService();

//            scheduleManager.scheduleAtFixedRate(new ArticleFetchRunnable(), 60L, 600L, TimeUnit.SECONDS);

            DynamoDB db = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build());
//            Articles articles = new Articles(db);
//            articles.getAllTopics();
//
//            Table table = db.getTable("Articles");
//
//            OutletArticle article = new OutletArticle("A title", "The body of an article", "www.imageUrl.com", "www.sampleArticleUrl.com", Outlet.Guardian.getSourceString());
//
//            ObjectMapper mapper = new ObjectMapper();
//
//            System.out.println("Adding a new item...");
//            PutItemOutcome outcome = table.putItem(new Item()
//                    .withPrimaryKey("articleUrl", article.getArticleUrl())
//                    .withMap("info", article.createNonPrimaryHashMap()));
//
//            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
//            System.out.println("Fetching articles");
//            List<OutletArticle> articleList = ArticleFetch.fetchArticles();
//            Articles articleManager = new Articles(db);
//            System.out.println("Sending articles");
//            articleManager.saveArticles(articleList);
//            System.out.println("Done");

//            MongoClient mongoClient = new MongoClient("localhost", 27017);
//
//            MongoDatabase db = mongoClient.getDatabase("NewsAggregator");
//
//            Articles articleCollection = new Articles(db);
//
//            List<OutletArticle> articles = articleCollection.getAllTopics();
//            System.out.println("Starting modelling");
//            List<TopicLabel> topics = TopicModelling.trainTopics(articles);
//            System.out.println("Finished modelling");
            Topics topicDB = new Topics(db);
            List<TopicLabel> labels = topicDB.getAllTopics();
//            topicDB.saveTopics(topics);
            TopicLabelling.generateTopicLabel(labels.get(0).getTopic());

////            TOI ap = new TOI();
////            List<OutletArticle> articles = new ArrayList<>(ap.getArticles());
////            for (OutletArticle article : articles) {
////                System.out.println(article.getBody());
////            }
////            articles.addAll(Guardian.getArticles());
////            articles.addAll(new Independent().getArticles());
////            List<WikipediaArticle> wikipediaArticleList = new ArrayList<>(Wikipedia.getArticles("Tim Cook"));
////            wikipediaArticleList.stream().forEach(wikipediaArticle -> System.out.println(wikipediaArticle.getTitle() + ": " + wikipediaArticle.getExtract()));
//            //TopicModelling.trainTopics(articles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
