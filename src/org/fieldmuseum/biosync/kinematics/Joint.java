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

import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class Joint extends Point2D {
    protected double x;
    protected double y;
    protected PropertyChangeSupport changeSupport = new PropertyChangeSupport(this); //TODO change this to plain ChangeEvent support.  Property changes aren't really needed.

    public Joint(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    public Joint(Point2D location) {
        this(location.getX(), location.getY());
    }

    public Joint() {
        this(0,0);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    /**
     * A joint is only equal to this joint if it is the same joint.
     * @param o
     * @return true if o is this joint
     */
    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public Object clone() {
        Joint clone = (Joint) super.clone();

        //x and y should be fine, just need to give it a separate PropertyChangeSupport
        clone.changeSupport = new PropertyChangeSupport(clone);

        return clone;
    }
}
