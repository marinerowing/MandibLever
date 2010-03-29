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
package org.fieldmuseum.biosync.biomechanics.muscle;

/**
 * <pre>
 * The normalized form of Hill's Equation
 * F' = (1 - V')/(1 + V'/k)
 * 
 * where F' = F/F0
 * 		 V' = V/Vmax
 *       k is an empirical constant in the range 0.15 < k < 0.25
 * 
 * From 
 * Hill, A.V., 1938. The heat of shortening and the dynamic constants of
 * muscle. Proc. R. Soc. B. 141, 104-117.
 * 
 * Cited by
 * Westneat, M. W. 2003. A biomechanical model for analysis of muscle
 * force, power output and lower jaw motion in fishes. J. Theor.
 * Biol. 223:269-281.
 * 
 * </pre>
 * 
 * @author kurie
 */
public abstract class HillEquation {
	static final double K = 0.25; //the value used by Mark in Westneat, M. W. 2003

	/** 
	 * Get the force at a given velocity.
	 * @param v the normalized velocity, V/Vmax
	 * @return the normalized force F/F0
	 */
	public static double getF(double v) {
		return (1 - v)/(1 + v/K);
	}

	/**
	 * Get the velocity at a given force.  (The inverse of getF()).
	 * @param f the normalized force F/F0
	 * @return v the normalized velocity, V/Vmax
	 */
	public static double getV(double f) {
		return (K - K * f)/(f + K);
	}
}
