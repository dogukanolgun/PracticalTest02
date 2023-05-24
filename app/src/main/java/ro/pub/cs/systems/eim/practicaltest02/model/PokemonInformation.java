package ro.pub.cs.systems.eim.practicaltest02.model;

public class PokemonInformation {

    private String name;
    private String url;

    public PokemonInformation() {
        this.name = null;
        this.url = null;
    }

    public PokemonInformation(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setTemperature(String temperature) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String windSpeed) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "WeatherForecastInformation{" +
                "name='" + name + '\'' +
                ", ur,='" + url + '\'' +
                '}';
    }

}
