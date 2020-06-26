package org.pacific_emis.surveys.offline_sync.data.model;

public abstract class Device {

    private String name;
    private String address;

    public Device(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
