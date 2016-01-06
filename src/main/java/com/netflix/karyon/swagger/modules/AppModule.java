/*
 * Copyright 2016 Jithu Menon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.karyon.swagger.modules;

import com.google.common.collect.Maps;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import io.swagger.sample.resource.RootResource;
import com.netflix.karyon.swagger.SwaggerConfiguration;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import io.swagger.sample.resource.SampleExceptionMapper;
import io.swagger.sample.util.ApiOriginCorsFilter;

import java.util.Map;

public class AppModule extends ServletModule {

    protected void configureServlets() {
        bind(RootResource.class).asEagerSingleton();
        bind(SampleExceptionMapper.class).asEagerSingleton();

        bind(SwaggerConfiguration.class).toInstance(new SwaggerConfiguration(getServletContext()));

        bind(GuiceContainer.class).asEagerSingleton();
        filter("/*").through(ApiOriginCorsFilter.class);
        // Filtering through GuiceContainer triggers previously raised issue #1068
        //filter("/*").through(GuiceContainer.class, createServletParams());
        // Till we fix #1068, using a Servlet is the only option to make Swagger play nicely
        serve("/*").with(GuiceContainer.class, createServletParams());

    }

    private Map<String, String> createServletParams() {
        Map<String, String> servletParams = Maps.newHashMapWithExpectedSize(2);
        // For static resources like HTML, JS, or CSS that should get served by the servlet container
        // Note that this is only effective when we are using a filter to handle requests - can't do that till we fix # 1068
        servletParams.put(ServletContainer.PROPERTY_WEB_PAGE_CONTENT_REGEX, "/(docs|js)/.*");
        // Need this to avoid issue reference here: http://stackoverflow.com/questions/15767973/jersey-what-does-couldnt-find-grammar-element-mean
        servletParams.put("com.sun.jersey.config.feature.DisableWADL", "true");
        return servletParams;

    }
}
