package discoveryscheduler.solver.move;


import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.ChangeMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import discoveryscheduler.domain.Task;
import discoveryscheduler.domain.Week;
import discoveryscheduler.domain.Timestamp;
//import discoveryscheduler.domain.Instructor;
//import discoveryscheduler.domain.Location;


public class OutOfBoundsTaskChangeMoveFilter implements SelectionFilter<Week, ChangeMove<Week>>{
	
	@Override
	public boolean accept(ScoreDirector<Week> scoreDirector, ChangeMove<Week> move) {
		Task task = (Task) move.getEntity();
		List<Object> values = new ArrayList<Object> (move.getPlanningValues());
		Timestamp start = new Timestamp();
		//Instructor instructor = null;
		//Location location = null;
		for(Object value : values){
			if(value instanceof Timestamp){
				start = (Timestamp) value;
			}//else if(value instanceof Instructor){
			//	instructor = (Instructor) value;
			//}else if(value instanceof Location){
			//	location = (Location) value;
			//}
		}/*
		if(task.isInstructorRequired() && instructor == null){
			return false;
		}
		if(task.isLocationRequired() && location == null){
			return false;
		}*/
		
		
		int groupLastTimestampIndex = task.getGroup().getGroupTimestampList().get(task.getGroup().getGroupTimestampList().size()-1).getTimestampIndex();
		return groupLastTimestampIndex >= start.getTimestampIndex()+task.getActivity().getLength();
	}	
}

