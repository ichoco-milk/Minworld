package site.ichocomilk.minworld.internal;

import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;

import java.io.File;
import java.util.UUID;

@Getter
final class MinWorldDataManager extends WorldNBTStorage {

    @Getter(value = AccessLevel.NONE)
    private final UUID uuid = UUID.randomUUID();
    private final WorldData worldData;
    private final IChunkLoader chunkLoader;

    // When unloading a world, Spigot tries to remove the region file from its cache.
    // To do so, it casts the world's IDataManager to a WorldNBTStorage, to be able
    // to use the getDirectory() method. Thanks to this, we have to create a custom
    // WorldNBTStorage with a fake file instead of just implementing the IDataManager interface
    //
    // Thanks Spigot!
    MinWorldDataManager(String worldName) {
        super(new File("temp_" + worldName), worldName, false);

        // The WorldNBTStorage automatically creates some files inside the base dir, so we have to delete them
        // (Thanks again Spigot)

        // Can't just access the baseDir field inside WorldNBTStorage cause it's private :P
        File baseDir = new File("temp_" + worldName, worldName);
        new File(baseDir, "session.lock").delete();
        new File(baseDir, "data").delete();

        chunkLoader = new MinWorldChunkLoader();
        worldData = new MinWorldData(worldName);

        baseDir.delete();
        baseDir.getParentFile().delete();
    }

    @Override
    public WorldData getWorldData() {
        return worldData;
    }

    @Override public void checkSession() { }

    @Override
    public IChunkLoader createChunkLoader(WorldProvider worldProvider) {
        return chunkLoader;
    }

    @Override
    public void saveWorldData(WorldData worldData, NBTTagCompound nbtTagCompound) {

    }

    @Override
    public void saveWorldData(WorldData worldData) {
        this.saveWorldData(worldData, null);
    }

    @Override
    public void a() {

    }

    @Override
    public File getDataFile(String s) {
        return null;
    }

    @Override
    public String g() {
        return null;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void save(EntityHuman entityHuman) {

    }

    @Override
    public NBTTagCompound load(EntityHuman entityHuman) {
        return null;
    }

    @Override public String[] getSeenPlayers() {
        return new String[0];
    }
}