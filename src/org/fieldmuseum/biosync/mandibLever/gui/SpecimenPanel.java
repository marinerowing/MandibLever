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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerListModel;
import org.fieldmuseum.biosync.mandibLever.model.Specimen;

/**
 *
 * @author kgu
 */
public class SpecimenPanel extends javax.swing.JPanel {
    private List<Specimen> specimens = new ArrayList<Specimen>();
    private SpecimenListener specListener = new SpecimenListener();
    private File currentFile;
    private Specimen undo;
    
    /**
     * The last-viewed specimen, so updates can be applied after the JSpinner is
     * changed. Generally, this will hold the specimen that is currently
     * displayed, but that's not guaranteed.  Use getCurrentSpecimen() instead
     * to get the current specimen.
     */
    private Specimen previousSpecimen;
    
    /** Creates new form SpecimenPanel */
    public SpecimenPanel() {
        initComponents();
    }
    
    /**
     * Populates the fields with the current specimen's currentFile
     */
    private void updateFields() {
        Specimen specimen = getCurrentSpecimen();
//        jtfName.setText(specimen.getName());
        dfA2.setValue(specimen.getA2().getRestingLength());
        dfA2A3Ins.setValue(specimen.getMandible().getA2A3InsertionDistance());
        dfA2Joint.setValue(specimen.getA2JointDist());
        dfA2Mass.setValue(specimen.getA2().getMass());
        dfA3Joint.setValue(specimen.getA3JointDist());
        dfA3Mass.setValue(specimen.getA3().getMass());
        dfA3Tendon.setValue(specimen.getA3().getTendonLength());
        dfA3Total.setValue(specimen.getA3().getRestingLength());
        dfInlevA2.setValue(specimen.getMandible().getA2InLever());
        dfInlevA3.setValue(specimen.getMandible().getA3InLever());
        dfInlevOpen.setValue(specimen.getMandible().getOpenInLever());
        dfOutLever.setValue(specimen.getMandible().getOutLever());
        dfLJTop.setValue(specimen.getMandible().getMandibleDorsalLength());
        dfLJBot.setValue(specimen.getMandible().getMandibleVentralLength());
    }

    /**
     * @return a new specimen based on the current morphometric values
     */
    private Specimen createSpecimen() {
        String name = jtfName.getText();
        double a2InLever = dfInlevA2.getValue();
        double a3InLever = dfInlevA3.getValue();
        double openInLever = dfInlevOpen.getValue();
        double outLever = dfOutLever.getValue();
        double a2Length = dfA2.getValue();
        double a3Length = dfA3Total.getValue();
        double a3TendonLength = dfA3Tendon.getValue();
        double a2JointDist = dfA2Joint.getValue();
        double a3JointDist = dfA3Joint.getValue();
        double a2A3InsertionDist = dfA2A3Ins.getValue();
        double mandibleDorsalLength = dfLJTop.getValue();
        double mandibleVentralLength = dfLJBot.getValue();
        double a2MuscleMass = dfA2Mass.getValue();
        double a3MuscleMass = dfA3Mass.getValue();

        Specimen specimen = new Specimen(name, a2InLever, a3InLever, openInLever, outLever,
        a2Length, a3Length, a3TendonLength,
        a2JointDist, a3JointDist, a2A3InsertionDist,
        mandibleDorsalLength, mandibleVentralLength,
        a2MuscleMass, a3MuscleMass);

        return specimen;
    }

    /**
     * Replace a given specimen with a another specimen.
     * @param oldSpecimen the specimen to replace.  If the oldSpecimen is not in the list,
     *      this method has no effect.
     * @param newSpecimen the replacement specimen
     */
    private void replaceSpecimen(Specimen oldSpecimen, Specimen newSpecimen) {
        try {
            int index = specimens.indexOf(oldSpecimen);
            if (index > -1) {
                specimens.set(index, newSpecimen);
                resetEdited();
                updateFields();
            }
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(SpecimenPanel.this, iae.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetEdited() {
        //reset all background fields edited status
        dfA2.resetEdited();
        dfA2A3Ins.resetEdited();
        dfA2Joint.resetEdited();
        dfA2Mass.resetEdited();
        dfA3Joint.resetEdited();
        dfA3Mass.resetEdited();
        dfA3Tendon.resetEdited();
        dfA3Total.resetEdited();
        dfInlevA2.resetEdited();
        dfInlevA3.resetEdited();
        dfInlevOpen.resetEdited();
        dfOutLever.resetEdited();
        dfLJTop.resetEdited();
        dfLJBot.resetEdited();
    }

    /**
     * Asks the user whether to update the previous specimen or discard the
     * changes.  (After specimen change on jSpinner1, but before GUI fields
     * are updated for the new specimen.)
     */
    private void updatePreviousSpecimen() {

        /* 
         * First check if the we've just loaded a new file, in which case we can't
         * really update a specimen in the old file.
         */
        if (specimens.contains(previousSpecimen)) {
            String message = "Some morphometric fields on this specimen have " +
                    "been edited.  Do you want to update it before changing " +
                    "specimens?  (If not, the changes will be discarded.)";
            int response = JOptionPane.showConfirmDialog(this,message, "Commit Changes?", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                replaceSpecimen(previousSpecimen, createSpecimen());
            }
        }

        //either way, reset the edited flag on the fields
        resetEdited();
    }

    private boolean isEdited() {
        boolean edited = dfA2.isEdited();
        edited |= dfA2A3Ins.isEdited();
        edited |= dfA2Joint.isEdited();
        edited |= dfA2Mass.isEdited();
        edited |= dfA3Joint.isEdited();
        edited |= dfA3Mass.isEdited();
        edited |= dfA3Tendon.isEdited();
        edited |= dfA3Total.isEdited();
        edited |= dfInlevA2.isEdited();
        edited |= dfInlevA3.isEdited();
        edited |= dfInlevOpen.isEdited();
        edited |= dfOutLever.isEdited();
        edited |= dfLJTop.isEdited();
        edited |= dfLJBot.isEdited();

        return edited;
    }

    public File getCurrentFile() {
        return currentFile;
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
        dfInlevA2 = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel5 = new javax.swing.JLabel();
        dfInlevA3 = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel6 = new javax.swing.JLabel();
        dfInlevOpen = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel7 = new javax.swing.JLabel();
        dfOutLever = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel8 = new javax.swing.JLabel();
        dfA2 = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel9 = new javax.swing.JLabel();
        dfA3Total = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel10 = new javax.swing.JLabel();
        dfA3Tendon = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel11 = new javax.swing.JLabel();
        dfA2Joint = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel12 = new javax.swing.JLabel();
        dfA3Joint = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel13 = new javax.swing.JLabel();
        dfA2A3Ins = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel14 = new javax.swing.JLabel();
        dfLJTop = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel15 = new javax.swing.JLabel();
        dfLJBot = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel16 = new javax.swing.JLabel();
        dfA2Mass = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jLabel17 = new javax.swing.JLabel();
        dfA3Mass = new org.fieldmuseum.biosync.mandibLever.gui.DecimalField();
        jtfName = new javax.swing.JTextField();
        jSpinner1 = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Specimen");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel1, gridBagConstraints);

        jLabel2.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel2, gridBagConstraints);

        jLabel3.setText("Morphometrics");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(jLabel3, gridBagConstraints);

        jLabel4.setLabelFor(dfInlevA2);
        jLabel4.setText("InlevA2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel4, gridBagConstraints);

        dfInlevA2.setColumns(6);
        dfInlevA2.setName("InlevA2"); // NOI18N
        dfInlevA2.addPropertyChangeListener(new SpecimenPanel.EditListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfInlevA2, gridBagConstraints);

        jLabel5.setLabelFor(dfInlevA3);
        jLabel5.setText("InlevA3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel5, gridBagConstraints);

        dfInlevA3.setColumns(6);
        dfInlevA3.setName("InlevA3"); // NOI18N
        dfInlevA3.addPropertyChangeListener(new SpecimenPanel.EditListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfInlevA3, gridBagConstraints);

        jLabel6.setText("InlevOpen");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel6, gridBagConstraints);

        dfInlevOpen.setColumns(6);
        dfInlevOpen.setName("InlevOpen"); // NOI18N
        dfInlevOpen.addPropertyChangeListener(new SpecimenPanel.EditListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfInlevOpen, gridBagConstraints);

        jLabel7.setText("OutLever");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel7, gridBagConstraints);

        dfOutLever.setColumns(6);
        dfOutLever.setName("OutLever"); // NOI18N
        dfOutLever.addPropertyChangeListener(new SpecimenPanel.EditListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfOutLever, gridBagConstraints);

        jLabel8.setText("A2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel8, gridBagConstraints);

        dfA2.setColumns(6);
        dfA2.setName("A2"); // NOI18N
        dfA2.addPropertyChangeListener(new SpecimenPanel.EditListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA2, gridBagConstraints);

        jLabel9.setText("A3 Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel9, gridBagConstraints);

        dfA3Total.setColumns(6);
        dfA3Total.setName("A3 total"); // NOI18N
        dfA3Total.addPropertyChangeListener(new SpecimenPanel.EditListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA3Total, gridBagConstraints);

        jLabel10.setText("A3Tendon");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel10, gridBagConstraints);

        dfA3Tendon.setColumns(6);
        dfA3Tendon.setName("A3 tendon"); // NOI18N
        dfA3Tendon.addPropertyChangeListener(new SpecimenPanel.EditListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA3Tendon, gridBagConstraints);

        jLabel11.setText("A2-Joint");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel11, gridBagConstraints);

        dfA2Joint.setColumns(6);
        dfA2Joint.setName("A2-Joint"); // NOI18N
        dfA2Joint.addPropertyChangeListener(new SpecimenPanel.EditListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA2Joint, gridBagConstraints);

        jLabel12.setText("A3-Joint");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel12, gridBagConstraints);

        dfA3Joint.setColumns(6);
        dfA3Joint.setName("A3-Joint"); // NOI18N
        dfA3Joint.addPropertyChangeListener(new SpecimenPanel.EditListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA3Joint, gridBagConstraints);

        jLabel13.setText("A2-A3Ins");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel13, gridBagConstraints);

        dfA2A3Ins.setColumns(6);
        dfA2A3Ins.setName("A2-A3Ins"); // NOI18N
        dfA2A3Ins.addPropertyChangeListener(new SpecimenPanel.EditListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA2A3Ins, gridBagConstraints);

        jLabel14.setText("LJ Top");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel14, gridBagConstraints);

        dfLJTop.setColumns(6);
        dfLJTop.setName("LJtop"); // NOI18N
        dfLJTop.addPropertyChangeListener(new SpecimenPanel.EditListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfLJTop, gridBagConstraints);

        jLabel15.setText("LJ Bot");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel15, gridBagConstraints);

        dfLJBot.setColumns(6);
        dfLJBot.setName("LJbot"); // NOI18N
        dfLJBot.addPropertyChangeListener(new SpecimenPanel.EditListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfLJBot, gridBagConstraints);

        jLabel16.setText("A2 Mass");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel16, gridBagConstraints);

        dfA2Mass.setColumns(6);
        dfA2Mass.setFormat(new DecimalFormat("0.0000"));
        dfA2Mass.setName("A2 Mass"); // NOI18N
        dfA2Mass.addPropertyChangeListener(new SpecimenPanel.EditListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA2Mass, gridBagConstraints);

        jLabel17.setText("A3 Mass");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel17, gridBagConstraints);

        dfA3Mass.setColumns(6);
        dfA3Mass.setFormat(new DecimalFormat("0.0000"));
        dfA3Mass.setName("A3 Mass"); // NOI18N
        dfA3Mass.addPropertyChangeListener(new SpecimenPanel.EditListener());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(dfA3Mass, gridBagConstraints);

        jtfName.setColumns(8);
        jtfName.setName("Name"); // NOI18N
        jtfName.addPropertyChangeListener(new SpecimenPanel.EditListener());
        jtfName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jtfName, gridBagConstraints);

        jSpinner1.setModel(new SpinnerListModel());
        jSpinner1.setName("Specimen"); // NOI18N
        javax.swing.JSpinner.ListEditor editor = (javax.swing.JSpinner.ListEditor)jSpinner1.getEditor();
        editor.getTextField().setColumns(6);
        editor.getTextField().setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jSpinner1, gridBagConstraints);

        jButton1.setAction(new FileOpenAction());
        jButton1.setText("Open");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(jButton1, gridBagConstraints);

        jButton2.setAction(new FileSaveAction());
        jButton2.setText("Save");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(jButton2, gridBagConstraints);

        jButton3.setAction(new SpecimenUpdateAction());
        jButton3.setText("Update");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jButton3, gridBagConstraints);

        jButton4.setAction(new NewSpecimenAction());
        jButton4.setText("New");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jButton4, gridBagConstraints);

        jButton5.setAction(new UndoAction());
        jButton5.setText("Undo");
        jButton5.setPreferredSize(jButton3.getPreferredSize());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jButton5, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        if (isEdited()) {
            updatePreviousSpecimen();
        }

        //current specimen has changed, update this panel and fire prop change event
        specListener.setSpecimen(getCurrentSpecimen());
        jtfName.setText(getCurrentSpecimen().getName());
        updateFields();
        firePropertyChange("currentSpecimen", null, getCurrentSpecimen());
        previousSpecimen = getCurrentSpecimen();
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jtfNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfNameFocusLost
        getCurrentSpecimen().setName(jtfName.getText());
    }//GEN-LAST:event_jtfNameFocusLost

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA2;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA2A3Ins;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA2Joint;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA2Mass;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA3Joint;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA3Mass;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA3Tendon;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfA3Total;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfInlevA2;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfInlevA3;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfInlevOpen;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfLJBot;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfLJTop;
    private org.fieldmuseum.biosync.mandibLever.gui.DecimalField dfOutLever;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jtfName;
    // End of variables declaration//GEN-END:variables
// </editor-fold>

    /**
     * @return the currentSpecimen
     */
    public Specimen getCurrentSpecimen() {
        return (Specimen) jSpinner1.getValue();
    }

    /**
     * Sets the current specimen to the given specimen, adding it to the
     * specimen list if it isn't already there.
     * @param currentSpecimen the currentSpecimen to set
     */
    public void setCurrentSpecimen(Specimen currentSpecimen) {
        if (!specimens.contains(currentSpecimen)) {
            addSpecimen(currentSpecimen);
        }

        //if the user hasn't yet loaded a data file, then specimens has to be set as the spinner model list
        SpinnerListModel model = (SpinnerListModel) jSpinner1.getModel();
        if ( model.getList() != specimens) {
            model.setList(specimens);
            firePropertyChange("specimens", null, specimens);
        }

        jSpinner1.setValue(currentSpecimen);
    }

    public void addSpecimen(Specimen specimen) {
        specimens.add(specimen);
    }

    public List<Specimen> getSpecimens() {
        return specimens;
    }

    public static void main(String[] args) throws Exception {
        final SpecimenPanel specimenPanel = new SpecimenPanel();
        File data = new File("data", "SnapperData.txt");
        BufferedReader in = new BufferedReader(new FileReader(data));
        String line;
        while((line = in.readLine()) != null) {
            Specimen fish = new Specimen(line);
            specimenPanel.addSpecimen(fish);
        }

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(specimenPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private class FileOpenAction extends AbstractAction {

        public FileOpenAction() {
            super("Open");
        }

        public void actionPerformed(ActionEvent e) {
            //TODO check if there are field edits that the user hasn't committed (similar to updatePreviousSpecimen())

            JFileChooser fileChooser = new JFileChooser(".");
            int status = fileChooser.showOpenDialog(SpecimenPanel.this);
            if (status == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();

                List<Specimen> oldSpecimens = specimens; //save old value for prop change event
                specimens = new ArrayList<Specimen>();
                try {
                    BufferedReader in = new BufferedReader(new FileReader(currentFile));
                    String line;
                    int lineCount = 0;
                    while ((line = in.readLine()) != null) {
                        lineCount++;
                        if (line.trim().length() > 0) {
                            try {
                                Specimen specimen = new Specimen(line);
                                specimens.add(specimen);
                            } catch (IllegalArgumentException exc) {
                                JOptionPane.showMessageDialog(SpecimenPanel.this, "Unable to create specimen on line " + lineCount + ".\n" + exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    in.close();

                    //update the model and fire a property change
                    SpinnerListModel model = (SpinnerListModel) jSpinner1.getModel();
                    model.setList(specimens);
                    SpecimenPanel.this.firePropertyChange("specimens", oldSpecimens, specimens);
                } catch (IOException exception) {
                    //display an error dialog
                    JOptionPane.showMessageDialog(SpecimenPanel.this, "Unable to read the selected file.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.err.println(exception.getStackTrace());
                }
            }
        }
    }

    private class FileSaveAction extends AbstractAction {
        public FileSaveAction() {
            super("Save");
        }

        public void actionPerformed(ActionEvent e) {

            JFileChooser fileChooser = new JFileChooser(currentFile.getParentFile());
            int status = fileChooser.showSaveDialog(SpecimenPanel.this);
            if (status == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();

                //try to delete the existing file
                if (currentFile.exists() && !currentFile.delete()) {
                    String message = "Can't write to the selected file.  Make sure that it's not being edited by another program.";
                    JOptionPane.showMessageDialog(SpecimenPanel.this, message, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //write the specimens
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(currentFile));
                    for (Specimen specimen : specimens) {
                        out.write(specimen.getDataString());
                        out.newLine();
                    }
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(SpecimenPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    /**
     * Replaces the current specimen with a specimen created from the current
     * values in the morphometric fields.
     */
    private class SpecimenUpdateAction extends AbstractAction {
        public SpecimenUpdateAction() {
            super("Update");
        }

        public void actionPerformed(ActionEvent e) {
            undo = getCurrentSpecimen();
            replaceSpecimen(getCurrentSpecimen(), createSpecimen());
            specListener.setSpecimen(getCurrentSpecimen());
            jtfName.setText(getCurrentSpecimen().getName());
            SpecimenPanel.this.firePropertyChange("currentSpecimen", null, getCurrentSpecimen());
            previousSpecimen = getCurrentSpecimen();
        }

    }

    /**
     * Replaces the current specimen with the last-saved "undo" specimen
     */
    private class UndoAction extends AbstractAction {
        public UndoAction() {
            super("Undo");
        }

        public void actionPerformed(ActionEvent e) {
            replaceSpecimen(getCurrentSpecimen(), undo);
            specListener.setSpecimen(getCurrentSpecimen());
            jtfName.setText(getCurrentSpecimen().getName());
            SpecimenPanel.this.firePropertyChange("currentSpecimen", null, getCurrentSpecimen());
            previousSpecimen = getCurrentSpecimen();
        }

    }

    /**
     * Creates a new specimen, adds it to the list and sets it as the
     * current specimen.
     */
    private class NewSpecimenAction extends AbstractAction {
        public NewSpecimenAction() {
            super("New");
        }

        public void actionPerformed(ActionEvent e) {
            setCurrentSpecimen(prototype());
        }

        private Specimen prototype() {
            String name = "*New Specimen*";
            Point2D qaJoint = new Point2D.Double(0,0);
            Point2D a2Insertion = new Point2D.Double(0,1);
            Point2D a3Insertion = new Point2D.Double(1,0);
            Point2D iomLigamentInsertion = new Point2D.Double(0,-1);
            Point2D anteriorJawTip = new Point2D.Double(3,1);
            Point2D a2Origin = new Point2D.Double(-2,1);
            Point2D a3Origin = new Point2D.Double(1,3);
            double a3TendonLength = 1.0;
            double a2Mass = 1.0;
            double a3Mass = 1.0;
            Specimen prototype = new Specimen(name, qaJoint, a2Insertion,
                    a3Insertion, iomLigamentInsertion, anteriorJawTip, a2Origin,
                    a3Origin, a3TendonLength, a2Mass, a3Mass);
            return prototype;
        }
    }

    /**
     * Listens to a specimen for changes, updates SpecimenPanel fields and
     * re-fires change events to SpecimenPanel PropertyChangeListeners.
     */
    private class SpecimenListener implements PropertyChangeListener {
        Specimen specimen;

        public void setSpecimen(Specimen specimen) {
            //stop listening to old specimen
            if (this.specimen != null) {
                this.specimen.removePropertyChangeListener(this);
            }

            //start listening to new specimen
            this.specimen = specimen;
            specimen.addPropertyChangeListener(this);
        }

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("name")) {
                //force the spinner editor to update the name displayed
                ((DefaultEditor) jSpinner1.getEditor()).getTextField().setValue(jSpinner1.getValue());
                jtfName.setText(specimen.getName());
            } else {
                updateFields();
            }

            //listens to the specimen and re-fires change events with "currentSpecimen." prepended
            firePropertyChange("currentSpecimen." + evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }

    /**
     * Listens to a DecimalField and sets its background color to a highlight
     * color if it is edited by the user.
     */
    private class EditListener implements PropertyChangeListener {
        Color editColor = Color.YELLOW;
        Color noEditColor = Color.WHITE;

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getSource() instanceof DecimalField && evt.getPropertyName().equals("edited")) {
                DecimalField source = (DecimalField) evt.getSource();
                if (source.isEdited()) {
                    source.setBackground(editColor);
                } else {
                    source.setBackground(noEditColor);
                }
            }
        }

    }
}
