package site.ichocomilk.minworld.reader;

import org.bukkit.World;
import site.ichocomilk.minworld.MinWorldCommon;
import site.ichocomilk.minworld.buffer.reader.BufferReader;
import site.ichocomilk.minworld.model.MinChunk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;

public class MinWorldFileReader {

    public static Collection<MinChunk> read(final File file, final World world) throws IOException {
        final long length = file.length();
        if (length > Integer.MAX_VALUE) {
            throw new IOException("File " + file + " weights more than " + Integer.MAX_VALUE + "bytes");
        }

        final byte[] buffer = new byte[(int)length];
        try(FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(buffer);
        }

        final BufferReader reader = new BufferReader(buffer);
        if (reader.readInt() != MinWorldCommon.IDENTIFIER) {
            throw new IOException("The file " + file + " isn't a valid minworld file");
        }

        return MinWorldChunksReader.read(reader, world);
    }
}
