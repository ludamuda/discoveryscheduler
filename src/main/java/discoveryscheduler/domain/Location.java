package discoveryscheduler.domain;

import org.optaplanner.examples.common.domain.AbstractPersistable;
import org.optaplanner.examples.common.swingui.components.Labeled;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Location")
public class Location extends AbstractPersistable implements Labeled{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	private int locationIndex;
	private String type;
	private int typeNumber;
	
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
	
	public int getTypeNumber() {
		return typeNumber;
	}
	public void setTypeNumber(int typeNumber) {
		this.typeNumber = typeNumber;
	}
	
	@Override
	public String getLabel() {
        return type + "-" + typeNumber;
    }

	@Override
	public String toString() {
	    return locationIndex + " " + type + "-" + typeNumber;
	}
}
