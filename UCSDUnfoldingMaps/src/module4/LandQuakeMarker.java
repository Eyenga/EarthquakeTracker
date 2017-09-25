package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/** Implements a visual marker for land earthquakes on an earthquake map
 * 
 *@formatter:off 
 * @author UC San Diego Intermediate Software Development MOOC team
 * 			Date: July 17, 2015
 * @author Frank Eyenga 
 * 			Edited: September 25, 2017 
 * @formatter:on
 */
public class LandQuakeMarker extends EarthquakeMarker
{

	public LandQuakeMarker(PointFeature quake)
	{
		// calling EarthquakeMarker constructor
		super(quake);

		// setting field in earthquake marker
		isOnLand = true;
	}

	/**
	 * Draws a circle to represent a land earthquake
	 * 
	 * @see module4.EarthquakeMarker#drawEarthquake(processing.core.PGraphics,
	 *      float, float)
	 */
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y)
	{
		pg.ellipseMode(pg.RADIUS);
		pg.ellipse(x, y, this.radius, this.radius);
		
	}

	// Get the country the earthquake is in
	public String getCountry()
	{
		return (String) getProperty("country");
	}

}