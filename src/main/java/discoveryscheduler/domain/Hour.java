package discoveryscheduler.domain;

import java.util.Properties;

import org.optaplanner.examples.common.domain.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import discoveryscheduler.persistence.DiscoverySchedulerImportConfig;

@XStreamAlias("Hour")
public class Hour extends AbstractPersistable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private static final Properties configuration = DiscoverySchedulerImportConfig.getConfig();
	//private static final String[] HOURS = configuration.getProperty("time_periods").split(",");
	private static final String[] HOURS = {"08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19.30"};

    private int hourIndex;

	public int getHourIndex() {
		return hourIndex;
	}
	public void setHourIndex(int hourIndex) {
		this.hourIndex = hourIndex;
	}
	
	public String getLabel() {
        String time = HOURS[hourIndex % HOURS.length];
        if (hourIndex > HOURS.length) {
            return "HOURSlot " + hourIndex;
        }
        return time;
    }

    @Override
    public String toString() {
        return Integer.toString(hourIndex);
    }
	    
}
