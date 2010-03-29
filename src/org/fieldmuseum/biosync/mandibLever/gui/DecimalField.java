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

import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;

public class DecimalField extends JFormattedTextField {
	private static final DecimalFormat defaultFormat = new DecimalFormat("0.000");
    private static final DecimalFormat editFormat = new DecimalFormat("0.############");
    private double minValue = -Double.MAX_VALUE;
    private double maxValue = Double.MAX_VALUE;
    private boolean edited = false;
	
	public DecimalField(final DecimalFormat format) {
		super();
        setFormat(format);
//        setValue(0.0);
	}
	
	public DecimalField() {
		this(defaultFormat);
	}

    public void setFormat(DecimalFormat format) {
        DefaultFormatterFactory factory = new DefaultFormatterFactory();
        factory.setDefaultFormatter(new DecimalFormatter(format));
        factory.setDisplayFormatter(new DecimalFormatter(format));
        factory.setEditFormatter(new DecimalFormatter(editFormat));
        factory.setNullFormatter(new DecimalFormatter(format));
        setFormatterFactory(factory);
    }

    @Override
    public void commitEdit() throws ParseException {
        this.edited = true;
        super.commitEdit();
        firePropertyChange("edited", false, true);
    }

    /**
     * @return whether this DecimalField has been edited (since the last
     * resetEdited()).
     */
    public boolean isEdited() {
        return edited;
    }

    /**
     * resets the edited flag to false
     */
    public void resetEdited() {
        this.edited = false;
        firePropertyChange("edited", true, false);
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Number) {
            super.setValue(((Number)value).doubleValue());
        } else if (value instanceof String) {
            super.setValue(new Double((String) value));
        } else {
            throw new IllegalArgumentException(value + " cannot be converted to a double.");
        }
        //ignore other values
    }

	@Override
	public Double getValue() {
		return (Double) super.getValue();
	}

    /**
     * @return the minValue
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * @param minValue the minValue to set
     */
    public void setMinValue(double minValue) {
        if (minValue > maxValue) {
            throw new IllegalArgumentException("The minimum value is greater than the current maximum value.");
        }
        this.minValue = minValue;
    }

    /**
     * @return the maxValue
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     * @param maxValue the maxValue to set
     */
    public void setMaxValue(double maxValue) {
        if (maxValue < minValue) {
            throw new IllegalArgumentException("The maximum value is less than the current minimum value.");
        }
        this.maxValue = maxValue;
    }

	@SuppressWarnings("serial") 
	private class DecimalFormatter extends DefaultFormatter {
		private Format format = new DecimalFormat("0.000");
		
		public DecimalFormatter(DecimalFormat format) {
			super();
			this.format = format;
			setValueClass(Double.class);
            setCommitsOnValidEdit(false);
		}
		
		@Override
		public String valueToString(Object value) throws ParseException {
			try {
                return format.format(value);
			} catch (IllegalArgumentException  e) {
				return "";
            }
		}

        @Override
        public Double stringToValue(String string) throws ParseException {
            Double value = null;
            try {
                value = Double.parseDouble(string);
            } catch (NumberFormatException e) {
                throw new ParseException("NumberFormatException", 0);
            }
            
            if (value < minValue) {
                value = minValue;
            } else if (value > maxValue) {
                value = maxValue;
            }

            return value;
        }
	}
}
