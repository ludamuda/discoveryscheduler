package discoveryscheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.examples.common.domain.AbstractPersistable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@PlanningEntity
@XStreamAlias("Task")
public class Task extends AbstractPersistable {
	private int index;
	
	private Activity activity;
	private Group group;
	
	//Planning variables
	private Timestamp start;
	private Instructor instructor;
	
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

	@PlanningVariable(nullable = true, valueRangeProviderRefs = {"instructorRange"})
	public Instructor getInstructor() {
		return instructor;
	}
	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
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
        return activity.getName() + "-" + group.getLabel() + (isInstructorRequired() ? "-I" : "-N");
    }
	
    @Override
    public String toString() {
        return activity + "-" + group + (isInstructorRequired() ? "-I" : "");
    }
    		
	@ValueRangeProvider(id = "possibleStartRange")   
    List<Timestamp> getPossibleStartList(){
    	List<Timestamp> starts = new ArrayList<Timestamp>();
    	for(Timestamp timestamp : group.getGroupTimestampList()){
    		int hour = timestamp.getHour().getHourIndex();
    		if((hour >= 0 && hour <= 4) || (hour >= 10 && hour <= 16)){ //8-10, 13-16
    			starts.add(timestamp);
    		}
    	}
    	return starts;
    }
	
	public Day getDay(){
		return start.getDay();
	}
	public boolean isInstructorRequired(){
		return activity.isInstructorRequired();
	}
    
}
