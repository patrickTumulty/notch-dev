package com.ptumulty.notch;

import com.ptumulty.notch.Checklist.ChecklistCategoryManager;
import com.ptumulty.notch.Checklist.ChecklistCategoryManagerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class NotchApplicationConfiguration
{
    @Bean
    public ChecklistCategoryManager checklistCategoryManager()
    {
        return new ChecklistCategoryManagerImpl();
    }

}
