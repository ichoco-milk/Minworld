package site.ichocomilk.minworld.internal;

import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.ExceptionWorldConflict;
import net.minecraft.server.v1_8_R3.IChunkLoader;
import net.minecraft.server.v1_8_R3.World;

import java.io.IOException;

final class MinWorldChunkLoader implements IChunkLoader {

    // Load chunk
    @Override
    public Chunk a(final World world, final int i, final int i1) {
        return new Chunk(world, i, i1);
    }

    // Save chunk
    @Override
    public void a(final World world, final Chunk chunk) throws IOException, ExceptionWorldConflict {

    }

    // Does literally nothing
    @Override
    public void b(final World world, final Chunk chunk) throws IOException {

    }

    // Does literally nothing
    @Override
    public void a() {

    }

    // Save all chunks
    @Override
    public void b() {

    }
}
