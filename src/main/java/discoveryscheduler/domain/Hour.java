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
	private static final String[] HOURS = configuration.getProperty("time_periods").split(",");

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
