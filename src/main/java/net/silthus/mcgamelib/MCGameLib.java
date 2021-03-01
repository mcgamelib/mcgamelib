package net.silthus.mcgamelib;

import kr.entree.spigradle.annotations.PluginMain;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

@PluginMain
public class MCGameLib extends JavaPlugin implements Listener {

    @Getter
    private GameManager gameManager;

    public MCGameLib() {
    }

    public MCGameLib(
            JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable() {

        this.gameManager = new GameManager(this);
    }
}
