package com.pm.wd.sl.college.projectsantaclaus.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grouping {
    private static final byte FORWARD_GROUPING = 0b00000001;
    private static final byte REVERSE_GROUPING = 0b00000010;
    private static final byte NO_GROUPING = 0b00000000;
    private static final byte IDENTICAL_GROUPING = 0b00000011;

    /**
     * Flattens the multi-dimensional array to 1D array.
     *
     * @param inputArray: The multidimensional array.
     * @return 1D array.
     * @throws IllegalArgumentException
     */
    public static Byte[] flatten(Object[] inputArray) throws IllegalArgumentException {
        if (inputArray == null) return null;

        List<Byte> flatList = new ArrayList<>();

        for (Object element : inputArray) {
            if (element instanceof Byte) {
                flatList.add((byte) element);
            } else if (element instanceof Object[]) {
                flatList.addAll(Arrays.asList(flatten((Object[]) element)));
            } else {
                throw new IllegalArgumentException("Input must be an array of Bytes or nested arrays of Bytes");
            }
        }
        return flatList.toArray(new Byte[0]);
    }

    /**
     * Compresses the data bytes.
     *
     * @param data: The data to be compressed.
     * @return compressed data.
     */
    public Byte[] compress(byte[] data) {
        byte[][] comp_blocks = new byte[(data.length / 8) + 1][];

        for (int i = 0, c = 0; i < data.length; i += 8, c++) {
            byte[] blk = Arrays.copyOfRange(data, i, i + 8);

            comp_blocks[c] = compressBlock(blk);
        }

        return flatten(comp_blocks);
    }

    /**
     * Compresses one block of length 8 bytes
     *
     * @param block: The block to be compressed.
     * @return compressed block.
     */
    public byte[] compressBlock(byte[] block) {
        byte[] g1 = group(block[0], block[1]);
        byte[] g2 = group(block[2], block[3]);
        byte[] g3 = group(block[4], block[5]);
        byte[] g4 = group(block[6], block[7]);

        byte meta = (byte) (g1[0] << 6 + g2[0] << 4 + g3[0] << 2 + g4[0]);

        int totBytes = 1 + (g1[0] == NO_GROUPING ? 2 : 1) +
                (g2[0] == NO_GROUPING ? 2 : 1) +
                (g3[0] == NO_GROUPING ? 2 : 1) +
                (g4[0] == NO_GROUPING ? 2 : 1);
        byte[] ret_bt = new byte[totBytes];

        ret_bt[0] = meta;

        int i = 1;
        if (g1[0] == NO_GROUPING) {
            ret_bt[i++] = block[0];
            ret_bt[i++] = block[1];
        } else {
            ret_bt[i++] = g1[1];
        }

        if (g2[0] == NO_GROUPING) {
            ret_bt[i++] = block[2];
            ret_bt[i++] = block[3];
        } else {
            ret_bt[i++] = g2[1];
        }

        if (g3[0] == NO_GROUPING) {
            ret_bt[i++] = block[4];
            ret_bt[i++] = block[5];
        } else {
            ret_bt[i++] = g3[1];
        }

        if (g4[0] == NO_GROUPING) {
            ret_bt[i++] = block[6];
            ret_bt[i++] = block[7];
        } else {
            ret_bt[i++] = g4[1];
        }

        return ret_bt;
    }

    /**
     * Method to Decompress.
     *
     * @param data: The comressed bytes to be decompressed.
     * @return decompressed bytes.
     */
    public Byte[] decompress(byte[] data) {
        ArrayList<byte[]> blocks = new ArrayList<>();
        for (int i = 0; i < data.length; ) {
            byte[] decBlock = new byte[8];
            int len = decompressBlock(data, decBlock, i);
            i += len;

            blocks.add(decBlock);
        }

        return flatten(blocks.toArray());
    }

    public int decompressBlock(byte[] data, byte[] blk, int start) {
        byte g1 = (byte) (0b00000011 & (data[start] >>> 6));
        byte g2 = (byte) (0b00000011 & (data[start] >>> 4));
        byte g3 = (byte) (0b00000011 & (data[start] >>> 2));
        byte g4 = (byte) (0b00000011 & (data[start]));

        int i = 1;

        i = ungroup(data, blk, start + i, g1, 0, 1);
        i = ungroup(data, blk, start + i, g2, 2, 3);
        i = ungroup(data, blk, start + i, g3, 4, 5);
        i = ungroup(data, blk, start + i, g4, 6, 7);

        return 1 + (g1 == NO_GROUPING ? 2 : 1) +
                (g2 == NO_GROUPING ? 2 : 1) +
                (g3 == NO_GROUPING ? 2 : 1) +
                (g4 == NO_GROUPING ? 2 : 1);
    }

    /**
     * Ungroups a single block from the ith Index of data.
     *
     * @return
     */
    private int ungroup(byte[] data, byte[] blk, int i, byte g, int x, int y) {
        switch (g) {
            case NO_GROUPING: {
                blk[x] = data[i++];
                blk[y] = data[i++];
                break;
            }
            case IDENTICAL_GROUPING: {
                blk[x] = data[i];
                blk[y] = data[i++];
                break;
            }
            case FORWARD_GROUPING: {
                byte first = (byte) (data[i] / 100);
                byte second = (byte) (data[i++] % 100);

                blk[x] = first;
                blk[y] = second;
                break;
            }
            case REVERSE_GROUPING: {
                byte first = (byte) (data[i] % 100);
                byte second = (byte) (data[i++] / 100);

                blk[x] = first;
                blk[y] = second;
                break;
            }
        }
        return i;
    }

    public byte[] group(byte first, byte second) {
        byte[] ret = new byte[2];
        int val;

        if (first == second) { // Identical Grouping
            ret[0] = IDENTICAL_GROUPING;
            ret[1] = first;
        } else if ((val = (first * 100) + second) < 255) { // Forward Grouping
            ret[0] = FORWARD_GROUPING;
            ret[1] = (byte) val;
        } else if ((val = (second * 100) + first) < 255) { // Reverse Grouping
            ret[0] = REVERSE_GROUPING;
            ret[1] = (byte) val;
        } else { // No Grouping
            ret[0] = NO_GROUPING;
        }
        return ret;
    }
}
