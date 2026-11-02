package com.ahmedbenidar.banque.service;

import com.ahmedbenidar.banque.domain.Compte;
import com.ahmedbenidar.banque.domain.TypeCompte;
import com.ahmedbenidar.banque.dto.StatistiquesBanque;
import com.ahmedbenidar.banque.exception.CompteIntrouvableException;
import com.ahmedbenidar.banque.repository.CompteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class CompteServiceImpl implements CompteService {

    private final CompteRepository compteRepository;

    public CompteServiceImpl(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    @Override
    public List<Compte> listerComptes() {
        return compteRepository.findAll();
    }

    @Override
    public List<Compte> listerParType(TypeCompte type) {
        return compteRepository.findByType(type);
    }

    @Override
    public List<Compte> rechercherParIntervalleDeSolde(double min, double max) {
        return compteRepository.findBySoldeBetweenOrderBySoldeAsc(min, max);
    }

    @Override
    public Compte trouverParId(Long id) {
        return compteRepository.findById(id)
                .orElseThrow(() -> new CompteIntrouvableException(id));
    }

    @Override
    public Compte trouverParNumero(String numeroCompte) {
        return compteRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new CompteIntrouvableException(
                        "Aucun compte bancaire ne porte la reference " + numeroCompte));
    }

    @Override
    @Transactional
    public Compte creer(Compte compte) {
        if (compte.getNumeroCompte() == null || compte.getNumeroCompte().isBlank()) {
            compte.setNumeroCompte("BQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        if (compte.getDateCreation() == null) {
            compte.setDateCreation(new Date());
        }
        if (compte.getType() == null) {
            compte.setType(TypeCompte.COURANT);
        }
        return compteRepository.save(compte);
    }

    @Override
    @Transactional
    public Compte modifier(Long id, Compte compte) {
        Compte existant = trouverParId(id);
        existant.setTitulaire(compte.getTitulaire());
        existant.setSolde(compte.getSolde());
        existant.setDateCreation(compte.getDateCreation());
        existant.setType(compte.getType());
        if (compte.getNumeroCompte() != null && !compte.getNumeroCompte().isBlank()) {
            existant.setNumeroCompte(compte.getNumeroCompte());
        }
        return compteRepository.save(existant);
    }

    @Override
    @Transactional
    public void supprimer(Long id) {
        Compte existant = trouverParId(id);
        compteRepository.delete(existant);
    }

    @Override
    public StatistiquesBanque statistiques() {
        List<Compte> comptes = compteRepository.findAll();
        double total = comptes.stream().mapToDouble(Compte::getSolde).sum();
        long nombre = comptes.size();
        double moyen = nombre == 0 ? 0 : arrondir(total / nombre);
        return new StatistiquesBanque(
                nombre,
                arrondir(total),
                moyen,
                arrondir(compteRepository.totalSoldeParType(TypeCompte.COURANT)),
                arrondir(compteRepository.totalSoldeParType(TypeCompte.EPARGNE)),
                "MAD");
    }

    private static double arrondir(double valeur) {
        return BigDecimal.valueOf(valeur).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
