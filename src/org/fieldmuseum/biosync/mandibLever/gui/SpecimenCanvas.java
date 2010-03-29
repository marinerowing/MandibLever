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
package org.fieldmuseum.biosync.mandibLever.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.MouseInputAdapter;
import org.fieldmuseum.biosync.biomechanics.muscle.Muscle;
import org.fieldmuseum.biosync.canvas.DrawingCanvas;
import org.fieldmuseum.biosync.kinematics.Joint;
import org.fieldmuseum.biosync.kinematics.PlasticBar;
import org.fieldmuseum.biosync.mandibLever.model.Mandible;
import org.fieldmuseum.biosync.mandibLever.model.Specimen;

/**
 *
 * @author kurie
 */
public class SpecimenCanvas extends DrawingCanvas {
    private Specimen specimen;
    private Object highlight;

    /** This circle gets moved around to highlight the nearest joint */
    private Ellipse2D.Double jointHighlight = new Ellipse2D.Double(0,0,9,9);

    private Color highlightColor = Color.BLUE;

    // a listener that, on any specimen property change, redraws the specimen
    PropertyChangeListener specimenListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            draw(specimen);
        }
    };

    public SpecimenCanvas() {
        SpecimenMouseListener mouseListener = new SpecimenMouseListener();
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    /**
     * @return the specimen
     */
    public Specimen getSpecimen() {
        return specimen;
    }

    /**
     * @param specimen the specimen to set
     */
    public void setSpecimen(Specimen specimen) {
        //remove listener from old specimen
        if (this.specimen != null) {
            this.specimen.removePropertyChangeListener(specimenListener);
        }

        this.specimen = specimen;
        updateDrawingBounds(specimen);
        draw(specimen);

        this.specimen.addPropertyChangeListener(specimenListener);
    }

    /**
     * Draws a specimen
     */
    private void draw(Specimen specimen) {
        Mandible mandible = specimen.getMandible();
        Muscle a2 = specimen.getA2();
        Muscle a3 = specimen.getA3();

        clearImage();
        setForeground(Color.LIGHT_GRAY);
        draw(mandible.getUpperJawShape());
        setForeground(Color.BLACK);
        draw(mandible.getShape());
        setForeground(Color.RED);
        draw(a2.getShape());
        draw(a3.getShape());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(highlightColor);
        g2d.setStroke(new BasicStroke(3));

        if (highlight instanceof Joint) {
            Joint j = (Joint) highlight;
            Point2D jInDeviceSpace = getTransform(drawingBounds, image).transform(j, null);
            jointHighlight.x = jInDeviceSpace.getX() - jointHighlight.width/2;
            jointHighlight.y = jInDeviceSpace.getY() - jointHighlight.height/2;
            g2d.draw(jointHighlight);
            g2d.drawString(specimen.getName(j), (int)jointHighlight.x, (int)jointHighlight.y);
        } else if (highlight instanceof PlasticBar) {
            PlasticBar bar = (PlasticBar)highlight;
            Line2D line = bar.getShape();
            Point2D p1 = getTransform(drawingBounds, image).transform(line.getP1(), null);
            Point2D p2 = getTransform(drawingBounds, image).transform(line.getP2(), null);
            Point2D midpoint = new Point2D.Double(p1.getX()/2 + p2.getX()/2, p1.getY()/2 + p2.getY()/2);
            g2d.draw(new Line2D.Double(p1, p2));
            g2d.drawString(specimen.getName(bar), (int)midpoint.getX(), (int)midpoint.getY()); //TODO rotate the text to run along the bar
        }
    }

    /**
     * Resizes the canvas to fit a specimen
     */
    public void updateDrawingBounds(Specimen specimen) {

        Mandible mandible = specimen.getMandible();
        Muscle a2 = specimen.getA2();
        Joint a2Origin = a2.getOtherJoint(mandible.getInsertion(a2));
        Muscle a3 = specimen.getA3();
        Joint a3Origin = a3.getOtherJoint(mandible.getInsertion(a3));

        //bottom and right sides determined by the jaw tip (give it enough space to rotate 90 degrees if it wants to)
        double xmax = mandible.getAnteriorJawTip().getX();
        double ymin = -mandible.getAnteriorJawTip().getX();

        //top and left determined by the muscle origins
        double xmin = Math.min(a2Origin.getX(), a3Origin.getX());
        double ymax = Math.max(a2Origin.getY(), a3Origin.getY());

        double width = xmax - xmin;
        double height = ymax - ymin;
        double pad = 0.1; //10% padding on all sides
        setDrawingBounds(new Rectangle2D.Double(xmin - pad*width, ymin - pad*height, width + 2*pad*width, height + 2*pad*height));
    }

    private void setHighlight(Object highlight) {
        this.highlight = highlight;
        repaint();
    }

    private class SpecimenMouseListener extends MouseInputAdapter {
        /** Maximum mouse selection distance <strong>in device-space pixels</strong>.*/
        private double selectionDist = 5;

        @Override
        public void mouseMoved(MouseEvent e) {
            if (specimen != null) {
                Joint nearestJoint = getNearestJoint(e.getPoint());
                if (getDeviceDistance(e.getPoint(), nearestJoint) <= selectionDist) {
                    setHighlight(nearestJoint);
                } else {
                    PlasticBar nearestMember = getNearestMember(e.getPoint());
                    if (getDeviceDistance(e.getPoint(), nearestMember.getShape()) <= selectionDist) {
                        setHighlight(nearestMember);
                    } else {
                        setHighlight(null);
                    }
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            /*
             * FIXME: this moves the current location of the joint, but not the
             * unrotated position, so the move is lost next time the jaw
             * rotates.  Working on new Joint implementation that will handle
             * its own unrotated (relative) location and its actual location,
             * so the mandible won't have to update joints.
             */
//            if (highlight != null && highlight instanceof Joint) {
//                Joint joint = (Joint) highlight;
//                try {
//                    Point2D newLocation = getTransform(drawingBounds, image).createInverse().transform(e.getPoint(), null);
//                    joint.setLocation(newLocation);
//                    repaint();
//                } catch (NoninvertibleTransformException ex) {
//                    //this should never happen, since the transform is just a scale and translate
//                    Logger.getLogger(SpecimenCanvas.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }

        }

        private Joint getNearestJoint(Point point) {
            double minDist = Double.MAX_VALUE;
            Joint minDistJoint = null;
            for (Joint j : getJoints(specimen)) {
                double dist = getDeviceDistance(point, j);
                if (dist < minDist) {
                    minDist = dist;
                    minDistJoint = j;
                }
            }

            return minDistJoint;
        }

        private PlasticBar getNearestMember(Point point) {
            double minDist = Double.MAX_VALUE;
            PlasticBar minDistMember = null;
            for (PlasticBar member : specimen.getMembers()) {
                double dist = getDeviceDistance(point, member.getShape());
                if (dist < minDist) {
                    minDist = dist;
                    minDistMember = member;
                }
            }

            return minDistMember;
        }

        private Set<Joint> getJoints(Specimen specimen) {
            HashSet<Joint> joints = new HashSet<Joint>();
            joints.addAll(specimen.getMandible().getJoints());
            joints.addAll(specimen.getA2().getJoints());
            joints.addAll(specimen.getA3().getJoints());
            return joints;
        }

        private double getDeviceDistance(Point pointInDeviceSpace, Joint jointInUserSpace) {
            Point2D jInDeviceSpace = getTransform(drawingBounds, image).transform(jointInUserSpace, null);
            return jInDeviceSpace.distance(pointInDeviceSpace);
        }

        private double getDeviceDistance(Point pointInDeviceSpace, Line2D memberInUserSpace) {
            Point2D p1InDeviceSpace = getTransform(drawingBounds, image).transform(memberInUserSpace.getP1(), null);
            Point2D p2InDeviceSpace = getTransform(drawingBounds, image).transform(memberInUserSpace.getP2(), null);
            Line2D lineInDeviceSpace = new Line2D.Double(p1InDeviceSpace, p2InDeviceSpace);
            return lineInDeviceSpace.ptSegDist(pointInDeviceSpace);
        }
    }
}
