package module4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/**
 * EarthquakeCityMap An application with an interactive map displaying
 * earthquake data.
 * @formatter:off 
 * @author UC San Diego Intermediate Software Development MOOC team
 * 			Date: July 17, 2015
 * @author Frank Eyenga 
 * 			Edited: September 25, 2017 
 * @formatter:on
 */
public class EarthquakeCityMap extends PApplet
{

	// We will use member variables, instead of local variables, to store the data
	// that the setUp and draw methods will need to access (as well as other
	// methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of
	// earthquakes
	// per country.

	// You can ignore this. It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = false;

	/**
	 * This is where to find the local tiles, for working without an Internet
	 * connection
	 */
	public static String mbTilesString = "blankLight-1-3.mbtiles";

	// feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";

	// The map
	private UnfoldingMap map;

	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;

	public void setup()
	{
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		if (offline)
		{
			map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
			earthquakesURL = "2.5_week.atom"; // The same feed, but saved August 7, 2015
		} else
		{
			// map = new UnfoldingMap(this, 200, 50, 650, 600, new
			// Google.GoogleMapProvider());
			map = new UnfoldingMap(this, 200, 50, 650, 600, new OpenStreetMap.OpenStreetMapProvider());

			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			// earthquakesURL = "2.5_week.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);

		// FOR TESTING: Set earthquakesURL to be one of the testing files by
		// uncommenting
		// one of the lines below. This will work whether you are online or offline
		// earthquakesURL = "test1.atom";
		// earthquakesURL = "test2.atom";

		// WHEN TAKING THIS QUIZ: Uncomment the next line
		// earthquakesURL = "quiz1.atom";

		// (2) Reading in earthquake data and geometric properties
		// STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);

		// STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for (Feature city : cities)
		{
			cityMarkers.add(new CityMarker(city));
		}

		// STEP 3: read in earthquake RSS feed
		List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
		quakeMarkers = new ArrayList<Marker>();

		for (PointFeature feature : earthquakes)
		{
			// check if LandQuake
			if (isLand(feature))
			{
				quakeMarkers.add(new LandQuakeMarker(feature));
			}
			// OceanQuakes
			else
			{
				quakeMarkers.add(new OceanQuakeMarker(feature));
			}
		}

		// could be used for debugging
		printQuakes();

		// (3) Add markers to map
		// NOTE: Country markers are not added to the map. They are used
		// for their geometric properties
		map.addMarkers(quakeMarkers);
		map.addMarkers(cityMarkers);

	} // End setup

	public void draw()
	{
		background(0);
		map.draw();
		addKey();

	}

	// helper method to draw key in GUI
	// TODO: Update this method as appropriate
	private void addKey()
	{
		pushStyle();

		fill(255, 250, 240);
		rect(15, 50, 160, 250);

		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", 50, 75);

		fill(CityMarker.COLOR);
		triangle(50, 100, 43, 115, 57, 115);
		fill(255);
		ellipse(50, 130, 15, 15);
		rectMode(CENTER);
		rect(50, 152, 15, 15);

		fill(0, 0, 0);
		text("City Marker", 75, 105);
		text("Land Quake", 75, 130);
		text("Ocean Quake", 75, 152);
		text("Marker size ~ Magnitude", 20, 172);
		text("Shallow", 75, 190);
		text("Intermediate", 75, 205); 
		text("Deep", 75, 220); 

		
		fill(EarthquakeMarker.SHALLOW_COLOR);
		ellipse(50, 190, 10, 10);
		fill(EarthquakeMarker.INTERMEDIATE_COLOR);
		ellipse(50, 205, 10, 10);
		fill(EarthquakeMarker.DEEP_COLOR);
		ellipse(50, 220, 10, 10);
	
		popStyle();
	}

	/**
	 * Checks whether this quake occurred on land. If it did, it sets the //
	 * "country" property of its PointFeature to the country where it occurred //
	 * and returns true.
	 * 
	 * @param earthquake
	 *            A PointFeature of the earthquake to test
	 * @return true if the earthquake occurred on land. false otherwise
	 */
	private boolean isLand(PointFeature earthquake)
	{
		// Loop over all the country markers.
		// For each, check if the earthquake PointFeature is in the
		// country in m.
		for (Marker m : countryMarkers)
		{
			if (isInCountry(earthquake, m)) { return true; }
		}

		// not inside any country
		return false;
	}

	/**
	 * Helper method that prints the number of earthquakes in each country and the
	 * ocean
	 */
	private void printQuakes()
	{
		HashMap<String, Integer> numOfQuakes = new HashMap<>();
		int oceanQuakes = 0;

		// Loop through list of countries and add mapping of country names to the number
		// of earthquakes in each
		for (Marker country : countryMarkers)
		{
			numOfQuakes.put(country.getStringProperty("name"), 0);
		}

		// Loop through all the earthquakes and update the corresponding quake counter
		for (Marker marker : quakeMarkers)
		{
			EarthquakeMarker quake = (EarthquakeMarker) marker;

			if (quake.isOnLand)
			{
				String country = quake.getStringProperty("country");
				int quakeCounter = numOfQuakes.get(country);
				numOfQuakes.replace(country, ++quakeCounter);
			} else
			{
				oceanQuakes++;
			}
		}

		// Print out information
		for (Map.Entry<String, Integer> entry : numOfQuakes.entrySet())
		{
			System.out.printf("%1$-34s\t%2$d%n", entry.getKey() + ":", entry.getValue());
		}
		System.out.printf("%1$-34s\t%2$d%n", "OCEAN QUAKES:", oceanQuakes);

	}

	/**
	 * Helper method to test whether a given earthquake is in a given country This
	 * will also add the country property to the properties of the earthquake.
	 * 
	 * @param earthquake
	 *            A PointFeature of the earthquake to test
	 * @param country
	 *            A Marker of the country to test against
	 * @return true if the given earthquake occurred in the given country. false
	 *         otherwise
	 */
	private boolean isInCountry(PointFeature earthquake, Marker country)
	{
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if (country.getClass() == MultiMarker.class)
		{

			// looping over markers making up MultiMarker
			for (Marker marker : ((MultiMarker) country).getMarkers())
			{

				// checking if inside
				if (((AbstractShapeMarker) marker).isInsideByLocation(checkLoc))
				{
					earthquake.addProperty("country", country.getProperty("name"));

					// return if is inside one
					return true;
				}
			}
		}

		// check if inside country represented by SimplePolygonMarker
		else if (((AbstractShapeMarker) country).isInsideByLocation(checkLoc))
		{
			earthquake.addProperty("country", country.getProperty("name"));

			return true;
		}
		return false;
	}

}
