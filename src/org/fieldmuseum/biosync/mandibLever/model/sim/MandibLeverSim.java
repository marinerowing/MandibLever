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
package org.fieldmuseum.biosync.mandibLever.model.sim;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.fieldmuseum.biosync.biomechanics.muscle.HillEquation;
import org.fieldmuseum.biosync.biomechanics.muscle.Muscle;
import org.fieldmuseum.biosync.mandibLever.gui.SimParamPanel;
import org.fieldmuseum.biosync.mandibLever.model.MandibLeverMuscle;
import org.fieldmuseum.biosync.mandibLever.model.Mandible;
import org.fieldmuseum.biosync.mandibLever.model.Specimen;

/**
 *
 * @author kurie
 */
public class MandibLeverSim extends AbstractAction {
    List<Specimen> specimens;
    SimParamPanel paramPanel;
    String name;
    File path;

    public MandibLeverSim(List<Specimen> specimens, SimParamPanel paramPanel, String outputFileBaseName, File outputFilePath) {
        super("Run Sim");
        this.specimens = specimens;
        this.paramPanel = paramPanel;
        this.name = outputFileBaseName;
        this.path = outputFilePath;
    }

    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        source.setEnabled(false);

        StringBuilder open = new StringBuilder().append(",Fish,Spec,JawAng(\u00B0),Gape(cm),OpenDur(ms),MAOpen,VROpen,AngV(\u00B0/s),VOpen(cm/s)\n");
        StringBuilder closed = new StringBuilder().append(",Fish,Spec,JawAng(\u00B0),BiteFA2(N),BiteFA3(N),TotBiteF(N),MaxBite(N),MAA2,MAA3,MusF(kPa),A2csa(cm2),FmaxA2(N),xFA2(N),xTqA2(Nm),WkA2(J),PwA2(W),A3csa(cm2),FmaxA3(N),xFA3(N),xTqA3(Nm),WkA3(J),PwA3(W),Vmax(l/s),HiVVmax(%),LoVVmax(%),HiFFmax(%),LoFFmax(%)\n");
        StringBuilder a2Out = new StringBuilder();
        StringBuilder a3Out = new StringBuilder();
        int bins = paramPanel.getBins();

        if (specimens != null) {
            int fishNum = 0;
            for (Specimen specimen : specimens) {
                fishNum++;

                //create a copy of the specimen (so the GUI doesn't get events from it, which will slow this simulation down a lot)
                try {
                    specimen = (Specimen) specimen.clone();
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(SimParamPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

                Mandible mandible = specimen.getMandible();
                MandibLeverMuscle a2 = specimen.getA2();
                MandibLeverMuscle a3 = specimen.getA3();

                //open summary
                mandible.setRotation(Mandible.getMaxRotation());
                open.append(",").append(fishNum)
                    .append(",").append(openSummary(specimen))
                    .append('\n');

                //append headers before each specimen
                a2Out.append("A2HillSim,Fish,Spec,\"LJangle(\u00B0)\",Gape(cm),BiteFA2(N),TotBilatBiteF(N),VVmaxA2,FFmaxA2,TimeA2(ms),A2c(cm),A2cont%,A2Fact(N),Torque(Nm),EMA,AngVel(\u00B0/ms),GapeVel(cm/ms),A2Work(J),A2Power(W),A2Power(W/kg)\n");
                a3Out.append("A3HillSim,Fish,Spec,\"LJangle(\u00B0)\",Gape(cm),BiteFA3(N),TotBilatBiteF(N),VVmaxA3,FFmaxA3,TimeA3(ms),A3c(cm),A3cont%,A3Fact(N),Torque(Nm),EMA,AngVel(\u00B0/ms),GapeVel(cm/ms),A3Work(J),A3Power(W),A3Power(W/kg)\n");

                //iterate a2 open to close
                MandibLeverMuscle muscle = a2;
                mandible.setRotation(Mandible.getMaxRotation());
                double inc = (specimen.getMinMuscleLength(muscle) - specimen.getMaxMuscleLength(muscle)) / bins;
                double totalForceA2 = 0;
                double totalTorqueA2 = 0;
                double inputdistA2 = mandible.getMaxDistanceMoved(muscle);
                double timeA2 = 0;
                for (int bin = 1; bin <= bins; bin++) {
                    double prevRot = specimen.getMandible().getRotation();

                    double len = specimen.getMaxMuscleLength(muscle) + inc * bin;
                    specimen.setLength(muscle, len);

                    double dAngle = mandible.getRotation() - prevRot;
                    totalForceA2 += muscle.getForce();
                    totalTorqueA2 += mandible.getTorque(muscle) / 100;
                    double work = muscle.getForce() * (inputdistA2/bins)/100; //nearly verbatim copy of mark's calc
                    double dt = specimen.getMaxContraction(muscle)/(bins * muscle.getVelocityFraction() * Muscle.getVelocityPerLengthMax());
                    double power = work/dt;
                    timeA2 += dt;
                    double angV = dAngle/dt;

                    a2Out.append(",").append(fishNum);
                    a2Out.append(",").append(getSummary(specimen, muscle, timeA2));
                    a2Out.append(",").append(String.format("%8.3f", Math.toDegrees(angV) / 1000)); //conversion to degrees/ms
                    a2Out.append(",").append(String.format("%8.4f", angV * mandible.getOutLever() / 1000)); // conversion to cm/ms
                    a2Out.append(",").append(String.format("%14.9f", work));
                    a2Out.append(",").append(String.format("%10.6f", power));
                    a2Out.append(",").append(String.format("%8.2f", power/(muscle.getMass()/1000)));
                    a2Out.append('\n');
                }

                //iterate a3 open to close
                muscle = a3;
                mandible.setRotation(Mandible.getMaxRotation());
                inc = (specimen.getMinMuscleLength(muscle) - specimen.getMaxMuscleLength(muscle)) / bins;
                double totalForceA3 = 0;
                double totalTorqueA3 = 0;
                double inputdistA3 = mandible.getMaxDistanceMoved(a3);
                double timeA3 = 0;
                for (int bin = 1; bin <= bins; bin++) {
                    double prevRot = specimen.getMandible().getRotation();

                    double len = specimen.getMaxMuscleLength(muscle) + inc * bin;
                    specimen.setLength(muscle, len);

                    double dAngle = mandible.getRotation() - prevRot;
                    totalForceA3 += muscle.getForce();
                    totalTorqueA3 += mandible.getTorque(muscle) / 100;
                    double work = muscle.getForce() * (inputdistA3/bins)/100; //nearly verbatim copy of mark's calc
                    double dt = specimen.getMaxContraction(muscle)/(bins * muscle.getVelocityFraction() * Muscle.getVelocityPerLengthMax());
                    double power = work/dt;
                    timeA3 += dt;
                    double angV = dAngle/dt;

                    a3Out.append(",").append(fishNum);
                    a3Out.append(",").append(getSummary(specimen, muscle, timeA3));
                    a3Out.append(",").append(String.format("%8.3f", Math.toDegrees(angV) / 1000)); //conversion to degrees/ms
                    a3Out.append(",").append(String.format("%8.4f", angV * mandible.getOutLever() / 1000)); // conversion to cm/ms
                    a3Out.append(",").append(String.format("%14.9f", work));
                    a3Out.append(",").append(String.format("%10.6f", power));
                    a3Out.append(",").append(String.format("%8.2f", power/(muscle.getMass()/1000)));
                    a3Out.append('\n');
                }

                //closed summary
                specimen.getMandible().setRotation(0);
                closed.append(",").append(fishNum)
                    .append(",").append(closeSummary(specimen, bins, timeA2, timeA3, totalForceA2, totalTorqueA2, totalForceA3, totalTorqueA3))
                    .append('\n');
            }
        }

        saveOutput(open.toString(), closed.toString(), a2Out.toString(), a3Out.toString(), source);

        source.setEnabled(true);
    }

    private String openSummary(Specimen specimen) {
        Mandible mandible = specimen.getMandible();
        StringBuilder open = new StringBuilder();
        double maOpen = mandible.getMechanicalAdvantage(mandible.getIomLigamentInsertion()); //open lever mechanical advantage
        double openDur = paramPanel.getOpenDur();
        mandible.setRotation(Mandible.getMaxRotation());
        double jawRotationDeg = Math.toDegrees(-Mandible.getMaxRotation());
        open.append(specimen.getName())
            .append(",").append(String.format("%8.0f", jawRotationDeg))
            .append(",").append(String.format("%8.2f", mandible.getGape()))
            .append(",").append(String.format("%8.2f", openDur))
            .append(",").append(String.format("%8.2f", maOpen))
            .append(",").append(String.format("%8.2f", 1/maOpen))
            .append(",").append(String.format("%8.2f", jawRotationDeg/openDur))
            .append(",").append(String.format("%8.4f", mandible.getGape()/openDur));

        return open.toString();
    }

    /**
     * Creates a string of summary statistics for the closed jaw.
     * @param specimen the specimen
     * @param bins the number of bins that the simulation used
     * @param totalForceA2 the a2 muscle force, summed over bins
     * @param totalTorqueA2 the a2 muscle torque, summed over bins
     * @param totalForceA3  the a2 muscle force, summed over bins
     * @param totalTorqueA3 the a2 muscle torque, summed over bins
     * @return ,Fish,Spec,JawAng(°),BiteFA2(N),BiteFA3(N),TotBiteF(N),MaxBite(N),MAA2,MAA3,MusF(kPa),A2csa(cm2),FmaxA2(N),xFA2(N),xTqA2(Nm),WkA2(J),PwA2(W),A3csa(cm2),FmaxA3(N),xFA3(N),xTqA3(Nm),WkA3(J),PwA3(W),Vmax(l/s),HiVVmax(%),LoVVmax(%),HiFFmax(%),LoFFmax(%)
     */
    private String closeSummary(Specimen specimen, int bins, double timeA2, double timeA3, double totalForceA2, double totalTorqueA2, double totalForceA3, double totalTorqueA3) {
        StringBuilder closed = new StringBuilder();
        DecimalFormat fmt2 = new DecimalFormat("0.00");
        DecimalFormat fmt6 = new DecimalFormat("0.000000");
        DecimalFormat fmt8 = new DecimalFormat("0.00000000");

        Mandible mandible = specimen.getMandible();
        MandibLeverMuscle a2 = specimen.getA2();
        MandibLeverMuscle a3 = specimen.getA3();

        double inputdistA2 = mandible.getMaxDistanceMoved(a2);
        double meanForceA2 = totalForceA2/bins;
        double totalWorkA2 = meanForceA2 * inputdistA2 / 100;
        double totalPowerPerKgA2 = totalWorkA2 / (timeA2 * (a2.getMass() / 1000));

        double inputdistA3 = mandible.getMaxDistanceMoved(a3);
        double meanForceA3 = totalForceA3/bins;
        double totalWorkA3 = meanForceA3 * inputdistA3 / 100;
        double totalPowerPerKgA3 = totalWorkA3 / (timeA3 * (a3.getMass() / 1000));
        double maxTotalBite = 2 * (specimen.getMaximumOutputForce(a2) + specimen.getMaximumOutputForce(a3));

        specimen.getMandible().setRotation(0);

        closed.append(specimen.getName())
            .append(",").append(String.format("%8.0f", Math.toDegrees(-Mandible.getMaxRotation())))
            .append(",").append(fmt6.format(specimen.getOutputForce(a2)))
            .append(",").append(fmt6.format(specimen.getOutputForce(a3)))
            .append(",").append(fmt6.format(2 * specimen.getBiteForceHalf()))
            .append(",").append(fmt6.format(maxTotalBite)) //max total bite
            .append(",").append(fmt2.format(mandible.getMechanicalAdvantage(a2)))
            .append(",").append(fmt2.format(mandible.getMechanicalAdvantage(a3)))
            .append(",").append(Muscle.getForcePerAreaMax())
            .append(",").append(fmt6.format(a2.getCrossSectionArea()))
            .append(",").append(fmt6.format(a2.getMaxForce()))
            .append(",").append(fmt6.format(totalForceA2/bins)) //xFA2(N) mean force
            .append(",").append(fmt8.format(totalTorqueA2/bins)) //xTqA2(Nm) mean torque
            .append(",").append(fmt8.format(totalWorkA2)) //WkA2(J)
            .append(",").append(fmt2.format(totalPowerPerKgA2)) //PwA2(W)
            .append(",").append(fmt6.format(a3.getCrossSectionArea()))
            .append(",").append(fmt6.format(a3.getMaxForce()))
            .append(",").append(fmt6.format(totalForceA3/bins)) //xFA3(N) mean force
            .append(",").append(fmt8.format(totalTorqueA3/bins)) //xTqA3(Nm) mean torque
            .append(",").append(fmt8.format(totalWorkA3)) //WkA3(J)
            .append(",").append(fmt2.format(totalPowerPerKgA3)) //PwA3(W)
            .append(",").append(Muscle.getVelocityPerLengthMax()) //Vmax(l/s)
            .append(",").append(MandibLeverMuscle.getPeakVMaxFraction()) //HiVVmax(%)
            .append(",").append(MandibLeverMuscle.getMinVMaxFraction()) //LoVVmax(%)
            .append(",").append(HillEquation.getF(MandibLeverMuscle.getMinVMaxFraction())) //HiFFmax(%)
            .append(",").append(HillEquation.getF(MandibLeverMuscle.getPeakVMaxFraction())); //LoFFmax(%)

        return closed.toString();
    }

    /**
     * Creates a comma-separated string of summary statistics for the current
     * state of a muscle
     * @param muscle
     * @return Name,LJAngle,Gape(cm),BiteFA2(N),TotBilatBiteF(N),VVmaxA2,FFmaxA2,TimeA2(ms),A2c(cm),A2cont%,A2Fact(N),Torque(Nm),EMA,AngVel(¡/ms),GapeVel(cm/ms)
     */
    public String getSummary(Specimen specimen, MandibLeverMuscle muscle, double time) {
        Mandible mandible = specimen.getMandible();

        double percentContraction = 100 * specimen.getContraction(muscle);
        StringBuilder sb = new StringBuilder();
        sb.append(specimen.getName())
            .append(",").append(String.format("%8.2f", Math.toDegrees(-mandible.getRotation())))
            .append(",").append(String.format("%10.3f", mandible.getGape())) //Gape is calculated here as the distance from the current tip location to its closed location.  (Slightly different from Mark's calc)
            .append(",").append(String.format("%12.6f", specimen.getOutputForce(muscle)))
            .append(",").append(String.format("%12.6f", 2 * specimen.getBiteForceHalf())) //Note to self: (Ask mark) mark seems to be adding forces from A2 and A3 from different times, so there is a small discrepancy on this output.
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

    private void saveOutput(String open, String closed, String a2Out, String a3Out, JComponent source) {
        try {
            if (path == null) {
                /*
                 * The user hasn't opened a specimen data file (but has created
                 * specimens manually in the GUI).  Make them choose the output
                 * directory
                 */
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int status = jfc.showOpenDialog(source);
                if (status == JFileChooser.APPROVE_OPTION) {
                    path = jfc.getSelectedFile();
                } else if (status == JFileChooser.CANCEL_OPTION){
                    return; //
                } else if (status == JFileChooser.ERROR_OPTION) {
                    //TODO error message
                }
            }

            if (name == null) {
                name = "MandibLever";
            }

            //Using OutputStreamWriters and ISO-8859-1 here because Excel refuses to treat my UTF-8 FileWriter output as UTF-8 (on win XP), so my degree symbols get an extra character added.
            File file = new File(path, name + ".OpenSum.csv");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1"));
            writer.write(open);
            writer.close();

            file = new File(path, name + ".CloseSum.csv");
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1"));
            writer.write(closed);
            writer.close();

            file = new File(path, name + ".A2Sim.csv");
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1"));
            writer.write(a2Out);
            writer.close();

            file = new File(path, name + ".A3Sim.csv");
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1"));
            writer.write(a3Out);
            writer.close();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(source, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
