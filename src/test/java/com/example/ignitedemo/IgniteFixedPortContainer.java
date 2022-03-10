package com.example.ignitedemo;

import org.testcontainers.containers.FixedHostPortGenericContainer;

public class IgniteFixedPortContainer extends FixedHostPortGenericContainer<IgniteFixedPortContainer> {

    private static final String IMAGE_NAME = "apacheignite/ignite:2.9.1";

    public static final int IGNITE_CLIENT_PORT = 10800;

    public static IgniteFixedPortContainer newIgniteContainer() {
        return new IgniteFixedPortContainer().withFixedExposedPort(IGNITE_CLIENT_PORT, IGNITE_CLIENT_PORT);
    }

    private IgniteFixedPortContainer() { super(IMAGE_NAME); }
}
