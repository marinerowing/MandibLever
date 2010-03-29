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

import org.fieldmuseum.biosync.biomechanics.muscle.HillEquation;

/**
 * A model where the velocity is proportional to the current muscle length,
 * starting with maximum velocity and maximum length and going to minimum
 * velocity and length.
 * @author kurie
 */
public class LinearVelocityModel {

    private MandibLeverMuscle muscle;

    private Specimen specimen;

    /**
     * Create a new LinearVelocityModel for a given muscle in a given specimen.
     */
    public LinearVelocityModel(MandibLeverMuscle muscle, Specimen specimen) {
        this.muscle = muscle;
        this.specimen = specimen;
    }

    /**
     * Get the velocity at a given length.
     * @param len the current muscle length
     * @return the velocity at this length
     */
    public double getV() {
        return v0() + dvdl() * (muscle.getLength() - len0());
    }

    /**
     * Gets the current time at a given length.
     * Time starts at t=0, jaw rotation = max open, velocity=vMax.
     * @param len the current muscle length
     * @return the time at this length
     */
    public double getT() {
        return -Math.log(getV()) / dvdl() + Math.log(v0()) / dvdl();
    }

    /**
     * Gets the force at a given length, based on the Hill equation.
     * @param len the current muscle length
     * @return the force at this length
     */
    public double getF() {
        double forceFraction = HillEquation.getF(getV()/muscle.getMaxVelocity());
        return forceFraction * muscle.getMaxForce();
    }

    /**
     * Gets the work performed by the muscle from the initial condition to the
     * present.
     */
    public double getTotalWork() {
        //I could do this analytically, but I'll just do a numerical approximation for now:
        int bins = 20;
        double length = len0();
        double step = (muscle.getLength() - length)/bins; //step from len0 to current length
        double work = 0;
        for (int bin = 0; bin < bins; bin++) {
            length += step;
            double velocity = v0() + dvdl() * (length - len0());
            double force = HillEquation.getF(velocity/muscle.getMaxVelocity()) * muscle.getMaxForce();
            work += Math.abs(force * step); //note: in the same units as (muscle velocity * muscle force) (not necessarily joules)
        }

        return work;
    }
    
    /**
     * Gets the slope of the length-velocity curve.
     * The jaw starts at max-open rotation with maximum velocity, ends jaw
     * closed with minimum velocity.
     * @return dv/dl
     */
    private double dvdl() {
        double len1 = specimen.getMinMuscleLength(muscle);
        double v1 = MandibLeverMuscle.getMinVMaxFraction() * muscle.getMaxVelocity();

        return (v1 - v0()) / (len1 - len0());
    }

    /**
     * @return the starting velocity of the muscle
     */
    private double v0() {
        return MandibLeverMuscle.getPeakVMaxFraction() * muscle.getMaxVelocity();
    }

    /**
     * @return the starting length of the muscle
     */
    private double len0() {
        return specimen.getMaxMuscleLength(muscle);
    }
}
