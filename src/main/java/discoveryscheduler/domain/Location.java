package discoveryscheduler.domain;

import org.optaplanner.examples.common.domain.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Location")
public class Location extends AbstractPersistable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	private int locationIndex;
	private String type;

	public int getLocationIndex() {
		return locationIndex;
	}
	public void setLocationIndex(int locationIndex) {
		this.locationIndex = locationIndex;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	public String getLabel() {
        return locationIndex + " " + type;
    }

	@Override
	public String toString() {
	    return locationIndex + " " + type;
	}

}
