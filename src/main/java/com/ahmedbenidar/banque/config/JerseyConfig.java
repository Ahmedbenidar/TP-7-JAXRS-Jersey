package com.ahmedbenidar.banque.config;

import com.ahmedbenidar.banque.exception.CompteIntrouvableMapper;
import com.ahmedbenidar.banque.web.CompteRestJaxRSAPI;
import com.ahmedbenidar.banque.web.CorsFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration du service REST avec Jersey (etape 7 du TP).
 *
 * <p>Le {@link ResourceConfig} enregistre la classe JAX-RS
 * {@link CompteRestJaxRSAPI} : c'est Jersey, et non Spring MVC, qui traite
 * ensuite les requetes HTTP.</p>
 *
 * <p>Les classes sont declarees une seule fois : Jersey resout leurs
 * dependances grace au pont Spring fourni par {@code jersey-spring6}.</p>
 */
@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig(ObjectMapper objectMapper) {
        // Ressource REST : tous les endpoints /banque/comptes
        register(CompteRestJaxRSAPI.class);

        // Gestion des erreurs applicatives -> HTTP 404 en JSON
        register(CompteIntrouvableMapper.class);

        // En-tetes CORS sur toutes les reponses
        register(CorsFilter.class);

        // Fournisseur JSON alimente par l'ObjectMapper de Spring Boot :
        // les dates sortent ainsi au format ISO (yyyy-MM-dd) et non en
        // millisecondes, en lecture comme en ecriture.
        register(new JacksonJsonProvider(objectMapper));

        // Le module jersey-media-jaxb, present au classpath, fournit
        // automatiquement la lecture / ecriture XML via JAXB, en s'appuyant
        // sur les annotations @XmlRootElement des classes modeles.
    }
}