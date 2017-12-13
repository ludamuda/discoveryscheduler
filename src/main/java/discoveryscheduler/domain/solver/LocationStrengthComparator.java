package discoveryscheduler.domain.solver;

import discoveryscheduler.domain.Location;
import java.util.Comparator;
import org.apache.commons.lang3.builder.CompareToBuilder;

public class LocationStrengthComparator implements Comparator<Location> {

    public int compare(Location a, Location b) {
        return new CompareToBuilder()
        		.append(b.getTypeNumber(), a.getTypeNumber())
                .append(a.getId(), b.getId())
                .toComparison();
    }

}
