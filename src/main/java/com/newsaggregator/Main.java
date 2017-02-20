package com.newsaggregator;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.newsaggregator.base.Outlet;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.base.Topic;
import com.newsaggregator.db.Articles;
import com.newsaggregator.ml.labelling.TopicLabelling;
import com.newsaggregator.ml.modelling.TopicModelling;
import com.newsaggregator.routes.RouterApplication;
import com.newsaggregator.server.ArticleFetch;
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

            DynamoDB db = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build());
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
//            List<OutletArticle> articles = articleCollection.getAllArticles();
//
//            List<Topic> topics = TopicModelling.trainTopics(articles);
//
//            TopicLabelling.generateTopicLabel(topics.get(0));

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
            System.out.println(e.toString());
        }
    }
}
