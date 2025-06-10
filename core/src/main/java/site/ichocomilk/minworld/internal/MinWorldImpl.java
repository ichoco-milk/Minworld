package site.ichocomilk.minworld.internal;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.util.LongObjectHashMap;
import org.bukkit.generator.ChunkGenerator;
import site.ichocomilk.minworld.model.MinChunk;

import java.util.Collection;

public final class MinWorldImpl extends WorldServer {

    private LongObjectHashMap<Chunk> chunks = new LongObjectHashMap<>();

    MinWorldImpl(
        final IDataManager idatamanager, final int i, final ChunkGenerator gen
    ) {
        super(
            MinecraftServer.getServer(),
            idatamanager,
            idatamanager.getWorldData(),
            i,
            MinecraftServer.getServer().methodProfiler,
            World.Environment.NORMAL,
            gen
        );
        b();
        getWorldData().setDifficulty(EnumDifficulty.PEACEFUL);
        getWorldData().setSpawn(new BlockPosition(0,180,0));
    }

    @Override
    public void save(final boolean flag, final IProgressUpdate iprogressupdate) {}

    @Override
    public Chunk getChunkAt(final int i, final int j) {
        return chunks.get(compressXZ(i, j));
    }

    public void setChunks(final Collection<MinChunk> chunks) {
        this.chunks = new LongObjectHashMap<>();
        for (final Chunk chunk : chunks) {
            this.chunks.put(compressXZ(chunk.locX, chunk.locZ), chunk);
        }
    }

    public static long compressXZ(final int x, final int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }
}