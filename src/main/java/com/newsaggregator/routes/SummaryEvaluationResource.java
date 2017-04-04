package com.newsaggregator.routes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.summarisation.Extractive.Extractive;
import com.newsaggregator.ml.summarisation.Summary;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kunalwagle on 04/04/2017.
 */
public class SummaryEvaluationResource extends ServerResource {

    @Get("json")
    public String summary() {
        List<String> texts = new ArrayList<>();
        texts.add((String) getRequestAttributes().get("first"));
        texts.add((String) getRequestAttributes().get("second"));
        texts.add((String) getRequestAttributes().get("third"));
        List<OutletArticle> templateArticles = new ArrayList<>();
        templateArticles.add(new OutletArticle("First", texts.get(0), null, null, "First", null));
        templateArticles.add(new OutletArticle("Second", texts.get(1), null, null, "Second", null));
        templateArticles.add(new OutletArticle("Third", texts.get(2), null, null, "Third", null));
        try {
            Summary extractiveSummary = new Extractive(templateArticles).summarise();
//            Summary abstractiveSummary = new Abstractive(extractiveSummary).summarise();
            Map<String, String> summaryMap = new HashMap<>();
            summaryMap.put("extractive", extractiveSummary.getText());
            return new ObjectMapper().writeValueAsString(summaryMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
