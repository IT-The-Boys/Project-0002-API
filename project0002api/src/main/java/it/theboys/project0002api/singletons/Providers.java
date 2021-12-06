package it.theboys.project0002api.singletons;

import java.lang.System.Logger;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import it.theboys.project0002api.server.Provider;

public final class Providers {
    private final static Logger logger = Logger.getLogger(Providers.class);
    private final static Map<Class<? extends Annotation>, Provider<?>> providers = new HashMap<Class<? extends Annotation>, Provider<?>>() {
        @Override
        public Provider<?> put(Class<? extends Annotation> key, Provider<?> value) {
            logger.trace("Added provider for " + key);
            return super.put(key, value);
        }
    };

    private Providers() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public static <P> Provider<P> get(Class<? extends Annotation> cls) {
        return (Provider<P>) providers.get(cls);
    }

    public static void add(Class<? extends Annotation> cls, Provider<?> provider) {
        providers.put(cls, provider);
    }
}