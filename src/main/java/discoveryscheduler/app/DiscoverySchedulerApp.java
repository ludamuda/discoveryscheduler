package discoveryscheduler.app;

//import org.optaplanner.core.api.solver.Solver;
//import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.examples.common.app.CommonApp;
import org.optaplanner.examples.common.persistence.AbstractSolutionImporter;
import org.optaplanner.examples.common.persistence.SolutionDao;
//import org.optaplanner.examples.common.swingui.SolutionPanel;

import discoveryscheduler.domain.Week;
import discoveryscheduler.persistence.DiscoverySchedulerDao;
import discoveryscheduler.persistence.DiscoverySchedulerImportConfig;
import discoveryscheduler.persistence.DiscoverySchedulerImporter;
import discoveryscheduler.swingui.DiscoverySchedulerPanel;

public class DiscoverySchedulerApp extends CommonApp<Week> {
	
	public static final String SOLVER_CONFIG 
		= "discoveryscheduler/solver/discoverySchedulerSolverConfig.xml";
	public static void main(String[] args) {
		/**
		 * Creates and loads solutionBussines - solver and data
		 * Creates solutionAndPersistenceFrame - the look of the app 
		 */
		prepareSwingEnvironment();
		new DiscoverySchedulerImportConfig().loadConfig();
        new DiscoverySchedulerApp().init();
	}
	
	/**
	 * Constructor for the application, uses the CommonApp constructor
	 */
	public DiscoverySchedulerApp() {
        super("Discovery program planner",
              "Master thesis scheduler for Outdoor Discovery s.r.o. company.",
              SOLVER_CONFIG,
              "discoveryscheduler/swingui/logo.png"); 
    }
	
	/**
	 * Creates solver for the solutionBussines, solverFactory must be provided with configuration of solver
	 * 		can be done with xml file or via programming
	 */
	/*@Override
    protected Solver<Week> createSolver() {
        SolverFactory<Week> solverFactory = SolverFactory.createFromXmlResource("discoveryscheduler/solver/discoverySchedulerSolverConfig.xml");
        return solverFactory.buildSolver();
    }*/
	
	/**
	 * References the method that handles visualization of solving process in app GUI
	 */
    @Override
    protected /*SolutionPanel*/ DiscoverySchedulerPanel createSolutionPanel() {
        return new DiscoverySchedulerPanel();
    }
    /**
     * References the method creates solution with the domain attributes of @PlanningSolution(week) which can then be worked with 
     * 		loaded with data, changed by solver...
     */
    @SuppressWarnings("rawtypes")
	@Override
    protected SolutionDao createSolutionDao() {
        return new DiscoverySchedulerDao();
    }
    /**
     * Array of importers from different file types
     */
    @SuppressWarnings("rawtypes")
	@Override
    protected AbstractSolutionImporter[] createSolutionImporters() {
        return new AbstractSolutionImporter[]{
                new DiscoverySchedulerImporter()
        };
    }
}
