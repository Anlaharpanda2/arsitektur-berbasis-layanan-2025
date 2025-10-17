package com.anla.genericservice.query.cqrs;

public interface EventHandler<T extends Event> {
    void handle(T event);
    Class<? extends Event> getEventType();
}
