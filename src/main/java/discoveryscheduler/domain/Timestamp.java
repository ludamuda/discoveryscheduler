package discoveryscheduler.domain;

import org.optaplanner.examples.common.domain.AbstractPersistable;
import org.optaplanner.examples.common.swingui.components.Labeled;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Timestamp")
public class Timestamp extends AbstractPersistable implements Labeled{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6L;

	int timestampIndex;
	
	private Day day;
	private Hour hour;
	
	public int getTimestampIndex() {
		return timestampIndex;
	}
	public void setTimestampIndex(int timestampIndex) {
		this.timestampIndex = timestampIndex;
	}
	public Day getDay() {
		return day;
	}
	public void setDay(Day day) {
		this.day = day;
	}
	public Hour getHour() {
		return hour;
	}
	public void setHour(Hour hour) {
		this.hour = hour;
	}
	
	/*public Timestamp plusHours(int hours){
		if(hour.getHourIndex() + hours > 13){//pocethodin ve dni
			day.setDayIndex(day.getDayIndex() + 1);
			day.setId(day.getId() + 1);
			hours = hour.getHourIndex() + hours - 13;
		}
		hour.setHourIndex(hour.getHourIndex() + hours);
		hour.setId(hour.getId() + hours);
		this.setTimestampIndex(this.getTimestampIndex() + hours);
		this.setId(this.getId() + hours);
		return this;
	}*/
	
	public String getLabel() {
        return day.getLabel() + " " + hour.getLabel();
    }

    @Override
    public String toString() {
        return day + "-" + hour;
    }

}
