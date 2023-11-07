package additionalObjects;

import baseObjects.Developer;
import org.hibernate.sql.Update;

public class Rank {
    private int estimation;
    private double value;
    private int power;

    private long developerID;

    private final static double CONST_DEFAULT_RANK = 3.0;

    @Override
    public boolean equals(Object obj) {
        Rank other = (Rank) obj;
        return other.getEstimation() == estimation;
    }

    public Rank(int estimation, double value, long developerID) {
        this.estimation = estimation;
        this.value = value;
        this.power = 1;
        this.developerID = developerID;
    }

    public Rank(int estimation, long developerID) {
        this.estimation = estimation;
        this.value = 0;
        this.power = 0;
        this.developerID = developerID;
    }

    public void setDefaultRank() {
        this.value = CONST_DEFAULT_RANK;
        this.power = 1;
    }

    public void updateRank(double addedValue) {
        value = (value * power + addedValue) / (power + 1);
        power++;
    }

    public int getEstimation() {
        return estimation;
    }

    public void setEstimation(int estimation) {
        this.estimation = estimation;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public long getDeveloperID() {
        return developerID;
    }

    public void setDeveloperID(long developerID) {
        this.developerID = developerID;
    }
}
