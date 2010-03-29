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

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.fieldmuseum.biosync.biomechanics.muscle.Muscle;
import org.fieldmuseum.biosync.kinematics.ImmobileJoint;
import org.fieldmuseum.biosync.kinematics.Joint;
import org.fieldmuseum.biosync.kinematics.TriangleCalc;

/**
 * This class adds property change support, automatic length-velocity-force
 * updating and some extra fields to hold MandibLever velocity constraints.
 * Also, a utility method for locating the origin of a muscle given two other
 * joints and the distance to them.
 * @author kurie
 */
public class MandibLeverMuscle extends Muscle {
    protected PropertyChangeSupport changeSupport;
    protected static PropertyChangeSupport staticChangeSupport = new PropertyChangeSupport(MandibLeverMuscle.class);

    private static double peakVMaxFraction = 0.8;
    private static double minVMaxFraction = 0.05;
    public final LinearVelocityModel velocityModel; //since it's final and immutable, it should be safe to make this public

	/**
	 * Create a new muscle
	 * @param tendonLength the tendon length, in cm
	 * @param mass the mass of the muscle, in g
	 */
	public MandibLeverMuscle(Specimen specimen, Joint j1, Joint j2, double tendonLength, double mass, double pennationAngle) {
		super(j1, j2, tendonLength, mass, pennationAngle);
        changeSupport = new PropertyChangeSupport(this);

        velocityModel = new LinearVelocityModel(this, specimen);

        j1.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                handleJointEvent(evt);
            }
        });

        j2.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                handleJointEvent(evt);
            }
        });
	}

    public MandibLeverMuscle() {
        super();
        velocityModel = null;
        changeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Creates a new ImmobileJoint at the origin of a muscle, given its
     * end-Joints and the distances from the joints to the origin of the muscle.
     * @param originToJoint
     *     the distance from the origin of the muscle to the quadrate-articular joint
     *
     * @param originToInsertion
     *     the length of the muscle
     *
     * @param qaJoint
     *     the quadrate-articular joint
     *
     * @param muscleInsertion
     *     the joint where the muscle attaches to the mandible
     *
     * @return the joint
     */
    protected static Point2D locateOrigin(double originToJoint, double originToInsertion, Joint qaJoint, Joint muscleInsertion) {

        //do a triangle check on the measurements
        double jointToInsertion = qaJoint.distance(muscleInsertion);
        if (!TriangleCalc.validTriangle(jointToInsertion, originToJoint, originToInsertion)) {
            String message = "{jointToInsertion, originToJoint, originToInsertion} = {"
                    + jointToInsertion + "," + originToJoint + "," + originToInsertion
                    + "} is not a valid triangle.";
            throw new IllegalArgumentException(message);
        }

        //locate the origin of the muscle
        double jointAngle = TriangleCalc.getAngleAB(jointToInsertion, originToJoint, originToInsertion); //the angle at the joint
        double inputAngle = Math.atan(muscleInsertion.getY()/muscleInsertion.getX()); //the angle of the joint-to-insertion member to horizontal
        double originAngle = jointAngle + inputAngle;  //The angle at the joint from horizontal to the origin of the a2
        double x = qaJoint.getX() + originToJoint * Math.cos(originAngle);
        double y = qaJoint.getY() + originToJoint * Math.sin(originAngle);
        Point2D origin = new Point2D.Double(x, y);

        return origin;
    }

    protected static Joint createOriginJoint(Mandible mandible, Joint insertion, double jointDist, double length) {
        Joint qaJoint = mandible.getQaJoint();
        return new ImmobileJoint(locateOrigin(jointDist, length, qaJoint, insertion));
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        //attempt to remove first, in case the listener was already added.  (Don't want to receive multiple events)
        removePropertyChangeListener(listener);
        changeSupport.addPropertyChangeListener(listener);
        staticChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
        staticChangeSupport.removePropertyChangeListener(listener);
    }

    public static void setFMax(double fMax) {
        double oldValue = getForcePerAreaMax();
        Muscle.setForcePerAreaMax(fMax);
        staticChangeSupport.firePropertyChange("fMax", oldValue, fMax);
    }

    @Override
    public void setForce(double force) {
        double oldValue = getForce();
        super.setForce(force);
        changeSupport.firePropertyChange("force", oldValue, force);
    }

    @Override
    public void setForceFraction(double forceFraction) {
        double oldValue = getForce();
        super.setForceFraction(forceFraction);
        changeSupport.firePropertyChange("force", oldValue, getForce());
    }

    @Override
    public void setMass(double mass) {
        double oldValue = getMass();
        super.setMass(mass);
        changeSupport.firePropertyChange("mass", oldValue, mass);
    }

    @Override
    public void setPennationAngle(double pennationAngle) {
        double oldValue = getPennationAngle();
        super.setPennationAngle(pennationAngle);
        changeSupport.firePropertyChange("pennationAngle", oldValue, pennationAngle);
    }

    @Override
    public void setTendonLength(double tendonLength) {
        double oldValue = getTendonLength();
        super.setTendonLength(tendonLength);
        changeSupport.firePropertyChange("tendonLength", oldValue, tendonLength);
    }

    public static void setVMax(double vMax) {
        double oldValue = getVelocityPerLengthMax();
        Muscle.setVelocityPerLengthMax(vMax);
        staticChangeSupport.firePropertyChange("vMax", oldValue, vMax);
    }

    @Override
    public void setVelocity(double velocity) {
        double oldValue = getVelocity();
        super.setVelocity(velocity);
        changeSupport.firePropertyChange("velocity", oldValue, velocity);
    }

    /**
     * @return the peakVMaxFraction
     */
    public static double getPeakVMaxFraction() {
        return peakVMaxFraction;
    }

    /**
     * @param peakVMaxFraction the peakVMaxFraction to set
     */
    public static void setPeakVMaxFraction(double peakVMaxFraction) {
        MandibLeverMuscle.peakVMaxFraction = peakVMaxFraction;
    }

    /**
     * @return the minVMaxFraction
     */
    public static double getMinVMaxFraction() {
        return minVMaxFraction;
    }

    /**
     * @param minVMaxFraction the minVMaxFraction to set
     */
    public static void setMinVMaxFraction(double minVMaxFraction) {
        MandibLeverMuscle.minVMaxFraction = minVMaxFraction;
    }

    private void handleJointEvent(PropertyChangeEvent evt) {
        Point2D oldLocation = (Point2D) evt.getOldValue();
        double oldLength;
        if (evt.getSource() == j1) {
            oldLength = MandibLeverMuscle.this.j2.distance(oldLocation);
        } else {
            oldLength = MandibLeverMuscle.this.j1.distance(oldLocation);
        }

        //update velocity and force
        setVelocity(velocityModel.getV());
        setForce(velocityModel.getF());

        changeSupport.firePropertyChange("length", oldLength, getLength());
    }
}
