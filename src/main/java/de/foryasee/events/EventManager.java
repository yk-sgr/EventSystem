package de.foryasee.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class EventManager implements IEventManager {

    private final ExecutorService executorService;
    private final Set<Object> listeners = new HashSet<>();
    private final Map<Class<? extends Event>, Map<Object, List<Method>>> methods = new HashMap<>();
    private Consumer<Throwable> errorHandler = t -> {
    };

    public EventManager(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public EventManager() {
        this(Executors.newCachedThreadPool());
    }

    public void register(Object eventListener) {
        if (listeners.add(eventListener))
            updateMethods();
    }

    public void register(Object... eventListeners) {
        if (listeners.addAll(Arrays.asList(eventListeners)))
            updateMethods();
    }

    public void unregister(Object eventListener) {
        if (listeners.remove(eventListener))
            updateMethods();
    }

    public void unregister(Object... eventListeners) {
        if (listeners.removeAll(Arrays.asList(eventListeners)))
            updateMethods();
    }

    public List<Object> getListeners() {
        return Collections.unmodifiableList(new LinkedList<>(listeners));
    }

    public void handle(Event event) {
        call(event, false);
    }

    public void handleAsync(Event event) {
        executorService.execute(() -> call(event, true));
    }

    private void call(Event event, boolean async) {
        Class<? extends Event> eventClass = event.getClass();
        Map<Object, List<Method>> listeners = methods.get(eventClass);
        if (listeners != null) {
            listeners.forEach((key, value) -> value.forEach(method -> {
                method.setAccessible(true);
                if (async)
                    executorService.execute(() -> {
                        try {
                            method.invoke(key, event);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            handleError(e);
                        }
                    });
                else {
                    try {
                        method.invoke(key, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        handleError(e);
                    }
                }
            }));
        }
    }

    private void updateMethods() {
        methods.clear();
        for (Object listener : listeners) {
            boolean isClass = listener instanceof Class;
            Class<?> clazz = isClass ? (Class) listener : listener.getClass();
            Method[] allMethods = clazz.getDeclaredMethods();
            for (Method method : allMethods) {
                if (!method.isAnnotationPresent(SubscribeEvent.class) || (isClass && !Modifier.isStatic(method.getModifiers())))
                    continue;
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1 && Event.class.isAssignableFrom(parameterTypes[0])) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Event> eventClass = (Class<? extends Event>) parameterTypes[0];
                    if (!methods.containsKey(eventClass))
                        methods.put(eventClass, new HashMap<>());
                    if (!methods.get(eventClass).containsKey(listener))
                        methods.get(eventClass).put(listener, new ArrayList<>());
                    methods.get(eventClass).get(listener).add(method);
                }
            }
        }
    }

    private void handleError(Throwable throwable) {
        if (errorHandler != null)
            errorHandler.accept(throwable);
    }
}
