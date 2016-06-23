package discoveryscheduler.persistence;

import org.optaplanner.examples.common.persistence.XStreamSolutionDao;

import discoveryscheduler.domain.Week;

public class DiscoverySchedulerDao extends XStreamSolutionDao {
	public DiscoverySchedulerDao() {
        super("discoveryscheduler", Week.class);
    }
}
