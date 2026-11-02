package com.ahmedbenidar.banque.dto;

import com.ahmedbenidar.banque.domain.Compte;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Enveloppe de collection.
 *
 * <p>JAXB ne sait pas serialiser directement une {@code List} : il lui faut un
 * element racine. Cette classe fournit une racine unique valable pour les deux
 * formats, ce qui rend les reponses JSON et XML strictement equivalentes :</p>
 *
 * <pre>
 * JSON : {"comptes":[ {...}, {...} ]}
 * XML  : &lt;comptes&gt;&lt;compte&gt;...&lt;/compte&gt;&lt;/comptes&gt;
 * </pre>
 */
@XmlRootElement(name = "comptes")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comptes {

    @XmlElement(name = "compte")
    @JsonProperty("compte")
    private List<Compte> comptes = new ArrayList<>();

    public static Comptes de(List<Compte> liste) {
        return new Comptes(new ArrayList<>(liste));
    }
}
