package com.sleepwalker.core;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.view.ViewToolContext;
import org.springframework.web.servlet.view.velocity.VelocityLayoutView;

public class VelocityToolbox20View extends VelocityLayoutView {

    @Override
    protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {
        ViewToolContext ctx = new ViewToolContext(getVelocityEngine(), request, response,
            getServletContext());

        ctx.putAll(model);

        if (getToolboxConfigLocation() != null) {
            ToolManager tm = new ToolManager();
            tm.setVelocityEngine(getVelocityEngine());
            tm.configure(getServletContext().getRealPath(getToolboxConfigLocation()));
            if (tm.getToolboxFactory().hasTools("request")) {
                ctx.addToolbox(tm.getToolboxFactory().createToolbox("request"));
            }
            if (tm.getToolboxFactory().hasTools("application")) {
                ctx.addToolbox(tm.getToolboxFactory().createToolbox("application"));
            }
            if (tm.getToolboxFactory().hasTools("session")) {
                ctx.addToolbox(tm.getToolboxFactory().createToolbox("session"));
            }
        }
        return ctx;
    }
}