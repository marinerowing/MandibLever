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

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import org.fieldmuseum.biosync.biomechanics.muscle.Muscle;
import org.fieldmuseum.biosync.kinematics.ImmobileJoint;
import org.fieldmuseum.biosync.kinematics.Joint;
import org.fieldmuseum.biosync.kinematics.PlasticBar;

/**
 * Represents a MandibLever specimen.
 * @author kgu
 */
public class Specimen implements Serializable, Cloneable {
	public static final String SEPARATOR = " ";
	
	private String name;
    private MandibLeverMuscle a2;
    private MandibLeverMuscle a3;
    private Mandible mandible;
    private PropertyChangeSupport changeSupport;

	/**
	 * Creates a new Specimen from a string of morphometric data.
	 * 
	 * @param data A specimen name followed by the following 14 numbers, separated by spaces
	 * <pre> 1. Inlever for A2 division of adductor mandibulae muscle, from quadrate-articular joint to insertion of A2 muscle on ascending process of articular.
	 * 2. Inlever for A3 division of adductor mandibulae muscle, from quadrate-articular joint to insertion of A3 muscle on medial face of mandible.
	 * 3. Inlever for jaw opening, from quadrate-articular joint to insertion of interoperculomandibular ligament on posteroventral margin of articular
	 * 4. Outlever of lower jaw, from quadrate-articular joint to anterior-most tip of dentary or tip of anteriormost canine tooth.
	 * 5. A2 muscle length, from origin on ventral margin of preopercle to insertion on ascending process of articular.
	 * 6. A3 muscle length, from origin on preopercle and hyomandibula to insertion on ascending process of articular (includes A3 tendon length-  green plus yellow line).
	 * 7. A3 tendon length, from origin on tapering end of A3 muscle to insertion on ascending process of articular (yellow line).
	 * 8. Distance from A2 origin to quadrate-articular joint. 
	 * 9. Distance from A3 origin to quadrate-articular joint. 
	 * 10. Distance from A2 insertion to A3 insertion.
	 * 11. Mandible dorsal length: tip of coronoid process of articular to anterior jaw tip.
	 * 12. Mandible ventral length: posteroventral margin of articular to anterior jaw tip.
	 * 13. A2 muscle mass. 
	 * 14. A3 muscle mass.</pre>
	 */
	public Specimen(String data) throws IllegalArgumentException {

        double a2InLever, a3InLever, openInLever, outLever;
        double a2Length, a3Length, a3TendonLength;
        double a2JointDist, a3JointDist, a2A3InsertionDist;
        double mandibleDorsalLength, mandibleVentralLength;
        double a2MuscleMass, a3MuscleMass;
        
        //parse the data
        try {
            StringTokenizer tokenizer = new StringTokenizer(data, SEPARATOR);
            name = tokenizer.nextToken();
            a2InLever = Double.valueOf(tokenizer.nextToken());
            a3InLever = Double.valueOf(tokenizer.nextToken());
            openInLever = Double.valueOf(tokenizer.nextToken());
            outLever = Double.valueOf(tokenizer.nextToken());
            a2Length = Double.valueOf(tokenizer.nextToken());
            a3Length = Double.valueOf(tokenizer.nextToken());
            a3TendonLength = Double.valueOf(tokenizer.nextToken());
            a2JointDist = Double.valueOf(tokenizer.nextToken());
            a3JointDist = Double.valueOf(tokenizer.nextToken());
            a2A3InsertionDist = Double.valueOf(tokenizer.nextToken());
            mandibleDorsalLength = Double.valueOf(tokenizer.nextToken());
            mandibleVentralLength = Double.valueOf(tokenizer.nextToken());
            a2MuscleMass = Double.valueOf(tokenizer.nextToken());
            a3MuscleMass = Double.valueOf(tokenizer.nextToken());
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Missing parameter in input string.", e);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Specimen \"" + getName() + "\": NumberFormatException " + e.getMessage(), e);
        }
        
        changeSupport = new PropertyChangeSupport(this);

        try {
            //create mandible
            Mandible mandible = new Mandible(a2InLever, a3InLever, openInLever, outLever, a2A3InsertionDist, mandibleDorsalLength, mandibleVentralLength);
            setMandible(mandible);

            //create muscles
            setA2(new A2Muscle(this, mandible.getA2Insertion(), a2JointDist, a2Length, a2MuscleMass));
            setA3(new A3Muscle(this, mandible.getA3Insertion(), a3JointDist, a3Length, a3TendonLength, a3MuscleMass));
        } catch (Exception e) {
            throw new IllegalArgumentException("Specimen \"" + getName() + "\": " + e.getMessage(), e);
        }
    }

    public Specimen(String name, double a2InLever, double a3InLever, double openInLever, double outLever,
        double a2Length, double a3Length, double a3TendonLength,
        double a2JointDist, double a3JointDist, double a2A3InsertionDist,
        double mandibleDorsalLength, double mandibleVentralLength,
        double a2MuscleMass, double a3MuscleMass)
        throws IllegalArgumentException
    {
        //TODO there's a lot of repeated code in these two constructors.  But the string parsing constructor cannot call this constructor (because the this() call can't come after the parsing).  Use a factory to create these?
        changeSupport = new PropertyChangeSupport(this);

        this.name = name;
        try {
            //create mandible
            Mandible mandible = new Mandible(a2InLever, a3InLever, openInLever, outLever, a2A3InsertionDist, mandibleDorsalLength, mandibleVentralLength);
            setMandible(mandible);

            //create muscles
            setA2(new A2Muscle(this, mandible.getA2Insertion(), a2JointDist, a2Length, a2MuscleMass));
            setA3(new A3Muscle(this, mandible.getA3Insertion(), a3JointDist, a3Length, a3TendonLength, a3MuscleMass));
        } catch (Exception e) {
            throw new IllegalArgumentException("Specimen \"" + getName() + "\": " + e.getMessage(), e);
        }
    }

    /**
     * Creates a new Specimen based on the 2D locations of points on the
     * specimen.
     * @param name the name of the specimen
     * @param qaJoint The quadrate-articular joint
     * @param a2Insertion The insertion of the A2 muscle on ascending process of the articular.
     * @param a3Insertion The insertion of the A3 muscle on medial face of the mandible.
     * @param iomLigamentInsertion The insertion of the interoperculomandibular ligament on the posteroventral margin of the articular
     * @param anteriorJawTip The anterior-most tip of the dentary or tip of the anteriormost canine tooth.
     * @param a2Origin The origin of the A2 muscle on the ventral margin of the pre-opercle
     * @param a3Origin The origin of the A3 muscle on the pre-opercle and hyomandibula
     * @param a3TendonLength The A3 tendon length, from origin on tapering end of A3 muscle to insertion on ascending process of articular
     * @param a2Mass The A2 muscle mass (in grams)
     * @param a3Mass The A3 muscle mass (in grams)
     */
    public Specimen(String name, Point2D qaJoint, Point2D a2Insertion, Point2D a3Insertion,
            Point2D iomLigamentInsertion, Point2D anteriorJawTip,
            Point2D a2Origin, Point2D a3Origin, double a3TendonLength, double a2Mass, double a3Mass) {

        changeSupport = new PropertyChangeSupport(this);
        this.name = name;

        Mandible mandible = new Mandible(qaJoint, a2Insertion, a3Insertion, iomLigamentInsertion, anteriorJawTip);
        setMandible(mandible);
        Joint a2InsertionJoint = mandible.getA2Insertion();
        Joint a3InsertionJoint = mandible.getA3Insertion();

        A2Muscle a2 = new A2Muscle(this, new ImmobileJoint(a2Origin), a2InsertionJoint, a2Mass);
        setA2(a2);

        A3Muscle a3 = new A3Muscle(this, new ImmobileJoint(a3Origin), a3InsertionJoint, a3TendonLength, a3Mass);
        setA3(a3);
    }

    /**
     * Creates a new Specimen with no name and all numerical fields = 0
     */
    public Specimen() {
        changeSupport = new PropertyChangeSupport(this);
        setName("");
        setMandible(new Mandible());
        setA2(new A2Muscle());
        setA3(new A3Muscle());
    }

    /**
     * @return the specimen data, in the format of {@link #Specimen(String)}
     */
	public String getDataString() {
		StringBuilder sb = new StringBuilder();
        DecimalFormat format = new DecimalFormat("0.####");
		sb.append(getName()).append(SEPARATOR)
			.append(format.format(mandible.getA2InLever())).append(SEPARATOR)
			.append(format.format(mandible.getA3InLever())).append(SEPARATOR)
			.append(format.format(mandible.getOpenInLever())).append(SEPARATOR)
			.append(format.format(mandible.getOutLever())).append(SEPARATOR)
			.append(format.format(a2.getLength())).append(SEPARATOR)
			.append(format.format(a3.getLength())).append(SEPARATOR)
			.append(format.format(a3.getTendonLength())).append(SEPARATOR)
			.append(format.format(getA2JointDist())).append(SEPARATOR)
			.append(format.format(getA3JointDist())).append(SEPARATOR)
			.append(format.format(mandible.getA2A3InsertionDistance())).append(SEPARATOR)
			.append(format.format(mandible.getMandibleDorsalLength())).append(SEPARATOR)
			.append(format.format(mandible.getMandibleVentralLength())).append(SEPARATOR)
			.append(format.format(a2.getMass())).append(SEPARATOR)
			.append(format.format(a3.getMass()));
		return sb.toString();
    }

    /**
     * Rotates the mandible to acheive a desired contraction of a given muscle.
     * Contraction is 0 when the jaw is fully open.
     * Contraction would be 1.0 if the entire muscle was contracted to zero
     * length (though the tendon would stay at its initial length for any
     * contraction).
     * @param muscle the muscle
     * @param contraction the contraction, a fraction of the muscle's maximum
     * (open-jaw) length (without the tendon).
     * @return whether the rotation succeeded.  If not, the mandible is not moved.
     */
    public boolean setContraction(Muscle muscle, double contraction) {
        double maxLength = getMaxMuscleLength(muscle);
        double newLength = maxLength - contraction * (maxLength - muscle.getTendonLength());
        return setLength(muscle, newLength);
    }

    /**
     * Gets the difference between the current length and the maximum length of
     * a muscle, as a fraction of the maximum length (minus any tendon length)
     * @param muscle
     * @return
     */
    public double getContraction(Muscle muscle) {
        return (getMaxMuscleLength(muscle) - muscle.getLength()) / (getMaxMuscleLength(muscle) - muscle.getTendonLength());
    }

    /**
     * @param muscle
     * @return the contraction of this muscle required to close the jaw
     */
    public double getMaxContraction(Muscle muscle) {
        /* 
         * TODO this assumes that the mandible does not rotate past the point
         * where the muscle is stretched to its maximum length (i.e. the input
         * lever pointing in the same direction as the muscle).  But the max jaw
         * angle is not currently being constrained to phyically plausible
         * angles in the GUI.
         */
        return (getMaxMuscleLength(muscle) - muscle.getRestingLength()) / (getMaxMuscleLength(muscle) - muscle.getTendonLength());
    }

    /**
     * Rotates the mandible to make a given muscle a given length.
     * @param muscle the muscle
     * @param length the desired length
     * @return whether the rotation succeeded.  If not, the mandible is not moved.
     */
    public boolean setLength(Muscle muscle, double length) {
        Joint insertion = mandible.getInsertion(muscle);
        Joint origin = muscle.getOtherJoint(insertion);
        boolean success = mandible.rotateJointTo(insertion, origin, length);

        return success;
    }

    /**
     * @param muscle a muscle in this specimen.
     * @param rotation the rotation of the jaw, in radians from closed.
     * @return the length of the muscle at this rotation
     */
    public double getMuscleLength(Muscle muscle, double rotation) {
        Joint insertion = mandible.getInsertion(muscle);
        Joint origin = muscle.getOtherJoint(insertion);

        AffineTransform transform = AffineTransform.getRotateInstance(rotation - mandible.getRotation());
        Point2D newInsertion = new Point2D.Double();
        transform.transform(insertion, newInsertion);

        return origin.distance(newInsertion);
    }

    /**
     * @param muscle
     * @return muscle length at max jaw-open rotation
     */
    public double getMaxMuscleLength(Muscle muscle) {
        return getMuscleLength(muscle, Mandible.getMaxRotation());
    }

    /**
     * @param muscle
     * @return muscle length when the jaw is closed
     */
    public double getMinMuscleLength(Muscle muscle) {
        return getMuscleLength(muscle, 0);
    }



    /**
     * @return the current bite force at the jaw tip which is contributed by a
     * muscle, in Newtons.
     */
    public double getOutputForce(Muscle m) {
        return m.getForce() * mandible.getEffectiveMechanicalAdvantage(m);
    }

    /**
     * @return the maximum bite force at the jaw tip which could be contributed
     * by a muscle, in Newtons.
     */
    public double getMaximumOutputForce(Muscle m) {
        return m.getMaxForce() * mandible.getEffectiveMechanicalAdvantage(m);
    }

    /**
     * @return the current output force at the jaw tip of both A2 and A3, in
     * Newtons.  Double this to get the full force of both sides of the jaw.
     */
    public double getBiteForceHalf() {
        return getOutputForce(a2) + getOutputForce(a3);
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
        String oldName = getName();
		this.name = name;
        changeSupport.firePropertyChange("name", oldName, name);
	}

    public MandibLeverMuscle getA2() {
        return a2;
    }

    public MandibLeverMuscle getA3() {
        return a3;
    }

    public Mandible getMandible() {
        return mandible;
    }

    public double getA2JointDist() {
        Joint insertion = mandible.getInsertion(a2);
        Joint origin = a2.getOtherJoint(insertion);
        return mandible.getQaJoint().distance(origin);
    }

    public double getA3JointDist() {
        Joint insertion = mandible.getInsertion(a3);
        Joint origin = a3.getOtherJoint(insertion);
        return mandible.getQaJoint().distance(origin);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Creates a comma-separated string of summary statistics for the current
     * state of a muscle
     * @param muscle
     * @return Name,LJAngle,Gape(cm),BiteFA2(N),TotBilatBiteF(N),VVmaxA2,FFmaxA2,TimeA2(ms),A2c(cm),A2cont%,A2Fact(N),Torque(Nm),EMA,AngVel(¡/ms),GapeVel(cm/ms)
     */
    public String getSummary(MandibLeverMuscle muscle, double time) {
        
        double percentContraction = 100 * getContraction(muscle);
        StringBuilder sb = new StringBuilder();
        sb.append(getName())
            .append(",").append(String.format("%8.2f", Math.toDegrees(-mandible.getRotation())))
            .append(",").append(String.format("%10.3f", mandible.getGape())) //Gape is calculated here as the distance from the current tip location to its closed location.  (Slightly different from Mark's calc)
            .append(",").append(String.format("%12.6f", getOutputForce(muscle)))
            .append(",").append(String.format("%12.6f", 2 * getBiteForceHalf())) //Note to self: (Ask mark) mark seems to be adding forces from A2 and A3 from different times, so there is a small discrepancy on this output.
            .append(",").append(String.format("%8.2f", muscle.getVelocityFraction()))
            .append(",").append(String.format("%12.6f", muscle.getForceFraction()))
            .append(",").append(String.format("%8.2f", time * 1000)) //replaced the analytical time with mark's numerical approx. of time
            .append(",").append(String.format("%8.4f", muscle.getLength()))
            .append(",").append(String.format("%8.2f", percentContraction))
            .append(",").append(String.format("%12.6f", muscle.getForce()))
            .append(",").append(String.format("%12.8f", mandible.getTorque(muscle) / 100)) //conversion to Nm
            .append(",").append(String.format("%8.3f", mandible.getEffectiveMechanicalAdvantage(muscle)));

        return sb.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Specimen clone = (Specimen) super.clone();

        Mandible cloneMandible = (Mandible) mandible.clone();
        clone.setMandible(cloneMandible);
        clone.setA2(new A2Muscle(clone, cloneMandible.getA2Insertion(), getA2JointDist(), a2.getRestingLength(), a2.getMass()));
        clone.setA3(new A3Muscle(clone, cloneMandible.getA3Insertion(), getA3JointDist(), a3.getRestingLength(), a3.getTendonLength(), a3.getMass()));
        clone.changeSupport = new PropertyChangeSupport(clone);

        return clone;
    }

    public String getName(Joint j) {
        String name = null;
        if (mandible.getJoints().contains(j)) {
            name = mandible.getName(j);
        } else if (a2.getJoints().contains(j)) {
            name = "A2 origin";
        } else if (a3.getJoints().contains(j)) {
            name = "A3 origin";
        }

        return name;
    }

    public String getName(PlasticBar member) {
        if (member.equals(a2)) return "A2";
        if (member.equals(a3)) return "A3";
        if (member.equals(new PlasticBar(mandible.getQaJoint(), a2.getOtherJoint(mandible.getA2Insertion())))) return "A2-Joint";
        if (member.equals(new PlasticBar(mandible.getQaJoint(), a3.getOtherJoint(mandible.getA3Insertion())))) return "A3-Joint";
        return mandible.getName(member);
    }

    public Set<PlasticBar> getMembers() {
        Set<PlasticBar> members = mandible.getMembers();
        members.add(a2);
        members.add(a3);
        return members;
    }

    /**
     * Sets the mandible.  Also attached a PropertyChangeListener to re-fire
     * change events.  Does <strong>not</strong> currently check if this mandible is
     * attached to the A2 and A3 muscles.
     * @param mandible the mandible
     */
    protected void setMandible(Mandible mandible) {
        this.mandible = mandible;

        mandible.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                changeSupport.firePropertyChange("mandible." + evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });
    }

    /**
     * Sets the A2 muscle.  Also attached a PropertyChangeListener to re-fire
     * change events.  Does <strong>not</strong> currently check if this muscle is
     * attached to the mandible.
     * @param a2 the A2 muscle
     */
    protected void setA2(A2Muscle a2) {
        this.a2 = a2;

        //attach a listener to re-fire change events to specimen listeners with "a2." appended
        a2.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                changeSupport.firePropertyChange("a2." + evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });
    }

    /**
     * Sets the A3 muscle.  Also attached a PropertyChangeListener to re-fire
     * change events.  Does <strong>not</strong> currently check if this muscle is
     * attached to the mandible.
     * @param a3 the A3 muscle
     */
    protected void setA3(A3Muscle a3) {
        this.a3 = a3;

        //attach a listener to re-fire change events to specimen listeners with "a3." appended
        a3.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                changeSupport.firePropertyChange("a3." + evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });
    }
}
