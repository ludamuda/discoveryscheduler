package discoveryscheduler.solver.move;

import java.util.ArrayList;
import java.util.List;
//import java.util.Properties;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import discoveryscheduler.domain.Task;
//import discoveryscheduler.domain.Timestamp;
import discoveryscheduler.domain.Location;
import discoveryscheduler.domain.Week;


public class LocationTaskSwapMoveFilter implements SelectionFilter<Week, SwapMove<Week>> {


	@Override
    public boolean accept(ScoreDirector<Week> scoreDirector, SwapMove<Week> move) {
        Task leftTask = (Task) move.getLeftEntity();
        Task rightTask = (Task) move.getRightEntity();
        if(rightTask.isLocked() || leftTask.isLocked()){
			return false;
		}
        boolean accept = true;
        Location location = new Location();

        List<Object> assignedValues = new ArrayList<Object>(move.getPlanningValues());
        for(Object assignedValue : assignedValues){
        	if(assignedValue instanceof Location){
				location = (Location) assignedValue;
				if(leftTask.equals(rightTask)){
					accept = false;
					break;
				} else if(location.getType() != leftTask.getActivity().getType() || location.getType() != rightTask.getActivity().getType()){
		    		accept = false;
					break;
		    	}
			} 
        	
        }
        return accept;
    }

}