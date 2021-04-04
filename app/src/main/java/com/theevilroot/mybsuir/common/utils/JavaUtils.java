package com.theevilroot.mybsuir.common.utils;

public class JavaUtils {

    public static byte[] serializeInt(final int value) {
        final byte[] bytes = new byte[4];
        bytes[0] = (byte) (value & 0xFF);
        bytes[1] = (byte) ((value & 0xFF00) >> 8);
        bytes[2] = (byte) ((value & 0xFF0000) >> 16);
        bytes[3] = (byte)((value & 0xFF000000) >> 24);
        return bytes;
    }

    public static int deserializeInt(final byte[] bytes, int offset) {
        if (bytes.length < 4)
            return 0;
        return (bytes[offset] & 0xff) | ((bytes[offset + 1] & 0xff) << 8) | ((bytes[offset + 2] & 0xff) << 16) | ((bytes[offset + 3] & 0xff) << 24);
    }

}
