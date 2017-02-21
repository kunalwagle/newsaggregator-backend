package com.newsaggregator.db;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.newsaggregator.base.OutletArticle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kunalwagle on 04/02/2017.
 */
public class Articles {

    private final Table table;
    private DynamoDB database;

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
                e.printStackTrace();
            }
        }
    }

    private void writeArticle(OutletArticle article) {
        try {
            table.putItem(new Item()
                    .withPrimaryKey("articleUrl", article.getArticleUrl())
                    .withMap("info", article.createNonPrimaryHashMap()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateArticle(OutletArticle article) {
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("articleUrl", article.getArticleUrl())
                .withUpdateExpression("set #i=:val1 set #t=:val2 set #b=:val3 set #s=:val4")
                .withNameMap(new NameMap()
                        .with("#i", "imageUrl")
                        .with("#t", "title")
                        .with("#b", "body")
                        .with("#s", "source"))
                .withValueMap(new ValueMap()
                        .withString(":val1", article.getImageUrl())
                        .withString(":val2", article.getTitle())
                        .withString(":val3", article.getBody())
                        .withString(":val4", article.getSource()))
                .withReturnValues(ReturnValue.ALL_NEW);
        try {
            table.updateItem(updateItemSpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean articleExists(OutletArticle article) {
        GetItemSpec spec = new GetItemSpec()
                .withPrimaryKey("articleUrl", article.getArticleUrl());
        Item outcome;
        try {
            outcome = table.getItem(spec);
        } catch (Exception e) {
            return false;
        }
        return outcome != null;
    }

    public List<OutletArticle> getAllArticles() {
        List<OutletArticle> articles = new ArrayList<>();
        ItemCollection<ScanOutcome> items = table.scan();
        for (Item nextItem : items) {
            Map<String, Object> item = nextItem.asMap();
            String articleUrl = (String) item.get("articleUrl");
            Map<String, Object> info = (Map<String, Object>) item.get("info");
            String title = (String) info.get("Title");
            String body = (String) info.get("Body");
            String imageUrl = (String) info.get("ImageUrl");
            String source = (String) info.get("Source");
            String datePublished = (String) info.get("DatePublished");
            articles.add(new OutletArticle(title, body, imageUrl, articleUrl, source, datePublished));
        }
        return articles;
    }

    private Table getCollection() {
        return database.getTable("Articles");
    }

}
