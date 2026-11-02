package com.ahmedbenidar.banque.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traduit {@link CompteIntrouvableException} en reponse HTTP 404 (JSON),
 * au lieu de laisser Jersey renvoyer une page d'erreur HTML generique.
 */
@Component
@Provider
public class CompteIntrouvableMapper implements ExceptionMapper<CompteIntrouvableException> {

    @Override
    public Response toResponse(CompteIntrouvableException exception) {
        Map<String, Object> corps = new LinkedHashMap<>();
        corps.put("code", 404);
        corps.put("erreur", "COMPTE_INTROUVABLE");
        corps.put("message", exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(corps)
                .build();
    }
}
