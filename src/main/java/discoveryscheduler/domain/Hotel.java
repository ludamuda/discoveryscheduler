package discoveryscheduler.domain;

import java.util.Map;

import org.optaplanner.examples.common.domain.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("Hotel")
public class Hotel extends AbstractPersistable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;
	private int hotelIndex;
	
	private Map<Location, Boolean> hotelLocationBusRequirement;
	private Map<Activity, Boolean> hotelActivityBusRequirement;
	private Map<Location, Integer> hotelLocationPenalty;
	
	public int getHotelIndex() {
		return hotelIndex;
	}

	public void setHotelIndex(int hotelIndex) {
		this.hotelIndex = hotelIndex;
	}

	public Map<Location, Boolean> getHotelLocationBusRequirement() {
		return hotelLocationBusRequirement;
	}

	public void setHotelLocationBusRequirement(Map<Location, Boolean> hotelLocationBusRequirement) {
		this.hotelLocationBusRequirement = hotelLocationBusRequirement;
	}

	public Map<Activity, Boolean> getHotelActivityBusRequirement() {
		return hotelActivityBusRequirement;
	}

	public void setHotelActivityBusRequirement(Map<Activity, Boolean> hotelActivityBusRequirement) {
		this.hotelActivityBusRequirement = hotelActivityBusRequirement;
	}

	public Map<Location, Integer> getHotelLocationPenalty() {
		return hotelLocationPenalty;
	}

	public void setHotelLocationPenalty(Map<Location, Integer> hotelLocationPenalty) {
		this.hotelLocationPenalty = hotelLocationPenalty;
	}

	public String getLabel() {
		return "Hotel " + hotelIndex;
	}
	
	@Override
    public String toString() {
        return "Hotel " + hotelIndex;
    }
	
	public Boolean isBusRequired(Location location){
		return hotelLocationBusRequirement.get(location);
	}
	public Boolean isBusRequired(Activity activity){
		return hotelActivityBusRequirement.get(activity);
	}

	
	public Integer getLocationPenalty(Location location){
		return hotelLocationPenalty.get(location);
	}
}
