package discoveryscheduler.solver.move;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import discoveryscheduler.domain.Task;

public class DifferentTaskSwapMoveFilter implements SelectionFilter<SwapMove> {

    public boolean accept(ScoreDirector scoreDirector, SwapMove move) {
        Task leftTask = (Task) move.getLeftEntity();
        Task rightTask = (Task) move.getRightEntity();
        return leftTask.getGroup().equals(rightTask.getGroup());
    }

}