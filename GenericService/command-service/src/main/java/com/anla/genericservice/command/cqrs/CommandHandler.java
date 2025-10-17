package com.anla.genericservice.command.cqrs;

public interface CommandHandler<T extends Command> {
    void handle(T command);
    Class<? extends Command> getCommandType();
}
