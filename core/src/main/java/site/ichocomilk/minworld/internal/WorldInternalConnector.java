package site.ichocomilk.minworld.internal;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import site.ichocomilk.minworld.model.MinChunk;

import java.util.Collection;

public final class WorldInternalConnector {

    public static MinWorldImpl createWorld(final String worldName) {
        final MinWorldDataManager dataManager = new MinWorldDataManager(worldName);
        int dimension = getNextDimensionId();
        return new MinWorldImpl(dataManager, dimension,null);
    }

    public static void registerWorld(final World world) {
        final WorldServer nmsWorld = ((CraftWorld)world).getHandle();
        final MinecraftServer mcServer = MinecraftServer.getServer();
        mcServer.server.addWorld(nmsWorld.getWorld());
        mcServer.worlds.add(nmsWorld);
    }

    public static void unregisterWorld(final World world) {
        final WorldServer nmsWorld = ((CraftWorld)world).getHandle();
        final MinecraftServer mcServer = MinecraftServer.getServer();
        mcServer.server.unloadWorld(nmsWorld.getWorld(), false);
        mcServer.worlds.remove(nmsWorld);
    }

    public static void setChunks(final World world, final Collection<MinChunk> chunks) {
        final MinWorldImpl nmsWorld = (MinWorldImpl)((CraftWorld)world).getHandle();
        nmsWorld.setChunks(chunks);
    }

    private static int getNextDimensionId() {
        final MinecraftServer mcServer = MinecraftServer.getServer();
        int dimension = CraftWorld.CUSTOM_DIMENSION_OFFSET + mcServer.worlds.size();
        boolean used = false;
        do {
            for (WorldServer server : mcServer.worlds) {
                used = server.dimension == dimension;
                if (used) {
                    dimension++;
                    break;
                }
            }
        } while (used);
        return dimension;
    }
}
