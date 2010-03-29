/*
 * Copyright (C) 2010 Mark Westneat, Kristopher Urie
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fieldmuseum.biosync.kinematics;

import java.awt.geom.Point2D;

/**
 * This is a re-implementation of Mark's TriangleCalc Pascal procedure.
 * @author kurie
 */
public class TriangleCalc {

	/**
	 * Calculates the angles of a triangle (in degrees), given the lengths of its sides.
	 * This method just calls triangleCalcRadians and converts to degrees.
     * @param sides the lengths of the sides
	 * @return the angles {1_2, 0_2, 0_1} in degrees
	 */
	public static double[] triangleCalc(double[] sides) {
		double[] angles = triangleCalcRadians(sides);
		for (int i = 0; i < angles.length; i++) {
			angles[i] = Math.toDegrees(angles[i]);
		}
		return angles;
	}
	
	/**
	 * Calculates the angles of a triangle, given the lengths of its sides.
     * @param sides the lengths of the sides.  All lengths must be positive.
	 * @return the angles {1_2, 0_2, 0_1} in radians
	 */
	public static double[] triangleCalcRadians(double[] sides) {
		double[] angles = new double[3]; //{angle1_2, angle0_2, angle0_1}

        angles[0] = getAngleAB(sides[1], sides[2], sides[0]);
        angles[1] = getAngleAB(sides[0], sides[2], sides[1]);
        angles[2] = getAngleAB(sides[0], sides[1], sides[2]);
		
		return angles;
	}

    /**
     * Gets an angle of a triangle, given all three sides. (Law of cosines.)
     * @return the angle between sideA and sideB, in radians
     */
    public static double getAngleAB(double sideA, double sideB, double sideC) {
			double num = sideA * sideA + sideB * sideB - sideC * sideC;
			double denom = 2 * sideA * sideB;
			return Math.acos(num/denom);
    }

    /**
     * Gets the angle of the line from pointA to pointB, relative to the +x axis.
     */
    public static double getAngle(Point2D pointA, Point2D pointB) {
        return Math.atan2(pointB.getY() - pointA.getY(), pointB.getX() - pointA.getX());
    }

    /**
     * @return true if sideA, sideB and side C can form a valid triangle
     */
    public static boolean validTriangle(double sideA, double sideB, double sideC) {
        boolean valid = true;
        valid &= sideA <= sideB + sideC;
        valid &= sideB <= sideA + sideC;
        valid &= sideC <= sideA + sideB;

        return valid;
    }
}
