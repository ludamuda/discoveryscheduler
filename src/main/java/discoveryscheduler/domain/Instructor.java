package discoveryscheduler.domain;

import org.optaplanner.examples.common.domain.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Instructor")
public class Instructor extends AbstractPersistable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	private int instructorIndex;
	private boolean isGuide;
	
	private int instructorSoftScore;

	public int getInstructorSoftScore() {
		return instructorSoftScore;
	}
	public void setInstructorSoftScore(int instructorSoftScore) {
		this.instructorSoftScore = instructorSoftScore;
	}
	public int getInstructorIndex() {
		return instructorIndex;
	}
	public void setInstructorIndex(int instructorIndex) {
		this.instructorIndex = instructorIndex;
	}
	public boolean isGuide() {
		return isGuide;
	}
	public void setGuide(boolean isGuide) {
		this.isGuide = isGuide;
	}
	
	
	public String getLabel() {
        return "Instructor " + instructorIndex;
    }

    @Override
    public String toString() {
    	return "Instructor " + instructorIndex;
    }

}
