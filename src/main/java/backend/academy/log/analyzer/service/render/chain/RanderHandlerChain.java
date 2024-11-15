package backend.academy.log.analyzer.service.render.chain;

import backend.academy.log.analyzer.model.RanderRequest;
import backend.academy.log.analyzer.service.render.ReportRander;
import java.util.Optional;

public interface RanderHandlerChain {
    RanderHandlerChain addNext(RanderHandlerChain link);

    Optional<ReportRander> handle(RanderRequest request);
}
