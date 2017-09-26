package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/**
 * Implements a visual marker for earthquakes on an earthquake map
 * 
 *@formatter:off 
 * @author UC San Diego Intermediate Software Development MOOC team
 * 			Date: July 17, 2015
 * @author Frank Eyenga 
 * 			Edited: September 25, 2017 
 * @formatter:on
 */
public abstract class EarthquakeMarker extends SimplePointMarker
{

	// Did the earthquake occur on land? This will be set by the subclasses.
	protected boolean isOnLand;

	// SimplePointMarker has a field "radius" which is inherited
	// by Earthquake marker:
	// protected float radius;
	//
	// You will want to set this in the constructor, either
	// using the thresholds below, or a continuous function
	// based on magnitude.

	/** Greater than or equal to this threshold is a moderate earthquake */
	public static final float THRESHOLD_MODERATE = 5;
	/** Greater than or equal to this threshold is a light earthquake */
	public static final float THRESHOLD_LIGHT = 4;

	/** Greater than or equal to this threshold is an intermediate depth */
	public static final float THRESHOLD_INTERMEDIATE = 70;
	/** Greater than or equal to this threshold is a deep depth */
	public static final float THRESHOLD_DEEP = 300;

	public static final int SHALLOW_COLOR = 0xFF0000FF; // Blue
	public static final int INTERMEDIATE_COLOR = 0xFFFFFF00; // Yellow
	public static final int DEEP_COLOR = 0xFFFF0000; // Red

	/**
	 * Draws a visual representation of the earthquake.
	 * 
	 * @param pg
	 *            the PGraphic to draw on
	 * @param x
	 *            The x coordinate of the epicenter of the earthquake
	 * @param y
	 *            The y coordinate of the epicenter of the earthquake
	 */
	public abstract void drawEarthquake(PGraphics pg, float x, float y);

	// constructor
	public EarthquakeMarker(PointFeature feature)
	{
		super(feature.getLocation());
		// Add a radius property and then set the properties
		java.util.HashMap<String, Object> properties = feature.getProperties();
		float magnitude = Float.parseFloat(properties.get("magnitude").toString());
		properties.put("radius", 2 * magnitude);
		setProperties(properties);
		this.radius = 1.75f * getMagnitude();
	}

	// calls abstract method drawEarthquake and then checks age and draws X if
	// needed
	public void draw(PGraphics pg, float x, float y)
	{
		// save previous styling
		pg.pushStyle();

		// determine color of marker from depth
		colorDetermine(pg);

		// call abstract method implemented in child class to draw marker shape
		drawEarthquake(pg, x, y);

		// OPTIONAL TODO: draw X over marker if within past day

		// reset to previous styling
		pg.popStyle();

	}

	/**
	 * Determines and sets color of marker based on earthquakes's depth
	 * 
	 * @param pg
	 *            The PGraphic to set color on.
	 */
	private void colorDetermine(PGraphics pg)
	{
		float depth = this.getDepth();
		
		if (depth < THRESHOLD_INTERMEDIATE)
		{
			pg.fill(SHALLOW_COLOR);
		} else if(depth < THRESHOLD_DEEP)
		{
			pg.fill(INTERMEDIATE_COLOR);
		} else
		{
			pg.fill(DEEP_COLOR);
		}
	}

	/*
	 * getters for earthquake properties
	 */

	public float getMagnitude()
	{
		return Float.parseFloat(getProperty("magnitude").toString());
	}

	public float getDepth()
	{
		return Float.parseFloat(getProperty("depth").toString());
	}

	public String getTitle()
	{
		return (String) getProperty("title");

	}

	public float getRadius()
	{
		return Float.parseFloat(getProperty("radius").toString());
	}

	public boolean isOnLand()
	{
		return isOnLand;
	}

}
