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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.fieldmuseum.biosync.mandibLever.model.MandibLeverMuscle;
import org.fieldmuseum.biosync.mandibLever.model.Mandible;
import org.fieldmuseum.biosync.mandibLever.model.Specimen;

/**
 *
 * @author kurie
 */
public class MuscleResultPanel extends javax.swing.JPanel {
    private Specimen specimen;

    /** Creates new form MuscleResultPanel */
    public MuscleResultPanel() {
        initComponents();
    }

    public void setSpecimen(Specimen specimen, final SimParamPanel paramPanel) {
        this.specimen = specimen;
        specimen.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                updateFields(paramPanel.getBins()); //just update on any specimen property change.
            }
        });
        updateFields(paramPanel.getBins());
    }

    private void updateFields(int bins) {
        Mandible mandible = specimen.getMandible();
        MandibLeverMuscle a2 = specimen.getA2();
        MandibLeverMuscle a3 = specimen.getA3();

        //work, time and power calculated exactly as mark did
        double inputdist = mandible.getMaxDistanceMoved(a2);
        double work = a2.getForce() * (inputdist/bins)/100;
        if (specimen.getContraction(a2) == 0) {
            work = 0;
        }
        double time = specimen.getMaxContraction(a2)/(bins * a2.getVelocityFraction() * MandibLeverMuscle.getVelocityPerLengthMax());
        double power = work / time;

        //A2
        dfA2BiteForceOut.setValue(specimen.getOutputForce(a2));
        dfA2CSArea.setValue(a2.getCrossSectionArea());
        dfA2Contraction.setValue(100 * specimen.getContraction(a2));
        dfA2ForceActing.setValue(a2.getForce());
        dfA2ForceMax.setValue(a2.getMaxForce());
        dfA2Work.setValue(work);
        dfA2Power.setValue(power);
        dfA2PowerPerKg.setValue(power / (a2.getMass()/1000));
        dfA2Torque.setValue(mandible.getTorque(a2) / 100); //conversion from N cm to N m


        inputdist = mandible.getMaxDistanceMoved(a3);
        work = a3.getForce() * (inputdist/bins)/100;
        if (specimen.getContraction(a3) == 0) {
            work = 0;
        }
        time = specimen.getMaxContraction(a3)/(bins * a3.getVelocityFraction() * MandibLeverMuscle.getVelocityPerLengthMax());
        power = work / time;
        
        //A3
        dfA3BiteForceOut.setValue(specimen.getOutputForce(a3));
        dfA3CSArea.setValue(a3.getCrossSectionArea());
        dfA3Contraction.setValue(100 * specimen.getContraction(a3));
        dfA3ForceActing.setValue(a3.getForce());
        dfA3ForceMax.setValue(a3.getMaxForce());
        dfA3Work.setValue(work);
        dfA3Power.setValue(power);
        dfA3PowerPerKg.setValue(power / (a3.getMass()/1000));
        dfA3Torque.setValue(mandible.getTorque(a3) / 100);

        //Totals
        dfBiteForceClosedBilat.setValue(2 * specimen.getBiteForceHalf());
        dfBiteForceClosedHalf.setValue(specimen.getBiteForceHalf());
        dfBiteForceMaxStatic.setValue(2 * (specimen.getMaximumOutputForce(a2) + specimen.getMaximumOutputForce(a3)));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        dfA2CSArea = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        dfA3CSArea = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel5 = new javax.swing.JLabel();
        dfA2Contraction = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        dfA3Contraction = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel6 = new javax.swing.JLabel();
        dfA2ForceMax = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        dfA3ForceMax = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel7 = new javax.swing.JLabel();
        dfA2ForceActing = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        dfA3ForceActing = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel8 = new javax.swing.JLabel();
        dfA2Torque = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        dfA3Torque = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel9 = new javax.swing.JLabel();
        dfA2Work = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        dfA3Work = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel10 = new javax.swing.JLabel();
        dfA2Power = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        dfA3Power = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel11 = new javax.swing.JLabel();
        dfA2PowerPerKg = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        dfA3PowerPerKg = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        dfBiteForceClosedHalf = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel13 = new javax.swing.JLabel();
        dfBiteForceClosedBilat = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel14 = new javax.swing.JLabel();
        dfBiteForceMaxStatic = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel15 = new javax.swing.JLabel();
        dfA2BiteForceOut = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        dfA3BiteForceOut = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();

        setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Results: Muscle Physiology");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        add(jLabel1, gridBagConstraints);

        jLabel2.setText("A2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        add(jLabel2, gridBagConstraints);

        jLabel3.setText("A3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        add(jLabel3, gridBagConstraints);

        jLabel4.setText("Muscle csArea");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel4, gridBagConstraints);

        dfA2CSArea.setColumns(6);
        dfA2CSArea.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA2CSArea, gridBagConstraints);

        dfA3CSArea.setColumns(6);
        dfA3CSArea.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA3CSArea, gridBagConstraints);

        jLabel5.setText("Muscle Cont%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel5, gridBagConstraints);

        dfA2Contraction.setColumns(6);
        dfA2Contraction.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA2Contraction, gridBagConstraints);

        dfA3Contraction.setColumns(6);
        dfA3Contraction.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA3Contraction, gridBagConstraints);

        jLabel6.setText("Force max (N)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel6, gridBagConstraints);

        dfA2ForceMax.setColumns(6);
        dfA2ForceMax.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA2ForceMax, gridBagConstraints);

        dfA3ForceMax.setColumns(6);
        dfA3ForceMax.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA3ForceMax, gridBagConstraints);

        jLabel7.setText("Force acting (N)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel7, gridBagConstraints);

        dfA2ForceActing.setColumns(6);
        dfA2ForceActing.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA2ForceActing, gridBagConstraints);

        dfA3ForceActing.setColumns(6);
        dfA3ForceActing.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA3ForceActing, gridBagConstraints);

        jLabel8.setText("Torque (Nm)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel8, gridBagConstraints);

        dfA2Torque.setColumns(6);
        dfA2Torque.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA2Torque, gridBagConstraints);
        dfA2Torque.setFormat(new java.text.DecimalFormat("0.000000"));

        dfA3Torque.setColumns(6);
        dfA3Torque.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA3Torque, gridBagConstraints);
        dfA3Torque.setFormat(new java.text.DecimalFormat("0.000000"));

        jLabel9.setText("Work (J)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel9, gridBagConstraints);

        dfA2Work.setColumns(6);
        dfA2Work.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA2Work, gridBagConstraints);
        dfA2Work.setFormat(new java.text.DecimalFormat("0.000000"));

        dfA3Work.setColumns(6);
        dfA3Work.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA3Work, gridBagConstraints);
        dfA3Work.setFormat(new java.text.DecimalFormat("0.000000"));

        jLabel10.setText("Power (W)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel10, gridBagConstraints);

        dfA2Power.setColumns(6);
        dfA2Power.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA2Power, gridBagConstraints);

        dfA3Power.setColumns(6);
        dfA3Power.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA3Power, gridBagConstraints);

        jLabel11.setText("Power (W/kg)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel11, gridBagConstraints);

        dfA2PowerPerKg.setColumns(6);
        dfA2PowerPerKg.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA2PowerPerKg, gridBagConstraints);

        dfA3PowerPerKg.setColumns(6);
        dfA3PowerPerKg.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA3PowerPerKg, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        add(jSeparator1, gridBagConstraints);

        jLabel12.setText("Bite Force Closed Half (N)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel12, gridBagConstraints);

        dfBiteForceClosedHalf.setColumns(6);
        dfBiteForceClosedHalf.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfBiteForceClosedHalf, gridBagConstraints);

        jLabel13.setText("Bite Force Closed Bilat (N)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel13, gridBagConstraints);

        dfBiteForceClosedBilat.setColumns(6);
        dfBiteForceClosedBilat.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfBiteForceClosedBilat, gridBagConstraints);

        jLabel14.setText("Bite Force Max Static(N)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel14, gridBagConstraints);

        dfBiteForceMaxStatic.setColumns(6);
        dfBiteForceMaxStatic.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfBiteForceMaxStatic, gridBagConstraints);

        jLabel15.setText("Bite Force Out (N)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel15, gridBagConstraints);

        dfA2BiteForceOut.setColumns(6);
        dfA2BiteForceOut.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA2BiteForceOut, gridBagConstraints);

        dfA3BiteForceOut.setColumns(6);
        dfA3BiteForceOut.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA3BiteForceOut, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA2BiteForceOut;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA2CSArea;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA2Contraction;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA2ForceActing;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA2ForceMax;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA2Power;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA2PowerPerKg;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA2Torque;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA2Work;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA3BiteForceOut;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA3CSArea;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA3Contraction;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA3ForceActing;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA3ForceMax;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA3Power;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA3PowerPerKg;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA3Torque;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA3Work;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfBiteForceClosedBilat;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfBiteForceClosedHalf;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfBiteForceMaxStatic;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables

}