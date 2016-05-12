package discoveryscheduler.domain;

import java.util.List;


import org.optaplanner.examples.common.domain.AbstractPersistable;
import org.optaplanner.examples.common.swingui.components.Labeled;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("Group")
public class Group extends AbstractPersistable implements Labeled {
	private String name;
	private List<Activity> requiredActivities;
	private List<Timestamp> groupTimestampList;
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	public List<Activity> getRequiredActivities() {
		return requiredActivities;
	}
	public void setRequiredActivities(List<Activity> requiredActivities) {
		this.requiredActivities = requiredActivities;
	}
	public List<Timestamp> getGroupTimestampList() {
		return groupTimestampList;
	}
	public void setGroupTimestampList(List<Timestamp> groupTimestampList) {
		this.groupTimestampList = groupTimestampList;
	}
	
	
	public String getLabel() {
		return name;
	}
	
	@Override
    public String toString() {
        return name;
    }
	
	
}
