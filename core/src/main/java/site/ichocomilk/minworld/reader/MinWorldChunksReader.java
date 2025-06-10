package site.ichocomilk.minworld.reader;

import lombok.experimental.UtilityClass;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import site.ichocomilk.minworld.buffer.reader.BufferReader;
import site.ichocomilk.minworld.model.MinChunk;

import java.util.ArrayList;
import java.util.Collection;

@UtilityClass
public final class MinWorldChunksReader {

    public static Collection<MinChunk> read(final BufferReader buffer, final World world) {
        final Collection<MinChunk> chunks = new ArrayList<>();

        while (buffer.hasNext()) {
            final int x = buffer.readVarInt();
            final int z = buffer.readVarInt();
            final int amountSections = buffer.readByte();

            final MinChunk chunk = new MinChunk(((CraftWorld) world).getHandle(), x, z);

            for (int a = 0; a < amountSections; a++) {
                final byte sectionId = buffer.readByte();
                final int uniqueValues = buffer.readVarInt();

                final char[] lut = new char[uniqueValues];
                for (int i = 0; i < uniqueValues; i++) {
                    lut[i] = buffer.readChar();
                }

                final byte[] compressed = buffer.readBytes(buffer.readVarInt());
                final BufferReader decompressed = new BufferReader(compressed);

                final int bitsPerEntry = decompressed.readByte();
                final long[] packed = decompressed.readLongArray();
                final char[] blocks = unpackBlocks(packed, lut, bitsPerEntry);

                chunk.setSection(sectionId, blocks);
            }

            chunks.add(chunk);
        }

        return chunks;
    }

    private static char[] unpackBlocks(final long[] data, final char[] lut, final int bitsPerEntry) {
        final int valuesPerLong = 64 / bitsPerEntry;
        final int mask = (1 << bitsPerEntry) - 1;
        final int totalBlocks = 4096;

        final char[] blocks = new char[totalBlocks];
        int blockIndex = 0;

        for (int i = 0; i < data.length && blockIndex < totalBlocks; i++) {
            final long entry = data[i];
            for (int bitIndex = 0; bitIndex < valuesPerLong && blockIndex < totalBlocks; bitIndex++) {
                final int valueIndex = (int) ((entry >> (bitIndex * bitsPerEntry)) & mask);
                blocks[blockIndex++] = lut[valueIndex];
            }
        }

        return blocks;
    }
}
