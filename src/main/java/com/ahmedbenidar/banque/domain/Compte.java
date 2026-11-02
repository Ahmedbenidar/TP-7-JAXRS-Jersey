package com.ahmedbenidar.banque.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Entite JPA representant un compte bancaire (etapes 3 et 10 du TP).
 *
 * <p>L'annotation {@link XmlRootElement} permet a JAXB de serialiser l'objet
 * en XML ; Jackson reutilise les memes annotations pour produire le JSON, ce
 * qui garantit une representation identique dans les deux formats.</p>
 */
@Entity
@Table(name = "compte")
@XmlRootElement(name = "compte")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Compte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Reference metier du compte (personnalisation : unicite fonctionnelle). */
    @Column(name = "numero_compte", unique = true, length = 20)
    private String numeroCompte;

    /** Titulaire du compte (personnalisation). */
    @Column(name = "titulaire", length = 80)
    private String titulaire;

    @Column(nullable = false)
    private double solde;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_creation")
    private Date dateCreation;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private TypeCompte type;
}
