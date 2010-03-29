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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.fieldmuseum.biosync.biomechanics.muscle.HillEquation;
import org.fieldmuseum.biosync.biomechanics.muscle.Muscle;
import org.fieldmuseum.biosync.mandibLever.model.MandibLeverMuscle;
import org.fieldmuseum.biosync.mandibLever.model.Mandible;
import org.fieldmuseum.biosync.mandibLever.model.Specimen;

/**
 *
 * @author kurie
 */
public class SimParamPanel extends javax.swing.JPanel {
//    List<Specimen> specimens;
//    String name;
//    File path;
    
    /** Creates new form SimParamPanel */
    public SimParamPanel() {
        initComponents();
        jBStartSim.setEnabled(false);
    }

    public void setSimAction(Action action) {
        jBStartSim.setAction(action);
        jBStartSim.setEnabled(true);
    }

    public double getMaxOpenAngle() {
        return dfMaxAngle.getValue();
    }

//    /**
//     * Set the current list of specimens to run the simulation on.
//     * @param specimens the specimens
//     * @param name the name of this data set.  (This will be used to name the
//     * output files.
//     * @param path the path that the output files will be written to
//     */
//    public void setSpecimens(List<Specimen> specimens, String name, File path) {
//        this.specimens = specimens;
//        this.name = name;
//        this.path = path;
//        jBStartSim.setEnabled(true);
//    }

    public double getOpenDur() {
        return dfOpenDur.getValue();
    }

    public int getBins() {
        return (Integer) jftfBins.getValue();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        a2Muscle1 = new org.fieldmuseum.biosync.mandibLever.model.A2Muscle();
        a3Muscle1 = new org.fieldmuseum.biosync.mandibLever.model.A3Muscle();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dfMaxAngle = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel3 = new javax.swing.JLabel();
        dfPeakRelativeV = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel4 = new javax.swing.JLabel();
        dfMinRelativeV = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel5 = new javax.swing.JLabel();
        dfPeakRelativeF = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel6 = new javax.swing.JLabel();
        dfMinRelativeF = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel7 = new javax.swing.JLabel();
        dfFPerAreaMax = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel8 = new javax.swing.JLabel();
        dfVPerLengthMax = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        dfOpenDur = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel11 = new javax.swing.JLabel();
        dfA2Pennation = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel12 = new javax.swing.JLabel();
        dfA3Pennation = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jBStartSim = new javax.swing.JButton();
        jftfBins = new javax.swing.JFormattedTextField(new Integer(20));

        setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Sim Parameters");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        add(jLabel1, gridBagConstraints);

        jLabel2.setText("Open Angle (\u00B0)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel2, gridBagConstraints);

        dfMaxAngle.setColumns(6);
        dfMaxAngle.setValue(-Math.toDegrees(org.fieldmuseum.biosync.mandibLever.model.Mandible.getMaxRotation()));
        dfMaxAngle.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dfMaxAnglePropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfMaxAngle, gridBagConstraints);

        jLabel3.setText("Peak V/Vmax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel3, gridBagConstraints);

        dfPeakRelativeV.setColumns(6);
        dfPeakRelativeV.setMaxValue(1.0);
        dfPeakRelativeV.setMinValue(0.0);
        dfPeakRelativeV.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dfPeakRelativeVPropertyChange(evt);
            }
        });
        dfPeakRelativeV.setValue(0.8);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfPeakRelativeV, gridBagConstraints);

        jLabel4.setText("Min V/Vmax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel4, gridBagConstraints);

        dfMinRelativeV.setColumns(6);
        dfMinRelativeV.setMaxValue(1.0);
        dfMinRelativeV.setMinValue(0.0);
        dfMinRelativeV.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dfMinRelativeVPropertyChange(evt);
            }
        });
        dfMinRelativeV.setValue(0.05);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfMinRelativeV, gridBagConstraints);

        jLabel5.setText("Peak F/Fmax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel5, gridBagConstraints);

        dfPeakRelativeF.setColumns(6);
        dfPeakRelativeF.setMaxValue(1.0);
        dfPeakRelativeF.setMinValue(0.0);
        dfPeakRelativeF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dfPeakRelativeFPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfPeakRelativeF, gridBagConstraints);

        jLabel6.setText("Min F/Fmax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel6, gridBagConstraints);

        dfMinRelativeF.setColumns(6);
        dfMinRelativeF.setMaxValue(1.0);
        dfMinRelativeF.setMinValue(0.0);
        dfMinRelativeF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dfMinRelativeFPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfMinRelativeF, gridBagConstraints);

        jLabel7.setText("F/area (kPa)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel7, gridBagConstraints);

        dfFPerAreaMax.setColumns(6);
        dfFPerAreaMax.setMinValue(0.0);
        dfFPerAreaMax.setValue(MandibLeverMuscle.getForcePerAreaMax());
        dfFPerAreaMax.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dfFPerAreaMaxPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfFPerAreaMax, gridBagConstraints);

        jLabel8.setText("Vmax (L/s)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel8, gridBagConstraints);

        dfVPerLengthMax.setColumns(6);
        dfVPerLengthMax.setMinValue(0.0);
        dfVPerLengthMax.setValue(MandibLeverMuscle.getVelocityPerLengthMax());
        dfVPerLengthMax.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dfVPerLengthMaxPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfVPerLengthMax, gridBagConstraints);

        jLabel9.setText("# bins sim");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel9, gridBagConstraints);

        jLabel10.setText("Open Dur (ms)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel10, gridBagConstraints);

        dfOpenDur.setColumns(6);
        dfOpenDur.setMinValue(0.0);
        dfOpenDur.setValue(30);
        dfOpenDur.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dfOpenDurPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfOpenDur, gridBagConstraints);

        jLabel11.setText("Pennation A2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel11, gridBagConstraints);

        dfA2Pennation.setColumns(6);
        dfA2Pennation.setMinValue(0.0);
        dfA2Pennation.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dfA2PennationPropertyChange(evt);
            }
        });
        dfA2Pennation.setValue(Math.toDegrees(a2Muscle1.getPennationAngle()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA2Pennation, gridBagConstraints);

        jLabel12.setText("Pennation A3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel12, gridBagConstraints);

        dfA3Pennation.setColumns(6);
        dfA3Pennation.setMinValue(0.0);
        dfA3Pennation.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dfA3PennationPropertyChange(evt);
            }
        });
        dfA3Pennation.setValue(Math.toDegrees(a3Muscle1.getPennationAngle()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA3Pennation, gridBagConstraints);

        jBStartSim.setText("Start");
        jBStartSim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBStartSimActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        add(jBStartSim, gridBagConstraints);

        jftfBins.setColumns(6);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        add(jftfBins, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void dfPeakRelativeVPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dfPeakRelativeVPropertyChange
        if (evt.getPropertyName().equals("value")) {
            double peakRelV = dfPeakRelativeV.getValue();
            dfMinRelativeF.setValue(HillEquation.getF(peakRelV));
            MandibLeverMuscle.setPeakVMaxFraction(peakRelV);
        }
    }//GEN-LAST:event_dfPeakRelativeVPropertyChange

    private void dfMinRelativeVPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dfMinRelativeVPropertyChange
        if (evt.getPropertyName().equals("value")) {
            double minRelV = dfMinRelativeV.getValue();
            dfPeakRelativeF.setValue(HillEquation.getF(minRelV));
            MandibLeverMuscle.setMinVMaxFraction(minRelV);
        }
    }//GEN-LAST:event_dfMinRelativeVPropertyChange

    private void dfA2PennationPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dfA2PennationPropertyChange
        if (evt.getPropertyName().equals("value")) {
            double pennationAngle = Math.toRadians(dfA2Pennation.getValue());
            a2Muscle1.setPennationAngle(pennationAngle);
        }
    }//GEN-LAST:event_dfA2PennationPropertyChange

    private void dfA3PennationPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dfA3PennationPropertyChange
        if (evt.getPropertyName().equals("value")) {
            double pennationAngle = Math.toRadians(dfA3Pennation.getValue());
            a3Muscle1.setPennationAngle(pennationAngle);
        }
    }//GEN-LAST:event_dfA3PennationPropertyChange

    private void dfPeakRelativeFPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dfPeakRelativeFPropertyChange
        if (evt.getPropertyName().equals("value")) {
            double peakRelF = dfPeakRelativeF.getValue();
            dfMinRelativeV.setValue(HillEquation.getV(peakRelF));
        }
    }//GEN-LAST:event_dfPeakRelativeFPropertyChange

    private void dfMinRelativeFPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dfMinRelativeFPropertyChange
        if (evt.getPropertyName().equals("value")) {
            double minRelF = dfMinRelativeF.getValue();
            dfPeakRelativeV.setValue(HillEquation.getV(minRelF));
        }
    }//GEN-LAST:event_dfMinRelativeFPropertyChange

    private void dfMaxAnglePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dfMaxAnglePropertyChange
        if (evt.getPropertyName().equals("value")) {
            Mandible.setMaxRotation(-Math.toRadians(dfMaxAngle.getValue()));
        }
}//GEN-LAST:event_dfMaxAnglePropertyChange

    private void jBStartSimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBStartSimActionPerformed
//        jBStartSim.setEnabled(false);
//
//        StringBuilder open = new StringBuilder().append(",Fish,Spec,JawAng(\u00B0),Gape(cm),OpenDur(ms),MAOpen,VROpen,AngV(\u00B0/s),VOpen(cm/s)\n");
//        StringBuilder closed = new StringBuilder().append(",Fish,Spec,JawAng(\u00B0),BiteFA2(N),BiteFA3(N),TotBiteF(N),MaxBite(N),MAA2,MAA3,MusF(kPa),A2csa(cm2),FmaxA2(N),xFA2(N),xTqA2(Nm),WkA2(J),PwA2(W),A3csa(cm2),FmaxA3(N),xFA3(N),xTqA3(Nm),WkA3(J),PwA3(W),Vmax(l/s),HiVVmax(%),LoVVmax(%),HiFFmax(%),LoFFmax(%)\n");
//        StringBuilder a2Out = new StringBuilder();
//        StringBuilder a3Out = new StringBuilder();
//        int bins = (Integer) jftfBins.getValue();
//
//        if (specimens != null) {
//            int fishNum = 0;
//            for (Specimen specimen : specimens) {
//                fishNum++;
//
//                //create a copy of the specimen (so the GUI doesn't get events from it, which will slow this simulation down a lot)
//                try {
//                    specimen = (Specimen) specimen.clone();
//                } catch (CloneNotSupportedException ex) {
//                    Logger.getLogger(SimParamPanel.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//                Mandible mandible = specimen.getMandible();
//                MandibLeverMuscle a2 = specimen.getA2();
//                MandibLeverMuscle a3 = specimen.getA3();
//
//                //open summary
//                mandible.setRotation(Math.toRadians(-Mandible.getMaxRotation()));
//                open.append(",").append(fishNum)
//                    .append(",").append(openSummary(specimen))
//                    .append('\n');
//
//                //append headers before each specimen
//                a2Out.append("A2HillSim,Fish,Spec,LJangle(\u00B0),Gape(cm),BiteFA2(N),TotBilatBiteF(N),VVmaxA2,FFmaxA2,TimeA2(ms),A2c(cm),A2cont%,A2Fact(N),Torque(Nm),EMA,AngVel(\u00B0/ms),GapeVel(cm/ms),A2Work(J),A2Power(W),A2Power(W/kg)\n");
//                a3Out.append("A3HillSim,Fish,Spec,LJangle(\u00B0),Gape(cm),BiteFA3(N),TotBilatBiteF(N),VVmaxA3,FFmaxA3,TimeA3(ms),A3c(cm),A3cont%,A3Fact(N),Torque(Nm),EMA,AngVel(\u00B0/ms),GapeVel(cm/ms),A3Work(J),A3Power(W),A3Power(W/kg)\n");
//
//                //iterate a2 open to close
//                MandibLeverMuscle muscle = a2;
//                mandible.setRotation(Math.toRadians(-Mandible.getMaxRotation()));
//                double inc = (specimen.getMinMuscleLength(muscle) - specimen.getMaxMuscleLength(muscle)) / bins;
//                double totalForceA2 = 0;
//                double totalTorqueA2 = 0;
//                double inputdistA2 = mandible.getMaxDistanceMoved(muscle);
//                double timeA2 = 0;
//                for (int bin = 1; bin <= bins; bin++) {
//                    double prevRot = specimen.getMandible().getRotation();
//
//                    double len = specimen.getMaxMuscleLength(muscle) + inc * bin;
//                    specimen.setLength(muscle, len);
//
//                    double dAngle = mandible.getRotation() - prevRot;
//                    totalForceA2 += muscle.getForce();
//                    totalTorqueA2 += mandible.getTorque(muscle) / 100;
//                    double work = muscle.getForce() * (inputdistA2/bins)/100; //nearly verbatim copy of mark's calc
//                    double dt = specimen.getMaxContraction(muscle)/(bins * muscle.getVelocityFraction() * Muscle.getVelocityPerLengthMax());
//                    double power = work/dt;
//                    timeA2 += dt;
//                    double angV = dAngle/dt;
//
//                    a2Out.append(",").append(fishNum);
//                    a2Out.append(",").append(specimen.getSummary(muscle, timeA2));
//                    a2Out.append(",").append(String.format("%8.3f", Math.toDegrees(angV) / 1000)); //conversion to degrees/ms
//                    a2Out.append(",").append(String.format("%8.4f", angV * mandible.getOutLever() / 1000)); // conversion to cm/ms
//                    a2Out.append(",").append(String.format("%14.9f", work));
//                    a2Out.append(",").append(String.format("%10.6f", power));
//                    a2Out.append(",").append(String.format("%8.2f", power/(muscle.getMass()/1000)));
//                    a2Out.append('\n');
//                }
//
//                //iterate a3 open to close
//                muscle = a3;
//                mandible.setRotation(Math.toRadians(-Mandible.getMaxRotation()));
//                inc = (specimen.getMinMuscleLength(muscle) - specimen.getMaxMuscleLength(muscle)) / bins;
//                double totalForceA3 = 0;
//                double totalTorqueA3 = 0;
//                double inputdistA3 = mandible.getMaxDistanceMoved(a3);
//                double timeA3 = 0;
//                for (int bin = 1; bin <= bins; bin++) {
//                    double prevRot = specimen.getMandible().getRotation();
//
//                    double len = specimen.getMaxMuscleLength(muscle) + inc * bin;
//                    specimen.setLength(muscle, len);
//
//                    double dAngle = mandible.getRotation() - prevRot;
//                    totalForceA3 += muscle.getForce();
//                    totalTorqueA3 += mandible.getTorque(muscle) / 100;
//                    double work = muscle.getForce() * (inputdistA3/bins)/100; //nearly verbatim copy of mark's calc
//                    double dt = specimen.getMaxContraction(muscle)/(bins * muscle.getVelocityFraction() * Muscle.getVelocityPerLengthMax());
//                    double power = work/dt;
//                    timeA3 += dt;
//                    double angV = dAngle/dt;
//
//                    a3Out.append(",").append(fishNum);
//                    a3Out.append(",").append(specimen.getSummary(muscle, timeA3));
//                    a3Out.append(",").append(String.format("%8.3f", Math.toDegrees(angV) / 1000)); //conversion to degrees/ms
//                    a3Out.append(",").append(String.format("%8.4f", angV * mandible.getOutLever() / 1000)); // conversion to cm/ms
//                    a3Out.append(",").append(String.format("%14.9f", work));
//                    a3Out.append(",").append(String.format("%10.6f", power));
//                    a3Out.append(",").append(String.format("%8.2f", power/(muscle.getMass()/1000)));
//                    a3Out.append('\n');
//                }
//
//                //closed summary
//                specimen.getMandible().setRotation(0);
//                closed.append(",").append(fishNum)
//                    .append(",").append(closeSummary(specimen, bins, timeA2, timeA3, totalForceA2, totalTorqueA2, totalForceA3, totalTorqueA3))
//                    .append('\n');
//            }
//        }
//
//        saveOutput(open.toString(), closed.toString(), a2Out.toString(), a3Out.toString());
//
//        jBStartSim.setEnabled(true);
}//GEN-LAST:event_jBStartSimActionPerformed

    private void dfOpenDurPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dfOpenDurPropertyChange
        if (evt.getPropertyName().equals("value")) {
            firePropertyChange("openDur", null, dfOpenDur.getValue());
        }
}//GEN-LAST:event_dfOpenDurPropertyChange

    private void dfFPerAreaMaxPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dfFPerAreaMaxPropertyChange
        double value = dfFPerAreaMax.getValue();
        MandibLeverMuscle.setFMax(value);
}//GEN-LAST:event_dfFPerAreaMaxPropertyChange

    private void dfVPerLengthMaxPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dfVPerLengthMaxPropertyChange
        double value = dfVPerLengthMax.getValue();
        MandibLeverMuscle.setVMax(value);
}//GEN-LAST:event_dfVPerLengthMaxPropertyChange

    private String openSummary(Specimen specimen) {
        DecimalFormat fmt2 = new DecimalFormat("0.00");
        DecimalFormat fmt4 = new DecimalFormat("0.0000");

        Mandible mandible = specimen.getMandible();
        StringBuilder open = new StringBuilder();
        double jawRotationDeg = dfMaxAngle.getValue();
        double maOpen = mandible.getMechanicalAdvantage(mandible.getIomLigamentInsertion()); //open lever mechanical advantage
        double openDur = dfOpenDur.getValue();
        mandible.setRotation(Math.toRadians(-jawRotationDeg));
        open.append(specimen.getName())
            .append(",").append(String.format("%8.0f", jawRotationDeg))
            .append(",").append(fmt2.format(mandible.getGape()))
            .append(",").append(openDur)
            .append(",").append(fmt2.format(maOpen))
            .append(",").append(fmt2.format(1/maOpen))
            .append(",").append(fmt2.format(jawRotationDeg/openDur))
            .append(",").append(fmt4.format(mandible.getGape()/openDur));

        return open.toString();
    }

//    /**
//     * Creates a string of summary statistics for the closed jaw.
//     * @param specimen the specimen
//     * @param bins the number of bins that the simulation used
//     * @param totalForceA2 the a2 muscle force, summed over bins
//     * @param totalTorqueA2 the a2 muscle torque, summed over bins
//     * @param totalForceA3  the a2 muscle force, summed over bins
//     * @param totalTorqueA3 the a2 muscle torque, summed over bins
//     * @return ,Fish,Spec,JawAng(°),BiteFA2(N),BiteFA3(N),TotBiteF(N),MaxBite(N),MAA2,MAA3,MusF(kPa),A2csa(cm2),FmaxA2(N),xFA2(N),xTqA2(Nm),WkA2(J),PwA2(W),A3csa(cm2),FmaxA3(N),xFA3(N),xTqA3(Nm),WkA3(J),PwA3(W),Vmax(l/s),HiVVmax(%),LoVVmax(%),HiFFmax(%),LoFFmax(%)
//     */
//    private String closeSummary(Specimen specimen, int bins, double timeA2, double timeA3, double totalForceA2, double totalTorqueA2, double totalForceA3, double totalTorqueA3) {
//        StringBuilder closed = new StringBuilder();
//        DecimalFormat fmt2 = new DecimalFormat("0.00");
//        DecimalFormat fmt6 = new DecimalFormat("0.000000");
//        DecimalFormat fmt8 = new DecimalFormat("0.00000000");
//
//        Mandible mandible = specimen.getMandible();
//        MandibLeverMuscle a2 = specimen.getA2();
//        MandibLeverMuscle a3 = specimen.getA3();
//
//        double inputdistA2 = mandible.getMaxDistanceMoved(a2);
//        double meanForceA2 = totalForceA2/bins;
//        double totalWorkA2 = meanForceA2 * inputdistA2 / 100;
//        double totalPowerPerKgA2 = totalWorkA2 / (timeA2 * (a2.getMass() / 1000));
//
//        double inputdistA3 = mandible.getMaxDistanceMoved(a3);
//        double meanForceA3 = totalForceA3/bins;
//        double totalWorkA3 = meanForceA3 * inputdistA3 / 100;
//        double totalPowerPerKgA3 = totalWorkA3 / (timeA3 * (a3.getMass() / 1000));
//        double maxTotalBite = 2 * (specimen.getMaximumOutputForce(a2) + specimen.getMaximumOutputForce(a3));
//
//        specimen.getMandible().setRotation(0);
//
//        closed.append(specimen.getName())
//            .append(",").append(String.format("%8.0f", Math.toDegrees(-Mandible.getMaxRotation())))
//            .append(",").append(fmt6.format(specimen.getOutputForce(a2)))
//            .append(",").append(fmt6.format(specimen.getOutputForce(a3)))
//            .append(",").append(fmt6.format(2 * specimen.getBiteForceHalf()))
//            .append(",").append(fmt6.format(maxTotalBite)) //max total bite
//            .append(",").append(fmt2.format(mandible.getMechanicalAdvantage(a2)))
//            .append(",").append(fmt2.format(mandible.getMechanicalAdvantage(a3)))
//            .append(",").append(Muscle.getForcePerAreaMax())
//            .append(",").append(fmt6.format(a2.getCrossSectionArea()))
//            .append(",").append(fmt6.format(a2.getMaxForce()))
//            .append(",").append(fmt6.format(totalForceA2/bins)) //xFA2(N) mean force
//            .append(",").append(fmt8.format(totalTorqueA2/bins)) //xTqA2(Nm) mean torque
//            .append(",").append(fmt8.format(totalWorkA2)) //WkA2(J)
//            .append(",").append(fmt2.format(totalPowerPerKgA2)) //PwA2(W)
//            .append(",").append(fmt6.format(a3.getCrossSectionArea()))
//            .append(",").append(fmt6.format(a3.getMaxForce()))
//            .append(",").append(fmt6.format(totalForceA3/bins)) //xFA3(N) mean force
//            .append(",").append(fmt8.format(totalTorqueA3/bins)) //xTqA3(Nm) mean torque
//            .append(",").append(fmt8.format(totalWorkA3)) //WkA3(J)
//            .append(",").append(fmt2.format(totalPowerPerKgA3)) //PwA3(W)
//            .append(",").append(Muscle.getVelocityPerLengthMax()) //Vmax(l/s)
//            .append(",").append(dfPeakRelativeV.getValue()) //HiVVmax(%)
//            .append(",").append(dfMinRelativeV.getValue()) //LoVVmax(%)
//            .append(",").append(dfPeakRelativeF.getValue()) //HiFFmax(%)
//            .append(",").append(dfMinRelativeF.getValue()); //LoFFmax(%)
//
//        return closed.toString();
//    }

//    private void saveOutput(String open, String closed, String a2Out, String a3Out) {
//        try {
//            if (path == null) {
//                /*
//                 * The user hasn't opened a specimen data file (but has created
//                 * specimens manually in the GUI).  Make them choose the output
//                 * directory
//                 */
//                JFileChooser jfc = new JFileChooser();
//                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                int status = jfc.showOpenDialog(this);
//                if (status == JFileChooser.APPROVE_OPTION) {
//                    path = jfc.getSelectedFile();
//                } else if (status == JFileChooser.CANCEL_OPTION){
//                    return; //
//                } else if (status == JFileChooser.ERROR_OPTION) {
//                    //TODO error message
//                }
//            }
//
//            if (name == null) {
//                name = "MandibLever";
//            }
//
//            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path, name + ".OpenSum.csv")));
//            writer.write(open);
//            writer.close();
//
//            writer = new BufferedWriter(new FileWriter(new File(path, name + ".CloseSum.csv")));
//            writer.write(closed);
//            writer.close();
//
//            writer = new BufferedWriter(new FileWriter(new File(path, name + ".A2Sim.csv")));
//            writer.write(a2Out);
//            writer.close();
//
//            writer = new BufferedWriter(new FileWriter(new File(path, name + ".A3Sim.csv")));
//            writer.write(a3Out);
//            writer.close();
//
//        } catch (IOException ex) {
//            JOptionPane.showMessageDialog(SimParamPanel.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.fieldmuseum.biosync.mandibLever.model.A2Muscle a2Muscle1;
    private org.fieldmuseum.biosync.mandibLever.model.A3Muscle a3Muscle1;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA2Pennation;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA3Pennation;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfFPerAreaMax;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfMaxAngle;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfMinRelativeF;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfMinRelativeV;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfOpenDur;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfPeakRelativeF;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfPeakRelativeV;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfVPerLengthMax;
    private javax.swing.JButton jBStartSim;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JFormattedTextField jftfBins;
    // End of variables declaration//GEN-END:variables

}