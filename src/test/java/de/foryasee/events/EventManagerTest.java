package de.foryasee.events;

import org.junit.Test;

public class EventManagerTest {

    @Test
    public void testEventHandler() {
        EventManager eventManager = new EventManager();
        eventManager.register(this);
        eventManager.handle(new ItWorksEvent());
    }

    @SubscribeEvent
    public void onItWorks(ItWorksEvent event) {
        System.out.printf("It Works: %b", event.really);
    }

    public class ItWorksEvent implements Event {

        private final boolean really = true;
    }
}
