package discoveryscheduler.domain.solver;

import discoveryscheduler.domain.Instructor;
import java.util.Comparator;
import org.apache.commons.lang3.builder.CompareToBuilder;

public class InstructorStrengthComparator  implements Comparator<Instructor> {

    public int compare(Instructor a, Instructor b) {
        return new CompareToBuilder()
                .append(a.getId(), b.getId())
                .toComparison();
    }

}
