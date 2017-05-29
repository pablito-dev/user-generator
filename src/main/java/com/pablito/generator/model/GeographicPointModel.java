package com.pablito.generator.model;

/**
 * Created by pavel_000 on 28/05/2017.
 */
public class GeographicPointModel {
    private Double lat;
    private Double lng;

    public GeographicPointModel(final Double lat, final Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    GeographicPointModel() {
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(final Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(final Double lng) {
        this.lng = lng;
    }

    public double milesFrom(final GeographicPointModel location) {
        double rInMiles = 3958.756;
        double lat1Radians = radiansFromDegrees(this.getLat());
        double lat2Radians = radiansFromDegrees(location.getLat());
        double deltaLat = radiansFromDegrees(location.getLat() - this.getLat());
        double deltaLong = radiansFromDegrees(location.getLng() - this.getLng());

        double a = Math.sin(deltaLat/2.0) * Math.sin(deltaLat/2.0) +
                Math.cos(lat1Radians) * Math.cos(lat2Radians) * Math.sin(deltaLong/2.0) * Math.sin(deltaLong/2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return rInMiles * c;
    }

    public double getLatitudeDifference(final GeographicPointModel location) {
        return Math.abs(this.lat - location.getLat());
    }
    public double getLongitudeDifference(final GeographicPointModel location) {
        return Math.abs(this.lng - location.getLng());
    }
    public GeographicPointModel getOffsetPoint(final double latitudeOffset, final double longitudeOffset) {
        return new GeographicPointModel(this.lat + latitudeOffset, this.lng + longitudeOffset);
    }

    private double radiansFromDegrees(final double degrees) {
        return (degrees * Math.PI) / 180.0;
    }

    @Override
    public String toString() {
        return lat + ", " + lng;
    }
}
