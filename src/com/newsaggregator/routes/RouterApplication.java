package com.newsaggregator.routes;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 * Created by kunalwagle on 06/02/2017.
 */

public class RouterApplication extends Application {


    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/wikipedia/{searchTerm}", WikipediaSearchResource.class);
        return router;
    }
}
