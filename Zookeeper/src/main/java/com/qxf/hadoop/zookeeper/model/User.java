package com.qxf.hadoop.zookeeper.model;


import java.io.Serializable;

/**
 * @author qiuxuefu
 * @since 2019-04-11
 * @version 0.0.1
 */
public class User implements Serializable {

    private static final long serialVersionUID = 7726840211952830151L;

    String name;
    String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

}
