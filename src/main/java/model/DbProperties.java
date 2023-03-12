package model;

public class DbProperties {

    public String server;

    public String port;

    public String username;

    public String password;

    public String database;

    public DbProperties(String server, String port, String username, String password, String database) {
        this.server = server;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }
}
