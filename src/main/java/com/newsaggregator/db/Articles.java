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
                //e.printStackTrace();
            }
        }
    }

    public List<OutletArticle> articlesToAdd(List<OutletArticle> articles) {
        return articles.stream().filter(article -> !articleExists(article)).collect(Collectors.toList());
    }

    private void writeArticle(OutletArticle article) {
        try {
            table.putItem(new Item()
                    .withPrimaryKey("articleUrl", article.getArticleUrl(), "DatePublished", article.getLastPublished())
                    .withString("Body", article.getBody())
                    .withString("ImageUrl", article.getImageUrl())
                    .withString("Title", article.getTitle())
                    .withString("Source", article.getSource()));
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private void updateArticle(OutletArticle article) {
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("articleUrl", article.getArticleUrl(), "DatePublished", article.getLastPublished())
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
        try {
            table.updateItem(updateItemSpec);
        } catch (Exception e) {
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
                    Map<String, Object> info = (Map<String, Object>) item.get("info");
                    String title = (String) info.get("Title");
                    String body = (String) info.get("Body");
                    String imageUrl = (String) info.get("ImageUrl");
                    String source = (String) info.get("Source");
                    String datePublished = (String) info.get("DatePublished");
                    articles.add(new OutletArticle(title, body, imageUrl, articleUrl, source, datePublished));
                } catch (Exception e) {
                    logger.info("Caught an exception, but still chugging along");
                }
            }
        } catch (Exception e) {
            logger.info("Caught an exception");
        }
        logger.info("About to return articles");
        return articles;
    }

    private Table getCollection() {
        return database.getTable("Articles");
    }

}
