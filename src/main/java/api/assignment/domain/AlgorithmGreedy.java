package api.assignment.domain;

import api.developer.domain.Developer;
import api.infrastructure.model.Rank;
import api.task.domain.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

//According to Weighted Shortest Job First conception shorter estimation tasks have higher priority
//O(n^2)
@Component
class AlgorithmGreedy implements Algorithm {
    class Comparator implements java.util.Comparator<Task> {

        @Override
        public int compare(Task o1, Task o2) {
            if (o1.getEstimation() < o2.getEstimation())
                return 1;
            else if(o1.getEstimation() == o2.getEstimation())
                return 0;
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
                assignments.add(Assignment.builder()
                        .projectId(task.getProject().getUuid())
                        .taskId(task.getUuid())
                        .developerId(developers.get(pos).getUuid())
                        .build());
                developers.remove(pos);
            }

        }
        return assignments;
    }


}
