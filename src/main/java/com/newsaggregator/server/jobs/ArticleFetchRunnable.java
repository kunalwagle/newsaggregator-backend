package com.newsaggregator.server.jobs;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.db.Articles;
import com.newsaggregator.server.ArticleFetch;

import java.util.List;

/**
 * Created by kunalwagle on 21/02/2017.
 */
public class ArticleFetchRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("Fetching articles");
        List<OutletArticle> articleList = ArticleFetch.fetchArticles();
        DynamoDB db = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build());
        Articles articleManager = new Articles(db);
        System.out.println("Sending articles");
        articleManager.saveArticles(articleList);
        System.out.println("Done");
    }

}
