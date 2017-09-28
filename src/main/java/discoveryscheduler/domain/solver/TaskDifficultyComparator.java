package discoveryscheduler.domain.solver;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import discoveryscheduler.domain.Task;

public class TaskDifficultyComparator implements Comparator<Task> {

	@Override
	public int compare(Task task1, Task task2) {
		return new CompareToBuilder()
			.append(task1.isInstructorRequired(), task2.isInstructorRequired())
			.append(task1.isLocationRequired(), task2.isLocationRequired())
            .append(task1.getGroup().getNumOfClients(), task2.getGroup().getNumOfClients())
            .append(task1.getId(), task2.getId())
            .toComparison();
	}
}
