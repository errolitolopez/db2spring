package com.errol.db2spring.jar;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;

public class JarLoader {

    public static void loadExternalDriver(String jarPath, String driverClassName) throws Exception {
        File jarFile = new File(jarPath);
        if (!jarFile.exists()) {
            throw new IllegalArgumentException("Driver JAR not found: " + jarPath);
        }

        // Convert the JAR path to a URL and create an isolated class loader
        URL jarUrl = jarFile.toURI().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{jarUrl}, JarLoader.class.getClassLoader());

        // Set the context class loader to allow JDBC access
        Thread.currentThread().setContextClassLoader(loader);

        // Load and register the driver
        Class<?> driverClass = Class.forName(driverClassName, true, loader);
        Driver driverInstance = (Driver) driverClass.getDeclaredConstructor().newInstance();
        DriverManager.registerDriver(new DriverShim(driverInstance));
    }
}
