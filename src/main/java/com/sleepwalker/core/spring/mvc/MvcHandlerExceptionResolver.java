package com.sleepwalker.core.spring.mvc;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.sleepwalker.core.spring.mvc.exception.DefaultExceptionHandler;

@Component
public class MvcHandlerExceptionResolver implements HandlerExceptionResolver {

    /** The default name of the exception attribute: "exception". */
    public static final String         DEFAULT_EXCEPTION_ATTRIBUTE = "exception";

    private Properties                 exceptionMappings;

    private String                     defaultErrorView;

    private Integer                    defaultStatusCode;

    private final Map<String, Integer> statusCodes                 = new HashMap<String, Integer>();

    private final String               exceptionAttribute          = DEFAULT_EXCEPTION_ATTRIBUTE;

    protected final Log                logger                      = LogFactory.getLog(this
                                                                       .getClass());

    @Autowired(required = false)
    private ExceptionHandler           exceptionHandler;

    private ExceptionHandler getExceptionHandler() {

        if (exceptionHandler == null) {
            logger.warn("None interface implements ExceptionHandler , use default one.");
            this.exceptionHandler = new DefaultExceptionHandler();
        }
        return this.exceptionHandler;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {

        StackTraceElement[] steArr = ex.getStackTrace();
        System.out.println(ex.getClass());
        for (int i = 0; i < 5; i++) {
            System.out.println(steArr[i].toString());
        }
        if (isAsyncRequest(request) || isAsyncRequest(handler)) {
            String msg = getExceptionHandler().resolveAsyncException(request, response, ex);
            try {
                response.setContentType("text/plain");
                response.getWriter().write(msg);
            } catch (IOException e) {
                logger.error("", e);
            }
            return new ModelAndView();
        } else {

            return getExceptionHandler().resolveSyncException(request, response, ex);
        }

    }

    protected boolean isAsyncRequest(Object handler) {

        if (handler == null) {
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        ResponseBody annotation = method.getAnnotation(ResponseBody.class);

        if (annotation != null) {
            return true;
        }
        return false;
    }

    protected boolean isAsyncRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        if (accept == null)
            accept = "";

        String xRequest = request.getHeader("X-Requested-With");
        if (xRequest == null)
            xRequest = "";

        if (accept.indexOf("application/json") > -1 || xRequest.indexOf("XMLHttpRequest") > -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determine the view name for the given exception, searching the {@link #setExceptionMappings "exceptionMappings"},
     * using the {@link #setDefaultErrorView "defaultErrorView"} as fallback.
     * @param ex the exception that got thrown during handler execution
     * @param request current HTTP request (useful for obtaining metadata)
     * @return the resolved view name, or <code>null</code> if none found
     */
    protected String determineViewName(Exception ex, HttpServletRequest request) {
        String viewName = null;
        // Check for specific exception mappings.
        if (this.exceptionMappings != null) {
            viewName = findMatchingViewName(this.exceptionMappings, ex);
        }
        // Return default error view else, if defined.
        if (viewName == null && this.defaultErrorView != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Resolving to default view '" + this.defaultErrorView
                             + "' for exception of type [" + ex.getClass().getName() + "]");
            }
            viewName = this.defaultErrorView;
        }
        return viewName;
    }

    /**
     * Find a matching view name in the given exception mappings.
     * @param exceptionMappings mappings between exception class names and error view names
     * @param ex the exception that got thrown during handler execution
     * @return the view name, or <code>null</code> if none found
     * @see #setExceptionMappings
     */
    protected String findMatchingViewName(Properties exceptionMappings, Exception ex) {
        String viewName = null;
        String dominantMapping = null;
        int deepest = Integer.MAX_VALUE;
        for (Enumeration<?> names = exceptionMappings.propertyNames(); names.hasMoreElements();) {
            String exceptionMapping = (String) names.nextElement();
            int depth = getDepth(exceptionMapping, ex);
            if (depth >= 0 && depth < deepest) {
                deepest = depth;
                dominantMapping = exceptionMapping;
                viewName = exceptionMappings.getProperty(exceptionMapping);
            }
        }
        if (viewName != null && logger.isDebugEnabled()) {
            logger.debug("Resolving to view '" + viewName + "' for exception of type ["
                         + ex.getClass().getName() + "], based on exception mapping ["
                         + dominantMapping + "]");
        }
        return viewName;
    }

    /**
     * Return the depth to the superclass matching.
     * <p>0 means ex matches exactly. Returns -1 if there's no match.
     * Otherwise, returns depth. Lowest depth wins.
     */
    protected int getDepth(String exceptionMapping, Exception ex) {
        return getDepth(exceptionMapping, ex.getClass(), 0);
    }

    private int getDepth(String exceptionMapping, Class<?> exceptionClass, int depth) {
        if (exceptionClass.getName().contains(exceptionMapping)) {
            // Found it!
            return depth;
        }
        // If we've gone as far as we can go and haven't found it...
        if (exceptionClass.equals(Throwable.class)) {
            return -1;
        }
        return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
    }

    /**
     * Determine the HTTP status code to apply for the given error view.
     * <p>The default implementation returns the status code for the given view name (specified through the
     * {@link #setStatusCodes(Properties) statusCodes} property), or falls back to the
     * {@link #setDefaultStatusCode defaultStatusCode} if there is no match.
     * <p>Override this in a custom subclass to customize this behavior.
     * @param request current HTTP request
     * @param viewName the name of the error view
     * @return the HTTP status code to use, or <code>null</code> for the servlet container's default
     * (200 in case of a standard error view)
     * @see #setDefaultStatusCode
     * @see #applyStatusCodeIfPossible
     */
    protected Integer determineStatusCode(HttpServletRequest request, String viewName) {
        if (this.statusCodes.containsKey(viewName)) {
            return this.statusCodes.get(viewName);
        }
        return this.defaultStatusCode;
    }

    /**
     * Apply the specified HTTP status code to the given response, if possible (that is,
     * if not executing within an include request).
     * @param request current HTTP request
     * @param response current HTTP response
     * @param statusCode the status code to apply
     * @see #determineStatusCode
     * @see #setDefaultStatusCode
     * @see HttpServletResponse#setStatus
     */
    protected void applyStatusCodeIfPossible(HttpServletRequest request,
                                             HttpServletResponse response, int statusCode) {
        if (!WebUtils.isIncludeRequest(request)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Applying HTTP status code " + statusCode);
            }
            response.setStatus(statusCode);
            request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, statusCode);
        }
    }

    /**
     * Return a ModelAndView for the given request, view name and exception.
     * <p>The default implementation delegates to {@link #getModelAndView(String, Exception)}.
     * @param viewName the name of the error view
     * @param ex the exception that got thrown during handler execution
     * @param request current HTTP request (useful for obtaining metadata)
     * @return the ModelAndView instance
     */
    protected ModelAndView getModelAndView(String viewName, Exception ex, HttpServletRequest request) {
        return getModelAndView(viewName, ex);
    }

    /**
     * Return a ModelAndView for the given view name and exception.
     * <p>The default implementation adds the specified exception attribute.
     * Can be overridden in subclasses.
     * @param viewName the name of the error view
     * @param ex the exception that got thrown during handler execution
     * @return the ModelAndView instance
     * @see #setExceptionAttribute
     */
    protected ModelAndView getModelAndView(String viewName, Exception ex) {
        ModelAndView mv = new ModelAndView(viewName);
        if (this.exceptionAttribute != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Exposing Exception as model attribute '" + this.exceptionAttribute
                             + "'");
            }
            mv.addObject(this.exceptionAttribute, ex);
        }
        return mv;
    }

}
