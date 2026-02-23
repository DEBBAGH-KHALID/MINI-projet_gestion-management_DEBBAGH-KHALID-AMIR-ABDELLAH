// MINI-projet_gestion-management_DEBBAGH-KHALID-AMIR-ABDELLAH
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.*;

public class GestionMagasin {
    static Scanner sc = new Scanner(System.in);
    static int max = 100;
    static String[] nomProduits   = new String[max];
    static float[] prix   = new float[max];
    static int[] Qte = new int[max];
    static int nbProduits  = 0;
    static String fichier  = "stock.txt";


    static void AfficheMenu() {
        System.out.println("===========================");
        System.out.println("1 Ajouter un nouveau produit");
        System.out.println("2 Afficher l'inventaire complet.");
        System.out.println("3 Modifier le produit");
        System.out.println("4 Réaliser une vente");
        System.out.println("5 Afficher les alertes");
        System.out.println("6 Sauvegarder les données.");
        System.out.println("7 Quitter");
        System.out.println("===========================");
    }

    static int saisirEntier(String message){
        while(true){
            System.out.print(message);
            try{
                return sc.nextInt();

            } catch (InputMismatchException errorVar){
                sc.next();
                System.out.println("ERROR");
            }
        }
    }

    public static int rechercherProduit(String nom) {
        for (int i = 0; i < nbProduits; i++) {
            if (nomProduits[i].equalsIgnoreCase(nom)) {
                return i;
            }
        } return -1;
    }

    public static void ajouterProduit() {

        if ( nbProduits>= max) {
            System.out.println("Le tableau est plein");
            return;
        }
        sc.nextLine();
        System.out.print("Entrez le nom du produit : ");
        String nom = sc.nextLine();

        System.out.print("Entrez le prix du produit : ");
        float prixProduit =  Float.parseFloat(sc.nextLine());

        System.out.print("Entrez la quantité : ");
        int quantite = sc.nextInt();
        sc.nextLine();
        nomProduits[nbProduits]= nom;
        prix[nbProduits] = prixProduit;
        Qte[nbProduits] = quantite;

        nbProduits++;

        System.out.println("Produit ajouté avec succès !");
    }

    public static void modifierPrix() {


        System.out.print("Entrez le nom du produit à modifier : ");
        String nom = sc.nextLine();


        int index = rechercherProduit(nom);

        if (index != -1) {

            System.out.println("Produit trouvé ! Prix actuel : " + prix[index]);
            System.out.print("Entrez le nouveau prix : ");
            prix[index] = sc.nextFloat();
            System.out.println("Prix modifié avec succès !");
        } else {
            System.out.println("ERROR 404");
        }
    }

    public static void acheterProduit() {
        Scanner sc = new Scanner(System.in);


        System.out.print("Entrez le nom du produit : ");
        String nom = sc.nextLine();

        System.out.print("Entrez la quantité souhaitée : ");
        int quantiteSouhaitee = sc.nextInt();
        sc.nextLine();


        int ID = rechercherProduit(nom);

        if (ID == -1) {
            System.out.println("Produit introuvable !");
            return;
        }

        if (quantiteSouhaitee > Qte[ID]) {
            System.out.println("Stock insuffisant ! Stock disponible : " + Qte[ID]);
            return;
        }
        float total = prix[ID] * quantiteSouhaitee;


        double remise = 0;
        if (total > 1000) {
            remise = total * 0.10;
            total  = (float) (total - remise);
        }


        Qte[ID] -= quantiteSouhaitee;

        // ─── Afficher le ticket de caisse ───
        System.out.println("\n----------------------------------");
                System.out.println("||         TICKET DE CAISSE         ||");
        System.out.println("||----------------------------------||");
        System.out.printf( "||  Produit    :  ||"+ nomProduits[ID]);
        System.out.printf( "||  Prix unit. :  ||"+ prix[ID]);
        System.out.printf( "||  Quantité   :  ||"+ quantiteSouhaitee);
        System.out.println("||----------------------------------||");

        if (remise > 0) {
            System.out.printf("||  Remise 10%% :  ||"+ remise);
            System.out.println("||----------------------------------||");
        }

        System.out.printf( "||  TOTAL      :  ||"+ total);
        System.out.println("||----------------------------------||");
        System.out.printf( "||  Stock restant :  ||"+ Qte[ID]);
        System.out.println("||----------------------------------||");
        System.out.println("       Merci pour votre achat !     ");
    }

    public static void afficherStock() {
        System.out.println("NOM \t | PRIX \t| QTE | \t VALEUR TOTAL");
        for(int i= 0 ; i<nbProduits;i++){
            System.out.println(nomProduits[i]  +"\t" + prix[i] +"\t" +Qte[i] +"\t" + prix[i]* Qte[i] );
        }

    }

    public static void etatAlerte() {
        System.out.println("NOM \t | PRIX \t| QTE | \t VALEUR TOTAL");
        for(int i= 0 ; i<nbProduits ;i++){
            if (Qte[i] < 5){
                System.out.println(nomProduits[i]+"\t"+prix[i]+"\t"+Qte[i]+"\t"+prix[i]*Qte[i]);
            }
        }

    }

    public static void sauvegarderStock() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fichier));

            for (int i = 0; i < nbProduits; i++) {
                bw.write(nomProduits[i] + ";" + prix[i] + ";" + Qte[i]);
                bw.newLine();
            }

            bw.close();
            System.out.println("✔ Stock sauvegardé dans " + fichier);

        } catch (IOException e) {
            System.out.println("Erreur sauvegarde : " + e.getMessage());
        }
    }

    public static void chargerStock() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fichier));

            String ligne;
            nbProduits = 0;

            while ((ligne = br.readLine()) != null) {

                String[] data = ligne.split(";");

                nomProduits[nbProduits]      = data[0];
                prix[nbProduits]      = Float.parseFloat(data[1]);
                Qte[nbProduits] = Integer.parseInt(data[2]);

                nbProduits++;
            }

            br.close();
            System.out.println("✔ Stock chargé ! (" + nbProduits + " produits)");

        } catch (FileNotFoundException e) {
            System.out.println("Aucun fichier trouvé, stock vide.");
        } catch (IOException e) {
            System.out.println("Erreur chargement : " + e.getMessage());
        }
    }

    public static void main(String[] args){
        chargerStock();
        int choix;
        do {
        AfficheMenu();
        choix = saisirEntier("choisi un nombre:");
        switch (choix){
            case 1: ajouterProduit(); break;
            case 2: afficherStock(); break;
            case 3: modifierPrix();break;
            case 4: acheterProduit();break;
            case 5: etatAlerte();break;
            case 6: sauvegarderStock();break;
            case 7: System.out.println("AdIIIosS");break;
        }
        }while (choix !=7);
    }
}
