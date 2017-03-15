package com.sleepwalker.core.spring.mvc;

import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;
import org.springframework.web.servlet.view.velocity.VelocityView;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

/**
 * 为VelocityViewResolver添加Toolbox开关
 * @version $Id: MvcVelocityViewResolver.java, v 0.1 2013-5-30 下午4:23:27 Nero Exp $
 */
public class MvcVelocityViewResolver extends VelocityViewResolver {

    private String toolbox;

    private String dateToolAttribute;

    private String numberToolAttribute;

    private String toolboxConfigLocation;

    public void setToolbox(String toolbox) {
        this.toolbox = toolbox;
    }

    @Override
    public void setDateToolAttribute(String dateToolAttribute) {
        this.dateToolAttribute = dateToolAttribute;
    }

    @Override
    public void setNumberToolAttribute(String numberToolAttribute) {
        this.numberToolAttribute = numberToolAttribute;
    }

    @Override
    public void setToolboxConfigLocation(String toolboxConfigLocation) {
        this.toolboxConfigLocation = toolboxConfigLocation;
    }

    @Override
    protected void initApplicationContext() {
        super.initApplicationContext();

        if (this.toolbox.equals("true") && this.toolboxConfigLocation != null) {
            if (VelocityView.class.equals(getViewClass())) {
                logger.info("Using VelocityToolboxView instead of default VelocityView "
                            + "due to specified toolboxConfigLocation");
                setViewClass(VelocityToolboxView.class);
            } else if (!VelocityToolboxView.class.isAssignableFrom(getViewClass())) {
                throw new IllegalArgumentException(
                    "Given view class ["
                            + getViewClass().getName()
                            + "] is not of type ["
                            + VelocityToolboxView.class.getName()
                            + "], which it needs to be in case of a specified toolboxConfigLocation");
            }
        }
    }

    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        VelocityView view = (VelocityView) super.buildView(viewName);
        view.setDateToolAttribute(this.dateToolAttribute);
        view.setNumberToolAttribute(this.numberToolAttribute);
        if (this.toolbox.equals("true") && this.toolboxConfigLocation != null) {
            ((VelocityToolboxView) view).setToolboxConfigLocation(this.toolboxConfigLocation);
        }
        return view;
    }
}
