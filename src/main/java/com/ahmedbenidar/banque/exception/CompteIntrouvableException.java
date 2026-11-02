package com.ahmedbenidar.banque.exception;

/**
 * Levee lorsqu'un compte demande n'existe pas.
 * Traduite en HTTP 404 par {@code web/CompteIntrouvableMapper}.
 */
public class CompteIntrouvableException extends RuntimeException {

    public CompteIntrouvableException(Long id) {
        super("Aucun compte bancaire ne correspond a l'identifiant " + id);
    }

    public CompteIntrouvableException(String message) {
        super(message);
    }
}
