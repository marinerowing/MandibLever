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
package org.fieldmuseum.biosync.kinematics;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.util.HashSet;
import java.util.Set;

public class PlasticBar implements Member {

    protected final Joint j1;
    protected final Joint j2;
    private final double initialLength;

	/**
	 * Creates a new PlasticBar.
	 * @param length the initial length of the bar
	 */
	public PlasticBar(Joint j1, Joint j2) {
		this.j1 = j1;
        this.j2 = j2;
        this.initialLength = getLength();
	}

	@Override
	public Line2D getShape() {
		return new Line2D.Double(j1, j2);
	}

	public double getLength() {
		return j1.distance(j2);
	}

    /** @return the engineering strain, (L-L0)/L0 */
    public double getStrain() {
        return (getLength() - initialLength) / initialLength;
    }
	
	public double getInitialLength() {
		return initialLength;
	}

    public Set<Joint> getJoints() {
        Set<Joint> joints = new HashSet<Joint>();
        joints.add(j1);
        joints.add(j2);
        return joints;
    }

    /**
     * Get the joint at the other end of the bar from a given joint.
     */
    public Joint getOtherJoint(Joint j) {
        return j == j1 ? j2 : j1;
    }

    public void addJoint(Joint j) {
        throw new UnsupportedOperationException("PlasticBar cannot add joints.");
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PlasticBar) {
            PlasticBar pbo = (PlasticBar) o;
            Set<Joint> joints = pbo.getJoints();
            return joints.contains(j1) && joints.contains(j2);
        } else {
            return false;
        }
    }
}
