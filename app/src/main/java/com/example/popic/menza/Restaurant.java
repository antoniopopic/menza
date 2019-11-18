package com.example.popic.menza;

public class Restaurant {

    private String name;
    private String longitude;
    private String latitude;
    private String noVotes;
    private String votesSum;
    private String cityName;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getNoVotes() {
        return noVotes;
    }

    public void setNoVotes(String noVotes) {
        this.noVotes = noVotes;
    }

    public String getVotesSum() {
        return votesSum;
    }

    public void setVotesSum(String votesSum) {
        this.votesSum = votesSum;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
