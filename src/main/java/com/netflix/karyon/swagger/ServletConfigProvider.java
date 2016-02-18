package com.netflix.karyon.swagger;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

import javax.servlet.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

/**
 * Created by rbolles on 2/17/16.
 *
 * A hack to allow Swagger, Jersey, and Guice/Karyon to play nice together. (https://github.com/swagger-api/swagger-core/issues/1619)
 *
 * It is common practice at Netflix to expose your JAX-RS Resources via filter and GuiceContainer.
 *
 * Without any intervention, Jersey will blow up with a cryptic message saying something to the effect:
 *   SEVERE: Missing dependency for method public javax.ws.rs.core.Response io.swagger.jaxrs.listing.ApiListingResource.getListing(javax.ws.rs.core.Application,javax.servlet.ServletConfig,javax.ws.rs.core.HttpHeaders,javax.ws.rs.core.UriInfo,java.lang.String) at parameter at index 1
 *
 * This is error is due to the fact that when your expose your JAX-RS resources via filter, there is no ServletConfig object
 * to inject into Swagger's ApiListingResource. Jersey blows up and prevents your app from starting up.
 *
 * A careful inspection of the Swagger code path, starting at ApiListingResource.java, does not actually require a ServletConfig
 * object in order to expose the /swagger.json endpoint. However, Swagger does allow you to plug in your own implementation of certain
 * Swagger classes (e.g. JaxrsScanner). As such, the Swagger maintainers were hesitant to remove ServletConfig references from their codebase.
 *
 * This class, when registered with Jersey (either directly or indirectly via Guice) provides a "dummy" ServletConfig
 * that Jersey can inject and go along on its merry way.
 *
 */
@Provider
public class ServletConfigProvider extends AbstractHttpContextInjectable<ServletConfig> implements InjectableProvider<Context, java.lang.reflect.Type> {
    @Override
    public ServletConfig getValue(HttpContext c) {
        return new ServletConfig() {

            @Context
            ServletContext servletContext;

            @Override
            public String getServletName() {
                return null;
            }

            @Override
            public ServletContext getServletContext() {
                return new ServletContext() {
                    @Override
                    public String getContextPath() {
                        return null;
                    }

                    @Override
                    public ServletContext getContext(String uripath) {
                        return null;
                    }

                    @Override
                    public int getMajorVersion() {
                        return 0;
                    }

                    @Override
                    public int getMinorVersion() {
                        return 0;
                    }

                    @Override
                    public String getMimeType(String file) {
                        return null;
                    }

                    @Override
                    public Set getResourcePaths(String path) {
                        return null;
                    }

                    @Override
                    public URL getResource(String path) throws MalformedURLException {
                        return null;
                    }

                    @Override
                    public InputStream getResourceAsStream(String path) {
                        return null;
                    }

                    @Override
                    public RequestDispatcher getRequestDispatcher(String path) {
                        return null;
                    }

                    @Override
                    public RequestDispatcher getNamedDispatcher(String name) {
                        return null;
                    }

                    @Override
                    public Servlet getServlet(String name) throws ServletException {
                        return null;
                    }

                    @Override
                    public Enumeration getServlets() {
                        return null;
                    }

                    @Override
                    public Enumeration getServletNames() {
                        return null;
                    }

                    @Override
                    public void log(String msg) {

                    }

                    @Override
                    public void log(Exception exception, String msg) {

                    }

                    @Override
                    public void log(String message, Throwable throwable) {

                    }

                    @Override
                    public String getRealPath(String path) {
                        return null;
                    }

                    @Override
                    public String getServerInfo() {
                        return null;
                    }

                    @Override
                    public String getInitParameter(String name) {
                        return null;
                    }

                    @Override
                    public Enumeration getInitParameterNames() {
                        return null;
                    }

                    @Override
                    public Object getAttribute(String name) {
                        return null;
                    }

                    @Override
                    public Enumeration getAttributeNames() {
                        return null;
                    }

                    @Override
                    public void setAttribute(String name, Object object) {

                    }

                    @Override
                    public void removeAttribute(String name) {

                    }

                    @Override
                    public String getServletContextName() {
                        return null;
                    }

                    @Override
                    public String toString() {
                        return "DUMMY SERVLET CONTEXT TO GET SWAGGER WORKING WITH KARYON";
                    }
                };
            }

            @Override
            public String getInitParameter(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getInitParameterNames() {
                return null;
            }

            @Override
            public String toString() {
                return "DUMMY SERVLET CONFIG TO GET SWAGGER WORKING WITH KARYON";
            }
        };
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.Singleton;
    }

    @Override
    public Injectable getInjectable(ComponentContext ic, Context context, Type type) {
        if (type.equals(ServletConfig.class)) {
            return this;
        }

        return null;
    }
}
