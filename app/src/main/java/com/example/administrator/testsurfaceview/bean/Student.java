package com.example.administrator.testsurfaceview.bean;

/**
 * Created by Liu on 2016/11/11.
 */
public class Student {
    private String name;
    private String[] courses;

    public Student(String name, String[] courses) {
        this.name = name;
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getCourses() {
        return courses;
    }

    public void setCourses(String[] courses) {
        this.courses = courses;
    }
}
