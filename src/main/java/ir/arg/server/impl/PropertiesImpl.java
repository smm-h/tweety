package ir.arg.server.impl;

import ir.arg.server.Properties;

public class PropertiesImpl implements Properties {
    @Override
    public String getUserDatabase() {
        return "db/users/";
    }

    @Override
    public String getTweetDatabase() {
        return "db/tweets/";
    }
}
