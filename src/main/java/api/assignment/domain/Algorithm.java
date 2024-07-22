package api.assignment.domain;


import api.developer.domain.Developer;
import api.task.domain.Task;

import java.util.ArrayList;


interface Algorithm {
    ArrayList<Assignment> delegate(ArrayList<Task> tasks, ArrayList<Developer> developers);
}
