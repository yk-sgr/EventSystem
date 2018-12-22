# Annotated Events
This API is a port of [JDA's AnnotatedEventManager](https://github.com/DV8FromTheWorld/JDA/blob/master/src/main/java/net/dv8tion/jda/core/hooks/AnnotatedEventManager.java) so you can use the EventManager without JDA dependencies and without relation to Discord.

[![](https://jitpack.io/v/ForYaSee/EventSystem.svg)](https://jitpack.io/#ForYaSee/EventSystem)

# Download

**Maven**
```xml
<repositories>
    <repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```
```xml
<dependency>
    <groupId>com.github.ForYaSee</groupId>
    <artifactId>EventSystem</artifactId>
	<version>v1.0.0</version>
</dependency>
```

**Gradle**
```gradle
repositories {
    ...
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.ForYaSee:EventSystem:v1.0.0'
}

```

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

// Optionally you can pass your own ExecutorService
EventManager eventManager = new EventManager();

// Register your event listener
eventManager.register(new EventListener());

// Call an event
eventManager.handle(new YourEvent(...));

// or async
eventManager.handleAsync(new YourEvent(...));

```
