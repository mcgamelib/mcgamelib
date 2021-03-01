package net.silthus.mcgamelib;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class MCGameLibTests {

    private ServerMock server;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(MCGameLib.class);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }
}
