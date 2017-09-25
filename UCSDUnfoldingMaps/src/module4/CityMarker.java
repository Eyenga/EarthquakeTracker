package module4;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;
import processing.core.PShapeSVG;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @formatter:off 
 * @author UC San Diego Intermediate Software Development MOOC team
 * 			Date: July 17, 2015
 * @author Frank Eyenga 
 * 			Edited: September 25, 2017 
 * @formatter:on
 *
 */
public class CityMarker extends SimplePointMarker
{

	// The size of the triangle marker
	// It's a good idea to use this variable in your draw method
	public static final int TRI_SIZE = 15;

	public static final int COLOR = 0xFF800000;

	public CityMarker(Location location)
	{
		super(location);
	}

	public CityMarker(Feature city)
	{
		super(((PointFeature) city).getLocation(), city.getProperties());
	}

	/**
	 * Implementation of method to draw marker on the map.
	 * 
	 * Marker is represented by a triangle
	 * 
	 * @see de.fhpotsdam.unfolding.marker.SimplePointMarker#draw(processing.core.PGraphics,
	 *      float, float)
	 */
	public void draw(PGraphics pg, float x, float y)
	{
		// Save previous drawing style
		pg.pushStyle();

		// Coordinates for the triangle's points are offset to ensure the marker is
		// centered at the location given.
		pg.fill(COLOR);
		float offSet = TRI_SIZE / 2;
		pg.triangle(x, y - offSet, x - offSet, y + offSet, x + offSet, y + offSet);

		// Restore previous drawing style
		pg.popStyle();
	}

	/*
	 * Local getters for some city properties. You might not need these in module 4.
	 */
	public String getCity()
	{
		return getStringProperty("name");
	}

	public String getCountry()
	{
		return getStringProperty("country");
	}

	public float getPopulation()
	{
		return Float.parseFloat(getStringProperty("population"));
	}

}
