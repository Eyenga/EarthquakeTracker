package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/**
 * Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @formatter:off 
 * @author UC San Diego Intermediate Software Development MOOC team
 * 			Date: July 17, 2015
 * @author Frank Eyenga 
 * 			Edited: September 25, 2017 
 * @formatter:on
 */
public class OceanQuakeMarker extends EarthquakeMarker
{

	public OceanQuakeMarker(PointFeature quake)
	{
		super(quake);

		// setting field in earthquake marker
		isOnLand = false;
	}

	/**
	 * Draws a square to represent a ocean earthquake
	 * 
	 * @see module4.EarthquakeMarker#drawEarthquake(processing.core.PGraphics,
	 *      float, float)
	 */
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y)
	{
		pg.rectMode(pg.RADIUS);
		pg.rect(x, y, this.radius, this.radius);

	}

}
