package discoveryscheduler.domain;

import org.optaplanner.examples.common.domain.AbstractPersistable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Activity")
public class Activity extends AbstractPersistable{
	
	private String name;
	private int length;
	private boolean instructorRequired;
	private boolean locationRequired; 
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	@Override
	public String toString() {
	    return name;
	}

}
