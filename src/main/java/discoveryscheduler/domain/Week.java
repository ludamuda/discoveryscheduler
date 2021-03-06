package discoveryscheduler.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
//import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
//import org.optaplanner.core.impl.score.buildin.simple.SimpleScoreDefinition;
import org.optaplanner.examples.common.domain.AbstractPersistable;
//import org.optaplanner.persistence.xstream.api.score.buildin.hardsoft.HardSoftScoreXStreamConverter;
import org.optaplanner.persistence.xstream.api.score.buildin.hardmediumsoft.HardMediumSoftScoreXStreamConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;

@PlanningSolution
@XStreamAlias("Week")
public class Week extends AbstractPersistable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int number;	
	
	private List<Day> dayList;
	private List<Hour> hourList;
	private List<Timestamp> timestampList;
	private List<Activity> activityList;	 
	private List<Instructor> instructorList;
	private List<Location> locationList;
	private List<Group> groupList;
	private List<Hotel> hotelList;
	
	private List<Task> taskList;
	
	@XStreamConverter(HardMediumSoftScoreXStreamConverter.class)
	private HardMediumSoftScore score;

	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	@ProblemFactCollectionProperty
	public List<Group> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}	
	@ProblemFactCollectionProperty
	public List<Day> getDayList() {
		return dayList;
	}
	public void setDayList(List<Day> dayList) {
		this.dayList = dayList;
	}
	@ProblemFactCollectionProperty
	 public List<Hour> getHourList() {
		return hourList;
	}
	public void setHourList(List<Hour> hourList) {
		this.hourList = hourList;
	}
	@ValueRangeProvider(id = "timestampRange")
	@ProblemFactCollectionProperty
	public List<Timestamp> getTimestampList() {
		return timestampList;
	}
	public void setTimestampList(List<Timestamp> timestampList) {
		this.timestampList = timestampList;
	}
	@ProblemFactCollectionProperty
	public List<Activity> getActivityList() {
		return activityList;
	}
	public void setActivityList(List<Activity> activityList) {
		this.activityList = activityList;
	}
	@PlanningEntityCollectionProperty
	public List<Task> getTaskList() {
		return taskList;
	}
	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}	
	@ValueRangeProvider(id = "instructorRange")
	@ProblemFactCollectionProperty
    public List<Instructor> getInstructorList() {
		return instructorList;
	}
	public void setInstructorList(List<Instructor> instructorList) {
		this.instructorList = instructorList;
	}
	@ValueRangeProvider(id = "locationRange")
	@ProblemFactCollectionProperty
	public List<Location> getLocationList() {
		return locationList;
	}
	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}
	@ProblemFactCollectionProperty
	public List<Hotel> getHotelList() {
		return hotelList;
	}
	public void setHotelList(List<Hotel> hotelList) {
		this.hotelList = hotelList;
	}
	@PlanningScore
	public HardMediumSoftScore getScore() {
		return score;
	}
	public void setScore(HardMediumSoftScore score) {
		this.score = score;
	}

	//Required for drools score calculation
    public Collection<? extends Object> getProblemFacts() {
        List<Object> facts = new ArrayList<Object>();
        facts.addAll(dayList);
        facts.addAll(hourList);
        facts.addAll(timestampList);
        facts.addAll(activityList);
        facts.addAll(groupList);
        facts.addAll(instructorList);
        facts.addAll(locationList);
        facts.addAll(hotelList);
        
        // Do not add the planning entity's (taskList) because that will be done automatically
        return facts;
    }
}
