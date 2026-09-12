# Examen — BA Badara (v3)

## Environnement

- JDK : OpenJDK 21.0.8 (Eclipse Temurin)
- Build : Apache Maven 3.9.11
- Tests : JUnit Jupiter 5.11.4

## Commandes (depuis la racine)

- Compiler : `mvn compile`
- Lancer la démo : `mvn -q compile && java -cp target/classes boutique.Demo`
- Lancer les tests : `mvn test`

## Ce que j'ai fait

- Modèle : `Produit`, `Catalogue` (avec une méthode `contient`), `Commande`
  et des lignes gérées en interne (classe imbriquée), état
  `BROUILLON`/`VALIDEE`, exceptions pour les refus de validation.
- Patron Strategy pour les frais de livraison : interface
  `StrategieLivraison`, classe abstraite `AbstractStrategieLivraison` qui
  vérifie les entrées puis délègue le calcul, avec
  `StrategieStandard` (gratuit dès le seuil), `StrategieExpress`
  (base + 200 par kilo entamé) et `StrategieRetrait` (0).
- Patron Singleton pour la configuration : `AppConfig` (enum) derrière
  l'interface `Configuration`, substitutable dans les tests via
  `override`/`reset`.
- Validation : refus si la commande est vide ou si le poids dépasse le
  maximum, total figé, état `VALIDEE`, plus aucune modification ensuite, et
  une notification envoyée via l'abstraction `Notifier` (console en démo,
  espion mémoire en test).
- Programme principal `Demo` et 11 scénarios de tests automatisés, tous verts.

## Ce que je sais qui n'est pas parfait

- Ajouter deux fois le même produit crée deux lignes séparées : je n'ai pas
  géré la fusion des lignes par produit.
- `Produit` n'a pas d'`equals`/`hashCode`, donc `Catalogue.contient` compare
  par identité : deux objets différents avec la même référence sont tous les
  deux acceptés.
- Le catalogue lui-même accepte les doublons.
- Les frais se calculent même sur une commande vide (500 en Standard par
  exemple), alors que cette commande serait refusée à la validation.
- `AppConfig.override` ne vérifie rien (ni null, ni auto-substitution) : je
  me suis arrêté à ce qui suffit pour les tests.
- L'affichage des caractères accentués est dégradé dans la console Windows
  (codepage), les sources restent en UTF-8.

## Remise

- J'ai travaillé sur une branche `dev` et je l'ai intégrée sur `main`
  (merge sans fast-forward), le tag annoté `v1.0.0` est posé sur le résultat.
- Résultats de tests réellement observés : `Tests run: 11, Failures: 0,
  Errors: 0` (BUILD SUCCESS).
