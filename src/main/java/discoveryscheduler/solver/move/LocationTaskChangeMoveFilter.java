package discoveryscheduler.solver.move;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.ChangeMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import discoveryscheduler.domain.Task;
import discoveryscheduler.domain.Week;
import discoveryscheduler.domain.Location;

public class LocationTaskChangeMoveFilter implements SelectionFilter<Week, ChangeMove<Week>>{
	
	@Override
	public boolean accept(ScoreDirector<Week> scoreDirector, ChangeMove<Week> move) {
		Task task = (Task) move.getEntity();
		if(task.isLocked()){
			return false;
		}
		List<Object> values = new ArrayList<Object> (move.getPlanningValues());
		for(Object value : values){
			if(value instanceof Location){
				if(task.getActivity().getType() != ((Location) value).getType()){
					return false;
				}
			}
		}	
		return true;
	}	
}

