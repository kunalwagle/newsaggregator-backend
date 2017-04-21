package com.newsaggregator.routes;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.service.CorsService;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by kunalwagle on 06/02/2017.
 */

public class RouterApplication extends Application {

    public RouterApplication() {
        CorsService corsService = new CorsService();
        corsService.setAllowedOrigins(new HashSet(Arrays.asList("*")));
        corsService.setAllowedCredentials(true);
        getServices().add(corsService);
    }


    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/wikipedia/{searchTerm}", WikipediaSearchResource.class);
        router.attach("/summarise/", SummaryEvaluationResource.class);
        router.attach("/topic/{topic}", TopicResource.class);
        router.attach("/user/subscribe/{user}/{topic}", SubscribeUserResource.class);
        router.attach("/user/subscriptions/{user}", SubscriptionsResource.class);
        return router;
    }
}
