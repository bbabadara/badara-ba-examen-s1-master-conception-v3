# Analyse et conception — Partie A

## 1. Contrats métier

### 1.1 Invariants
1. **Données produit** : une référence n'est jamais vide, le prix est positif ou
   nul, le poids d'un produit et la quantité d'une ligne sont strictement
   positifs.
2. **Lignes de commande** : une ligne est créée et gérée uniquement par sa
   commande, jamais partagée entre deux commandes.
3. **Commande validée verrouillée** : état `VALIDEE`, total figé, aucune
   modification de ligne ou de stratégie, aucune seconde validation ; pas de
   retour possible vers `BROUILLON`.

### 1.2 Préconditions de `valider(Notifier)`
- commande à l'état `BROUILLON` ;
- au moins une ligne ;
- poids total ≤ poids maximal (30 000 g par défaut), quel que soit le mode ;
- `Notifier` non nul.

### 1.3 Postconditions de `valider(Notifier)`
- état `VALIDEE` ;
- montant figé : `total = sous-total + frais de la stratégie en cours` ;
- `notifier(clientId, total)` appelé une unique fois, seulement en cas de
  succès ;
- toute modification ultérieure ou revalidation lève une exception ;
- en cas de refus (vide ou poids dépassé), l'état reste `BROUILLON`, le total
  n'est pas figé et aucune notification n'est émise.

## 2. Rôles des deux patrons

- **Strategy** : `StrategieLivraison` isole le calcul des frais
  (`StrategieStandard`, `StrategieExpress`, `StrategieRetrait`), substituable
  avant validation.
- **Singleton** : `AppConfig` (enum) fournit une configuration unique par
  chargeur de classes ; le métier dépend de l'interface `Configuration`, la
  substitution en test passe par `override`/`reset`.

## 3. Justification des relations et des principes SOLID (10-15 lignes)

L'**héritage** est porté par `AbstractStrategieLivraison`, classe abstraite qui
centralise le contrat de l'interface : elle refuse les montants négatifs puis
délègue à une méthode `calculer` abstraite, ce qui évite de dupliquer la
vérification dans les trois stratégies. La **composition** gerbe de
`Commande` → `LigneCommande` (classe imbriquée) garantit que les lignes
naissent et meurent avec la commande. L'**agrégation** `Catalogue` → `Produit`
traduit l'indépendance du produit vis-à-vis du catalogue. La **dépendance
ponctuelle** `Commande` → `Notifier` borne le couplage à la seule méthode
`valider`. Côté SOLID : **OCP**, une nouvelle stratégie est une sous-classe de
plus sans retoucher le noyau ; **DIP**, `Commande` et les stratégies ne
référencent que des interfaces (`Configuration`, `StrategieLivraison`,
`Notifier`). Le contrat substituable est unique — même signature, mêmes
préconditions faibles, résultat entier autonome — donc le changement de
stratégie avant validation est sans effet secondaire sur le reste.

## 4. Ce que j'ai laissé de côté par manque de temps

Le verrouillage n'existe qu'en mémoire : si on persistait les commandes, il
faudrait aussi verrouiller en base. Et `AppConfig.override` est global, donc
deux tests qui voudraient des seuils différents en parallèle ne peuvent pas
s'isoler facilement.