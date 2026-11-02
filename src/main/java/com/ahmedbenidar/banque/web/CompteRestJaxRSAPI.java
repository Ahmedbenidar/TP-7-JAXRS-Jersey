package com.ahmedbenidar.banque.web;

import com.ahmedbenidar.banque.domain.Compte;
import com.ahmedbenidar.banque.domain.TypeCompte;
import com.ahmedbenidar.banque.dto.Comptes;
import com.ahmedbenidar.banque.dto.StatistiquesBanque;
import com.ahmedbenidar.banque.service.CompteService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

/**
 * Service REST des comptes bancaires (etapes 8, 9 et 10 du TP).
 *
 * <p>Implemente avec les annotations JAX-RS ({@code @Path}, {@code @GET},
 * {@code @POST}, {@code @PUT}, {@code @DELETE}) et execute par Jersey.
 * Chaque methode declare produire a la fois du JSON et du XML : le format
 * reellement renvoye depend de l'en-tete {@code Accept} du client.</p>
 */
@Component
@Path("/banque")
public class CompteRestJaxRSAPI {

    private final CompteService compteService;

    public CompteRestJaxRSAPI(CompteService compteService) {
        this.compteService = compteService;
    }

    // ------------------------------------------------------------------
    // READ : liste des comptes
    // ------------------------------------------------------------------

    @Path("/comptes")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Comptes getComptes(@QueryParam("type") TypeCompte type,
                              @QueryParam("soldeMin") Double soldeMin,
                              @QueryParam("soldeMax") Double soldeMax) {
        List<Compte> resultat;
        if (type != null) {
            resultat = compteService.listerParType(type);
        } else if (soldeMin != null && soldeMax != null) {
            resultat = compteService.rechercherParIntervalleDeSolde(soldeMin, soldeMax);
        } else {
            resultat = compteService.listerComptes();
        }
        return Comptes.de(resultat);
    }

    // ------------------------------------------------------------------
    // READ : un compte par identifiant ou par reference metier
    // ------------------------------------------------------------------

    @Path("/comptes/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Compte getCompte(@PathParam("id") Long id) {
        return compteService.trouverParId(id);
    }

    @Path("/comptes/reference/{numero}")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Compte getCompteParNumero(@PathParam("numero") String numero) {
        return compteService.trouverParNumero(numero);
    }

    // ------------------------------------------------------------------
    // READ : indicateurs agreges (personnalisation)
    // ------------------------------------------------------------------

    @Path("/comptes/statistiques")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public StatistiquesBanque getStatistiques() {
        return compteService.statistiques();
    }

    // ------------------------------------------------------------------
    // CREATE
    // ------------------------------------------------------------------

    @Path("/comptes")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addCompte(Compte compte) {
        Compte cree = compteService.creer(compte);
        return Response.created(URI.create("/banque/comptes/" + cree.getId()))
                .entity(cree)
                .build();
    }

    // ------------------------------------------------------------------
    // UPDATE
    // ------------------------------------------------------------------

    @Path("/comptes/{id}")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Compte updateCompte(@PathParam("id") Long id, Compte compte) {
        return compteService.modifier(id, compte);
    }

    // ------------------------------------------------------------------
    // DELETE
    // ------------------------------------------------------------------

    @Path("/comptes/{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteCompte(@PathParam("id") Long id) {
        compteService.supprimer(id);
        return Response.noContent().build();
    }
}
