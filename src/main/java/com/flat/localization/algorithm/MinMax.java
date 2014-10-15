package com.flat.localization.algorithm;

import com.flat.localization.Node;

/**
 * @author Jacob Phillips
 */
public final class MinMax extends PositionAlgorithm {
    /**
     * Calculate a position based on the Min/Max algorithm (up to three dimensions).
     * @param positions list of reference nodes' coordinates
     * @param ranges linear distance to each reference node
     * @return the new position.
     */
    @Override
    public double[] findCoords(double[][] positions, double[] ranges) {
        // initialize x, y, and z coordinates.
        double[] min = { Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE};
        double[] max = { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};

        // Find the maximum min and the minimum max values in each dimension.
        for (int i = 0; i < positions.length; ++i) {
            for (int j = 0; j < 3; ++j) {
                double ref = positions[i][j];
                if (ref-ranges[i] > min[j]) min[j] = ref-ranges[i];   // max of min
                if (ref+ranges[i] < max[j]) max[j] = ref+ranges[i];   // min of max
            }
        }

        // New position is center of min/max rectangle
        return new double[] {(min[0]+max[0])/2,
                             (min[1]+max[1])/2,
                             (min[2]+max[2])/2};
    }
}
