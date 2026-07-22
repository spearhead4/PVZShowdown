package com.pvzh.simulator.engine;

import com.pvzh.simulator.engine.events.GameEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A central Event Bus (Observer Pattern) for the game engine.
 * Allows cards and abilities to subscribe to specific game events.
 */
public class EventManager {
    private final Map<Class<? extends GameEvent>, List<Consumer<? extends GameEvent>>> listeners = new HashMap<>();

    /**
     * Subscribes a listener to a specific event type.
     */
    public <T extends GameEvent> void subscribe(Class<T> eventType, Consumer<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    /**
     * Unsubscribes a listener from a specific event type.
     */
    public <T extends GameEvent> void unsubscribe(Class<T> eventType, Consumer<T> listener) {
        List<Consumer<? extends GameEvent>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }

    /**
     * Publishes an event to all subscribed listeners.
     */
    @SuppressWarnings("unchecked")
    public <T extends GameEvent> void publish(T event) {
        List<Consumer<? extends GameEvent>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            // Iterate over a copy to prevent concurrent modification issues if listeners unsubscribe during the event
            for (Consumer<? extends GameEvent> listener : new ArrayList<>(eventListeners)) {
                ((Consumer<T>) listener).accept(event);
            }
        }
    }
}
