package com.ahmedbenidar.banque.repository;

import com.ahmedbenidar.banque.domain.Compte;
import com.ahmedbenidar.banque.domain.TypeCompte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Depot JPA des comptes bancaires (etape 5 du TP).
 *
 * <p>Les methodes ajoutees au-dela de {@link JpaRepository} servent aux
 * endpoints de personnalisation du service REST.</p>
 */
public interface CompteRepository extends JpaRepository<Compte, Long> {

    List<Compte> findByType(TypeCompte type);

    List<Compte> findBySoldeBetweenOrderBySoldeAsc(double min, double max);

    Optional<Compte> findByNumeroCompte(String numeroCompte);

    @Query("select coalesce(sum(c.solde), 0) from Compte c where c.type = :type")
    double totalSoldeParType(@Param("type") TypeCompte type);
}
