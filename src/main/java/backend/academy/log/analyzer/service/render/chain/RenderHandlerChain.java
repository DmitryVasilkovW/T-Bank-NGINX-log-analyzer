package backend.academy.log.analyzer.service.render.chain;

import backend.academy.log.analyzer.model.RenderRequest;
import backend.academy.log.analyzer.service.render.ReportRender;
import java.util.Optional;

public interface RenderHandlerChain {
    RenderHandlerChain addNext(RenderHandlerChain link);

    Optional<ReportRender> handle(RenderRequest request);
}
