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

import java.io.Serializable;
import org.fieldmuseum.biosync.kinematics.Joint;
import org.fieldmuseum.biosync.kinematics.MobileJoint;
import org.fieldmuseum.biosync.kinematics.PlasticBar;

public class Muscle extends PlasticBar implements Serializable {

	/**
	 * The maximum isometric force per unit area of a muscle, in terms of kPa.
	 * Varies among vertebrate muscles in different body regions, but values for
	 * fish muscle range from about 100 to 200 kN/m2 (kPa) in red fibers or
	 * white fibers involved in locomotion.
	 */
	private static double forcePerAreaMax = 200;
	
	/**
	 * The maximum contraction velocity of a muscle, in terms of muscle lengths
	 * per second. Values range from 3-5 muscle lengths/s in fish red muscle, to
	 * as high as 8-10 lengths/s in fast white muscle of fishes
	 */
	private static double velocityPerLengthMax = 10;
	
	/** The density of a muscle, in g/cm^3 */
	public static double muscleDensity = 1.05;

    /** The tendon length, in cm */
	private double tendonLength;

    /** The mass, in grams */
	private double mass;

    /** The pennation angle, in radians */
	private double pennationAngle;

    /*
     * Note: I decided to store the fractional force and velocity instead of the
     * actual values, so that changes to other fields (e.g. mass) would be
     * reflected on the next call to, for example, getForce().
     */
    /** The current muscle contraction force, as a fraction of getMaxForce() */
    private double forceFraction;
    /** The current velocity, as a fraction of getMaxVelocity() */
    private double velocityFraction;

	/**
     * Create a new muscle
     * @param j1 a joint at an end of the muscle
     * @param j2 a joint at an end of the muscle
     * @param tendonLength the length of the tendon, in cm
     * @param mass the mass, in grams
     * @param pennationAngle, the pennation angle, in radians
     */
	public Muscle(Joint j1, Joint j2, double tendonLength, double mass, double pennationAngle) {
		super(j1, j2);
		this.tendonLength = tendonLength;
		this.mass = mass;
		this.pennationAngle = pennationAngle;
        this.forceFraction = 0;
        this.velocityFraction = 0;
	}

    /**
     * Creates a Muscle with default values for all fields.
     */
    public Muscle() {
        this(new MobileJoint(), new MobileJoint(), 0.0, 0.0, 0.0);
    }

     /**
     * @return the velocityPerLengthMax
     */
    public static double getVelocityPerLengthMax() {
        return velocityPerLengthMax;
    }

    /**
     * @param vMax the velocityPerLengthMax to set
     */
    public static void setVelocityPerLengthMax(double vMax) {
        Muscle.velocityPerLengthMax = vMax;
    }

    /**
     * @return the forcePerAreaMax
     */
    public static double getForcePerAreaMax() {
        return forcePerAreaMax;
    }

    /**
     * @param fMax the forcePerAreaMax to set
     */
    public static void setForcePerAreaMax(double fMax) {
        Muscle.forcePerAreaMax = fMax;
    }

    /**
     * @return the distance between the muscle's end joints when it was
     * constructed.  (This is just PlasticBar.getInitialLength() renamed for
     * clarity.) Includes tendon.
     */
    public double getRestingLength() {
        return getInitialLength();
    }
    
	/**
	 * @return an estimate of average muscle cross-sectional area, based on mass and length, in cm^2
	 */
	public double getCrossSectionArea() {
		double vol = getMass() / muscleDensity;
		return vol/getFiberLength(); 
	}

    /**
     * @return the current length of the muscle tissue (without the tendon), in cm.
     * The tendon is assumed to be inelastic.
     */
    public double getCurrentMuscleLength() {
        return getLength() - getTendonLength();
    }
	
	/**
	 * @return the resting length of the pennate muscle fibers, in cm
	 */
	public double getFiberLength() {
		return Math.cos(getPennationAngle()) * getRestingLength() - getTendonLength();
	}
	
	/**
	 * @return the maximum tension this muscle is capable of, in Newtons
	 */
	public double getMaxForce() {
        //note conversions: forcePerAreaMax is in kN and crossSectionArea is in cm^2
		return getForcePerAreaMax() * 1000 * getCrossSectionArea() / 10000;
	}

    /**
     * @return the maximum velocity this muscle is capable of, in cm/s
     */
    public double getMaxVelocity() {
        return getVelocityPerLengthMax() * getFiberLength();
    }

    /**
     * @return the tendonLength
     */
    public double getTendonLength() {
        return tendonLength;
    }

    /**
     * @param tendonLength the tendonLength to set
     */
    public void setTendonLength(double tendonLength) {
        this.tendonLength = tendonLength;
    }

    /**
     * @return the mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * @return the pennationAngle, in radians
     */
    public double getPennationAngle() {
        return pennationAngle;
    }

    /**
     * @param pennationAngle the pennationAngle to set, in radians
     */
    public void setPennationAngle(double pennationAngle) {
        this.pennationAngle = pennationAngle;
    }

    /**
     * @return the current force of contraction
     */
    public double getForce() {
        return getForceFraction() * getMaxForce();
    }

    /**
     * Set the current force of contraction (in N).  The force will be
     * constrained to the range [0, this.getMaxForce()]
     * @param force the force to set
     */
    public void setForce(double force) {
        if (force < 0) {
            this.forceFraction = 0.0;
        } else if (force > getMaxForce()) {
            this.forceFraction = 1.0;
        } else {
            this.forceFraction = force/getMaxForce();
        }
    }

    /**
     * @return the current force as a fraction of the maximum force this muscle
     * is capable of.
     */
    public double getForceFraction() {
        return forceFraction;
    }

    /**
     * Sets the current force of contraction, as a fraction of the
     * maximum force.  The fraction will be constrained to the range [0, 1]
     * @param force the force to set
     */
    public void setForceFraction(double forceFraction) {
        if (forceFraction < 0) {
            this.forceFraction = 0;
        } else if (forceFraction > 1.0) {
            this.forceFraction = 1.0;
        } else {
            this.forceFraction = forceFraction;
        }
    }

    /**
     * @return the velocity, in cm/s
     */
    public double getVelocity() {
        return velocityFraction * getMaxVelocity();
    }

    /**
     * @param velocity the velocity to set, in cm/s
     */
    public void setVelocity(double velocity) {
        if (velocity > getMaxVelocity()) {
            this.velocityFraction = 1.0;
        } else if (velocity < 0.0) {
            this.velocityFraction = 0.0;
        } else {
            this.velocityFraction = velocity/getMaxVelocity();
        }
    }

    /**
     * @return the velocity, as a fraction of the maximum velocity that this
     * muscle is capable of
     */
    public double getVelocityFraction() {
        return velocityFraction;
    }

    /**
     * @param velocity the velocity to set, as a fraction of the maximum
     * velocity that this muscle is capable of
     */
    public void setVelocityFraction(double velocityFraction) {
        if (velocityFraction > 1.0) {
            this.velocityFraction = 1.0;
        } else if (velocityFraction < 0.0) {
            this.velocityFraction = 0.0;
        } else {
            this.velocityFraction = velocityFraction;
        }
    }
}
