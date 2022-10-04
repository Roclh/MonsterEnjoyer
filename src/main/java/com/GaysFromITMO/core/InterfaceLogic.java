package com.GaysFromITMO.core;

import com.GaysFromITMO.core.window.input.MouseInput;

public interface InterfaceLogic {
    void init() throws Exception;

    void input();

    void update(float interval, MouseInput mouseInput);

    void render();

    void cleanup();
}
