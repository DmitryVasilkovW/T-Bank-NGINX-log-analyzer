package backend.academy.log.analyzer.service.render.chain.impl;

import backend.academy.log.analyzer.model.RenderRequest;
import backend.academy.log.analyzer.service.render.ReportRender;
import backend.academy.log.analyzer.service.render.chain.RenderHandlerChain;
import java.util.Optional;

public abstract class RenderHandlerChainImpl implements RenderHandlerChain {
    protected RenderHandlerChain next;

    @Override
    public RenderHandlerChain addNext(RenderHandlerChain link) {
        if (next == null) {
            next = link;
        } else {
            next.addNext(link);
        }

        return this;
    }

    public abstract Optional<ReportRender> handle(RenderRequest request);
}
