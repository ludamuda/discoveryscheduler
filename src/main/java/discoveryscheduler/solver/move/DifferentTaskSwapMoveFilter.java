package discoveryscheduler.solver.move;

import java.util.List;
import java.util.ArrayList;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import discoveryscheduler.domain.Task;
import discoveryscheduler.domain.Timestamp;

public class DifferentTaskSwapMoveFilter implements SelectionFilter<SwapMove> {

    public boolean accept(ScoreDirector scoreDirector, SwapMove move) {
        Task leftTask = (Task) move.getLeftEntity();
        Task rightTask = (Task) move.getRightEntity();
        
        if(leftTask.getGroup().equals(rightTask.getGroup())){
        	return false;
    	} else {
    		boolean accept = true;
	        List<Object> assignedValues = new ArrayList<Object>(move.getPlanningValues());
	        for(Object assignedValue : assignedValues){
	        	if(assignedValue instanceof Timestamp){
	        		if(((Timestamp)assignedValue == leftTask.getStart() && leftTask.getStart().getTimestampIndex()+rightTask.getActivity().getLength() > rightTask.getGroup().getGroupTimestampList().get(rightTask.getGroup().getGroupTimestampList().size()-1).getTimestampIndex())
	        				|| ((Timestamp)assignedValue == rightTask.getStart() && rightTask.getStart().getTimestampIndex()+leftTask.getActivity().getLength() > leftTask.getGroup().getGroupTimestampList().get(leftTask.getGroup().getGroupTimestampList().size()-1).getTimestampIndex())){
	        			accept = false;
	        		}
	        	}
	        }
	        return accept;
    	}
    }

}