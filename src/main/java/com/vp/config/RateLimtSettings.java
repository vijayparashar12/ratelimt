package com.vp.config;

public class RateLimtSettings implements Settings {
    private int window;
    private int threshold;

    public RateLimtSettings(int threshold, int window) {
        this.threshold = threshold;
        this.window = window;
    }

    public int getWindow() {
        return window;
    }

    public int getThreshold() {
        return threshold;
    }
}
