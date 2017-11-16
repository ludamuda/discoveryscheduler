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
//import discoveryscheduler.persistence.DiscoverySchedulerImportConfig;

public class LocationTaskSwapMoveFilter implements SelectionFilter<Week, SwapMove<Week>> {

	//private static final Properties configuration = DiscoverySchedulerImportConfig.getConfig();
	//private static final int TIME_PERIODS_IN_DAY = Integer.parseInt(configuration.getProperty("time_periods_in_day"));
	@Override
    public boolean accept(ScoreDirector<Week> scoreDirector, SwapMove<Week> move) {
        Task leftTask = (Task) move.getLeftEntity();
        Task rightTask = (Task) move.getRightEntity();
        if(rightTask.isLocked() || leftTask.isLocked()){
			return false;
		}
        boolean accept = true;
        Location location = new Location();
        //Timestamp start = new Timestamp();
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
        	/*else if(assignedValue instanceof Timestamp){
				start = (Timestamp) assignedValue;
				if(leftTask.equals(rightTask)){
					accept = false;
					break;
		    	} else if((start.getTimestampIndex()+rightTask.getActivity().getLength() > rightTask.getGroup().getGroupTimestampList().get(rightTask.getGroup().getGroupTimestampList().size()-1).getTimestampIndex())
						|| (start.getTimestampIndex()+leftTask.getActivity().getLength() > leftTask.getGroup().getGroupTimestampList().get(leftTask.getGroup().getGroupTimestampList().size()-1).getTimestampIndex())){
		    		accept = false;
					break;
		    	} else if((start.getTimestampIndex()%TIME_PERIODS_IN_DAY != start.getTimestampIndex()+rightTask.getActivity().getLength()%TIME_PERIODS_IN_DAY)
		    			|| (start.getTimestampIndex()%TIME_PERIODS_IN_DAY != start.getTimestampIndex()+leftTask.getActivity().getLength()%TIME_PERIODS_IN_DAY)){
		    		accept = false;
					break;
		    	}
			} */
        }
        return accept;
    }

}