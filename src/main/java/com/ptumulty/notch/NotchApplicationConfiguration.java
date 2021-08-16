package com.ptumulty.notch;

import com.ptumulty.notch.Checklist.ChecklistManager;
import com.ptumulty.notch.Checklist.ChecklistManagerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotchApplicationConfiguration
{
    @Bean
    public ChecklistManager checklistManager()
    {
        return new ChecklistManagerImpl();
    }

}
