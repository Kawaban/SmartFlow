package algorithm;

import additionalObjects.Rank;
import additionalObjects.Unit;
import baseObjects.Task;

import java.util.ArrayList;
import java.util.Map;

public interface Algorithm {
    abstract public ArrayList<Unit> delegate(Map<Integer, ArrayList<Task>> taskMap, ArrayList<ArrayList<Rank>> ranks);
}
