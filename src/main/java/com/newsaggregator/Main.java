package com.newsaggregator;

import com.newsaggregator.routes.RouterApplication;
import com.newsaggregator.server.jobs.ArticleFetchRunnable;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.service.TaskService;

import java.util.concurrent.TimeUnit;


public class Main {

    public static void main(String[] args) throws Exception {

        Component component = new Component();

        component.getServers().add(Protocol.HTTP, 8182);

        component.getDefaultHost().attach("/api", new RouterApplication());

        component.start();


        try {

            TaskService scheduleManager = new TaskService();

            scheduleManager.scheduleAtFixedRate(new ArticleFetchRunnable(), 60L, 600L, TimeUnit.SECONDS);

//            DynamoDB db = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build());
//            Articles articles = new Articles(db);
//            articles.getAllArticles();
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
            e.printStackTrace();
        }
    }
}
