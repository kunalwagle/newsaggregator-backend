package com.newsaggregator.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.Utils;
import com.newsaggregator.base.DigestString;
import com.newsaggregator.db.Digests;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Created by kunalwagle on 18/05/2017.
 */
public class DigestResource extends ServerResource {

    @Get
    public String digests() {
        String digestId = (String) getRequestAttributes().get("digestId");
        try {
            Digests digests = new Digests(Utils.getDatabase());

            DigestString digestHolder = digests.getSingleDigest(digestId);

            return new ObjectMapper().writeValueAsString(digestHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
