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
		
	@Override
	public String getLabel() {
        return day.getLabel() + " " + hour.getLabel();
    }

    @Override
    public String toString() {
        return day + "-" + hour;
    }

}
