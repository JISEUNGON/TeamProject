package com.example.busapp;

@FunctionalInterface
public interface Compare {
    public int compareTo(String src, String dest);
}
