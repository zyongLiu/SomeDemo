package com.example.administrator.testsurfaceview.callback;

/**
 * Created by Liu on 2016/12/13.
 */
public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
