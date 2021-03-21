package com.nuke3dtv.nukesecon.tools;

public class MathTools {
    // For situations where ints are converted to 2 16 bit ints.
    public static int[] intToShortInts(int inInt) {
        // Try not to lose your shorts! Haha. Ha.
        int[] results = new int[2];
        results[0] = inInt & 0xffff;
        results[1] = (inInt >> 16) & 0xffff;
        return results;
    }
    public static int shortIntsToInt(int inInt1, int inInt2) {
        int result = 0;
        result = (result & 0xffff0000) + (inInt1 & 0xffff);
        result = (result & 0x0000ffff) | (inInt2 << 16);
        return result;
    }
    public static int shortIntsToInt(int[] inVals) {
        return shortIntsToInt(inVals[0], inVals[1]);
    }

    // Convert a long to two ints and back
    public static int[] longToTwoInts(long inLong) {
        int[] results = new int[2];
        results[0] = (int) (inLong >> 32);
        results[1] = (int) inLong;
        return results;
    }
    public static long twoIntsToLong(int inInt1, int inInt2) {
        return (long) inInt1 << 32 | inInt2 & 0xFFFFFFFFL;
    }
    public static long twoIntsToLong(int[] inVals) {
        return twoIntsToLong(inVals[0], inVals[1]);
    }
}
