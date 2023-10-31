package algorithm;

import additionalObjects.Rank;
import baseObjects.Assignment;
import baseObjects.Developer;
import baseObjects.Task;

import java.util.ArrayList;
import java.util.Map;

public interface Algorithm {
    abstract public ArrayList<Assignment> delegate(ArrayList<Task> tasks, ArrayList<Developer> developers);
}
