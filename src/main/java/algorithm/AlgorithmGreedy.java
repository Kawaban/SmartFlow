package algorithm;

import additionalObjects.Rank;
import additionalObjects.TaskState;
import baseObjects.Assignment;
import baseObjects.Task;

import java.util.ArrayList;
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
    public ArrayList<Assignment> delegate(Map<Integer, ArrayList<Task>> taskMap, ArrayList<ArrayList<Rank>> developersRanks) {
            ArrayList<Assignment> updateInstances=new ArrayList<Assignment>();
            for(ArrayList<Rank> ranks:developersRanks)
            {
                ranks.sort(new Comparator());
                for(Rank rank:ranks)
                {
                    if(taskMap.containsKey(rank.getEstimation()) && !taskMap.get(rank.getEstimation()).isEmpty())
                    {
                        //save instances is needed
                        updateInstances.add(new Assignment(rank.getDeveloperID(),taskMap.get(rank.getEstimation()).get(0).getId()));
                        //
                        taskMap.get(rank.getEstimation()).remove(0);
                    }
                }
            }
            return updateInstances;
    }
}
