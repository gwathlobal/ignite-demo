package com.example.ignitedemo;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class IgniteContainer extends GenericContainer<IgniteContainer> {

    public static final int IGNITE_PORT = 10800;

    private static final DockerImageName IMAGE_NAME = DockerImageName.parse("apacheignite/ignite:2.9.1");

    private static IgniteContainer container;

    public static IgniteContainer getInstance() {
        if (container == null) {
            container = new IgniteContainer(IMAGE_NAME);
        }
        return container;
    }

    private IgniteContainer(final DockerImageName dockerImageName) {
        super(dockerImageName);
    }
}
