package ru.rzn.gmyasoedov.test4xxi.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * bean for forecast
 */
public class Forecast implements Serializable{
    private Date date;
    private double tempDay;
    private double tempNight;
    private String icon;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTempNight() {
        return tempNight;
    }

    public void setTempNight(double tempNight) {
        this.tempNight = tempNight;
    }

    public double getTempDay() {
        return tempDay;
    }

    public void setTempDay(double tempDay) {
        this.tempDay = tempDay;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Forecast forecast = (Forecast) o;

        if (Double.compare(forecast.tempDay, tempDay) != 0) return false;
        if (Double.compare(forecast.tempNight, tempNight) != 0) return false;
        if (date != null ? !date.equals(forecast.date) : forecast.date != null) return false;
        if (icon != null ? !icon.equals(forecast.icon) : forecast.icon != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = date != null ? date.hashCode() : 0;
        temp = Double.doubleToLongBits(tempDay);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(tempNight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        return result;
    }
}
