package com.ptumulty.notch;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppContext
{
    private static final AnnotationConfigApplicationContext instance = new AnnotationConfigApplicationContext();

    static
    {
        registerConfigurations();
    }

    private static void registerConfigurations()
    {
        instance.register(NotchApplicationConfiguration.class);

        // This should be the last thing in this scope
        instance.refresh();
    }

    public static AnnotationConfigApplicationContext get()
    {
        return instance;
    }
}
