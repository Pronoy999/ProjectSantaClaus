package com.pm.wd.sl.college.projectsantaclaus.Helper;

import java.util.HashMap;

public class RLE {
    public class Pixel {
        public byte r;
        public byte g;
        public byte b;

        public boolean equalsPixel(Pixel o) {
            return o.r == r && o.g == g && o.b == b;
        }

        public byte[] getBytes() {
            return new byte[]{r, g, b};
        }
    }

    /**
     * Method to compress the pixels.
     *
     * @param pixels: The raster pixels to be compressed.
     * @return: compressed Pixel Map.
     */
    public HashMap<Pixel, Integer> compress(Pixel[] pixels) {
        HashMap<Pixel, Integer> ret_map = new HashMap<>();
        for (int i = 0; i < pixels.length; i++) {
            int count = 1;
            while (i < pixels.length - 1 && pixels[i].equalsPixel(pixels[i + 1])) {
                count++;
                i++;
            }

            ret_map.put(pixels[i], count);
        }
        return ret_map;
    }

    /**
     * Method to get the Decompressed Pixel array.
     *
     * @param pixels_map: the compressed Pixel map.
     * @return decompressed pixel array.
     */
    public Pixel[] decompress(HashMap<Pixel, Integer> pixels_map) {
        int totSize = 0;

        for (int cnt : pixels_map.values()) {
            totSize += cnt;
        }

        int c = 0;
        Pixel[] pixels = new Pixel[totSize];

        for (Pixel px : pixels_map.keySet()) {
            int cnt = pixels_map.get(px);
            int start = c;
            int end = c + cnt;
            for (int i = start; i < end; i++, c++) {
                pixels[i] = px;
            }
        }

        return pixels;
    }
}
