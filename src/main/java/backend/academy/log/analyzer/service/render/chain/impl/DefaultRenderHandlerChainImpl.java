package backend.academy.log.analyzer.service.render.chain.impl;

import backend.academy.log.analyzer.model.RanderRequest;
import backend.academy.log.analyzer.service.render.ReportRender;
import java.util.Optional;

public class DefaultRenderHandlerChainImpl extends RenderHandlerChainImpl {
    @Override
    public Optional<ReportRender> handle(RanderRequest request) {
        return Optional.empty();
    }
}
