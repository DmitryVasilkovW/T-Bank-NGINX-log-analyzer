package backend.academy.log.analyzer.service.render.chain.impl;

import backend.academy.log.analyzer.model.RanderRequest;
import backend.academy.log.analyzer.service.render.ReportRander;
import backend.academy.log.analyzer.service.render.chain.RanderHandlerChain;
import java.util.Optional;

public abstract class RanderHandlerChainImpl implements RanderHandlerChain {
    protected RanderHandlerChain next;

    @Override
    public RanderHandlerChain addNext(RanderHandlerChain link) {
        if (next == null) {
            next = link;
        } else {
            next.addNext(link);
        }

        return this;
    }

    public abstract Optional<ReportRander> handle(RanderRequest request);
}
