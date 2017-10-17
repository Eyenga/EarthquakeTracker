package module5;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

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
// TODO: Change SimplePointMarker to CommonMarker as the very first thing you do
// in module 5 (i.e. CityMarker extends CommonMarker). It will cause an error.
// That's what's expected.
public class CityMarker extends CommonMarker
{

	public static int TRI_SIZE = 5; // The size of the triangle marker
	public static final int SELECT_COLOR = 0xFFFFFFFF;

	public CityMarker(Location location)
	{
		super(location);
	}

	public CityMarker(Feature city)
	{
		super(((PointFeature) city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
	}

	public void drawMarker(PGraphics pg, float x, float y)
	{
		// Save previous drawing style
		pg.pushStyle();

		// IMPLEMENT: drawing triangle for each city
		pg.fill(150, 30, 30);
		pg.triangle(x, y - TRI_SIZE, x - TRI_SIZE, y + TRI_SIZE, x + TRI_SIZE, y + TRI_SIZE);

		// Restore previous drawing style
		pg.popStyle();
	}

	/** Show the title of the city if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		// Variables to help improve aesthetics and readability of GUI and code
		// respectively
		float textMargin = 2.5f; // Margin between text and enclosing box
		float posMargin = this.radius + 5; // Margin between location of marker and location of title
		String title = String.format("%1$s %2$s %3$s", getCity(), getCountry(), getPopulation());

		pg.pushStyle();

		pg.fill(SELECT_COLOR);
		pg.rect(x, y + posMargin, pg.textWidth(title) + (textMargin * 2), pg.textAscent() + pg.textDescent());

		pg.fill(0);
		pg.textAlign(pg.LEFT, pg.TOP);
		pg.text(title, x + textMargin, y + posMargin);
		
		pg.popStyle();

	}


	/*
	 * Local getters for some city properties.
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
