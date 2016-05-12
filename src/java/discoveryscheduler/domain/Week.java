package discoveryscheduler.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
//import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.impl.score.buildin.hardsoft.HardSoftScoreDefinition;
//import org.optaplanner.core.impl.score.buildin.simple.SimpleScoreDefinition;
import org.optaplanner.examples.common.domain.AbstractPersistable;

import org.optaplanner.persistence.xstream.impl.score.XStreamScoreConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;

//import discoveryscheduler.domain.solver.ActivityConflict;

@PlanningSolution
@XStreamAlias("Week")
public class Week extends AbstractPersistable implements Solution<HardSoftScore>/*Solution<SimpleScore>*/{
	
	private int number;	
	
	private List<Day> dayList;
	private List<Hour> hourList;
	private List<Timestamp> timestampList;
	private List<Activity> activityList;	 
	private List<Instructor> instructorList;	
	private List<Group> groupList;
	
	private List<Task> taskList;
		
	//@XStreamConverter(value = XStreamScoreConverter.class, types = {SimpleScoreDefinition.class})
	//private SimpleScore score;
	
	@XStreamConverter(value = XStreamScoreConverter.class, types = {HardSoftScoreDefinition.class})
	private HardSoftScore score;

	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public List<Group> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}	
	public List<Day> getDayList() {
		return dayList;
	}
	public void setDayList(List<Day> dayList) {
		this.dayList = dayList;
	}
	 public List<Hour> getHourList() {
		return hourList;
	}
	public void setHourList(List<Hour> hourList) {
		this.hourList = hourList;
	}
	@ValueRangeProvider(id = "timestampRange")
	public List<Timestamp> getTimestampList() {
		return timestampList;
	}
	public void setTimestampList(List<Timestamp> timestampList) {
		this.timestampList = timestampList;
	}
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
    public List<Instructor> getInstructorList() {
		return instructorList;
	}
	public void setInstructorList(List<Instructor> instructorList) {
		this.instructorList = instructorList;
	}
	public HardSoftScore getScore() {
		return score;
	}
	public void setScore(HardSoftScore score) {
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
        

        
        // Do not add the planning entity's (processList) because that will be done automatically
        return facts;
    }
}
