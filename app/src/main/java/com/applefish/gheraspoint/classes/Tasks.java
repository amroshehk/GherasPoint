package com.applefish.gheraspoint.classes;

/**
 * Created by Amro on 12/02/2017.
 */

public class Tasks {

    private int id;
    private String name;
    private String	describe;
    private int points;
    public Tasks() {

    }

    public Tasks(int id, String name, String describe, int points) {
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
