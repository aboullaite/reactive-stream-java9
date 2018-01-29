package me.aboullaite.reactivestream;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

public class Main {
    public static void main(String[] args) {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        /** disable discovery and register classes manually */
        try (SeContainer container = initializer.initialize()) {
            container.select(TweetService.class);
        }
    }
}
