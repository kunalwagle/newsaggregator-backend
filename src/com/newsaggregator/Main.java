package com.newsaggregator;

import com.newsaggregator.routes.RouterApplication;
import org.restlet.Component;
import org.restlet.data.Protocol;

public class Main {

    public static void main(String[] args) throws Exception {

        Component component = new Component();

        component.getServers().add(Protocol.HTTP, 8182);

        component.getDefaultHost().attach("/api", new RouterApplication());

        component.start();


//        try {
//            MongoClient mongoClient = new MongoClient("localhost", 27017);
//
//            MongoDatabase db = mongoClient.getDatabase("NewsAggregator");
//
//            Articles articleCollection = new Articles(db);
//
//            articleCollection.saveArticles(ArticleFetch.fetchArticles());
//            List<OutletArticle> articles = articleCollection.getAllArticles();
//
//            System.out.println("Obtained articles: " + articles.size());
//
//
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
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
    }
}
