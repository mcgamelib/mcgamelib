package net.silthus.mcgamelib.events.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.silthus.mcgamelib.User;
import net.silthus.mcgamelib.events.MCGameLibEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UserEvent extends MCGameLibEvent {

    private final User user;
}
