package com.ruoyi.environment.ingest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherJson {

    private String timestamp;

    @JsonProperty("data")
    private DataNode data;

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public DataNode getData() { return data; }
    public void setData(DataNode data) { this.data = data; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataNode {
        private String timestamp;
        private String machine;

        @JsonProperty("weather_data")
        private List<PlatformBlock> weatherData;

        @JsonProperty("sector_wind_speeds")
        private List<SectorWind> sectorWindSpeeds;

        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public String getMachine() { return machine; }
        public void setMachine(String machine) { this.machine = machine; }
        public List<PlatformBlock> getWeatherData() { return weatherData; }
        public void setWeatherData(List<PlatformBlock> weatherData) { this.weatherData = weatherData; }
        public List<SectorWind> getSectorWindSpeeds() { return sectorWindSpeeds; }
        public void setSectorWindSpeeds(List<SectorWind> sectorWindSpeeds) { this.sectorWindSpeeds = sectorWindSpeeds; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlatformBlock {
        private String platform;
        private PlatformData data;

        public String getPlatform() { return platform; }
        public void setPlatform(String platform) { this.platform = platform; }
        public PlatformData getData() { return data; }
        public void setData(PlatformData data) { this.data = data; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlatformData {
        @JsonProperty("wind_speed")
        private Double windSpeed;
        @JsonProperty("wind_direction")
        private Double windDirection;
        private Double temperature;
        private Double humidity;
        private Double pressure;

        public Double getWindSpeed() { return windSpeed; }
        public void setWindSpeed(Double windSpeed) { this.windSpeed = windSpeed; }
        public Double getWindDirection() { return windDirection; }
        public void setWindDirection(Double windDirection) { this.windDirection = windDirection; }
        public Double getTemperature() { return temperature; }
        public void setTemperature(Double temperature) { this.temperature = temperature; }
        public Double getHumidity() { return humidity; }
        public void setHumidity(Double humidity) { this.humidity = humidity; }
        public Double getPressure() { return pressure; }
        public void setPressure(Double pressure) { this.pressure = pressure; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SectorWind {
        private Integer sector;
        @JsonProperty("wind_speeds")
        private Map<String, Double> windSpeeds; // key: triangle_5, triangle_10, triangle_14

        public Integer getSector() { return sector; }
        public void setSector(Integer sector) { this.sector = sector; }
        public Map<String, Double> getWindSpeeds() { return windSpeeds; }
        public void setWindSpeeds(Map<String, Double> windSpeeds) { this.windSpeeds = windSpeeds; }
    }
}