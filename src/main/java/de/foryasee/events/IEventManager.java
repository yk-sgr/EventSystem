package de.foryasee.events;

public interface IEventManager {

    void register(Object listener);

    void unregister(Object listener);

    void handle(Event event);

    void handleAsync(Event event);
}
