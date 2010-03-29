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

/**
 * A 1D linear elastic bar
 * @author kurie
 */
public class ElasticBar extends PlasticBar {
    /** The modulus of elasticity, in Pa */
    private double modulus;

    public ElasticBar(Joint j1, Joint j2, double modulus) {
        super(j1, j2);
    }

    /**
     * @return the modulus
     */
    public double getModulus() {
        return modulus;
    }

    /**
     * @param modulus the modulus to set
     */
    public void setModulus(double modulus) {
        this.modulus = modulus;
    }

    public double getStress() {
        return modulus * getStrain();
    }
}
