package module3;

//Java utilities libraries
import java.util.ArrayList;
import java.util.Iterator;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/**
 * EarthquakeCityMap An application with an interactive map displaying
 * earthquake data. Author: UC San Diego Intermediate Software Development MOOC
 * team
 * 
 * @author Your name here Date: July 17, 2015
 */
public class EarthquakeCityMap extends PApplet
{

	// You can ignore this. It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;

	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	public static final int LIGHT_QUAKE_COLOR = new PApplet().color(0, 0, 255); // Indicates earthquake of light
																				// magnitude
	public static final int MODERATE_QUAKE_COLOR = new PApplet().color(255, 255, 0); // For earthquake of moderate
																						// magnitude
	public static final int EXTREMME_QUAKE_COLOR = new PApplet().color(255, 0, 0); // For earthquake of large magnitude
	
	public static final float MARKER_SIZE = 15;

	/**
	 * This is where to find the local tiles, for working without an Internet
	 * connection
	 */
	public static String mbTilesString = "blankLight-1-3.mbtiles";

	// The map
	private UnfoldingMap map;

	// feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	public void setup()
	{
		size(950, 600, OPENGL);

		if (offline)
		{
			map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
			earthquakesURL = "2.5_week.atom"; // Same feed, saved Aug 7, 2015, for working offline
		} else
		{
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			// earthquakesURL = "2.5_week.atom";
		}

		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		// Use provided parser to collect properties for each earthquake
		// PointFeatures have a getLocation method
		List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);

		// The List you will populate with new SimplePointMarkers
		List<Marker> markers = new ArrayList<Marker>();

		// Create SimplePointMarkers for each PointFeature. Populate list of markers
		for (PointFeature eq : earthquakes)
		{
			Marker eqMarker = createMarker(eq);
			markers.add(eqMarker);
		}

		// Add the markers to the map so that they are displayed
		map.addMarkers(markers);
	}

	public void draw()
	{
		background(127.5f);
		map.draw();
		addKey();
	}
	
	/**
	 * Helper method that takes in an earthquake feature and returns a
	 * SimplePointMarker for that earthquake
	 * 
	 * @param feature
	 *            PointFeature used to create the SimplePointMarker
	 * @return A SimplePointMarker
	 */
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// System.out.println(feature.getProperties());

		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation(), feature.getProperties());

		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());

		// style the marker's size and color according to the earthquake's magnitude
		if (mag <= THRESHOLD_LIGHT)
		{
			marker.setColor(LIGHT_QUAKE_COLOR);
			marker.setRadius(MARKER_SIZE);
		} else if (mag <= THRESHOLD_MODERATE)
		{
			marker.setColor(MODERATE_QUAKE_COLOR);
			marker.setRadius(MARKER_SIZE);
		} else
		{
			marker.setColor(EXTREMME_QUAKE_COLOR);
			marker.setRadius(MARKER_SIZE);
		}

		// Finally return the marker
		return marker;
	}

	/**
	 * 
	 */
	private void addKey()
	{
		float keyX = 10, keyY = 50, keyW = 180, keyH = 200, keyBG = 255;
		float margin = 20;
		char[] title = "Key".toCharArray();

		fill(keyBG);
		// Add a rectangle
		rect(keyX, keyY, keyW, keyH);		
		// Add relevant shapes and color
		ellipseMode(CORNER);
		fill(LIGHT_QUAKE_COLOR);
		ellipse((keyX + margin), (keyY + 2*margin), MARKER_SIZE, MARKER_SIZE);
		fill(MODERATE_QUAKE_COLOR);
		ellipse((keyX + margin), (keyY + 3*margin), MARKER_SIZE, MARKER_SIZE);
		fill(EXTREMME_QUAKE_COLOR);
		ellipse((keyX + margin), (keyY + 4*margin), MARKER_SIZE, MARKER_SIZE);
		// Add text
		fill(0);
		textAlign(CENTER);
		text("Earthquake Tracker", (keyW / 2), keyY + margin);
		textAlign(LEFT, TOP);
		text("Below 4.0 Magnitude", (keyX + MARKER_SIZE + 2*margin), (keyY + 2*margin));
		text("4.0+ Magnitude", (keyX + MARKER_SIZE + 2*margin), (keyY + 3*margin));
		text("5.0+ Magnitude", (keyX + MARKER_SIZE + 2*margin), (keyY + 4*margin));
		//noFill();

	}
}
