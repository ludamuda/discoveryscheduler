package discoveryscheduler.domain;

import org.optaplanner.examples.common.domain.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import discoveryscheduler.persistence.DiscoverySchedulerImportConfig;

import java.util.List;
import java.util.Properties;

@SuppressWarnings("serial")
@XStreamAlias("Day")
public class Day extends AbstractPersistable{
	/**
	 * 
	 */
	//private static final long serialVersionUID = 7L;

	@SuppressWarnings("unused")
	private static final Properties configuration = DiscoverySchedulerImportConfig.getConfig();
	//private static final String[] DAYS = configuration.getProperty("week_days").split(",");
	private static final String[] DAYS = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    private int dayIndex;
    private List<Timestamp> timestampList;

	public int getDayIndex() {
		return dayIndex;
	}
	public void setDayIndex(int dayIndex) {
		this.dayIndex = dayIndex;
	}
	public List<Timestamp> getTimestampList() {
		return timestampList;
	}
	public void setTimestampList(List<Timestamp> timestampList) {
		this.timestampList = timestampList;
	}

	public String getLabel() {
        String day = DAYS[dayIndex % DAYS.length];
        if (dayIndex > DAYS.length) {
            return "Day " + dayIndex;
        }
        return day;
    }

    @Override
    public String toString() {
        return Integer.toString(dayIndex);
    }
    
}
