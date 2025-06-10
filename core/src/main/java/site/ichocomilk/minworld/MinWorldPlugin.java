package site.ichocomilk.minworld;

import org.bukkit.plugin.java.JavaPlugin;
import site.ichocomilk.minworld.command.MinWorldConvertCommand;
import site.ichocomilk.minworld.command.MinWorldLoaderCommand;

public final class MinWorldPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginCommand("mwsave").setExecutor(new MinWorldConvertCommand(getDataFolder()));
        getServer().getPluginCommand("mwload").setExecutor(new MinWorldLoaderCommand(getDataFolder()));
    }
}
