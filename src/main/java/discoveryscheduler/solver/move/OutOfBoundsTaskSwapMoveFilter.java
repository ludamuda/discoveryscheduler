package discoveryscheduler.solver.move;

//import java.util.List;
//import java.util.ArrayList;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import discoveryscheduler.domain.Task;
//import discoveryscheduler.domain.Timestamp;
import discoveryscheduler.domain.Week;

public class OutOfBoundsTaskSwapMoveFilter implements SelectionFilter<Week, SwapMove<Week>> {

	@Override
    public boolean accept(ScoreDirector<Week> scoreDirector, SwapMove<Week> move) {
        Task leftTask = (Task) move.getLeftEntity();
        Task rightTask = (Task) move.getRightEntity();
        boolean accept = true;
        if(leftTask.equals(rightTask)){
        	accept = false;
    	} else {
    		/*boolean accept = true;
	        List<Object> assignedValues = new ArrayList<Object>(move.getPlanningValues());
	        for(Object assignedValue : assignedValues){
	        	if(assignedValue instanceof Timestamp){
	        		if((((Timestamp) assignedValue).getTimestampIndex()+rightTask.getActivity().getLength() > rightTask.getGroup().getGroupTimestampList().get(rightTask.getGroup().getGroupTimestampList().size()-1).getTimestampIndex())
	        				|| (((Timestamp) assignedValue).getTimestampIndex()+leftTask.getActivity().getLength() > leftTask.getGroup().getGroupTimestampList().get(leftTask.getGroup().getGroupTimestampList().size()-1).getTimestampIndex())){
	        			accept = false;
	        		}
	        	}
	        }
	        return accept;*/
	        accept = ((rightTask.getStart().getTimestampIndex()+rightTask.getActivity().getLength() > rightTask.getGroup().getGroupTimestampList().get(rightTask.getGroup().getGroupTimestampList().size()-1).getTimestampIndex())
				|| (leftTask.getStart().getTimestampIndex()+leftTask.getActivity().getLength() > leftTask.getGroup().getGroupTimestampList().get(leftTask.getGroup().getGroupTimestampList().size()-1).getTimestampIndex()));
    	}
        return accept;
    }

}