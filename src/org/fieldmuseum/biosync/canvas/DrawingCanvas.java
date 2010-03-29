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
package org.fieldmuseum.biosync.canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
/**
 * A component that draws shapes, scaling them from some drawing coordinate system to the display coordinate system.
 * @author kurie
 */
public class DrawingCanvas extends JPanel {
	protected Rectangle2D drawingBounds;
	protected BufferedImage image;
	
	private boolean debug = false;
	
	/**
	 * Set the bounds of the drawing space to display
	 * @param r
	 */
	public void setDrawingBounds(Rectangle2D r) {
        Rectangle2D oldBounds = getDrawingBounds();
//		this.drawingBounds = (Rectangle2D) r.clone();
        this.drawingBounds = r;
        firePropertyChange("drawingbounds", oldBounds, r);
	}
	
	/**
	 * @return the bounds of the drawing space to display
	 */
	public Rectangle2D getDrawingBounds() {
		return drawingBounds;
	}
	
	/**
	 * Draw shapes on the canvas
	 * @param shapes the shapes to draw
	 * @deprecated replaced by draw(Shape)
	 */
	@Deprecated
	public void draw(Shape[] shapes) {
		Graphics2D g2d = image.createGraphics();
		g2d.setStroke(new BasicStroke(0.0f));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		AffineTransform transform = getTransform(drawingBounds, image);

		//draw the shapes
		g2d.setColor(getForeground());
		for (Shape shape : shapes) {
			//transform the shape, then draw
			g2d.draw(transform.createTransformedShape(shape));
		}
		
		repaint();
	}
	
	/**
	 * Draw a shape to the image buffer.  
	 *
	 * This method should be thread safe.
	 * @param shape
	 */
	public void draw(Shape shape) {
		Graphics2D g2d = image.createGraphics();
		g2d.setStroke(new BasicStroke(0.0f)); //use thinnest possible stroke (to avoid stroke scaling).
		g2d.setColor(getForeground());
		
		AffineTransform drawingToImage = getTransform(drawingBounds, image);
		g2d.transform(drawingToImage);
		g2d.draw(shape);
		
		if(debug) {
			g2d.setColor(Color.BLUE);
			g2d.draw(drawingBounds);
		}
		
		repaint();
	}
	
	/**
	 * Creates an {@link AffineTransform} to transform shapes from drawing coordinates to screen coordinates
	 * @param drawingBounds the drawing coordinates bounds
	 * @param image the screen image that will be drawn to
	 * @return the transform
	 */
	public static AffineTransform getTransform(Rectangle2D drawingBounds, BufferedImage image) {
		if (drawingBounds == null) {
			return new AffineTransform(); //return default (identity) transform
		}

		AffineTransform transform = new AffineTransform();
		
		//calculate the scaling from drawing to image 
		double sx = image.getWidth() / drawingBounds.getWidth();
		double sy = image.getHeight() / drawingBounds.getHeight();
		
		//make the scales isometric and invert y
		sx = Math.min(sx, sy);
		sy = -sx;
		transform.scale(sx, sy);
		
		//transform the image into drawing coordinates to find its center
		double imageCenterX = image.getWidth() / (2.0 * sx) ;
		double imageCenterY = image.getHeight() / (2.0 * sy) ;
		
		//translate the center of the drawing to the center of the image
		double dx = imageCenterX - drawingBounds.getCenterX();
		double dy = imageCenterY - drawingBounds.getCenterY();
		transform.translate(dx, dy);

		return transform;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		//create an image to draw to, if none has been created
		if (image == null) {
			createImage(g2d);
		}
		
		//draw the current image
		g2d.drawImage(image, new AffineTransform(), null);
	}
	
	/** 
	 * Creates an image compatible with a given {@link Graphics2D} and fills it with the background color
	 * @param g2d
	 */
	private void createImage(Graphics2D g2d) {
		image = g2d.getDeviceConfiguration().createCompatibleImage(getWidth(), getHeight());
		clearImage();
	}
	
	/**
	 * Fills the current image with the background color
	 */
	public void clearImage() {
		//fill with the background color
		Graphics2D imageG2D = image.createGraphics();
		imageG2D.setColor(getBackground());
		imageG2D.fillRect(image.getMinX(), image.getMinY(), image.getWidth(), image.getHeight());
		repaint();
	}

	public void setColor(Color c) {
		setForeground(c);
	}

    /**
     * @return the debug
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * @param debug the debug to set
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
	
	

}
