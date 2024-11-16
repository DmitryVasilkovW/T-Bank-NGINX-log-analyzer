package backend.academy.log.analyzer.service.render.error.chain;

import backend.academy.log.analyzer.model.ErrorRequest;
import backend.academy.log.analyzer.service.render.error.ErrorRender;

public interface ErrorRenderHandlerChain {
    ErrorRenderHandlerChain addNext(ErrorRenderHandlerChain link);

    ErrorRender handle(ErrorRequest request);
}
