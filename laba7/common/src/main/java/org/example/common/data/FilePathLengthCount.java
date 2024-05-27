package org.example.common.data;

import java.io.Serializable;

public class FilePathLengthCount implements Serializable {
    private String path;
    private int length;
    private int count;
    public FilePathLengthCount(String path,int length,int count) {
        this.count=count;
        this.length=length;
        this.path=path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
