package discoveryscheduler.domain;

import org.optaplanner.examples.common.domain.AbstractPersistable;
import org.optaplanner.examples.common.swingui.components.Labeled;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Instructor")
public class Instructor extends AbstractPersistable implements Labeled{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	private int instructorIndex;

	public int getInstructorIndex() {
		return instructorIndex;
	}
	public void setInstructorIndex(int instructorIndex) {
		this.instructorIndex = instructorIndex;
	}
	
	@Override
	public String getLabel() {
        return "Instructor " + instructorIndex;
    }

    @Override
    public String toString() {
    	return "Instructor " + instructorIndex;
    }

}
