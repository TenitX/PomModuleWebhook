package com.tenitx;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.tenitx.connect.http.ApiRequest;
import com.tenitx.connect.http.ExternalApiCaller;
import com.tenitx.connect.http.ResponseWithBody;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Goal which sends a webhook notifying of a pom update.
 *
 */
@Mojo( name = "updateWebhook", defaultPhase = LifecyclePhase.DEPLOY )
public class PomUpdateWebhookMojo
    extends AbstractMojo
{
    private static final Logger LOG = LoggerFactory.getLogger(PomUpdateWebhookMojo.class);
    private final ExternalApiCaller externalApiCaller = new ExternalApiCaller();
    /**
     * Where to send the webhook.
     */
    @Parameter( defaultValue = "", property = "webhookUrl", required = true )
    private String webhookUrl;

    @Parameter(defaultValue = "${project.artifactId}")
    private String artifactId;

    public void execute()
        throws MojoExecutionException
    {
        LOG.info("Running PomUpdateWebhook for artifact {}", artifactId);
        if (webhookUrl.isEmpty()) {
            LOG.warn("Skipping sending webhook: webhookUrl is empty");
            return;
        }
        LOG.info("Sending webhook to {}", webhookUrl);
        ResponseWithBody response = externalApiCaller.get(ApiRequest.builder()
                        .withBaseUri(webhookUrl)
                        .putQueryParams("artifact", artifactId)
                .build()).join();
        if (response.getResponse().isSuccessful()) {
            LOG.info("Webhook sent successfully");
        } else {
            LOG.error("Webhook failed with status code {}", response.getResponse().code());
        }
    }
}
