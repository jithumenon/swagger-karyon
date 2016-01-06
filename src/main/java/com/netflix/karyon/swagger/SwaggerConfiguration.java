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

package com.netflix.karyon.swagger;

import com.google.common.collect.ImmutableList;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Objects;

public class SwaggerConfiguration {

    private static final List<Scheme> DEFAULT_SCHEMES = ImmutableList.of(Scheme.HTTP);
    private static final String SWAGGER_ATTRIBUTE = "swagger";

    private final ServletContext servletContext;

    @Inject
    public SwaggerConfiguration(@Nonnull ServletContext servletContext) {
        this.servletContext = Objects.requireNonNull(servletContext, "servletContext");
    }

    @PostConstruct
    public void init() {
        configureSwaggerModel();
        initSwaggerBeanConfig();

    }

    private void configureSwaggerModel() {
        Swagger swagger = new Swagger()
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON);

        addResourceTagsTo(swagger);

        // This attribute is used by the subsequent BeanConfig to access the newly created swagger config
        servletContext.setAttribute(SWAGGER_ATTRIBUTE, swagger);

    }

    /**
     * Swagger uses Tags to group resources.
     * Create a tag for your resource here and add it to Swagger with optional description
     * Then, simply use the tag name in the @Api definition within your resource
     * @param swagger - the pre-populated swagger spec for your app
     */
    protected void addResourceTagsTo(@Nonnull Swagger swagger) {
        Objects.requireNonNull(swagger, "swagger");
        swagger.tag(new Tag().name("petstore").description("Petstore Operations"))
                .tag(new Tag().name("pet").description("Everything about your Pets"))
                .tag(new Tag().name("store").description("Access to Petstore orders"))
                .tag(new Tag().name("user").description("Operations about user"));
    }

    protected String getBasePath() {
        return "karyon";
    }

    protected String getHostname() {
        return "localhost:9090";

    }

    protected String getPackageNames() {
        return "io.swagger.sample.resource";
    }

    private void initSwaggerBeanConfig() {
        Objects.requireNonNull(servletContext.getAttribute(SWAGGER_ATTRIBUTE),
                "Swagger spec should be created and added to servletContext before creating BeanConfig");
        BeanConfig swaggerBeanConfig = new BeanConfig();
        swaggerBeanConfig.setVersion("1.0.0");
        swaggerBeanConfig.setTitle("Swagger Petstore");
        swaggerBeanConfig.setDescription("Example Pet Store app using Karyon, Jersey, Guice and Servlets");
        swaggerBeanConfig.setHost(getHostname());
        swaggerBeanConfig.setBasePath(getBasePath());
        swaggerBeanConfig.setSchemes(new String[]{"http"});
        swaggerBeanConfig.setResourcePackage(getPackageNames());
        swaggerBeanConfig.setScan(true);
    }
}
