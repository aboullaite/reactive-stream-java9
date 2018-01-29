package me.aboullaite.reactivestream;

import me.aboullaite.reactivestream.config.Property;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TweetPublisher implements Flow.Publisher {
    private static final int CORE_POOL_SIZE = 1;
    private static final int NB_THREADS = 1;
    private static final int INITIAL_DELAY = 1;
    private static final int DELAY = 5;
    @Inject
    @Property("consumerKey")
    private String _consumerKey;

    @Inject
    @Property("consumerSecret")
    private String _consumerSecret;

    @Inject
    @Property("accessToken")
    private String _accessToken;

    @Inject
    @Property("accessTokenSecret")
    private String _accesessTokenSecret;

    @Inject
    @Property("query")
    private String query;

    @Inject
    private Logger logger;

    private  Query twitterQuery;
    private final ExecutorService EXECUTOR = Executors.newFixedThreadPool(NB_THREADS);
    private Twitter twitter;
    private SubmissionPublisher<Status> sb = new SubmissionPublisher<Status>(EXECUTOR, Flow.defaultBufferSize());

    private Map<Long, Object> tweetCache = new HashMap<>();

    @PostConstruct
    public void setup() {
        twitterQuery = new Query(query);
        twitterQuery.setResultType(Query.ResultType.mixed);
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setOAuthConsumerKey(_consumerKey)
                .setOAuthConsumerSecret(_consumerSecret)
                .setOAuthAccessToken(_accessToken)
                .setOAuthAccessTokenSecret(_accesessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public void getTweets(){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
        Runnable tweets = () -> {
            try {
                twitter.search(twitterQuery).getTweets()
                        .stream()
                        .filter(status -> !tweetCache.containsKey(status.getId()))
                        .forEach(status -> {
                            tweetCache.put(status.getId(), null);
                            sb.submit(status);
                        });
            } catch (TwitterException e) {
                logger.log(Level.SEVERE, "AN error occured while fetching tweets: {}", e);
            }
        };
        executor.scheduleWithFixedDelay(tweets, INITIAL_DELAY, DELAY, TimeUnit.SECONDS);
    }

    @Override
    public void subscribe(Flow.Subscriber subscriber) {
        sb.subscribe(subscriber);
    }
}
