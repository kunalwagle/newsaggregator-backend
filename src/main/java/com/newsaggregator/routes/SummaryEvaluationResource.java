package com.newsaggregator.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.api.outlets.Independent;
import com.newsaggregator.api.outlets.Reuters;
import com.newsaggregator.api.outlets.Telegraph;
import com.newsaggregator.base.OutletArticle;
import com.newsaggregator.ml.summarisation.Extractive.Extractive;
import com.newsaggregator.ml.summarisation.Summary;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kunalwagle on 04/04/2017.
 */
public class SummaryEvaluationResource extends ServerResource {

    @Post
    public String summary(Representation representation) {
        try {
            final Form form = new Form(representation);
            JSONObject jsonObject = new JSONObject(form.get(0).getValue().substring(10));
            String firstUrl = jsonObject.getString("first");
            String secondUrl = jsonObject.getString("second");
            String thirdUrl = jsonObject.getString("third");
            String firstBody = new Independent().getSingleArticle(firstUrl);
            String secondBody = new Telegraph().getSingleArticle(secondUrl);
            String thirdBody = new Reuters().getSingleArticle(thirdUrl);
            List<OutletArticle> templateArticles = new ArrayList<>();
            templateArticles.add(new OutletArticle("First", firstBody, null, firstUrl, "First", null));
            templateArticles.add(new OutletArticle("Second", secondBody, null, secondUrl, "Second", null));
            templateArticles.add(new OutletArticle("Third", thirdBody, null, thirdUrl, "Third", null));
            Summary extractiveSummary = new Extractive(templateArticles).summarise();
//            Summary abstractiveSummary = new Abstractive(extractiveSummary).summarise();
            Map<String, String> summaryMap = new HashMap<>();
            summaryMap.put("extractive", extractiveSummary.getText());
            return new ObjectMapper().writeValueAsString(summaryMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
