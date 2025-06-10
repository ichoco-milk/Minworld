package site.ichocomilk.minworld.writer;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.ChunkSection;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import site.ichocomilk.minworld.buffer.writer.DynamicSizeBuffer;
import site.ichocomilk.minworld.buffer.writer.WriteBuffer;

@UtilityClass
public final class MinWorldChunksWriter {

    public static WorldWriteResult writeChunksData(final CraftChunk[] chunks, final WriteBuffer buffer) {
        int totalSections = 0, totalChunks = 0;

        for (final CraftChunk craftChunk : chunks) {
            final Chunk chunk = craftChunk.getHandle();
            final ChunkSection[] sections = chunk.getSections();

            int amountSections = 0;
            for (final ChunkSection section : sections) {
                if (section != null && !section.a()) {
                    amountSections++;
                }
            }

            if (amountSections == 0) {
                continue;
            }

            totalSections += amountSections;
            ++totalChunks;

            buffer.writeVarInt(craftChunk.getX());
            buffer.writeVarInt(craftChunk.getZ());
            buffer.writeByte((byte)amountSections);

            for (int j = 0; j < 16; j++) {
                final ChunkSection section = sections[j];
                if (section == null || section.a()) {
                    continue;
                }
                buffer.writeByte((byte)j); // Chunk Section ID

                final DynamicSizeBuffer temporaryBuffer = new DynamicSizeBuffer(512);
                final char[] blocks = section.getIdArray();
                final int uniqueValues = countUniqueAndWriteLUT(temporaryBuffer, blocks);
                final int bitsPerEntry = Integer.SIZE - Integer.numberOfLeadingZeros(uniqueValues - 1);

                buffer.writeVarInt(uniqueValues);

                final byte[] lutTable = temporaryBuffer.compress();
                buffer.writeVarInt(lutTable.length);
                buffer.writeBytes(lutTable);

                buffer.writeByte(bitsPerEntry);
                writeBlocks(blocks, buffer, bitsPerEntry);
            }
        }
        return new WorldWriteResult(totalSections, totalChunks, buffer.getIndex());
    }

    private static int countUniqueAndWriteLUT(final WriteBuffer buffer, final char[] values) {
        long[] words = new long[(512 / 64) + 1];

        for (final char value : values) {
            final int index = value >> 6;

            if (index >= words.length) {
                final long[] newWords = new long[index + 1];
                System.arraycopy(words, 0, newWords, 0, words.length);
                words = newWords;
            }
            final long bitPosition = 1L << value;
            if ((words[index] & bitPosition) == 0) { // Write no duplicated values
                buffer.writeChar(value);
            }
            words[index] |= bitPosition;
        }

        int uniqueValues = 0;
        for (final long bitmask : words) {
            uniqueValues += Long.bitCount(bitmask);
        }
        return uniqueValues;
    }

    private static void writeBlocks(final char[] blocks, final WriteBuffer buffer, final int bitsPerEntry) {
        final int valuesPerLong = 64 / bitsPerEntry;
        final long[] packedArray = new long[((blocks.length + valuesPerLong - 1) / valuesPerLong)];

        int i = 0;
        for (final char block : blocks) {
            final int bitIndex = (i % valuesPerLong) * bitsPerEntry;
            final int longIndex = i / valuesPerLong;
            packedArray[longIndex] |= ((long) block & ((1L << bitsPerEntry) - 1)) << bitIndex;
            i++;
        }
        buffer.writeLongArray(packedArray);
    }
}