package algorithm;

import additionalObjects.Rank;
import additionalObjects.TaskState;
import additionalObjects.Unit;
import baseObjects.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class AlgorithmGreedy implements Algorithm {

    class Comparator implements java.util.Comparator<Rank>{

        @Override
        public int compare(Rank o1, Rank o2) {
            if(o1.getValue()<o2.getValue())
                return 1;
            else
                return 0;
        }
    }
    @Override
    public ArrayList<Unit> delegate(Map<Integer, ArrayList<Task>> taskMap, ArrayList<ArrayList<Rank>> developersRanks) {
            ArrayList<Unit> updateInstances=new ArrayList<Unit>();
            for(ArrayList<Rank> ranks:developersRanks)
            {
                ranks.sort(new Comparator());
                for(Rank rank:ranks)
                {
                    if(taskMap.containsKey(rank.getEstimation()) && !taskMap.get(rank.getEstimation()).isEmpty())
                    {
                        taskMap.get(rank.getEstimation()).get(0).setTaskState(TaskState.ASSIGNED);
                        taskMap.get(rank.getEstimation()).get(0).setAssignedTo(rank.getDeveloper());
                        rank.getDeveloper().setTask(taskMap.get(rank.getEstimation()).get(0));
                        //save instances is needed
                        updateInstances.add(taskMap.get(rank.getEstimation()).get(0));
                        updateInstances.add(rank.getDeveloper());
                        //
                        taskMap.get(rank.getEstimation()).remove(0);
                    }
                }
            }
            return updateInstances;
    }
}
