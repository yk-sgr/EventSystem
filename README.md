# Annotated Events
This API is a port of [JDA's AnnotatedEventManager](https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/core/hooks/AnnotatedEventManager.java) so you can use the EventManager without JDA dependencies and without relation to Discord.

# How to use?

`YourEvent.java`
```java
// Your custom event you want to call
public class YourEvent implements Event {
    
    private final yourVar...;
}
```


`EventListener.java`
```java

//Your event listener
public class EventListener {
    
    // Dont forget this annotation
    @SubscribeEvent
    public void onYourEvent(YourEvent event) {
        // do smth
    }
}

```


`The class or method where you wanto to initialize the EventManager`
```java

// Optionally you can pass an own ExecutorService
EventManager eventManager = new EventManager();

// Register your event listener
eventManager.register(new EventListener());

// Call an event
eventManager.handle(new YourEvent(...));

// or async
eventManager.handleAsync(new YourEvent(...));

```