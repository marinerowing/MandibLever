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

/**
 *
 * @author kurie
 */
public class ImmobileJoint extends Joint {

    public ImmobileJoint(double x, double y) {
        super(x, y);
    }

    public ImmobileJoint(Point2D location) {
        super(location);
    }

    public ImmobileJoint() {
        super();
    }

    @Override
    public void setLocation(Point2D location) {
        setLocation(location.getX(), location.getY());
    }

    @Override
    public void setLocation(double x, double y) {
        if (x != this.x || y != this.y) {
            throw new UnsupportedOperationException("This joint is immobile, cannot change location.");
        }
    }
}
