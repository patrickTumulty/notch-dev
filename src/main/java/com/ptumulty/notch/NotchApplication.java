package com.ptumulty.notch;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebApplicationContext;

@SpringBootApplication
public class NotchApplication
{
    public static void main(String[] args)
    {
        Application.launch(NotchUIApplication.class, args);
    }
}
