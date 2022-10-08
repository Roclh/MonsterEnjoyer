package com.GaysFromITMO.test;

import com.GaysFromITMO.binder.Binder;
import com.GaysFromITMO.binder.SingletonClassProvider;
import com.GaysFromITMO.core.rendering.ObjectLoader;
import com.GaysFromITMO.core.rendering.renderers.DefaultRenderManager;
import com.GaysFromITMO.core.rendering.renderers.TextRenderManager;
import com.GaysFromITMO.core.window.WindowManager;
import com.GaysFromITMO.core.window.input.MouseInput;

public class TestBinder implements Binder {

    @Override
    public void bind() throws Exception {
        SingletonClassProvider.register(new WindowManager("Monster Enjoyer", 1600, 900, false));
        SingletonClassProvider.register(new MouseInput());
        SingletonClassProvider.register(new DefaultRenderManager());
        SingletonClassProvider.register(((DefaultRenderManager) SingletonClassProvider.getStoredClass(DefaultRenderManager.class)).isLogged(false).getShader());
        SingletonClassProvider.register(new TextRenderManager());
        SingletonClassProvider.register(((TextRenderManager)SingletonClassProvider.getStoredClass(TextRenderManager.class)).isLogged(false).getShader());
        SingletonClassProvider.register(new ObjectLoader().isLogged(false));
        SingletonClassProvider.register(new TestLauncher());

    }
}
