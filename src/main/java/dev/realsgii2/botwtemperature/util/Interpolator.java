package dev.realsgii2.botwtemperature.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// https://stackoverflow.com/a/7913617
public class Interpolator {
    public static Double LinearInterpolate(Double y1, Double y2, Double mu) {
        return (y1 * (1 - mu) + y2 * mu);
    }

    public static Double[] interpolate(Double[] a) {

        // Check that have at least the very first and very last values non-null
        if (!(a[0] != null && a[a.length - 1] != null))
            return null;

        ArrayList<Integer> non_null_idx = new ArrayList<Integer>();
        ArrayList<Integer> steps = new ArrayList<Integer>();

        int step_cnt = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != null) {
                non_null_idx.add(i);
                if (step_cnt != 0) {
                    steps.add(step_cnt);
                    System.err.println("aDDed step >> " + step_cnt);
                }
                step_cnt = 0;
            } else {
                step_cnt++;
            }
        }

        Double f_start = null;
        Double f_end = null;
        Double f_step = null;
        Double f_mu = null;

        int i = 0;
        while (i < a.length - 1) // Don't do anything for the very last element (which should never be null)
        {
            if (a[i] != null && non_null_idx.size() > 1 && steps.size() > 0) {
                f_start = a[non_null_idx.get(0)];
                f_end = a[non_null_idx.get(1)];
                f_step = new Double(1.0) / new Double(steps.get(0) + 1);
                f_mu = f_step;
                non_null_idx.remove(0);
                steps.remove(0);
            } else if (a[i] == null) {
                a[i] = LinearInterpolate(f_start, f_end, f_mu);
                f_mu += f_step;
            }
            i++;
        }

        return a;
    }

    public static Double[] interpolate2(Double[] x, int newLength) {
        Double[] y = null;
        if (newLength > 0) {
            int N = x.length;
            if (N == 1) {
                y = new Double[1];
                y[0] = x[0];
                return y;
            } else if (newLength == 1) {
                y = new Double[1];
                int ind = (int) Math.floor(N * 0.5 + 0.5);
                ind = Math.max(1, ind);
                ind = Math.min(ind, N);
                y[0] = x[ind - 1];
                return y;
            } else {
                y = new Double[newLength];
                double newBeta = 1.0;

                if (newLength > 2)
                    newBeta = (N - 2.0) / (newLength - 2.0);

                y[0] = x[0];
                y[1] = x[1];
                y[newLength - 1] = x[N - 1];

                double tmp, alpha;
                int i, j;
                for (i = 2; i <= newLength - 2; i++) {
                    tmp = 1.0 + (i - 1) * newBeta;
                    j = (int) Math.floor(tmp);
                    alpha = tmp - j;
                    y[i] = (1.0 - alpha) * x[Math.max(0, j)] + alpha * x[Math.min(N - 1, j + 1)];
                }
            }
        }

        return y;
    }

    public static Double[] interpolate3(Double[] contour) {
        List<Double> tmpX = Arrays.asList(contour);

        Collections.replaceAll(tmpX, null, 0.0);

        contour = (Double[]) tmpX.toArray();

        for (int i = 0; i < contour.length; i++) {
            if (contour[i] == 0) {
                int index = findNextIndexNonZero(contour, i);
                // System.out.println("i: "+i+"index: "+index);
                if (index == -1) {
                    for (int j = (i == 0 ? 1 : i); j < contour.length; j++) {
                        contour[j] = contour[j - 1];
                    }
                    break;
                } else {
                    for (int j = i; j < index; j++) {
                        // contour[j] = contour[i-1] * (index - j) + contour[index] * (j - (i-1)) / (
                        // index - i );
                        if (i == 0) {
                            contour[j] = contour[index];
                        } else {
                            contour[j] = contour[j - 1] + ((contour[index] - contour[i - 1]) / (index - i + 1));
                        }
                    }
                    i = index - 1;
                }
            }
        }

        return contour;
    }

    /**
     * To find next NonZero index in a given array
     * 
     * @param contour
     *                contour
     * @param current
     *                current
     * @return -1
     */
    public static int findNextIndexNonZero(Double[] contour, int current) {
        for (int i = current + 1; i < contour.length; i++) {
            if (contour[i] != 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Find the maximum of all elements in the array, ignoring elements that are
     * NaN.
     * 
     * @param data
     * @return
     */
    public static double max(double[] data) {
        double max = Double.NaN;
        for (int i = 0; i < data.length; i++) {
            if (Double.isNaN(data[i]))
                continue;
            if (Double.isNaN(max) || data[i] > max)
                max = data[i];
        }
        return max;
    }

    public static int max(int[] data) {
        int max = data[0];
        for (int i = 1; i < data.length; i++) {
            if (data[i] > max)
                max = data[i];
        }
        return max;
    }

    /**
     * Find the minimum of all elements in the array, ignoring elements that are
     * NaN.
     * 
     * @param data
     * @return
     */
    public static double min(double[] data) {
        double min = Double.NaN;
        for (int i = 0; i < data.length; i++) {
            if (Double.isNaN(data[i]))
                continue;
            if (Double.isNaN(min) || data[i] < min)
                min = data[i];
        }
        return min;
    }

    public static int min(int[] data) {
        int min = data[0];
        for (int i = 1; i < data.length; i++) {
            if (data[i] < min)
                min = data[i];
        }
        return min;
    }
}
