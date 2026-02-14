package com.project.society.util;

/**
 * Basic vector utilities: norm and cosine similarity.
 */
public final class VectorUtil {

    private VectorUtil() {}

    public static double dot(double[] a, double[] b) {
        int n = Math.min(a.length, b.length);
        double s = 0.0;
        for (int i = 0; i < n; i++) s += a[i] * b[i];
        return s;
    }

    public static double norm(double[] a) {
        double s = 0.0;
        for (double v : a) s += v * v;
        return Math.sqrt(s);
    }

    public static double cosineSimilarity(double[] a, double[] b) {
        double an = norm(a);
        double bn = norm(b);
        if (an == 0 || bn == 0) {
            // fallback: if both zero vectors, return 1. else 0
            if (an == 0 && bn == 0) return 1.0;
            return 0.0;
        }
        return dot(a, b) / (an * bn);
    }
}
