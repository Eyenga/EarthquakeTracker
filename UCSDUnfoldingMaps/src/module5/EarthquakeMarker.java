package module5;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/** Implements a visual marker for earthquakes on an earthquake map
 * 
 * @formatter:off 
 * @author UC San Diego Intermediate Software Development MOOC team
 * 			Date: July 17, 2015
 * @author Frank Eyenga 
 * 			Edited: September 25, 2017 
 * @formatter:on
 *
 */
public abstract class EarthquakeMarker extends CommonMarker
{

	// Did the earthquake occur on land? This will be set by the subclasses.
	protected boolean isOnLand;

	// The radius of the Earthquake marker
	// You will want to set this in the constructor, either
	// using the thresholds below, or a continuous function
	// based on magnitude.
	protected float radius;

	// constants for distance
	protected static final float kmPerMile = 1.6f;

	/** Greater than or equal to this threshold is a moderate earthquake */
	public static final float THRESHOLD_MODERATE = 5;
	/** Greater than or equal to this threshold is a light earthquake */
	public static final float THRESHOLD_LIGHT = 4;

	/** Greater than or equal to this threshold is an intermediate depth */
	public static final float THRESHOLD_INTERMEDIATE = 70;
	/** Greater than or equal to this threshold is a deep depth */
	public static final float THRESHOLD_DEEP = 300;

	// ADD constants for colors if you want
	public static final int SELECT_COLOR = 0xFFFFFFFF;

	// abstract method implemented in derived classes
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
	@Override
	public void drawMarker(PGraphics pg, float x, float y)
	{
		// save previous styling
		pg.pushStyle();

		// determine color of marker from depth
		colorDetermine(pg);

		// call abstract method implemented in child class to draw marker shape
		drawEarthquake(pg, x, y);

		// IMPLEMENT: add X over marker if within past day
		String age = getStringProperty("age");
		if ("Past Hour".equals(age) || "Past Day".equals(age))
		{

			pg.strokeWeight(2);
			int buffer = 2;
			pg.line(x - (radius + buffer), y - (radius + buffer), x + radius + buffer, y + radius + buffer);
			pg.line(x - (radius + buffer), y + (radius + buffer), x + radius + buffer, y - (radius + buffer));

		}

		// reset to previous styling
		pg.popStyle();

	}

	/** Show the title of the earthquake if this marker is selected */
	@Override
	public void showTitle(PGraphics pg, float x, float y)
	{
		// Variables to help improve aesthetics and readability of GUI and code
		// respectively
		float textMargin = 2.5f; // Margin between text and enclosing box
		float posMargin = this.radius + 5; // Margin between location of marker and location of title
		String title = getTitle();

		pg.pushStyle();

		pg.fill(SELECT_COLOR);
		pg.rect(x, y + posMargin, pg.textWidth(title) + (textMargin * 2), pg.textAscent() + pg.textDescent());

		pg.fill(0);
		pg.textAlign(pg.LEFT, pg.TOP);
		pg.text(title, x + textMargin, y + posMargin);

		pg.popStyle();
	}

	/**
	 * Return the "threat circle" radius, or distance up to which this earthquake
	 * can affect things, for this earthquake. DISCLAIMER: this formula is for
	 * illustration purposes only and is not intended to be used for safety-critical
	 * or predictive applications.
	 */
	public double threatCircle()
	{
		double miles = 20.0f * Math.pow(1.8, 2 * getMagnitude() - 5);
		double km = (miles * kmPerMile);
		return km;
	}

	// determine color of marker from depth
	// We use: Deep = red, intermediate = blue, shallow = yellow
	private void colorDetermine(PGraphics pg)
	{
		float depth = getDepth();

		if (depth < THRESHOLD_INTERMEDIATE)
		{
			pg.fill(255, 255, 0);
		} else if (depth < THRESHOLD_DEEP)
		{
			pg.fill(0, 0, 255);
		} else
		{
			pg.fill(255, 0, 0);
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
