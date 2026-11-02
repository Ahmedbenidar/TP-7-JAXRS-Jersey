package com.ahmedbenidar.banque.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Indicateurs agreges sur les comptes (endpoint de personnalisation
 * {@code GET /banque/comptes/statistiques}).
 */
@XmlRootElement(name = "statistiques")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatistiquesBanque {

    private long nombreComptes;
    private double soldeTotal;
    private double soldeMoyen;
    private double soldeComptesCourants;
    private double soldeComptesEpargne;
    private String devise;
}
