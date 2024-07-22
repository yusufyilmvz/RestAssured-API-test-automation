import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.DecimalFormat;

public class Data {
    private int year;
    private double price;
    @JsonProperty("CPU Model")
    private String cpuModel;
    @JsonProperty("Hard disk size")
    private String hardDiskSize;
    private String color;

    public Data(int year, double price, String cpuModel, String hardDiskSize, String color) {
        this.year = year;
        this.price = price;
        this.cpuModel = cpuModel;
        this.hardDiskSize = hardDiskSize;
        this.color = color;
    }

    public Data(int year, double price, String cpuModel, String hardDiskSize) {
        this.year = year;
        this.price = price;
        this.cpuModel = cpuModel;
        this.hardDiskSize = hardDiskSize;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
    }

    public String getHardDiskSize() {
        return hardDiskSize;
    }

    public void setHardDiskSize(String hardDiskSize) {
        this.hardDiskSize = hardDiskSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
