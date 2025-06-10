package site.ichocomilk.minworld.writer;

import lombok.experimental.UtilityClass;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import site.ichocomilk.minworld.MinWorldCommon;
import site.ichocomilk.minworld.buffer.writer.DynamicSizeBuffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@UtilityClass
public final class MinWorldFileWriter {

    public static WorldWriteResult write(final CraftChunk[] chunks, final File file) throws IOException {
        final DynamicSizeBuffer dynamicSizeBuffer = new DynamicSizeBuffer(4096);
        dynamicSizeBuffer.writeInt(MinWorldCommon.IDENTIFIER);

        final WorldWriteResult result = MinWorldChunksWriter.writeChunksData(chunks, dynamicSizeBuffer);

        try(FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(dynamicSizeBuffer.compress());
        }

        return result;
    }
}
