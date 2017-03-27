package com.newsaggregator;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.newsaggregator.api.outlets.Reuters;
import com.newsaggregator.base.LabelledArticle;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.base.Topic;
import com.newsaggregator.base.TopicLabel;
import com.newsaggregator.db.Articles;
import com.newsaggregator.ml.labelling.TopicLabelling;
import com.newsaggregator.ml.modelling.TopicModelling;
import com.newsaggregator.routes.RouterApplication;
import org.restlet.Component;
import org.restlet.data.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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
            List<OutletArticle> articleList = new Reuters().getArticles();
//            List<String> strings = articleList.stream().map(OutletArticle::getBody).collect(Collectors.toList());
//            TfIdf tfidf = new TfIdf(strings);
//            tfidf.performTfIdf(strings.get(1), "Trump");
//            Articles articleManager = new Articles(db);
//            System.out.println("Sending articles");
//            articleManager.saveArticles(articleList);
//            System.out.println("Done");

//            MongoClient mongoClient = new MongoClient("localhost", 27017);
//
//            MongoDatabase db = mongoClient.getDatabase("NewsAggregator");
//
            Articles articleCollection = new Articles(db);
//
            List<OutletArticle> articles = articleCollection.getAllArticles();
            System.out.println("Starting modelling");
            TopicModelling modelling = new TopicModelling();
            List<TopicLabel> topics = modelling.trainTopics(articles);
//            modelling.getModel(articleList.get(0).getBody());
            System.out.println("Finished modelling");
//           Topics topicDB = new Topics(db);
//            List<TopicLabel> labels = topicDB.getAllTopics();
//            topicDB.saveTopics(topics);


            List<LabelledArticle> labelledArticleList = new ArrayList<>();

            Random rand = new Random();

            for (int i = 0; i < 5; i++) {
                System.out.println("Now on article " + i);
                int index = rand.nextInt(articles.size());
                Topic topic = modelling.getModel(articles.get(index));
                List<String> topicLabel = TopicLabelling.generateTopicLabel(topic);
                labelledArticleList.add(new LabelledArticle(topicLabel, articles.get(index)));
            }

            for (int i = 0; i < 5; i++) {
                System.out.println("Article Title: " + labelledArticleList.get(i).getArticle().getTitle());
                System.out.println("Article Body: " + labelledArticleList.get(i).getArticle().getBody());
                System.out.println("");
                System.out.print("Labels: ");
                System.out.println(labelledArticleList.get(i).getLabels().toString());
            }


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
