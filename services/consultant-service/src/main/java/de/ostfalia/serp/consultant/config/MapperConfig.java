package de.ostfalia.serp.consultant.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

// Il sert à transformer tes données d'un format "Base de données" 
// vers un format "Internet" (API) sans que tu aies à écrire des lignes de code répétitives.