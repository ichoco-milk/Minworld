package site.ichocomilk.minworld.buffer.reader;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.UUID;

public final class BufferReader {

    private final byte[] buffer;
    private int index;

    public BufferReader(byte[] buffer) {
        this.buffer = buffer;
    }

    public BufferReader(byte[] buffer, int index) {
        this.buffer = buffer;
        this.index = index;
    }

    public int readVarInt() {
        int value = 0;
        int size = 0;

        byte b;
        do {
            if (size >= 35) {
                throw new RuntimeException("VarInt wider than 5 bytes");
            }
            b = readByte();
            value |= (b & 0x7F) << size;
            size += 7;
        } while ((b & 0x80) == 0x80);

        return value;
    }

    public byte[] readBytes(int length) {
        final byte[] result = new byte[length];
        System.arraycopy(buffer, index, result, 0, length);
        index += length;
        return result;
    }

    public char[] readChars(int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = (char) ((buffer[index++] & 0xFF) << 8 | (buffer[index++] & 0xFF));
        }
        return chars;
    }

    public long[] readLongArray() {
        final int length = readVarInt();
        if (length < 0) {
            throw new IllegalArgumentException("Array cannot have length less than 0.");
        }
        long[] l = new long[length];
        for (int index = 0; index < length; index++) {
            l[index] = readLong();
        }
        return l;
    }

    public BitSet readBitSet() {
        final int length = readVarInt();
        final long[] data = new long[length];
        for (int i = 0; i < length; i++) {
            data[i] = readLong();
        }
        return BitSet.valueOf(data);
    }

    public boolean readBoolean() {
        return buffer[index++] == 1;
    }

    public byte readByte() {
        return buffer[index++];
    }

    public int readUnsignedByte() {
        return buffer[index++] & 0xFF;
    }

    public short readShort() {
        return (short)((buffer[index++] & 0xFF) << 8 | (buffer[index++] & 0xFF));
    }

    public char readChar() {
        return (char)((buffer[index++] & 0xFF) << 8 | (buffer[index++] & 0xFF));
    }

    public int readInt() {
        return (buffer[index++] & 0xFF) << 24 |
            (buffer[index++] & 0xFF) << 16 |
            (buffer[index++] & 0xFF) << 8 |
            (buffer[index++] & 0xFF);
    }

    public UUID readUUID() {
        return new UUID(readLong(), readLong());
    }

    public long readLong() {
        return ((long)(buffer[index++] & 0xFF) << 56) |
            ((long)(buffer[index++] & 0xFF) << 48) |
            ((long)(buffer[index++] & 0xFF) << 40) |
            ((long)(buffer[index++] & 0xFF) << 32) |
            ((long)(buffer[index++] & 0xFF) << 24) |
            ((long)(buffer[index++] & 0xFF) << 16) |
            ((long)(buffer[index++] & 0xFF) << 8) |
            ((long)(buffer[index++] & 0xFF));
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public String readString() {
        final int length = readVarInt();
        final byte[] bytes = new byte[length];
        System.arraycopy(buffer, index, bytes, 0, length);
        index += length;
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public int getIndex() {
        return index;
    }

    public boolean hasNext() {
        return index < buffer.length;
    }

    public void skipTo(int index) {
        this.index = index;
    }
}