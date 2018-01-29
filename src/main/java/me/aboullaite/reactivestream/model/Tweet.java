package me.aboullaite.reactivestream.model;

import java.util.Date;

public class Tweet {
    String user, tweet;
    Date tweetDate;

    public static final String BLUE = "\u001B[34m";
    //Reset code
    public static final String RESET = "\u001B[0m";

    public Tweet(String user, String tweet, Date tweetDate) {
        this.user = user;
        this.tweet = tweet;
        this.tweetDate = tweetDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Date getTweetDate() {
        return tweetDate;
    }

    public void setTweetDate(Date tweetDate) {
        this.tweetDate = tweetDate;
    }

    @Override
    public String toString() {
        return  "@"+ user + " tweeted: '"+ BLUE + tweet + RESET + "' [" + tweetDate + "]";
    }
}
