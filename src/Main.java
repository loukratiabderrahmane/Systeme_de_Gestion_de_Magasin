import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;


public class Main {

    static final int MAX = 20;
    static String[] noms = new String[MAX];
    static double[] prix = new double[MAX];
    static int[] quantites = new int[MAX];
    static int nbProduits = 0;
    static Scanner scanner = new Scanner(System.in);

    //---Fonction d Affichage et Saisie

    public static void afficherMenu() {
        System.out.println("---- MENU GESTION MAGASIN: ----");
        System.out.println("1. Ajouter un nouveau produit");
        System.out.println("2. Afficher l'inventaire complet");
        System.out.println("3. Rechercher un produit (par nom)");
        System.out.println("4. Realiser une vente (avec mise a jour stock)");
        System.out.println("5. Afficher les alertes (stock bas < 5)");
        System.out.println("6. Sauvegarder les donnees");
        System.out.println("7. Quitter");
        System.out.println("----------------------------------");
    }

    public static int saisirEntier(String message) {
        System.out.print(message);
        int choix = scanner.nextInt();
        scanner.nextLine();
        return choix;
    }

    //---Gestion de l Inventaire

    public static void ajouterProduit() {
        if (nbProduits >= MAX) {
            System.out.println("le Stock est plein");
            return;
        }
        System.out.print("Nom du produit: ");
        String nom = scanner.nextLine();

        System.out.print("Prix: ");
        double p = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Quantite initiale: ");
        int q = scanner.nextInt();
        scanner.nextLine();

        noms[nbProduits] = nom;
        prix[nbProduits] = p;
        quantites[nbProduits] = q;
        nbProduits++;

        System.out.println("Produit ajoute avec succes.");
    }

    public static void modifierPrix() {
        System.out.print("Nom du produit a modifier : ");
        String nom = scanner.nextLine();
        int i = rechercherProduit(nom);

        if (i == -1) {
            System.out.println("Produit introuvable.");
            return;
        }

        System.out.print("Saisir Nouveau Prix: ");
        double nouveaup = scanner.nextDouble();

        prix[i] = nouveaup;

        System.out.println("Prix modifie.");

    }

    public static int rechercherProduit(String nom) {

        for (int i = 0; i < nbProduits; i++) {
            if (noms[i].equalsIgnoreCase(nom)) {
                return i;
            }
        }
        return -1;
    }

    //---Gestion des Ventes

    public static void effectuerVente() {
        System.out.print("Nom du produit: ");
        String nom = scanner.nextLine();

        int i = rechercherProduit(nom);
        if (i == -1) {
            System.out.println("Produit introuvable.");
            return;
        }
        System.out.print("Quantite souhaitee: ");
        int qteDemandee = scanner.nextInt();
        scanner.nextLine();


        if (qteDemandee > nbProduits) {
            System.out.println("Quantite invalide");
            return;
        }
        if (qteDemandee < 0) {
            System.out.println("Quantite insuffisant");
            return;
        }

        double total = prix[i] * qteDemandee;

        double totalFinale = total;
        double remise = 0.0;

        if (total > 1000) {
            remise = total * 0.10;
            totalFinale = total - remise;
        }
        quantites[i] -= qteDemandee;

        System.out.println("---- TICKET DE CAISSE ----");
        System.out.println("Produit      : " + noms[i]);
        System.out.println("Prix unitaire: " + prix[i] + " DH");
        System.out.println("Quantite     : " + qteDemandee);
        System.out.println("total   : " + total + " DH");
        System.out.println("Remise       : " + remise + " DH");
        System.out.println("TOTAL Finale        : " + totalFinale + " DH");
        System.out.println("----------------------");
    }


    //---Gestion des Ventes

    public static void afficherStock() {
        if (nbProduits == 0) {
            System.out.println("Stock vide.");
            return;
        }

        System.out.println("\n---------Les Produits en Stock: ---------\n");
        System.out.println("--------------------------------------------");
        System.out.println("NOM | PRIX | QTE | VALEUR TOTAL");
        System.out.println("--------------------------------------------");

        for (int i = 0; i < nbProduits; i++) {

            double valeur = prix[i] * quantites[i];

            System.out.println(
                    noms[i] + " | " +
                            prix[i] + " | " +
                            quantites[i] + " | " +
                            valeur
            );
        }
        System.out.println("--------------------------------------------");

    }

    public static void etatAlerte() {
        if (nbProduits == 0) {
            System.out.println("Stock vide.");
            return;
        }
        System.out.println("\n---------Les Produits en etat Alerte ---------\n");
        System.out.println("--------------------------------------------");
        System.out.println("NOM | PRIX | QTE | VALEUR TOTAL");
        System.out.println("--------------------------------------------");
        for (int i = 0; i < nbProduits; i++) {

            if (quantites[i] < 5) {
                double valeur = prix[i] * quantites[i];

                System.out.println(
                        noms[i] + " | " +
                                prix[i] + " | " +
                                quantites[i] + " | " +
                                valeur
                );
            }
        }
        System.out.println("--------------------------------------------");

    }

    //---Gestion des Fichiers
    public static void sauvegarderStock() {
        try {
            FileWriter writer = new FileWriter("stock.txt");

            for (int i = 0; i < nbProduits; i++) {
                writer.write(noms[i] + ";" + prix[i] + ";" + quantites[i] + "\n");
            }
            writer.close();

            System.out.println("Stock sauvegarde avec succes.");


        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde.");
        }
    }

    public static void chargerStock() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("stock.txt"));

            String ligne;

            while ((ligne = reader.readLine()) != null) {
                String[] parties = ligne.split(";");

                noms[nbProduits] = parties[0];
                prix[nbProduits] = Double.parseDouble(parties[1]);
                quantites[nbProduits] = Integer.parseInt(parties[2]);

                nbProduits++;
            }

            reader.close();
            System.out.println("Stock charge avec succes.");


        } catch (IOException e) {
            System.out.println("Aucun fichier trouve. Stock vide.");
        }

    }


    public static void main(String[] args) {
        chargerStock();

        while (true) {
            afficherMenu();
            int choix = saisirEntier("Choix: ");

            switch (choix) {
                case 1:
                    ajouterProduit();
                    break;

                case 2:
                    afficherStock();
                    break;

                case 3: {
                    System.out.print("Nom du produit a chercher: ");
                    String nom = scanner.nextLine();
                    int i = rechercherProduit(nom);

                    if (i == -1) {
                        System.out.println("Introuvable.");
                    } else {
                        System.out.println("Trouve: ID=" + (i + 1) + ", Nom=" + noms[i] +
                                ", Prix=" + prix[i] + " DH, QTE=" + quantites[i]);
                    }
                    break;
                }

                case 4:
                    effectuerVente();
                    break;

                case 5:
                    etatAlerte();
                    break;

                case 6:
                    sauvegarderStock();
                    break;

                case 7:
                    sauvegarderStock();
                    System.out.println("Au revoir !");
                    return; // ✅ mieux que System.exit(0)

                default:
                    System.out.println("Choix invalide.");
            }
        }

    }
}