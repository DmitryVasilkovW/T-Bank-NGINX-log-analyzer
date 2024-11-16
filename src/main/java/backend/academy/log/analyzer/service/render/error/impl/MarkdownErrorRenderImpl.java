package backend.academy.log.analyzer.service.render.error.impl;

import backend.academy.log.analyzer.service.render.error.ErrorRender;

public class MarkdownErrorRenderImpl implements ErrorRender {
    private static final String ERROR_FORMAT = "**Error:** ";

    @Override
    public String render(String message) {
        return ERROR_FORMAT + message;
    }
}
