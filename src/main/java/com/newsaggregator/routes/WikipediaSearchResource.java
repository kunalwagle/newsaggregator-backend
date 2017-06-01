package com.newsaggregator.routes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.api.Wikipedia;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Created by kunalwagle on 06/02/2017.
 */
public class WikipediaSearchResource extends ServerResource {

    @Get("json")
    public String articles() {
        String searchTerm = (String) getRequestAttributes().get("searchTerm");
        try {
            return new ObjectMapper().writeValueAsString(Wikipedia.getArticles(searchTerm, 10));
        } catch (JsonProcessingException e) {
            //e.printStackTrace();
            return null;
        }
    }

}
