package com.ahmedbenidar.banque.service;

import com.ahmedbenidar.banque.domain.Compte;
import com.ahmedbenidar.banque.domain.TypeCompte;
import com.ahmedbenidar.banque.dto.StatistiquesBanque;

import java.util.List;

/**
 * Contrat metier de la banque.
 *
 * <p>Personnalisation par rapport a l'enonce : la couche JAX-RS ne parle plus
 * directement au depot, elle delegue a ce service.</p>
 */
public interface CompteService {

    List<Compte> listerComptes();

    List<Compte> listerParType(TypeCompte type);

    List<Compte> rechercherParIntervalleDeSolde(double min, double max);

    Compte trouverParId(Long id);

    Compte trouverParNumero(String numeroCompte);

    Compte creer(Compte compte);

    Compte modifier(Long id, Compte compte);

    void supprimer(Long id);

    StatistiquesBanque statistiques();
}
