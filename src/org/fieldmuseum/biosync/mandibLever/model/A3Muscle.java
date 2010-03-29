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
package org.fieldmuseum.biosync.mandibLever.model;

import java.io.Serializable;
import org.fieldmuseum.biosync.kinematics.Joint;

/**
 *
 * @author kurie
 */
public class A3Muscle extends MandibLeverMuscle implements Serializable {
    /**
     * All specimens' A3 muscles in MandibLever are given the same pennation angle
     */
    private static double pennationAngle; //hides the field in the superclass

    /**
     * A convenience constructor for creating a muscle of known length to be
     * attached to an existing mandible.
     * Note: to use this constructor, the specimen must already have a mandible.
     * @param specimen
     * @param jointDist
     * @param length
     * @param mass
     */
    public A3Muscle(Specimen specimen, Joint insertion, double jointDist, double length, double tendonLength, double mass) {
        super(specimen, createOriginJoint(specimen.getMandible(), insertion, jointDist, length), insertion, tendonLength, mass, pennationAngle);
    }

    public A3Muscle(Specimen specimen, Joint j1, Joint j2, double tendonLength, double mass) {
        super(specimen, j1, j2, tendonLength, mass, pennationAngle);
    }

    public A3Muscle() {
        super();
    }

     /**
     * @return the pennationAngle
     */
    @Override
    public double getPennationAngle() {
        return pennationAngle;
    }

    /**
     * Sets the pennation angle for the A2 muscle in all specimens
     * @param aPennationAngle the pennationAngle to set
     */
    @Override
    public void setPennationAngle(double aPennationAngle) {
        double oldValue = pennationAngle;
        pennationAngle = aPennationAngle;
        staticChangeSupport.firePropertyChange("pennationAngle", oldValue, pennationAngle);
    }
}