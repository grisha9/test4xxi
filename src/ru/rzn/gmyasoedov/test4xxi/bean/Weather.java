package ru.rzn.gmyasoedov.test4xxi.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * weather bean
 */
public class Weather implements Serializable{
    private int id;
    private String name;
    private String icon;
    private double temp;
    private int pressure;
    private int humidity;
    private Date date;
    private List<Forecast> forecasts;

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weather weather = (Weather) o;

        if (humidity != weather.humidity) return false;
        if (id != weather.id) return false;
        if (pressure != weather.pressure) return false;
        if (Double.compare(weather.temp, temp) != 0) return false;
        if (date != null ? !date.equals(weather.date) : weather.date != null) return false;
        if (forecasts != null ? !forecasts.equals(weather.forecasts) : weather.forecasts != null) return false;
        if (icon != null ? !icon.equals(weather.icon) : weather.icon != null) return false;
        if (name != null ? !name.equals(weather.name) : weather.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp1;
        result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        temp1 = Double.doubleToLongBits(temp);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        result = 31 * result + pressure;
        result = 31 * result + humidity;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (forecasts != null ? forecasts.hashCode() : 0);
        return result;
    }
}
