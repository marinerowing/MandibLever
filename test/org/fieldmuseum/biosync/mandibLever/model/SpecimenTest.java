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
import org.fieldmuseum.biosync.biomechanics.muscle.Muscle;
import org.fieldmuseum.biosync.kinematics.ImmobileJoint;
import org.fieldmuseum.biosync.kinematics.Joint;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kurie
 */
public class SpecimenTest {
    final double precision = 0.0005;

    public SpecimenTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getDataString method, of class Specimen.
     */
    @Test
    public void testGetDataString() {
        System.out.println("getDataString");
        Specimen instance = new Specimen("Testdat1 0.598 0.510 0.246 1.509 1.085 2.180 0.60 0.689 1.796 0.420 1.051 1.695 0.12 0.2");
        String expResult = "Testdat1 0.598 0.51 0.246 1.509 1.085 2.18 0.6 0.689 1.796 0.42 1.051 1.695 0.12 0.2"; //note: same as above, but trailing zeroes were removed
        String result = instance.getDataString();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class Specimen.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Specimen instance = new Specimen("Testdat1 0.598 0.510 0.246 1.509 1.085 2.180 0.60 0.689 1.796 0.420 1.051 1.695 0.12 0.2");
        String expResult = "Testdat1";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getA2 method, of class Specimen.
     */
    @Test
    public void testGetA2() {
        System.out.println("getA2");
        Specimen specimen = new Specimen("Testdat1 0.598 0.510 0.246 1.509 1.085 2.180 0.60 0.689 1.796 0.420 1.051 1.695 0.12 0.2");
        Muscle a2 = specimen.getA2();
        
        //length
        double expLength = 1.085;
        double length = a2.getLength();
        assertEquals(expLength, length, precision);

        //mass
        double expMass = 0.12;
        double mass = a2.getMass();
        assertEquals(expMass, mass, precision);

        //joints
        Joint insertion = specimen.getMandible().getA2Insertion();
        Joint origin = a2.getOtherJoint(insertion);
        assertTrue(a2.getJoints().contains(insertion));
        assertTrue(a2.getJoints().contains(origin));



    }

    /**
     * Test of getA3 method, of class Specimen.
     */
    @Test
    public void testGetA3() {
        System.out.println("getA3");
        Specimen specimen = new Specimen("Testdat1 0.598 0.510 0.246 1.509 1.085 2.180 0.60 0.689 1.796 0.420 1.051 1.695 0.12 0.2");
        Muscle a3 = specimen.getA3();

        //length
        double expLength = 2.18;
        double length = a3.getLength();
        assertEquals(expLength, length, precision);

        //mass
        double expMass = 0.2;
        double mass = a3.getMass();
        assertEquals(expMass, mass, precision);

        //joints
        Joint insertion = specimen.getMandible().getA3Insertion();
        Joint origin = a3.getOtherJoint(insertion);
        assertTrue(a3.getJoints().contains(insertion));
        assertTrue(a3.getJoints().contains(origin));

    }

    /**
     * Test of getA2JointDist method, of class Specimen.
     */
    @Test
    public void testGetA2JointDist() {
        System.out.println("getA2JointDist");
        Specimen specimen = new Specimen("Testdat1 0.598 0.510 0.246 1.509 1.085 2.180 0.60 0.689 1.796 0.420 1.051 1.695 0.12 0.2");
        
        Muscle a2 = specimen.getA2();
        Joint insertion = specimen.getMandible().getInsertion(a2);
        Joint origin = specimen.getA2().getOtherJoint(insertion);
        double expOriginJointDist = 0.689;
        double originJointDist = specimen.getMandible().getQaJoint().distance(origin);
        assertEquals(expOriginJointDist, originJointDist, precision);
    }

    /**
     * Test of getA3JointDist method, of class Specimen.
     */
    @Test
    public void testGetA3JointDist() {
        System.out.println("getA3JointDist");
        Specimen specimen = new Specimen("Testdat1 0.598 0.510 0.246 1.509 1.085 2.180 0.60 0.689 1.796 0.420 1.051 1.695 0.12 0.2");
        
        Muscle a3 = specimen.getA3();
        Joint insertion = specimen.getMandible().getInsertion(a3);
        Joint origin = specimen.getA3().getOtherJoint(insertion);
        double expOriginJointDist = 1.796;
        double originJointDist = specimen.getMandible().getQaJoint().distance(origin);
        assertEquals(expOriginJointDist, originJointDist, precision);
    }

    /**
     * Test of locateOrigin method, of class Specimen.
     */
    @Test
    public void testCreateMuscleOriginJoint() {
        //TODO: this method ("createMuscleOriginJoint") was renamed and moved to MandibLeverMuscle.  Rename the test and move it to a MandibLeverMuscleTest class.
        System.out.println("createMuscleOriginJoint");

        Specimen specimen = new Specimen("Testdat1 0.598 0.510 0.246 1.509 1.085 2.180 0.60 0.689 1.796 0.420 1.051 1.695 0.12 0.2");
        Joint qaJoint = specimen.getMandible().getQaJoint();

        //A2
        double originToJoint = 0.689;
        double originToInsertion = 1.085;
        Joint muscleInsertion = specimen.getMandible().getA2Insertion();
        Point2D expResult = new Point2D.Double(-0.663, 0.186);
        Point2D result = MandibLeverMuscle.locateOrigin(originToJoint, originToInsertion, qaJoint, muscleInsertion);
        assertEquals(expResult.getX(), result.getX(), precision);
        assertEquals(expResult.getY(), result.getY(), precision);

        //A3
        originToJoint = 1.796;
        originToInsertion = 2.18;
        muscleInsertion = specimen.getMandible().getA3Insertion();
        expResult = new ImmobileJoint(-1.369, 1.162);
        result = MandibLeverMuscle.locateOrigin(originToJoint, originToInsertion, qaJoint, muscleInsertion);
        assertEquals(expResult.getX(), result.getX(), precision);
        assertEquals(expResult.getY(), result.getY(), precision);

    }

    @Test
    public void testSetContraction() {
        Specimen specimen = new Specimen("Testdat1 0.598 0.510 0.246 1.509 1.085 2.180 0.60 0.689 1.796 0.420 1.051 1.695 0.12 0.2");
        Mandible.setMaxRotation(Math.PI/6); //contraction is relative to max-open length, so we'll make sure this is set to a reasonable value
        
        //A2 //////////////////////////////////////////////////////////////////
        Muscle muscle = specimen.getA2();
        double restingLength = muscle.getLength();
        
        //rotate open
        specimen.getMandible().setRotation(Math.PI/6);
        double maxLength = specimen.getMaxMuscleLength(muscle);
        assertEquals(maxLength, muscle.getLength(), precision);

        //try to contract A2 to its resting length
        double contraction = (maxLength - restingLength)/maxLength;
        boolean success = specimen.setContraction(muscle, contraction);
        assertTrue(success);
        assertEquals(restingLength, muscle.getLength(), precision);

        //A3 //////////////////////////////////////////////////////////////////
        muscle = specimen.getA3();
        restingLength = muscle.getLength();

        //rotate open
        specimen.getMandible().setRotation(Math.PI/6);
        maxLength = specimen.getMaxMuscleLength(muscle);
        assertEquals(maxLength, muscle.getLength(), precision);

        //try to contract A3 to its resting length
        contraction = (maxLength - restingLength)/(maxLength - muscle.getTendonLength());
        success = specimen.setContraction(muscle, contraction);
        assertTrue(success);
        assertEquals(restingLength, muscle.getLength(), precision);
    }

}