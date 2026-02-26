package in.co.rays.proj4.bean;

/**
 * RouteBean encapsulates route attributes
 */
public class RouteBean extends BaseBean {
    
    private String routeCode;
    private String source;
    private String destination;
    private double distance;

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String getKey() {
        return id + "";
    }

    @Override
    public String getValue() {
        return routeCode + " - " + source + " to " + destination;
    }

    @Override
    public String toString() {
        return "RouteBean [routeCode=" + routeCode + ", source=" + source + ", destination=" + destination
                + ", distance=" + distance + "]";
    }
}