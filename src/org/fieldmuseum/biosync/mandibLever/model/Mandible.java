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

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Set;
import org.fieldmuseum.biosync.biomechanics.muscle.Muscle;
import org.fieldmuseum.biosync.kinematics.ImmobileJoint;
import org.fieldmuseum.biosync.kinematics.Joint;
import org.fieldmuseum.biosync.kinematics.Member;
import org.fieldmuseum.biosync.kinematics.MobileJoint;
import org.fieldmuseum.biosync.kinematics.PlasticBar;
import org.fieldmuseum.biosync.kinematics.TriangleCalc;

/**
 * A fish mandible, constructed based on morphometric data.
 * @author kurie
 */
public class Mandible implements Member, Cloneable {
    //TODO put everything in standard units and get rid of the conversions.  They're hard to document and error prone.

    private static double maxRotation = -Math.PI/6;
    private static PropertyChangeSupport staticChangeSupport = new PropertyChangeSupport(Mandible.class);

    /***************************************************************************
     * The mandible joints.  Each joint has a corresponding Unrotated copy, so
     * that the current transform can be applied to the original locations.
     **************************************************************************/
    /** The quadrate-articular joint */
    private Joint qaJoint, qaJointUnrotated;

    /** The insertion of the A2 muscle on ascending process of the articular. */
    private Joint a2Insertion, a2InsertionUnrotated;

    /** The insertion of the A3 muscle on medial face of the mandible.*/
    private Joint a3Insertion, a3InsertionUnrotated;

    /** The insertion of the interoperculomandibular ligament on the posteroventral margin of the articular */
    private Joint iomLigamentInsertion, iomLigamentInsertionUnrotated;

    /** The anterior-most tip of the dentary or tip of the anteriormost canine tooth. */
    private Joint anteriorJawTip, anteriorJawTipUnrotated;

    /**
     * The current rotation of the mandible
     */
    private AffineTransform rotation;

    private PropertyChangeSupport changeSupport;

    /**
     * Creates a new Mandible with the following measurements (all in centimeters):
     * @param a2InLever
     * 1. Inlever for A2 division of adductor mandibulae muscle, from quadrate-articular joint to
     * insertion of A2 muscle on ascending process of articular.
     *
     * @param a3InLever
     * 2. Inlever for A3 division of adductor mandibulae muscle, from quadrate-articular joint to
     * insertion of A3 muscle on medial face of mandible.
     *
     * @param openInLever
     * 3. Inlever for jaw opening, from quadrate-articular joint to insertion of
     * interoperculomandibular ligament on posteroventral margin of articular
     *
     * @param outLever
     * 4. Outlever of lower jaw, from quadrate-articular joint to anterior-most tip of dentary or tip of
     * anteriormost canine tooth.
     *
     * @param a2A3InsertionDistance
     * 10. Distance from A2 insertion to A3 insertion.
     *
     * @param mandibleDorsalLength
     * 11. Mandible dorsal length: tip of coronoid process of articular to anterior jaw tip.
     *
     * @param mandibleVentralLength
     * 12. Mandible ventral length: posteroventral margin of articular to anterior jaw tip.
     */
    public Mandible(double a2InLever, double a3InLever, double openInLever, double outLever, double a2A3InsertionDistance, double mandibleDorsalLength, double mandibleVentralLength) {
        this.changeSupport = new PropertyChangeSupport(this);

        //do a triangle check of the three sides of the three triangles (upper, lower and A3 lever)
        if (!TriangleCalc.validTriangle(a2InLever, outLever, mandibleDorsalLength)) {
            throw new IllegalArgumentException("a2InLever, outLever and mandibleDorsalLength cannot form a valid triangle.");
        }
        if (!TriangleCalc.validTriangle(openInLever, outLever, mandibleVentralLength)) {
            throw new IllegalArgumentException("openInLever, outLever and mandibleVentralLength cannot form a valid triangle.");
        }
        if (!TriangleCalc.validTriangle(a2InLever, a3InLever, a2A3InsertionDistance)) {
            throw new IllegalArgumentException("a2InLever, a3InLever and a2A3InsertionDistance cannot form a valid triangle.");
        }

        //place the rotation joint at the origin
        this.qaJoint = new ImmobileJoint();

        //The line from the qaJoint to the anteriorJawTip will be horizontal with the tip at the right.
        this.anteriorJawTip = new MobileJoint(outLever, 0);

        //The iomLigamentInsertion is the third corner of the lower triangle, and is defined to be below the outlever
        double qaAngle = -TriangleCalc.getAngleAB(openInLever, outLever, mandibleVentralLength); //the angle formed by openInLever and outLever
        this.iomLigamentInsertion = new MobileJoint(openInLever * Math.cos(qaAngle), openInLever * Math.sin(qaAngle));

        //A2 insertion is the third corner of the upper triangle, and is defined to be above the outlever
        qaAngle = TriangleCalc.getAngleAB(a2InLever, outLever, mandibleDorsalLength); //the angle formed by a2InLever and outLever
        this.a2Insertion = new MobileJoint(a2InLever * Math.cos(qaAngle), a2InLever * Math.sin(qaAngle));

        /*
         * The a3Insertion forms the third corner of another triangle
         * (Note: This one makes the assumption that the a3Insertion is anterior
         * to a2InLever, which may not always be true.  The location of this
         * point could instead be specified by the caller to avoid any ambiguity.)
         */
        qaAngle = qaAngle - TriangleCalc.getAngleAB(a2InLever, a3InLever, a2A3InsertionDistance); //the angle between outLever and a3InLever
        this.a3Insertion = new MobileJoint(a3InLever * Math.cos(qaAngle), a3InLever * Math.sin(qaAngle));

        //make initial rotation such that the a2Insertion-to-anteriorJawTip member is horizontal.
        setUnrotatedLocations(); //set initial locations, so I can rotate relative to them
        double dorsalMemberAngle = TriangleCalc.getAngle(a2Insertion, anteriorJawTip);
        setRotation(-dorsalMemberAngle);
        setUnrotatedLocations(); //set initial locations again, to zero the rotation
    }

    /**
     * Creates a new Mandible from a set of joint locations.
     * @param qaJoint The quadrate-articular joint
     * @param a2Insertion The insertion of the A2 muscle on ascending process of the articular.
     * @param a3Insertion The insertion of the A3 muscle on medial face of the mandible.
     * @param iomLigamentInsertion The insertion of the interoperculomandibular ligament on the posteroventral margin of the articular
     * @param anteriorJawTip The anterior-most tip of the dentary or tip of the anteriormost canine tooth.
     */
    public Mandible(Point2D qaJoint, Point2D a2Insertion, Point2D a3Insertion, Point2D iomLigamentInsertion, Point2D anteriorJawTip) {
        this.qaJoint = new ImmobileJoint(qaJoint); //FIXME: all rotations should be around the QA joint, which may not be at (0,0)
        this.a2Insertion = new MobileJoint(a2Insertion);
        this.a3Insertion = new MobileJoint(a3Insertion);
        this.iomLigamentInsertion = new MobileJoint(iomLigamentInsertion);
        this.anteriorJawTip = new MobileJoint(anteriorJawTip);
        rotation = new AffineTransform(); //this constructor leaves the initial positions where they are (unlike the length-based one)
        setUnrotatedLocations();
        this.changeSupport = new PropertyChangeSupport(this);
    }

    public Mandible() {
        this(new Point2D.Double(), new Point2D.Double(), new Point2D.Double(), new Point2D.Double(), new Point2D.Double());
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public static void addStaticPropertyChangeListener(PropertyChangeListener listener) {
        staticChangeSupport.addPropertyChangeListener(listener);
    }

    public static void removeStaticPropertyChangeListener(PropertyChangeListener listener) {
        staticChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * @return the maxRotation
     */
    public static double getMaxRotation() {
        return maxRotation;
    }

    /**
     * @param radians the maxRotation to set, in radians
     */
    public static void setMaxRotation(double radians) {
        /* 
         * FIXME check this against the physical constraint that the lever arm
         * should not rotate past the muscle (for either muscle).  This method
         * will have to be made non-static.
         */
        double oldValue = maxRotation;
        maxRotation = radians;
        staticChangeSupport.firePropertyChange("maxRotation", oldValue, radians);
    }

    /**
     * Rotates the mandible.  Note that the jaw extends to the right , so a
     * positive rotation closes the jaw, and that the angle is in radians,
     * relative to its current orientation.
     * @param radians amount to rotate
     */
    public void rotate(double radians) {
        double oldValue = getRotation();
        rotation.rotate(radians);
        updateJointLocations();
        changeSupport.firePropertyChange("rotation", oldValue, getRotation());
    }

    /**
     * Rotates the mandible.  Note that a positive rotation closes the jaw, and
     * that the angle is in degrees, relative to its current orientation.
     * @param degrees amount to rotate
     */
    public void rotateDegrees(double degrees) {
        rotate(Math.toRadians(degrees));
    }

    /**
     * Sets the mandible rotation relative to the original orientation (closed).
     * @param radians the angle of rotation.  Note that negative rotations open
     * the jaw.
     */
    public void setRotation(double radians) {
        double oldValue = getRotation();
        rotation = AffineTransform.getRotateInstance(radians);
        updateJointLocations();
        changeSupport.firePropertyChange("rotation", oldValue, getRotation());
    }

    /**
     * @return the current rotation, in radians
     */
    public double getRotation() {
        return Math.asin(rotation.getShearY());
    }

    /**
     * @return the distance the jaw tip has moved from its original position, in
     * centimeters
     */
    public double getGape() {
        return anteriorJawTip.distance(anteriorJawTipUnrotated);
    }
    
    public void resetRotation() {
        double oldValue = getRotation();
        
        //reset rotation to identity transform
        rotation = new AffineTransform();

        //reset the joints
        updateJointLocations();

        changeSupport.firePropertyChange("rotation", oldValue, getRotation());
    }

    /**
     * Rotate the mandible so that a given joint is at a specified distance from
     * a given location.  This will be used by Specimen to achieve some desired
     * muscle contraction.
     * @param joint the joint
     * @param location the location
     * @param radius the desred distance from the location
     * @return whether this rotation is possible
     */
    public boolean rotateJointTo(Joint joint, Point2D location, double radius) {
        double pivotToLocation = qaJoint.distance(location);
        double pivotToJoint = qaJoint.distance(joint);
        double jointToLocation = joint.distance(location);

        double minRadius = Math.abs(pivotToLocation - pivotToJoint);
        double maxRadius = pivotToLocation + pivotToJoint;
        if (radius < minRadius || radius > maxRadius) {
            return false; //this joint cannot be rotated to the desired radius of location
        }

        //the old angle between the pivot-to-location line and the pivot-to-joint-location line
        double anglePivotOld = -TriangleCalc.getAngleAB(pivotToJoint, pivotToLocation, jointToLocation);

        //the new angle between the pivot-to-location line and the pivot-to-new-joint-location line
        double anglePivotNew = -TriangleCalc.getAngleAB(pivotToJoint, pivotToLocation, radius);

        rotate(anglePivotNew - anglePivotOld);

        return true;
    }

    /**
     * @return the shape of the mandible, for drawing
     */
    public Shape getShape() {

        Path2D.Double shape = new Path2D.Double();
        shape.moveTo(qaJoint.getX(), qaJoint.getY());
        shape.lineTo(iomLigamentInsertion.getX(), iomLigamentInsertion.getY());
        shape.lineTo(anteriorJawTip.getX(), anteriorJawTip.getY());
        shape.lineTo(a2Insertion.getX(), a2Insertion.getY());
        shape.lineTo(qaJoint.getX(), qaJoint.getY());
        shape.lineTo(a3Insertion.getX(), a3Insertion.getY());
        shape.lineTo(a2Insertion.getX(), a2Insertion.getY());
        shape.closePath();

        return shape;
    }

    /**
     * @return a shape to indicate the closed jaw position, for reference.
     */
    public Shape getUpperJawShape() {
        return new Line2D.Double(a2InsertionUnrotated, anteriorJawTipUnrotated);
    }

    /**
     * Gets the mechanical advantage of a force applied at the given point
     * transmitted to the end of the output lever arm (from the
     * quadrate-articular joint to the anterior tip of the jaw).
     * @param location the point where the force is applied to the jaw
     * @return the mechanical advantage
     */
    public double getMechanicalAdvantage(Point2D location) {
        double inputLever = qaJoint.distance(location);
        return inputLever/getOutLever();
    }

    /**
     * Gets the mechanical advantage of a force applied at the insertion point
     * of a given muscle.
     * @param muscle the muscle
     * @return the mechanical advantage
     * @throws IllegalArgumentException if the muscle is not attached to the mandible,
     * or for some other reason doesn't have two proper end joints.
     */
    public double getMechanicalAdvantage(Muscle muscle) {
        Joint insertion = getInsertion(muscle);
        return getMechanicalAdvantage(insertion);
    }

    /**
     * Gets a muscle's angle relative to its lever arm
     */
    public double getMuscleAngle(Muscle muscle) {
        Joint insertion = getInsertion(muscle);
        Joint origin = muscle.getOtherJoint(insertion);

        double inputLeverAngle = TriangleCalc.getAngle(qaJoint, insertion);
        double muscleAngle = TriangleCalc.getAngle(insertion, origin);

        return (Math.PI - (muscleAngle - inputLeverAngle)) % (2 * Math.PI);
    }

    /**
     * Gets the effective mechanical advantage of a force applied by the given
     * muscle.
     * @param muscle the muscle
     * @return the effective MA
     * @throws IllegalArgumentException if the muscle is not attached to the mandible,
     * or for some other reason doesn't have two proper end joints.
     */
    public double getEffectiveMechanicalAdvantage(Muscle muscle) {
        return Math.sin(getMuscleAngle(muscle)) * getMechanicalAdvantage(muscle);
    }

    /**
     * @return the torque applied by this muscle in N * cm
     */
    public double getTorque(Muscle m) {
        double inputLever = getInsertion(m).distance(qaJoint);
        return m.getForce() * Math.sin(getMuscleAngle(m)) * inputLever;
    }

    /**
     * @return the instantaneous angular velocity (in radians per second) of the
     * mandible due to a given muscle's contraction velocity.
     */
    public double getAngularVelocity(Muscle m) {
        //the tangential velocity of the insertion joint
        double velocity = m.getVelocity() / Math.sin(getMuscleAngle(m)); //in cm/s

        //the distance from the rotation joint to the insertion joint
        double distance = getInsertion(m).distance(qaJoint); //in cm
        return velocity / distance;
    }

    /**
     * Gets the distance that the insertion point of a muscle has moved from the
     * closed (zero-rotation) position.
     * @param m the muscle
     * @return the distance, in cm
     */
    public double getDistanceMoved(Muscle m) {
        Joint insertion = getInsertion(m);
        AffineTransform transform = AffineTransform.getRotateInstance(-getRotation());
        Point2D originalLocation = transform.transform(insertion, null);
        return insertion.distance(originalLocation);
    }

    /**
     * Gets the total distance that the insertion of a muscle will move from
     * jaw-open to jaw closed
     * @param m
     * @return
     */
    public double getMaxDistanceMoved(Muscle m) {
        Joint insertion = getInsertion(m);
        AffineTransform transform = AffineTransform.getRotateInstance(-getRotation()); //to closed position
        Point2D endLocation = transform.transform(insertion, null);
        transform = AffineTransform.getRotateInstance(getMaxRotation()); //to open position
        Point2D startLocation = transform.transform(endLocation, null);
        return startLocation.distance(endLocation);
    }

    /**
     * @return the instantaneous velocity of the jaw tip (in cm/s) due to a
     * given muscle's contraction velocity.
     */
    public double getTipVelocity(Muscle m) {
        double angV = getAngularVelocity(m);
        return angV * getOutLever();
    }

    /** @return the a2 input lever, in centimeters */
    public double getA2InLever() {
        return qaJoint.distance(a2Insertion);
    }

    /** @return the a3 input lever, in centimeters */
    public double getA3InLever() {
        return qaJoint.distance(a3Insertion);
    }

    /** @return the opening input lever, in centimeters */
    public double getOpenInLever() {
        return qaJoint.distance(iomLigamentInsertion);
    }

    /** @return the output lever, in centimeters */
    public double getOutLever() {
        return qaJoint.distance(anteriorJawTip);
    }

    /** @return the distance between a2 and a3 insertion points, in centimeters */
    public double getA2A3InsertionDistance() {
        return a2Insertion.distance(a3Insertion);
    }

    /** @return the distance from the a2 insertion to the jaw tip , in centimeters */
    public double getMandibleDorsalLength() {
        return anteriorJawTip.distance(a2Insertion);
    }

    /** @return the distance from the interoperculomandibular ligament insertion to the jaw tip , in centimeters */
    public double getMandibleVentralLength() {
        return anteriorJawTip.distance(iomLigamentInsertion);
    }

    // <editor-fold defaultstate="collapsed" desc="Boilerplate getters for joints">
    /**
     * @return the qaJoint
     */
    public Joint getQaJoint() {
        return qaJoint;
    }

    /**
     * @return the a2Insertion
     */
    public Joint getA2Insertion() {
        return a2Insertion;
    }

    /**
     * @return the a3Insertion
     */
    public Joint getA3Insertion() {
        return a3Insertion;
    }

    /**
     * @return the iomLigamentInsertion
     */
    public Joint getIomLigamentInsertion() {
        return iomLigamentInsertion;
    }

    /**
     * @return the anteriorJawTip
     */
    public Joint getAnteriorJawTip() {
        return anteriorJawTip;
    }

    public Set<Joint> getJoints() {
        Set<Joint> joints = new HashSet<Joint>();
        joints.add(qaJoint);
        joints.add(a2Insertion);
        joints.add(a3Insertion);
        joints.add(iomLigamentInsertion);
        joints.add(anteriorJawTip);
        return joints;
    }

    public void addJoint(Joint j) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    // </editor-fold>

    /**
     * Gets the joint where a muscle attaches to this mandible.  (Assumes that
     * exactly one joint of the muscle is attached to this mandible.)
     * @param muscle
     * @return
     */
    public Joint getInsertion(Muscle muscle) {
        Set<Joint> muscleJoints = muscle.getJoints();
        Set<Joint> jawJoints = getJoints();

        //figure out which muscle joint is attached to this mandible
        Joint insertion = null;
        for (Joint joint : muscleJoints) {
            if (jawJoints.contains(joint)) {
                insertion = joint;
            }
        }

        if (insertion == null) {
            throw new IllegalArgumentException("The muscle " + muscle + " isn't attached to this mandible.");
        }

        return insertion;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Mandible clone = (Mandible) super.clone();

        clone.a2Insertion = (Joint) a2Insertion.clone();
        clone.a2InsertionUnrotated = (Joint) a2InsertionUnrotated.clone();
        clone.a3Insertion = (Joint) a3Insertion.clone();
        clone.a3InsertionUnrotated = (Joint) a3InsertionUnrotated.clone();
        clone.anteriorJawTip = (Joint) anteriorJawTip.clone();
        clone.anteriorJawTipUnrotated = (Joint) anteriorJawTipUnrotated.clone();
        clone.changeSupport = new PropertyChangeSupport(clone);
        clone.iomLigamentInsertion = (Joint) iomLigamentInsertion.clone();
        clone.iomLigamentInsertionUnrotated = (Joint) iomLigamentInsertionUnrotated.clone();
        clone.qaJoint = (Joint) qaJoint.clone();
        clone.qaJointUnrotated = (Joint) qaJointUnrotated.clone();
        clone.rotation = (AffineTransform) rotation.clone();

        return clone;
    }

    public String getName(Joint j) {
        if(j == a2Insertion) return "A2 insertion";
        if(j == a3Insertion) return "A3 insertion";
        if(j == iomLigamentInsertion) return "interoperculomandibular ligament insertion";
        if(j == anteriorJawTip) return "jaw tip";
        if(j == qaJoint) return "quadrate-articular joint";

        return null;
    }

    public String getName(PlasticBar member) {
        if (member.equals(new PlasticBar(qaJoint, a2Insertion))) return "InlevA2";
        if (member.equals(new PlasticBar(qaJoint, a3Insertion))) return "InlevA3";
        if (member.equals(new PlasticBar(qaJoint, iomLigamentInsertion))) return "InlevOpen";
        if (member.equals(new PlasticBar(qaJoint, anteriorJawTip))) return "OutLever";
        if (member.equals(new PlasticBar(a2Insertion, a3Insertion))) return "A2-A3Ins";
        if (member.equals(new PlasticBar(a2Insertion, anteriorJawTip))) return "LJTop";
        if (member.equals(new PlasticBar(iomLigamentInsertion, anteriorJawTip))) return "LJBot";
        return null;
    }

    public Set<PlasticBar> getMembers() {
        Set<PlasticBar> members = new HashSet<PlasticBar>();
        members.add(new PlasticBar(qaJoint, a2Insertion));
        members.add(new PlasticBar(qaJoint, a3Insertion));
        members.add(new PlasticBar(qaJoint, iomLigamentInsertion));
        members.add(new PlasticBar(qaJoint, anteriorJawTip));
        members.add(new PlasticBar(a2Insertion, a3Insertion));
        members.add(new PlasticBar(a2Insertion, anteriorJawTip));
        members.add(new PlasticBar(iomLigamentInsertion, anteriorJawTip));
        return members;
    }
    
    /**
     * Rotates the joints with the current transform.
     */
    private void updateJointLocations() {
        rotation.transform(qaJointUnrotated, qaJoint);
        rotation.transform(a2InsertionUnrotated, a2Insertion);
        rotation.transform(a3InsertionUnrotated, a3Insertion);
        rotation.transform(iomLigamentInsertionUnrotated, iomLigamentInsertion);
        rotation.transform(anteriorJawTipUnrotated, anteriorJawTip);
    }

    /**
     * Sets the unrotated joint locations to the current locations and the 
     * rotation transform to identity.  (So the current orientation is set as
     * the starting orientation.)
     */
    private void setUnrotatedLocations() {
        rotation = new AffineTransform();

        qaJointUnrotated = new ImmobileJoint(qaJoint);
        a2InsertionUnrotated = new ImmobileJoint(a2Insertion);
        a3InsertionUnrotated = new ImmobileJoint(a3Insertion);
        iomLigamentInsertionUnrotated = new ImmobileJoint(iomLigamentInsertion);
        anteriorJawTipUnrotated = new ImmobileJoint(anteriorJawTip);
    }
}
