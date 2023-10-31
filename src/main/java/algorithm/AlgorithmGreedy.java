package algorithm;

import additionalObjects.Rank;
import additionalObjects.TaskState;
import baseObjects.Assignment;
import baseObjects.Developer;
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
    public ArrayList<Assignment> delegate(ArrayList<Task> tasks, ArrayList<Developer> developers) {
        ArrayList<Assignment> assignments=new ArrayList<Assignment>();
        for(Task task:tasks)
        {
            Rank maxRank=null;
            int pos=-1;
            for(int i=0;i<developers.size();i++)
            {
                Rank temp=developers.get(i).calculateRank(task.getEstimation());
                if(maxRank==null || temp.getValue()>maxRank.getValue())
                {
                    maxRank=temp;
                    pos=i;
                }
            }
            if(maxRank!=null)
            {
                assignments.add(new Assignment(maxRank.getDeveloperID(),task.getId()));
                developers.remove(pos);
            }

        }
        return assignments;
    }



}
