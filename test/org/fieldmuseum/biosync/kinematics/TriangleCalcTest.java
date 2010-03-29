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

import static org.junit.Assert.*;

import org.fieldmuseum.biosync.kinematics.TriangleCalc;
import org.junit.Test;

public class TriangleCalcTest {
	static final double CON = 0.01745329252;

	@Test
	public void testTriangleCalc() {
		//generate some sides and test TriangleCalc.triangleCalc() against this.triangleCalcReference()
		testOneTriangle(new double[] {1,1,1});
		testOneTriangle(new double[] {3,4,5});
		testOneTriangle(new double[] {42,42,1});
		testOneTriangle(new double[] {42,1,42});
		testOneTriangle(new double[] {1,42,42});
		testOneTriangle(new double[] {42, 21, 21});
		testOneTriangle(new double[] {21, 42, 21});
		testOneTriangle(new double[] {21, 21, 42});
		testOneTriangle(new double[] {42,1,1});
		testOneTriangle(new double[] {1,42,1});
		testOneTriangle(new double[] {1,1,42});
	}
	
	private void testOneTriangle(double[] sides) {
		double epsilon = 0.001;
		
		double[] anglesExpected = triangleCalcReference(sides[0], sides[1], sides[2]);
		double[] angles = TriangleCalc.triangleCalc(sides);
		assertEquals("sidea", anglesExpected[0], angles[0], epsilon);
		assertEquals("sideb", anglesExpected[1], angles[1], epsilon);
		assertEquals("sidec", anglesExpected[2], angles[2], epsilon);
	}

	/**
	 * Literal translation of Mark's procedure to java
	 * @return {angbc, angac, angab} in degrees
	 */
	public static double[] triangleCalcReference(double sidea, double sideb, double sidec) {
		
		double angab = 0;
		double angac = 0; 
		double angbc = 0;
		boolean check1 = false;
		boolean check2 = false;
		boolean check3 = false;
		
		if (sidea * sidea < sideb * sideb + sidec * sidec) {
			double a = sideb * sideb + sidec * sidec - sidea * sidea;
			double b = 2 * sideb * sidec;
			double ang = a / b;
			double c = 1 - ang * ang;
			angbc = Math.atan(Math.sqrt(c) / ang) / CON;
			check1 = true;
		}
		
		if (sideb * sideb < sidea * sidea + sidec * sidec) {
			double a = sidea * sidea + sidec * sidec - sideb * sideb;
			double b = 2 * sidea * sidec;
			double ang = a / b;
			double c = 1 - ang * ang;
			angac = Math.atan(Math.sqrt(c) / ang) / CON;
			check2 = true;
		}
		
		if (sidec * sidec < sidea * sidea + sideb * sideb) {
			double a = sidea * sidea + sideb * sideb - sidec * sidec;
			double b = 2 * sidea * sideb;
			double ang = a / b;
			double c = 1 - ang * ang;
			angab = Math.atan(Math.sqrt(c) / ang) / CON;
			check3 = true;
		}
		
		if (!check1) {
			angbc = 180 - (angab + angac);
		}
		if (!check2) {
			angac = 180 - (angab + angbc);
		}
		if (!check3) {
			angab = 180 - (angbc + angac);
		}
		
		return new double[] {angbc, angac, angab};
	}
}
