package algorithm;

import additionalObjects.Rank;
import additionalObjects.TaskState;
import baseObjects.Assignment;
import baseObjects.Developer;
import baseObjects.Task;

import java.util.ArrayList;
import java.util.Map;

public class AlgorithmGreedy implements Algorithm {
    class Comparator implements java.util.Comparator<Task> {

        @Override
        public int compare(Task o1, Task o2) {
            if (o1.getEstimation() < o2.getEstimation())
                return 1;
            else
                return -1;
        }
    }

    @Override
    public ArrayList<Assignment> delegate(ArrayList<Task> tasks, ArrayList<Developer> developers) {
        tasks.sort(new Comparator());
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        for (Task task : tasks) {
            Rank maxRank = null;
            int pos = -1;
            for (int i = 0; i < developers.size(); i++) {
                Rank temp = developers.get(i).calculateRank(task.getEstimation());
                if (maxRank == null || temp.getValue() > maxRank.getValue()) {
                    maxRank = temp;
                    pos = i;
                }
            }
            if (maxRank != null) {
                assignments.add(new Assignment(maxRank.getDeveloperID(), task.getId()));
                developers.remove(pos);
            }

        }
        return assignments;
    }


}
