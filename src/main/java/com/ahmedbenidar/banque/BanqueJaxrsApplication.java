package com.ahmedbenidar.banque;

import com.ahmedbenidar.banque.domain.Compte;
import com.ahmedbenidar.banque.domain.TypeCompte;
import com.ahmedbenidar.banque.repository.CompteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Point d'entree de l'application.
 *
 * <p>Le service REST n'est pas expose par Spring MVC mais par <b>Jersey</b>
 * (implementation JAX-RS) : voir {@code config/JerseyConfig}.</p>
 */
@SpringBootApplication
public class BanqueJaxrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BanqueJaxrsApplication.class, args);
    }

    /**
     * Initialisation du jeu de donnees au demarrage (etape 6 du TP).
     * Les comptes ne sont crees que si la table est vide, afin de ne pas dupliquer
     * les donnees lorsque la commande est rejouee.
     */
    @Bean
    CommandLineRunner initialiserComptes(CompteRepository compteRepository) {
        return args -> {
            if (compteRepository.count() == 0) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                List<Compte> comptes = List.of(
                        new Compte(null, "BQ-2026-000101", "Nadia El Amrani",
                                12500.75, formater(format, "2024-01-15"), TypeCompte.COURANT),
                        new Compte(null, "BQ-2026-000102", "Youssef Bennis",
                                48200.00, formater(format, "2023-11-02"), TypeCompte.EPARGNE),
                        new Compte(null, "BQ-2026-000103", "Salma Ouazzani",
                                7300.40, formater(format, "2024-03-21"), TypeCompte.COURANT),
                        new Compte(null, "BQ-2026-000104", "Karim Tazi",
                                15600.00, formater(format, "2024-06-09"), TypeCompte.EPARGNE),
                        new Compte(null, "BQ-2026-000105", "Hind Berrada",
                                Math.round(Math.random() * 900000) / 100.0, formater(format, "2025-01-30"), TypeCompte.COURANT)
                );
                compteRepository.saveAll(comptes);
                System.out.println("=== Jeu de donnees initial : " + comptes.size() + " comptes bancaires inseres ===");
            }

            System.out.println("=== Contenu de la table COMPTE ===");
            compteRepository.findAll().forEach(System.out::println);
            System.out.println("=== Nombre total de comptes : " + compteRepository.count() + " ===");
        };
    }

    private static Date formater(SimpleDateFormat format, String valeur) {
        try {
            return format.parse(valeur);
        } catch (Exception e) {
            return new Date();
        }
    }
}
