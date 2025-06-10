package site.ichocomilk.minworld.command;


import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import site.ichocomilk.minworld.writer.MinWorldFileWriter;
import site.ichocomilk.minworld.writer.WorldWriteResult;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class MinWorldConvertCommand implements CommandExecutor {
    private final File worldFolder;

    public MinWorldConvertCommand(File worldFolder) {
        this.worldFolder = worldFolder;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§cFormat: /mwsave (worldname)");
            return true;
        }

        final World world = Bukkit.getWorld(args[0]);
        if (world == null) {
            sender.sendMessage("§cThe world " + args[0] + " don't exist");
            return true;
        }

        final CraftChunk[] chunks = (CraftChunk[]) world.getLoadedChunks();
        if (chunks.length > Short.MAX_VALUE) {
            sender.sendMessage("§cTo many chunks loaded. Amount: " + chunks.length + ". Maximum: " + Short.MAX_VALUE);
            return true;
        }

        final File file = new File(worldFolder, world.getName());
        if (!worldFolder.exists()) {
            worldFolder.mkdirs();
        }

        long time = System.currentTimeMillis();

        final WorldWriteResult result;
        try {
            result = MinWorldFileWriter.write(chunks, new File(worldFolder, world.getName() + ".minworld"));
        } catch (IOException e) {
            sender.sendMessage("§cError on write the file " + file + " . See console for full log");
            Bukkit.getLogger().log(Level.SEVERE, "Error on write the file " + file, e);
            return true;
        }

        long finish = System.currentTimeMillis() - time;
        sender.sendMessage(
            "\n  §aFile writed in:  " + finish + "ms" +
            "\n  §aAmount chunks: " + result.amountChunks() +
            "\n  §aAmount chunk sections: " + result.amountSections() +
            "\n  §aBuffer size: " + result.bufferSize()
        );
        return true;
    }
}