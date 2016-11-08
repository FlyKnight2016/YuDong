package net.zgyejy.yudong.modle;

/**
 * 课程抽象类
 * Created by FlyKnight on 2016/11/1.
 */

public class Course {
    private int id;
    private String name;
    private int pid;
    private String path;
    private String paths;

    public Course(int id, String name, int pid, String path, String paths) {
        this.id = id;
        this.name = name;
        this.pid = pid;
        this.path = path;
        this.paths = paths;
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

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPaths() {
        return paths;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pid=" + pid +
                ", path='" + path + '\'' +
                ", paths='" + paths + '\'' +
                '}';
    }
}
