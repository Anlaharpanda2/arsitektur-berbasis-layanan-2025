package com.anla.genericservice.command.cqrs;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommandDispatcher implements ApplicationContextAware {

    private final Map<Class<? extends Command>, CommandHandler> commandHandlers = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        Map<String, CommandHandler> handlers = applicationContext.getBeansOfType(CommandHandler.class);
        handlers.values().forEach(handler -> commandHandlers.put(handler.getCommandType(), handler));
    }

    public void dispatch(Command command) {
        CommandHandler handler = commandHandlers.get(command.getClass());
        if (handler != null) {
            handler.handle(command);
        } else {
            throw new IllegalStateException("No handler registered for command: " + command.getClass().getSimpleName());
        }
    }
}
