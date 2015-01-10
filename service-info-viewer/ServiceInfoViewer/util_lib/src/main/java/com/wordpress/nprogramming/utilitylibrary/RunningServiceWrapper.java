package com.wordpress.nprogramming.utilitylibrary;

import android.app.ActivityManager;

public class RunningServiceWrapper {

    private final ActivityManager.RunningServiceInfo info;

    public RunningServiceWrapper(ActivityManager.RunningServiceInfo info) {
        this.info = info;
    }

    public ActivityManager.RunningServiceInfo getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return info.service.flattenToShortString();
    }
}
