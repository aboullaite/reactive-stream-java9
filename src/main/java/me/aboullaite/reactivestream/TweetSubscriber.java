package me.aboullaite.reactivestream;

import me.aboullaite.reactivestream.model.Tweet;
import twitter4j.Status;
import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.Flow;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TweetSubscriber implements Flow.Subscriber<Status> {

    private final String id = UUID.randomUUID().toString();
    private Flow.Subscription subscription;
    private static final int SUB_REQ=1;
    private static final int SLEEP=1000;
    @Inject
    private Logger logger ;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        logger.info(  "subscriber: "+ id +" ==> Subscribed");
        this.subscription = subscription;
        subscription.request(SUB_REQ);
    }

    @Override
    public void onNext(Status status) {
        try {
            Thread.sleep(SLEEP);
        } catch (InterruptedException e) {
           logger.log(Level.SEVERE,"An error has occured: {}", e);
        }
        Tweet t = new Tweet(status.getUser().getScreenName(), status.getText(), status.getCreatedAt());
        logger.info("New Tweet: --->");
        System.out.println(t.toString());
        subscription.request(SUB_REQ);
    }

    @Override
    public void onError(Throwable throwable) {
        logger.log(Level.SEVERE, "An error occured: {}", throwable);
    }

    @Override
    public void onComplete() {
        logger.info("Done!");
    }
}
