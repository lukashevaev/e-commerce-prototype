package com.bubusyaka.demo.service;

public interface EntityFormatter<S> {
    String convert(S source);
}
