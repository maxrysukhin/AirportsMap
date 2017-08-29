package main; /**
 * Created by max on 12/28/15.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PImage;

/** An applet that shows airports (and routes)
 * on a world map.
 *
 */
public class AirportMap extends PApplet {

    UnfoldingMap map;
    private List<Marker> airportList;
    private List<Marker> routeList;
    private HashMap<Integer, Location> airports;
    private List<ShapeFeature> routes;
    private PImage img;

    private CommonMarker lastSelected;
    private CommonMarker lastClicked;

    public void setup() {
        // setting up PApplet
        size(800, 600, OPENGL);

        // setting up map and default events
        map = new UnfoldingMap(this, 50, 50, 730, 530, new Google.GoogleMapProvider());
        MapUtils.createDefaultEventDispatcher(this, map);

        // get features from airport data
        List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");

        // list for markers, hashmap for quicker access when matching with routes
        airportList = new ArrayList<Marker>();
        airports = new HashMap<Integer, Location>();

        img = loadImage("plane-flight-icon.png");

        // create markers from features
        for(PointFeature feature : features) {
            AirportMarker m = new AirportMarker(feature, img);

            airportList.add(m);

            // put airport in hashmap with OpenFlights unique id for key
            airports.put(Integer.parseInt(feature.getId()), feature.getLocation());

        }


        // parse route data
        routes = ParseFeed.parseRoutes(this, "routes.dat");
        routeList = new ArrayList<Marker>();
        for(ShapeFeature route : routes) {

            // get source and destination airportIds
            int source = Integer.parseInt((String)route.getProperty("source"));
            int dest = Integer.parseInt((String)route.getProperty("destination"));

            // get locations for airports on route
            if(airports.containsKey(source) && airports.containsKey(dest)) {
                route.addLocation(airports.get(source));
                route.addLocation(airports.get(dest));
            }

            SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());

            // hiding the route marker
            sl.setHidden(true);
            routeList.add(sl);

        }

        // adding routes to the map
        map.addMarkers(routeList);
        // adding airports to the map
        map.addMarkers(airportList);

    }

    // being called each time when the mouse is moving over the map
    public void mouseMoved()
    {
        // clear the last selection
        if (lastSelected != null) {
            lastSelected.setSelected(false);
            lastSelected = null;

        }


        selectMarkerIfHover(airportList);
    }

    // used in mouseMoved(), shows information about the airport when being hovered
    private void selectMarkerIfHover(List<Marker> markers)
    {
        // Abort if there's already a marker selected
        if (lastSelected != null) {
            return;
        }

        for (Marker m : markers)
        {
            CommonMarker marker = (CommonMarker)m;
            if (marker.isInside(map,  mouseX, mouseY)) {
                lastSelected = marker;
                marker.setSelected(true);
                return;
            }
        }
    }

    public void draw() {
        background(0);
        map.draw();

    }

    // being called each time mouse is clicked
    public void mouseClicked()
    {
        // if there is only one airport marker shown on the map
        if (lastClicked != null) {
            unhideAirports(); // shows all the other airport markers
            hideRoutes(); // hide all the route markers for the airport that was active
            lastClicked = null;
        }
        // if no airport has been shosen before the mouse click
        else if (lastClicked == null)
        {
            checkForClick();
        }
    }

    /*
     * Method is being called when the mouse is clicked
     */
    private void checkForClick()
    {
        if (lastClicked != null) return;
        // Loop over the earthquake markers to see if one of them is selected
        for (Marker marker : airportList) {
            if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
                lastClicked = (CommonMarker)marker;
                showRoutes(lastClicked);

                // Hide all the other airports
                for (Marker mhide : airportList) {
                    if (mhide != lastClicked) {
                        mhide.setHidden(true);
                    }
                }
                return;
            }
        }
    }

    // show routes for chosen airport
    private void showRoutes(Marker airport){

        Location loc = airport.getLocation();
        Location source;

        for(Marker m : this.routeList) {
            source = ((SimpleLinesMarker)m).getLocations().get(0);
            if(source.equals(loc))
                m.setHidden(false);
        }


    }

    // loop over and unhide all airports
    private void unhideAirports() {
        for(Marker marker : airportList) {
            marker.setHidden(false);
        }

    }

    // loop over and hide all routes
    private void hideRoutes() {

        for(Marker marker : routeList) {
            marker.setHidden(true);
        }
    }

}
