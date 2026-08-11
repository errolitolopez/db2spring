package com.errol.db2spring.maven;

import com.errol.db2spring.Db2springApplication;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


@Mojo(name = "generate")
public class Db2springGenerateMojo extends AbstractMojo {

    @Parameter(property = "config", defaultValue = "db2spring-config.xml")
    private String config;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            getLog().info("Running db2spring generator...");

            Db2springApplication.main(new String[]{config});

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to execute db2spring generator", e);
        }
    }
}