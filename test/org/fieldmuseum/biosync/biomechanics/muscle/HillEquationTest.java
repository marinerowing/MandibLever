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

import static org.junit.Assert.*;

import org.fieldmuseum.biosync.biomechanics.muscle.HillEquation;
import org.junit.Test;

public class HillEquationTest {

	/**
	 * Tests the a given v against HillEquation.getV(HillEquation.getF(v))
	 */
	@Test
	public void testGetV() {
		double epsilon = 0.00001;
		
		for (double v = 0.0; v <= 3.0; v += 0.1) {
			double f = HillEquation.getF(v);
			double vCalc = HillEquation.getV(f);
			assertEquals("v = " + v, vCalc, v, epsilon);
		}
	}

}
