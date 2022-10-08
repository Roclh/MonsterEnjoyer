package com.GaysFromITMO.logger;

public interface Loggable<T> {
    boolean isLogged();
    T isLogged(boolean isLogged);
}
