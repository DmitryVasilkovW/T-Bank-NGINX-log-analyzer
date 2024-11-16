package backend.academy.log.analyzer.service.render.chain;

import backend.academy.log.analyzer.model.RanderRequest;
import backend.academy.log.analyzer.service.render.ReportRender;
import java.util.Optional;

public interface RenderHandlerChain {
    RenderHandlerChain addNext(RenderHandlerChain link);

    Optional<ReportRender> handle(RanderRequest request);
}
