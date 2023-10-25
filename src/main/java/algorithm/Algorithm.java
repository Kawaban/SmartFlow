package algorithm;

import additionalObjects.Rank;
import baseObjects.Assignment;
import baseObjects.Task;

import java.util.ArrayList;
import java.util.Map;

public interface Algorithm {
    abstract public ArrayList<Assignment> delegate(Map<Integer, ArrayList<Task>> taskMap, ArrayList<ArrayList<Rank>> ranks);
}
