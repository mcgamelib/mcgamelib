package net.silthus.mcgamelib;

public interface Feature extends GameObject {

    default void load() {}

    void enable();

    void disable();
}
