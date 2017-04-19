package com.newsaggregator.db;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.newsaggregator.base.OutletArticle;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by kunalwagle on 04/02/2017.
 */
public class Articles {

    private final Table table;
    private DynamoDB database;
    private Logger logger = Logger.getLogger(getClass());

    public Articles(DynamoDB database) {
        this.database = database;
        this.table = getCollection();
    }

    public void saveArticles(List<OutletArticle> articles) {
        for (OutletArticle article : articles) {
            try {
                if (articleExists(article)) {
                    updateArticle(article);
                } else {
                    writeArticle(article);
                }
            } catch (Exception e) {
                logger.error("Saving articles error", e);
            }
        }
    }

    public List<OutletArticle> articlesToAdd(List<OutletArticle> articles) {
        return articles.stream().filter(article -> !articleExists(article)).collect(Collectors.toList());
    }

    private void writeArticle(OutletArticle article) {
        try {
            table.putItem(new Item()
                    .withPrimaryKey("articleUrl", article.getArticleUrl(), "datePublished", article.getLastPublished())
                    .withString("Body", article.getBody())
                    .withString("ImageUrl", article.getImageUrl())
                    .withString("Title", article.getTitle())
                    .withString("Source", article.getSource()));
        } catch (Exception e) {
            logger.error("Writing articles error", e);
            //e.printStackTrace();
        }
    }

    private void updateArticle(OutletArticle article) {
        try {
            UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                    .withPrimaryKey("articleUrl", article.getArticleUrl(), "datePublished", article.getLastPublished())
                    .withUpdateExpression("set #i=:val1, #t=:val2, #b=:val3, #s=:val4")
                    .withNameMap(new NameMap()
                            .with("#i", "ImageUrl")
                            .with("#t", "Title")
                            .with("#b", "Body")
                            .with("#s", "Source"))
                    .withValueMap(new ValueMap()
                            .withString(":val1", article.getImageUrl())
                            .withString(":val2", article.getTitle())
                            .withString(":val3", article.getBody())
                            .withString(":val4", article.getSource()))
                    .withReturnValues(ReturnValue.ALL_NEW);
            table.updateItem(updateItemSpec);
        } catch (Exception e) {
            logger.error("Updating Article error", e);
            //e.printStackTrace();
        }
    }

    private boolean articleExists(OutletArticle article) {
        try {
            QuerySpec spec = new QuerySpec()
                    .withKeyConditionExpression("articleUrl = :a_url")
                    .withValueMap(new ValueMap()
                            .withString(":a_url", article.getArticleUrl()));
            ItemCollection<QueryOutcome> items = table.query(spec);
            Iterator<Item> iterator = items.iterator();
            return iterator.next() != null;
        } catch (Exception e) {
            return false;
        }
    }

    public List<OutletArticle> getAllArticles() {
        List<OutletArticle> articles = new ArrayList<>();
        try {
            logger.info("Starting to scan database");
            ItemCollection<ScanOutcome> items = table.scan();
            logger.info("Scanned. Got " + items.getAccumulatedItemCount() + " items");
            for (Item nextItem : items) {
                logger.info("Adding an item");
                try {
                    Map<String, Object> item = nextItem.asMap();
                    String articleUrl = (String) item.get("articleUrl");
                    String title = (String) item.get("Title");
                    String body = (String) item.get("Body");
                    String imageUrl = (String) item.get("ImageUrl");
                    String source = (String) item.get("Source");
                    String datePublished = (String) item.get("datePublished");
                    articles.add(new OutletArticle(title, body, imageUrl, articleUrl, source, datePublished));
                } catch (Exception e) {
                    logger.error("Reading articles exception", e);
                }
            }
        } catch (Exception e) {
            logger.error("Caught an exception", e);
        }
        logger.info("About to return articles");
        return articles;
    }

    private Table getCollection() {
        return database.getTable("Articles");
    }

}
