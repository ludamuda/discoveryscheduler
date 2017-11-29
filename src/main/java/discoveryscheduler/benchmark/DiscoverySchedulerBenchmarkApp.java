package discoveryscheduler.benchmark;

import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;

public class DiscoverySchedulerBenchmarkApp {
	
	public static void main(String[] args) {
        // Build the PlannerBenchmark
        PlannerBenchmarkFactory plannerBenchmarkFactory = PlannerBenchmarkFactory.createFromXmlResource(
                "discoveryscheduler/benchmark/discoverySchedulerBenchmarkConfig.xml");
        PlannerBenchmark plannerBenchmark = plannerBenchmarkFactory.buildPlannerBenchmark();

        // Benchmark the problem
        plannerBenchmark.benchmark();

        // Show the benchmark report
        System.out.println("\nPlease open the benchmark report in:  \n"
                + plannerBenchmarkFactory.getPlannerBenchmarkConfig().getBenchmarkDirectory().getAbsolutePath());
    }

}
