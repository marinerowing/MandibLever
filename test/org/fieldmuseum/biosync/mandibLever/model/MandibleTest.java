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

import org.fieldmuseum.biosync.biomechanics.muscle.Muscle;
import org.fieldmuseum.biosync.kinematics.Joint;
import org.fieldmuseum.biosync.kinematics.TriangleCalc;
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
public class MandibleTest {
    /* morphometric data from specimen Testdat1 in SampleData.txt */
    final double a2InLever = 0.598;
    final double a3InLever = 0.51;
    final double openInLever = 0.246;
    final double outLever = 1.509;
    final double a2A3InsertionDistance = 0.42;
    final double mandibleDorsalLength = 1.051;
    final double mandibleVentralLength = 1.695;

    final double precision = 0.0005; //test precision

    public MandibleTest() {
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
     * Test of getA2InLever method, of class Mandible.
     */
    @Test
    public void testGetA2InLever() {
        System.out.println("getA2InLever");
        Mandible instance = new Mandible(a2InLever, a3InLever, openInLever, outLever, a2A3InsertionDistance, mandibleDorsalLength, mandibleVentralLength);
        double expResult = a2InLever;
        double result = instance.getA2InLever();
        assertEquals(expResult, result, precision);
    }

    /**
     * Test of getA3InLever method, of class Mandible.
     */
    @Test
    public void testGetA3InLever() {
        System.out.println("getA3InLever");
        Mandible instance = new Mandible(a2InLever, a3InLever, openInLever, outLever, a2A3InsertionDistance, mandibleDorsalLength, mandibleVentralLength);
        double expResult = a3InLever;
        double result = instance.getA3InLever();
        assertEquals(expResult, result, precision);
    }

    /**
     * Test of getOpenInLever method, of class Mandible.
     */
    @Test
    public void testGetOpenInLever() {
        System.out.println("getOpenInLever");
        Mandible instance = new Mandible(a2InLever, a3InLever, openInLever, outLever, a2A3InsertionDistance, mandibleDorsalLength, mandibleVentralLength);
        double expResult = openInLever;
        double result = instance.getOpenInLever();
        assertEquals(expResult, result, precision);
    }

    /**
     * Test of getOutLever method, of class Mandible.
     */
    @Test
    public void testGetOutLever() {
        System.out.println("getOutLever");
        Mandible instance = new Mandible(a2InLever, a3InLever, openInLever, outLever, a2A3InsertionDistance, mandibleDorsalLength, mandibleVentralLength);
        double expResult = outLever;
        double result = instance.getOutLever();
        assertEquals(expResult, result, precision);
    }

    /**
     * Test of getA2A3InsertionDistance method, of class Mandible.
     */
    @Test
    public void testGetA2A3InsertionDistance() {
        System.out.println("getA2A3InsertionDistance");
        Mandible instance = new Mandible(a2InLever, a3InLever, openInLever, outLever, a2A3InsertionDistance, mandibleDorsalLength, mandibleVentralLength);
        double expResult = a2A3InsertionDistance;
        double result = instance.getA2A3InsertionDistance();
        assertEquals(expResult, result, precision);
    }

    /**
     * Test of getMandibleDorsalLength method, of class Mandible.
     */
    @Test
    public void testGetMandibleDorsalLength() {
        System.out.println("getMandibleDorsalLength");
        Mandible instance = new Mandible(a2InLever, a3InLever, openInLever, outLever, a2A3InsertionDistance, mandibleDorsalLength, mandibleVentralLength);
        double expResult = mandibleDorsalLength;
        double result = instance.getMandibleDorsalLength();
        assertEquals(expResult, result, precision);
    }

    /**
     * Test of getMandibleVentralLength method, of class Mandible.
     */
    @Test
    public void testGetMandibleVentralLength() {
        System.out.println("getMandibleVentralLength");
        Mandible instance = new Mandible(a2InLever, a3InLever, openInLever, outLever, a2A3InsertionDistance, mandibleDorsalLength, mandibleVentralLength);
        double expResult = mandibleVentralLength;
        double result = instance.getMandibleVentralLength();
        assertEquals(expResult, result, precision);
    }

    @Test
    public void testGetMechanicalAdvantage() {
        Specimen specimen = new Specimen("Testdat1 0.598 0.510 0.246 1.509 1.085 2.180 0.60 0.689 1.796 0.420 1.051 1.695 0.12 0.2");
        Mandible mandible = specimen.getMandible();
        double lowPrecision = 0.005;  //the output in SampleData.txt.CloseSum for mechanical advatange is only 2 digits of precision

        //a2
        Joint a2Insertion = mandible.getA2Insertion();
        double expMechAdvantage = 0.4; //from CloseSum output of Mark's MandibLever 3.2
        double mechAdvantage = mandible.getMechanicalAdvantage(a2Insertion);
        assertEquals(expMechAdvantage, mechAdvantage, lowPrecision);

        //a3
        Joint a3Insertion = mandible.getA3Insertion();
        expMechAdvantage = 0.34; //from CloseSum output of Mark's MandibLever 3.2
        mechAdvantage = mandible.getMechanicalAdvantage(a3Insertion);
        assertEquals(expMechAdvantage, mechAdvantage, lowPrecision);

        //open
        Joint iomInsertion = mandible.getIomLigamentInsertion();
        expMechAdvantage = 0.16; //from OpenSum output of Mark's MandibLever 3.2
        mechAdvantage = mandible.getMechanicalAdvantage(iomInsertion);
        assertEquals(expMechAdvantage, mechAdvantage, lowPrecision);
    }

    @Test
    public void testGetEffectiveMechanicalAdvantage() {
        Specimen specimen = new Specimen("Testdat1 0.598 0.510 0.246 1.509 1.085 2.180 0.60 0.689 1.796 0.420 1.051 1.695 0.12 0.2");
        Mandible mandible = specimen.getMandible();

        //a2
        double expectedEMA = 0.229; //from A2Sim output of Mark's MandibLever 3.2 (at gape = 0)
        double ema = mandible.getEffectiveMechanicalAdvantage(specimen.getA2());
        assertEquals(expectedEMA, ema, precision);

        //a3
        expectedEMA = 0.201; //from A3Sim output of Mark's MandibLever 3.2 (at gape = 0)
        ema = mandible.getEffectiveMechanicalAdvantage(specimen.getA3());
        assertEquals(expectedEMA, ema, precision);

    }

    @Test
    public void testRotate() {
        Specimen specimen = new Specimen("Testdat1 0.598 0.510 0.246 1.509 1.085 2.180 0.60 0.689 1.796 0.420 1.051 1.695 0.12 0.2");
        Mandible mandible = specimen.getMandible();
        Muscle a2 = specimen.getA2();
        Muscle a3 = specimen.getA3();
        
        //get a copy of the initial joint positions
        Joint qaJoint = (Joint) mandible.getQaJoint().clone();
        Joint a2Insertion = (Joint) mandible.getA2Insertion().clone();
        Joint a3Insertion = (Joint) mandible.getA3Insertion().clone();
        Joint jawTip = (Joint) mandible.getAnteriorJawTip().clone();
        Joint iomInsertion = (Joint) mandible.getIomLigamentInsertion().clone();

        //get initial muscle lengths
        double a2InitialLength = a2.getLength();
        double a3InitialLength = a3.getLength();

        mandible.rotate(-Math.PI/6); //open 30 degrees
        
        //the qa joint should not move
        assertJointsEqual(qaJoint, mandible.getQaJoint());
        
        //the other joints should all have moved
        assertJointsNotEqual(a2Insertion, mandible.getA2Insertion());
        assertJointsNotEqual(a3Insertion, mandible.getA3Insertion());
        assertJointsNotEqual(jawTip, mandible.getAnteriorJawTip());
        assertJointsNotEqual(iomInsertion, mandible.getIomLigamentInsertion());

        //the angle to the jaw tip should be 30 degrees from its previous location
        double tipAngle = TriangleCalc.getAngle(qaJoint, mandible.getAnteriorJawTip()) - TriangleCalc.getAngle(qaJoint, jawTip);
        assertEquals(-Math.PI/6, tipAngle, precision);

        //all of the morphometric measurements should still be the same. (Relative joint locations have not changed.)
        //note: expected values are taken from the specimen constructor string above.
        assertEquals(0.598, mandible.getA2InLever(), precision);
        assertEquals(0.510, mandible.getA3InLever(), precision);
        assertEquals(0.246, mandible.getOpenInLever(), precision);
        assertEquals(1.509, mandible.getOutLever(), precision);
        assertEquals(0.420, mandible.getA2A3InsertionDistance(), precision);
        assertEquals(1.051, mandible.getMandibleDorsalLength(), precision);
        assertEquals(1.695, mandible.getMandibleVentralLength(), precision);

        //the muscle lengths should have changed
        assertFalse(a2InitialLength == a2.getLength());
        assertFalse(a3InitialLength == a3.getLength());
    }

    private void assertJointsEqual(Joint j1, Joint j2) {
        //because I changed the .equals() method in Joint, I have to test the coordinates individually
        assertEquals(j1.getX(), j2.getX(), precision);
        assertEquals(j1.getY(), j2.getY(), precision);
    }

    private void assertJointsNotEqual(Joint j1, Joint j2) {
        double dx = Math.abs(j1.getX() - j2.getX());
        double dy = Math.abs(j1.getY() - j2.getY());
        assertTrue(dx > 0 || dy > 0);
    }

    @Test
    public void testResetRotation() {
        Specimen specimen = new Specimen("Testdat1 0.598 0.510 0.246 1.509 1.085 2.180 0.60 0.689 1.796 0.420 1.051 1.695 0.12 0.2");
        Mandible mandible = specimen.getMandible();

        //get a copy of the initial joint positions
        Joint qaJoint = (Joint) mandible.getQaJoint().clone();
        Joint a2Insertion = (Joint) mandible.getA2Insertion().clone();
        Joint a3Insertion = (Joint) mandible.getA3Insertion().clone();
        Joint jawTip = (Joint) mandible.getAnteriorJawTip().clone();
        Joint iomInsertion = (Joint) mandible.getIomLigamentInsertion().clone();

        mandible.rotate(-Math.PI/6); //open 30 degrees
        mandible.resetRotation();

        //all joints should be at their original location
        assertJointsEqual(qaJoint, mandible.getQaJoint());
        assertJointsEqual(a2Insertion, mandible.getA2Insertion());
        assertJointsEqual(a3Insertion, mandible.getA3Insertion());
        assertJointsEqual(jawTip, mandible.getAnteriorJawTip());
        assertJointsEqual(iomInsertion, mandible.getIomLigamentInsertion());
    }

    @Test
    public void testRotateJointTo() {
        Specimen specimen = new Specimen("Testdat1 0.598 0.510 0.246 1.509 1.085 2.180 0.60 0.689 1.796 0.420 1.051 1.695 0.12 0.2");
        Mandible mandible = specimen.getMandible();
        Muscle a2 = specimen.getA2();

        Joint a2Insertion = mandible.getA2Insertion();
        Joint a2Origin = a2.getOtherJoint(a2Insertion);

        /*
         * we're going to try to rotate the a2Insertion so that it is half as
         * far from the a2Origin as it is now.
         */
        double halfDistance = 0.5 * a2Origin.distance(a2Insertion);
        boolean success = mandible.rotateJointTo(a2Insertion, a2Origin, halfDistance);
        assertTrue(success);
        assertEquals(halfDistance, a2Origin.distance(a2Insertion), precision);

        /*
         * Now try to double the original distance. (This should fail since the
         * a2 insertion is not far enough from the QA joint to make this 
         * rotation)
         */
        double doubleDistance = 4 * halfDistance;
        success = mandible.rotateJointTo(a2Insertion, a2Origin, doubleDistance);
        //the rotation should fail
        assertFalse(success);
        //the joint should not have moved from its previous position
        assertEquals(halfDistance, a2Origin.distance(a2Insertion), precision);
    }

    @Test
    public void testGetRotation() {
        Specimen specimen = new Specimen("Testdat1 0.598 0.510 0.246 1.509 1.085 2.180 0.60 0.689 1.796 0.420 1.051 1.695 0.12 0.2");
        Mandible mandible = specimen.getMandible();

        //open 30 degrees
        mandible.rotate(-Math.PI/6); 
        assertEquals(-Math.PI/6, mandible.getRotation(), precision);

        //close 10 degrees
        mandible.rotate(Math.PI/18);
        assertEquals(-2 * Math.PI/18, mandible.getRotation(), precision);

        //close 10 degrees
        mandible.rotate(Math.PI/18);
        assertEquals(- Math.PI/18, mandible.getRotation(), precision);

        //close 10 degrees
        mandible.rotate(Math.PI/18);
        assertEquals(0.0, mandible.getRotation(), precision);
    }
}