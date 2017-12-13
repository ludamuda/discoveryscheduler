package discoveryscheduler.domain.solver;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import discoveryscheduler.domain.Timestamp;;

public class TimestampStrengthComparator implements Comparator<Timestamp> {

    public int compare(Timestamp a, Timestamp b) {
        return new CompareToBuilder()
        		.append(a.getDay().getDayIndex(), b.getDay().getDayIndex())
        		.append(a.getHour().getHourIndex(), b.getHour().getHourIndex())
                .append(a.getId(), b.getId())
                .toComparison();
    }

}