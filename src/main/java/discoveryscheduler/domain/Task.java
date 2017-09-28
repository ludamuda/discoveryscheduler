package discoveryscheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.examples.common.domain.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import discoveryscheduler.persistence.DiscoverySchedulerImportConfig;
import discoveryscheduler.domain.solver.TaskDifficultyComparator;
import discoveryscheduler.domain.solver.OutOfBoundsTaskSelectionFilter;

import java.util.ArrayList;
//import java.util.Collection;
import java.util.List;
import java.util.Properties;


@PlanningEntity(difficultyComparatorClass = TaskDifficultyComparator.class,
				movableEntitySelectionFilter = OutOfBoundsTaskSelectionFilter.class)
@XStreamAlias("Task")
public class Task extends AbstractPersistable {
	
	private static final Properties configuration = DiscoverySchedulerImportConfig.getConfig();
	private static final int MORNING_EST = Integer.parseInt(configuration.getProperty("morning_earliest_start_time"));
	private static final int MORNING_LST = Integer.parseInt(configuration.getProperty("morning_latest_start_time"));
	private static final int AFTERNOON_EST = Integer.parseInt(configuration.getProperty("afternoon_earliest_start_time"));
	private static final int AFTERNOON_LST = Integer.parseInt(configuration.getProperty("afternoon_latest_start_time"));
	
	private int index;
	
	private Activity activity;
	private Group group;
	
	//Planning variables
	private Timestamp start;
	private Instructor instructor;
	private Location location;
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}	
	@PlanningVariable(valueRangeProviderRefs = {"possibleStartRange"})
	public Timestamp getStart() {
		return start;
	}	
	public void setStart(Timestamp start) {
		this.start = start;
	}
	public Timestamp getEnd(){
		return group.getGroupTimestampList().get(group.getGroupTimestampList().indexOf(start) + activity.getLength() - 1);
	}

	@PlanningVariable(valueRangeProviderRefs = {"instructorRange"}, nullable = true)
	public Instructor getInstructor() {
		return instructor;
	}
	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}
	
	@PlanningVariable(valueRangeProviderRefs = {"locationRange"}, nullable = true)
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	
	/**
	 * complex methods
	 * 
	 */
	
	public String getLabel() {
        return activity.getName() + " " + group.getLabel() + " " + 
        		"Reqs:" + (isInstructorRequired() ? " -I" : "") + (isLocationRequired() ? " -L" : "");
    }
	
    @Override
    public String toString() {
        return activity + " " + group + " " +
        		"Reqs: " + (isInstructorRequired() ? " -I" : "") + (isLocationRequired() ? " -L" : "");
    }
    		
	@ValueRangeProvider(id = "possibleStartRange")   
    List<Timestamp> getPossibleStartList(){		
    	List<Timestamp> starts = new ArrayList<Timestamp>();
    	for(Timestamp timestamp : group.getGroupTimestampList()){
    		int hour = timestamp.getHour().getHourIndex();
    		// snidane nejdrive v 7.30, 30 min snidane, 30 min odpocinek = 1 hod, 
    		// lunchtime is: est: 12.00 lst: 13.30, typical length: 30 min + 0,5 - 1 hod odpocinek = 1 - 1,5 hod
    		// vecere est 17.30, lst 19.00
    		// morning activity: 
    		if((hour >= MORNING_EST && hour <= MORNING_LST) || (hour >= AFTERNOON_EST && hour <= AFTERNOON_LST)){ //8-10, 13-16
    			starts.add(timestamp);
    		}
    	}
    	return starts;
    }
	
	public Day getDay(){
		return start.getDay();
	}
	public boolean isInstructorRequired(){
		return getActivity().isInstructorRequired();
	}
	public boolean isLocationRequired(){
		return getActivity().isLocationRequired();
	}
    
}
