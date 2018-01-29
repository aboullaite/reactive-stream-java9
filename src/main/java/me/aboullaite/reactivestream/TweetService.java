package me.aboullaite.reactivestream;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.interceptor.Interceptor;


@ApplicationScoped
public class TweetService {
    @Inject
    private TweetPublisher tweetPublisher;
    @Inject
    private TweetSubscriber subscriber;

    @PostConstruct
    public void subscribeToTweets() {
        tweetPublisher.subscribe(subscriber);
    }

    public void init(@Observes @Priority(Interceptor.Priority.APPLICATION - 100)
                     @Initialized(ApplicationScoped.class) Object init) throws Exception{
        tweetPublisher.getTweets();
    }


}
