package discoveryscheduler.benchmark;

import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;

import discoveryscheduler.persistence.DiscoverySchedulerImportConfig;

public class DiscoverySchedulerBenchmarkApp {
	
	public static void main(String[] args) {
		
		new DiscoverySchedulerImportConfig().loadConfig();
		
        // Build the PlannerBenchmark
        PlannerBenchmarkFactory plannerBenchmarkFactory = PlannerBenchmarkFactory.createFromXmlResource(
                "discoveryscheduler/benchmark/discoverySchedulerBenchmarkConfigSAconst.xml");
        PlannerBenchmark plannerBenchmark = plannerBenchmarkFactory.buildPlannerBenchmark();

        // Benchmark the problem
        plannerBenchmark.benchmark();

        // Show the benchmark report
        System.out.println("\nPlease open the benchmark report in:  \n"
                + plannerBenchmarkFactory.getPlannerBenchmarkConfig().getBenchmarkDirectory().getAbsolutePath());
    }

}
