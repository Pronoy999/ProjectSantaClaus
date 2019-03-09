package com.pm.wd.sl.college.projectsantaclaus.Helper;

public class LSBWatermarkUtils {
    static final int LENGTH_BITCOUNT = 8;

    private static byte[] embed(byte[] data, int len, byte[] mark) {
        int count = 0;
        for (int bit_i = LENGTH_BITCOUNT - 1; bit_i >= 0; bit_i--) {
            int bit = (len >>> bit_i) & 1;

            byte old_byte = data[LENGTH_BITCOUNT - 1 - bit_i];

            data[LENGTH_BITCOUNT - 1 - bit_i] = (byte) ((data[LENGTH_BITCOUNT - 1 - bit_i] & 0b11111110) | bit);

            count += old_byte == data[LENGTH_BITCOUNT - 1 - bit_i] ? 0 : 1;
        }

        int gap = (data.length - LENGTH_BITCOUNT) / ((len * 8) + 1);

        for (int i = 0; i < len; i++) {
            for (int j = 7; j >= 0; j--) {
                int off = LENGTH_BITCOUNT + ((i * 8) + 7 - j + 1) * gap;
                int bit = (mark[i] >>> j) & 1;

                byte old_byte = data[off];

                data[off] = (byte) ((data[off] & 0b11111110) | bit);

                count += old_byte == data[off] ? 0 : 1;
            }
        }

        /*
        double percent_ = ((double) count * 100.0 / (double) data.length);

        System.out.println("% change: " + df.format(percent_));
        System.out.println("% accuracy: " + df.format(1 - percent_));
        System.out.println();
        */

        return data;
    }

    private static byte[] ascend(byte[] data) {
        int len = 0;

        for (int i = 0; i < LENGTH_BITCOUNT; i++) {
            len = (len << 1) | (data[i] & 1);
        }

        byte[] ret = new byte[len];

        int gap = (data.length - LENGTH_BITCOUNT) / ((len * 8) + 1);


        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 8; j++) {
                int off = LENGTH_BITCOUNT + ((i * 8) + j + 1) * gap;

                ret[i] = (byte) ((ret[i] << 1) | (data[off] & 1));
            }
        }
        return ret;
    }
}
