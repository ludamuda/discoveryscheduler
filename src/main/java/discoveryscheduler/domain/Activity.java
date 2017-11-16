package discoveryscheduler.domain;

import java.util.List;

import org.optaplanner.examples.common.domain.AbstractPersistable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Activity")
public class Activity extends AbstractPersistable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9L;
	private int activityIndex;
	private String type;
	private int length;
	private boolean instructorRequired;
	private boolean locationRequired; 
	private List<Location> possibleLocations;
	

	public int getActivityIndex() {
		return activityIndex;
	}
	public void setActivityIndex(int activityIndex) {
		this.activityIndex = activityIndex;
	}
	public String getType() {
		return type;
	}
	public void setType(String name) {
		this.type = name;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
	public boolean isInstructorRequired() {
		return instructorRequired;
	}
	public void setInstructorRequired(boolean instructorRequired) {
		this.instructorRequired = instructorRequired;
	}
	
	public boolean isLocationRequired() {
		return locationRequired;
	}
	public void setLocationRequired(boolean locationRequired) {
		this.locationRequired = locationRequired;
	}
	public List<Location> getPossibleLocations() {
		return possibleLocations;
	}
	public void setPossibleLocations(List<Location> possibleLocations) {
		this.possibleLocations = possibleLocations;
	}
	@Override
	public String toString() {
	    return type;
	}

}
