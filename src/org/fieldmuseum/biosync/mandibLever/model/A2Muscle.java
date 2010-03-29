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
public class A2Muscle extends MandibLeverMuscle implements Serializable {
    /**
     * All specimens' A2 muscles in MandibLever are given the same pennation angle
     */
    private static double pennationAngle = 0;

    private static final double tendonLength = 0; //A2 tendon is not used in MandibLever

    /**
     * A convenience constructor for creating a muscle of known length to be
     * attached to an existing mandible.
     * Note: to use this constructor, the specimen must already have a mandible.
     * This muscle will be attached to the A2 insertion joint on the mandible.
     * @param specimen
     * @param jointDist
     * @param length
     * @param mass
     */
    public A2Muscle(Specimen specimen, Joint insertion, double jointDist, double length, double mass) {
        super(specimen, createOriginJoint(specimen.getMandible(), insertion, jointDist, length), insertion, tendonLength, mass, pennationAngle);
    }

    public A2Muscle(Specimen specimen, Joint j1, Joint j2, double mass) {
        super(specimen, j1, j2, tendonLength, mass, pennationAngle);
    }

    public A2Muscle() {
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
