package com.example.ignitedemo;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class IgniteContainer extends GenericContainer<IgniteContainer> {

    private static final DockerImageName IMAGE_NAME = DockerImageName.parse("apacheignite/ignite:2.9.1");

    public static final int IGNITE_CLIENT_PORT = 10800;

    public static IgniteContainer newIgniteContainer() {
        return new IgniteContainer().withExposedPorts(IGNITE_CLIENT_PORT);
    }

    private IgniteContainer() { super(IMAGE_NAME); }
}
