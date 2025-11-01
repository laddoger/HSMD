package com.ruoyi.environment.ingest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * 用于解析 temperature_data.json 文件的结构化对象
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TempsJson {

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

        private List<Sector> sectors;

        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public String getMachine() { return machine; }
        public void setMachine(String machine) { this.machine = machine; }
        public List<Sector> getSectors() { return sectors; }
        public void setSectors(List<Sector> sectors) { this.sectors = sectors; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sector {
        private Integer sector;

        @JsonProperty("return_water_temps")
        private List<Rwt> returnWaterTemps;

        public Integer getSector() { return sector; }
        public void setSector(Integer sector) { this.sector = sector; }
        public List<Rwt> getReturnWaterTemps() { return returnWaterTemps; }
        public void setReturnWaterTemps(List<Rwt> returnWaterTemps) { this.returnWaterTemps = returnWaterTemps; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rwt {
        private Integer triangle;

        @JsonProperty("left_temp")
        private Double leftTemp;

        @JsonProperty("right_temp")
        private Double rightTemp;

        public Integer getTriangle() { return triangle; }
        public void setTriangle(Integer triangle) { this.triangle = triangle; }
        public Double getLeftTemp() { return leftTemp; }
        public void setLeftTemp(Double leftTemp) { this.leftTemp = leftTemp; }
        public Double getRightTemp() { return rightTemp; }
        public void setRightTemp(Double rightTemp) { this.rightTemp = rightTemp; }
    }
}