package ru.vitoliot.desktop.utilities;

import java.util.Objects;

public class MyFirstTime extends Object{
    public long hours;
    public long minutes;

    public MyFirstTime(long hours, long minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public MyFirstTime(long time) {
        long min = time / 1000 / 60;
        this.hours = min / 60;
        this.minutes = min - hours*60;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyFirstTime that = (MyFirstTime) o;
        return hours > 0 && hours == that.hours;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes);
    }

    @Override
    public String toString() {
        return hours > 0 && minutes > 0 ? hours + "h " + minutes + "m" : "прошло";
    }
}
