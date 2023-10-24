package additionalObjects;

import baseObjects.Developer;
import org.hibernate.sql.Update;

public class Rank {
    private int estimation;
    private double value;
    private int power;

    private Developer developer;

    @Override
    public boolean equals(Object obj) {
        Rank other= (Rank) obj;
        return other.getEstimation()==estimation;
    }

    public Rank(int estimation, double value,Developer developer) {
        this.estimation = estimation;
        this.value = value;
        int power=1;
        this.developer=developer;
    }

    public void updateRank(double addedValue)
    {
        value=(value*power+addedValue)/(power+1);
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

    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }
}
