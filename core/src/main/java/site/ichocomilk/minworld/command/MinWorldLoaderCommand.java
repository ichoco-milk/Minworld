package site.ichocomilk.minworld.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import site.ichocomilk.minworld.internal.MinWorldImpl;
import site.ichocomilk.minworld.internal.WorldInternalConnector;
import site.ichocomilk.minworld.model.MinChunk;
import site.ichocomilk.minworld.reader.MinWorldFileReader;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;

public final class MinWorldLoaderCommand implements CommandExecutor {
    private final File worldFolder;

    public MinWorldLoaderCommand(File worldFolder) {
        this.worldFolder = worldFolder;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§cFormat: /mwload (worldname)");
            return true;
        }

        if (Bukkit.getWorld(args[0]) != null) {
            sender.sendMessage("§cAlready is loaded a world with the name " + args[0]);
            return true;
        }

        final File[] worldFiles = worldFolder.listFiles((file) -> {
            final String[] fileExtensionAndName = file.getName().split(".");
            return fileExtensionAndName.length >= 2 && fileExtensionAndName[1].equals(".minworld") && fileExtensionAndName[0].equalsIgnoreCase(args[0]);
        });

        if (worldFiles == null) {
            sender.sendMessage("§cCan't found the world " + args[0] + " in the folder" + worldFolder);
            return true;
        }

        long time = System.currentTimeMillis();
        try {
            final Collection<MinChunk> chunks = MinWorldFileReader.read(worldFiles[0], null);
            final MinWorldImpl world = WorldInternalConnector.createWorld(args[0]);
            world.setChunks(chunks);
            WorldInternalConnector.registerWorld(world.getWorld());
        } catch (IOException e) {
            sender.sendMessage("§cError on load the world " + args[0] + " in the path " + worldFiles[0] + ". See console for full log");
            Bukkit.getLogger().log(Level.SEVERE, "§cError on load the world " + args[0], e);
        }
        long finish = System.currentTimeMillis() - time;
        sender.sendMessage("§aWorld loaded in :  " + finish + "ms");

        return true;
    }
}