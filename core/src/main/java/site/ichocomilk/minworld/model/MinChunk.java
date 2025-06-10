package site.ichocomilk.minworld.model;

import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.ChunkSection;
import net.minecraft.server.v1_8_R3.NibbleArray;
import net.minecraft.server.v1_8_R3.World;

public final class MinChunk extends Chunk {

    private static final NibbleArray FULL_LIGHT = new NibbleArray();
    private static final NibbleArray EMPTY_ARRAY = new NibbleArray();
    static {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 16; y++) {
                    FULL_LIGHT.a(x,y,z, 15);
                }
            }
        }
    }

    public MinChunk(final World world, final int x, final int z) {
        super(world, x, z);
    }

    public ChunkSection setSection(final int index, final char[] blocks) {
        final ChunkSection section = new ChunkSection(index, true, blocks);
        getSections()[index] = section;
        section.b(FULL_LIGHT);
        section.a(FULL_LIGHT);
        return section;
    }
}