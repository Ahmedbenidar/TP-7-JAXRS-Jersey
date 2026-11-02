package com.ahmedbenidar.banque.web;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filtre JAX-RS ajoutant les en-tetes CORS a toutes les reponses du service.
 * Personnalisation : permet de tester l'API depuis un navigateur ou SoapUI.
 */
@Component
@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Accept");
    }
}
