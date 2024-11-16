package backend.academy.log.analyzer.service.render.error.impl;

import backend.academy.log.analyzer.service.render.error.ErrorRender;

public class TxtErrorRenderImpl implements ErrorRender {

    @Override
    public String render(String message) {
        return message;
    }
}
