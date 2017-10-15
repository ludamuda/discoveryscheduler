package discoveryscheduler.solver.move;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.ChangeMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import discoveryscheduler.domain.Instructor;
//import discoveryscheduler.domain.Location;
import discoveryscheduler.domain.Task;
//import discoveryscheduler.domain.Timestamp;
import discoveryscheduler.domain.Week;

public class InstructorReqTaskSelectionFilter implements SelectionFilter<Week, ChangeMove<Week>> {
	public boolean accept(ScoreDirector<Week> scoreDirector, ChangeMove<Week> move) {
		Task task = (Task) move.getEntity();
		List<Object> values = new ArrayList<Object> (move.getPlanningValues());
		Instructor instructor = null;
		for(Object value : values){
			if(value instanceof Instructor){
				instructor = (Instructor) value;
			}
		}
		return task.isInstructorRequired() && instructor != null;	
	}
}
