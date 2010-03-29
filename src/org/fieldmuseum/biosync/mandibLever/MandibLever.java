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
package org.fieldmuseum.biosync.mandibLever;

import java.io.File;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import org.fieldmuseum.biosync.mandibLever.model.Specimen;
import org.fieldmuseum.biosync.mandibLever.model.sim.MandibLeverSim;

/**
 *
 * @author kurie
 */
public class MandibLever extends javax.swing.JFrame {

    /** Creates new form MandibLever */
    public MandibLever() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        specimenPanel1 = new org.fieldmuseum.biosync.mandibLever.gui.SpecimenPanel();
        muscleResultPanel1 = new org.fieldmuseum.biosync.mandibLever.gui.MuscleResultPanel();
        simParamPanel1 = new org.fieldmuseum.biosync.mandibLever.gui.SimParamPanel();
        kinematicResultPanel1 = new org.fieldmuseum.biosync.mandibLever.gui.KinematicResultPanel();
        sliderPanel1 = new org.fieldmuseum.biosync.mandibLever.gui.SliderPanel();
        openLeverResultsPanel1 = new org.fieldmuseum.biosync.mandibLever.gui.OpenLeverResultsPanel();
        specimenCanvas1 = new org.fieldmuseum.biosync.mandibLever.gui.SpecimenCanvas();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MandibLever");

        specimenPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        specimenPanel1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                currentSpecimenChangeHandler(evt);
            }
        });

        muscleResultPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        simParamPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        kinematicResultPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        openLeverResultsPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        specimenCanvas1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout specimenCanvas1Layout = new javax.swing.GroupLayout(specimenCanvas1);
        specimenCanvas1.setLayout(specimenCanvas1Layout);
        specimenCanvas1Layout.setHorizontalGroup(
            specimenCanvas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 557, Short.MAX_VALUE)
        );
        specimenCanvas1Layout.setVerticalGroup(
            specimenCanvas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 436, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(specimenPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                    .addComponent(simParamPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(openLeverResultsPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                    .addComponent(kinematicResultPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(muscleResultPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(specimenCanvas1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sliderPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(specimenPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(simParamPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(muscleResultPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kinematicResultPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(openLeverResultsPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(specimenCanvas1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sliderPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void currentSpecimenChangeHandler(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_currentSpecimenChangeHandler
        if (evt.getPropertyName().equals("currentSpecimen")) {
            //set the new specimen in the output data panels
            Specimen newSpecimen = (Specimen) evt.getNewValue();
            muscleResultPanel1.setSpecimen(newSpecimen, simParamPanel1);
            kinematicResultPanel1.setSpecimen(newSpecimen, simParamPanel1);
            sliderPanel1.setSpecimen(newSpecimen);
            openLeverResultsPanel1.setSpecimen(newSpecimen, simParamPanel1);
            specimenCanvas1.setSpecimen(newSpecimen);
        } else if (evt.getPropertyName().equals("specimens")) {
            File path = null;
            String name = null;
            if (specimenPanel1.getCurrentFile() != null) {
                //TODO consider moving this into SimParamPanel.setSpecimens()
                //the user has a file open, use the same path and base file name for the output files
                path = specimenPanel1.getCurrentFile().getParentFile();
                name = specimenPanel1.getCurrentFile().getName();
                name = name.replaceFirst("\\.[^\\.]*$", ""); //trim the extension, if any
            }
            
            MandibLeverSim simAction = new MandibLeverSim(specimenPanel1.getSpecimens(), simParamPanel1, name, path);
            simParamPanel1.setSimAction(simAction);
        }
    }//GEN-LAST:event_currentSpecimenChangeHandler

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            // TODO handle exception
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MandibLever().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.fieldmuseum.biosync.mandibLever.gui.KinematicResultPanel kinematicResultPanel1;
    private org.fieldmuseum.biosync.mandibLever.gui.MuscleResultPanel muscleResultPanel1;
    private org.fieldmuseum.biosync.mandibLever.gui.OpenLeverResultsPanel openLeverResultsPanel1;
    private org.fieldmuseum.biosync.mandibLever.gui.SimParamPanel simParamPanel1;
    private org.fieldmuseum.biosync.mandibLever.gui.SliderPanel sliderPanel1;
    private org.fieldmuseum.biosync.mandibLever.gui.SpecimenCanvas specimenCanvas1;
    private org.fieldmuseum.biosync.mandibLever.gui.SpecimenPanel specimenPanel1;
    // End of variables declaration//GEN-END:variables

}