package main; /**
 * Created by max on 12/28/15.
 */
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * A class to represent AirportMarkers on a world map.
 *
 *
 */
public class AirportMarker extends CommonMarker {
    public static List<SimpleLinesMarker> routes;

    PImage img;

    public AirportMarker(Feature city, PImage img) {
        super(((PointFeature)city).getLocation(), city.getProperties());
        this.img = img;

    }

    @Override
    public void drawMarker(PGraphics pg, float x, float y) {


        pg.image(img, x-10, y-10, 20, 20);


    }

    @Override
    public void showTitle(PGraphics pg, float x, float y) {
        // show rectangle with title
        String title = getName() + ", " + getCity() + ", " + getCountry() + ";";
        pg.pushStyle();

        pg.rectMode(PConstants.CORNER);

        pg.stroke(110);
        pg.fill(255, 255, 255);
        pg.rect(x, y + 15, pg.textWidth(title) +6, 18, 5);

        pg.textAlign(PConstants.LEFT, PConstants.TOP);
        pg.fill(0);
        pg.text(title, x + 3 , y +18);

        pg.popStyle();
        // show routes


    }

    public String getName() {
        return (String) getProperty("name");
    }

    public String getCity() {
        return (String) getProperty("city");
    }

    public String getCountry() {
        return (String) getProperty("country");
    }
}
