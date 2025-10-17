package com.anla.genericservice.query.cqrs;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventDispatcher implements ApplicationContextAware {

    private final Map<Class<? extends Event>, EventHandler> eventHandlers = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        Map<String, EventHandler> handlers = applicationContext.getBeansOfType(EventHandler.class);
        handlers.values().forEach(handler -> eventHandlers.put(handler.getEventType(), handler));
    }

    @RabbitListener(queues = "${app.messaging.queue.name}")
    public void dispatch(Event event) {
        EventHandler handler = eventHandlers.get(event.getClass());
        if (handler != null) {
            handler.handle(event);
        } else {
            // It's common to have events that are not handled by a specific projection,
            // so we just log this instead of throwing an exception.
            System.out.println("No handler for event: " + event.getClass().getSimpleName());
        }
    }
}
