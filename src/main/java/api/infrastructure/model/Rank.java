package api.infrastructure.model;


import lombok.Data;

import java.util.UUID;

@Data
public class Rank {
    private int estimation;
    private double value;
    private int power;

    private UUID developerID;

    private final static double CONST_DEFAULT_RANK = 3.0;

    @Override
    public boolean equals(Object obj) {
        Rank other = (Rank) obj;
        return other.getEstimation() == estimation;
    }

    public Rank(int estimation, double value, UUID developerID) {
        this.estimation = estimation;
        this.value = value;
        this.power = 1;
        this.developerID = developerID;
    }

    public Rank(int estimation, UUID developerID) {
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


}
