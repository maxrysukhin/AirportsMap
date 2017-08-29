package main;

/**
 * Created by max on 12/29/15.
 */
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/** Implements a common marker for airports on a map
 *
 *
 */
public abstract class CommonMarker extends SimplePointMarker {

    // Records whether this marker has been clicked (most recently)
    protected boolean clicked = false;

    public CommonMarker(Location location) {
        super(location);
    }

    public CommonMarker(Location location, java.util.HashMap<java.lang.String,java.lang.Object> properties) {
        super(location, properties);
    }

    // Getter method for clicked field
    public boolean getClicked() {
        return clicked;
    }

    // Setter method for clicked field
    public void setClicked(boolean state) {
        clicked = state;
    }

    // Common piece of drawing method for markers;
    // implemented in AirportMarker
    public void draw(PGraphics pg, float x, float y) {
        if (!hidden) {
            drawMarker(pg, x, y);
            if (selected) {
                showTitle(pg, x, y);
            }
        }
    }
    // implemented in AirportMarker
    public abstract void drawMarker(PGraphics pg, float x, float y);
    // implemented in AirportMarker
    public abstract void showTitle(PGraphics pg, float x, float y);
}
