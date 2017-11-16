package discoveryscheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.examples.common.domain.AbstractPersistable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import discoveryscheduler.persistence.DiscoverySchedulerImportConfig;
import discoveryscheduler.domain.solver.TaskDifficultyComparator;


import java.util.ArrayList;

import java.util.List;
import java.util.Properties;


@PlanningEntity(difficultyComparatorClass = TaskDifficultyComparator.class)
@XStreamAlias("Task")
public class Task extends AbstractPersistable {
	
	private static final long serialVersionUID = 5L;
	private static final Properties configuration = DiscoverySchedulerImportConfig.getConfig();
	private static final int MORNING_EST = Integer.parseInt(configuration.getProperty("morning_earliest_start_time"));
	private static final int MORNING_LST = Integer.parseInt(configuration.getProperty("morning_latest_start_time"));
	private static final int AFTERNOON_EST = Integer.parseInt(configuration.getProperty("afternoon_earliest_start_time"));
	private static final int AFTERNOON_LST = Integer.parseInt(configuration.getProperty("afternoon_latest_start_time"));
	
	private int index;
	
	private Activity activity;
	private Group group;
	
	boolean locked;
	
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
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
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
	
	@PlanningVariable(valueRangeProviderRefs = {"possibleLocations"}, nullable = true)
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
        return group.getLabel() + ": " + activity.getType().toUpperCase() + ", " + 
        		"Requires:" + (isInstructorRequired() ? " -instructor" : "") + (isLocationRequired() ? " -location" : "");
    }
	
    @Override
    public String toString() {
    	return group.getLabel() + ": " + activity.getType().toUpperCase() + ", " + 
        		"Requires:" + (isInstructorRequired() ? " -instructor" : "") + (isLocationRequired() ? " -location" : "");
    }
    		
	@ValueRangeProvider(id = "possibleStartRange")   
    List<Timestamp> getPossibleStartList(){		
    	List<Timestamp> starts = new ArrayList<Timestamp>();
    	for(Timestamp timestamp : group.getGroupTimestampList()){
    		int hour = timestamp.getHour().getHourIndex();
    		if((hour >= MORNING_EST && hour <= MORNING_LST) || (hour >= AFTERNOON_EST && hour <= AFTERNOON_LST)){
    			starts.add(timestamp);
    		}
    	}
    	return starts;
    }
	
	@ValueRangeProvider(id = "possibleLocations")   
    List<Location> getPossibleLocations(){
		return activity.getPossibleLocations();
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
