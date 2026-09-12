package boutique.modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Catalogue {

    private final List<Produit> produits = new ArrayList<>();

    public void ajouter(Produit produit) {
        if (produit == null) {
            throw new IllegalArgumentException("Impossible d'ajouter un produit nul.");
        }
        produits.add(produit);
    }

    public boolean contient(Produit produit) {
        return produits.contains(produit);
    }

    public List<Produit> produits() {
        return Collections.unmodifiableList(produits);
    }
}