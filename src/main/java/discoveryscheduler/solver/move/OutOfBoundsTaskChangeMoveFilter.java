package discoveryscheduler.solver.move;


import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.ChangeMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import discoveryscheduler.domain.Task;
import discoveryscheduler.domain.Week;
import discoveryscheduler.domain.Timestamp;


public class OutOfBoundsTaskChangeMoveFilter implements SelectionFilter<Week, ChangeMove<Week>>{
		
	@Override
	public boolean accept(ScoreDirector<Week> scoreDirector, ChangeMove<Week> move) {
		Task task = (Task) move.getEntity();
		if(task.isLocked()){
			return false;
		}
		List<Object> values = new ArrayList<Object> (move.getPlanningValues());
		Timestamp start = new Timestamp();
		for(Object value : values){
			if(value instanceof Timestamp){
				start = (Timestamp) value;
				int groupLastTimestampIndex = task.getGroup().getGroupTimestampList().get(task.getGroup().getGroupTimestampList().size()-1).getTimestampIndex();
				if (groupLastTimestampIndex < start.getTimestampIndex()+task.getActivity().getLength()){
					return false;
				} else if(!(start.getDay().equals(task.getGroup().getGroupTimestampList().get(start.getTimestampIndex()-task.getGroup().getGroupTimestampList().get(0).getTimestampIndex()+task.getActivity().getLength()).getDay()))) {
				
					return false;
				}
			}
		}	
		return true;
	}	
}

