package discoveryscheduler.domain;

import org.optaplanner.examples.common.domain.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Hotel")
public class Hotel extends AbstractPersistable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;
	private int hotelIndex;
	
	public int getHotelIndex() {
		return hotelIndex;
	}

	public void setHotelIndex(int hotelIndex) {
		this.hotelIndex = hotelIndex;
	}

	public String getLabel() {
		return "Hotel " + hotelIndex;
	}
	
	@Override
    public String toString() {
        return "Hotel " + hotelIndex;
    }
}
