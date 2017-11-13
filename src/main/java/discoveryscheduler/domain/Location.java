package discoveryscheduler.domain;

import java.util.Map;

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
	private int typeNumber;
	
	private Map<Hotel, Boolean> locationHotelBusRequriemnet;
	private Map<Hotel, Integer> locationHotelPreference;

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
	public Map<Hotel, Boolean> getLocationHotelBusRequriemnet() {
		return locationHotelBusRequriemnet;
	}
	public void setLocationHotelBusRequriemnet(Map<Hotel, Boolean> locationHotelBusRequriemnet) {
		this.locationHotelBusRequriemnet = locationHotelBusRequriemnet;
	}
	public Map<Hotel, Integer> getLocationHotelPreference() {
		return locationHotelPreference;
	}
	public void setLocationHotelPreference(Map<Hotel, Integer> locationHotelPreference) {
		this.locationHotelPreference = locationHotelPreference;
	}
	public String getLabel() {
        return type + "-" + typeNumber;
    }

	@Override
	public String toString() {
	    return locationIndex + " " + type + "-" + typeNumber;
	}
	
	
	public Boolean isBusRequired(Hotel hotel){
		return locationHotelBusRequriemnet.get(hotel);
	}
	
	public Integer getLocationPreference(Hotel hotel){
		return locationHotelPreference.get(hotel);
	}

}
