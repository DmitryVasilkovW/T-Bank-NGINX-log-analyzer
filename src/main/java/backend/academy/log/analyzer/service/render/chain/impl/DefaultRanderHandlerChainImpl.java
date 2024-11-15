package backend.academy.log.analyzer.service.render.chain.impl;

import backend.academy.log.analyzer.model.RanderRequest;
import backend.academy.log.analyzer.service.render.ReportRander;
import java.util.Optional;

public class DefaultRanderHandlerChainImpl extends RanderHandlerChainImpl {
    @Override
    public Optional<ReportRander> handle(RanderRequest request) {
        return Optional.empty();
    }
}
