package discoveryscheduler.domain.solver;


import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import discoveryscheduler.domain.Task;
import discoveryscheduler.domain.Week;


public class OutOfBoundsTaskSelectionFilter implements SelectionFilter<Week, Task>{
	public boolean accept(ScoreDirector<Week> scoreDirector, Task task) {
		if (task.getStart() == null){
			return true;
		} else {
			return task.getGroup().getGroupTimestampList().get(task.getGroup().getGroupTimestampList().size()-1).getTimestampIndex() > task.getStart().getTimestampIndex()+task.getActivity().getLength();
		}
    }
}

