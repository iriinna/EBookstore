import java.util.*;
/*
 Proiectul o sa aiba urmatoarele obiecte:
 -Biblioteca
 -Carte
 -Autor
 -Utilizator


 Proiectul o sa aiba urmatoarele actiuni:
 CA UTILIZATOR:
 -vizualizeze stocul disponibil -VERIFICA
 -vizualizare carti indisponibile  -VERIFICA
 -imprumutat carte                 -VERIFICA
 -adus carte inapoi                -VERIFICA
 -interogare carte
 -istoricul imprumturilor
 -autentificare
 -verificat carti care sunt imprumutate de el+ data la care s-au imprumutat

 CA ADMIN:
 -adaugat carti noi
 -scos carti

 CA AUTOR:
 -semnat carti scrise de el



 */

class Autor {
    private String nume;
    private String email;
    private int varsta;
    private List<Carte> cartiScrise;

    public Autor(String nume, String email, int varsta) {
        this.nume = nume;
        this.email = email;
        this.varsta = varsta;
        this.cartiScrise = new ArrayList<>();
    }

    public void adaugaCarte(Carte carte) {
        cartiScrise.add(carte);
    }

    public void semneazaCarte(Carte carte) {
        if (cartiScrise.contains(carte)) {
            System.out.println(nume + " a semnat cartea: " + carte.getTitlu());
        } else {
            System.out.println("Această carte nu a fost scrisă de " + nume);
        }
    }
}

// Clasa pentru Carti
class Carte {
    private String titlu;
    private String autor;
    private String categorie;
    private boolean disponibila;
    private boolean semnat;

    public Carte(String titlu, String autor, String categorie) {
        this.titlu = titlu;
        this.autor = autor;
        this.categorie = categorie;
        this.disponibila = true;
        this.semnat = false;
    }

    public String getTitlu() { return titlu; }
    public String getAutor() { return autor; }
    public String getCategorie() { return categorie; }
    public boolean isDisponibila() { return disponibila; }
    public void setDisponibila(boolean disponibila) { this.disponibila = disponibila; }

    @Override
    public String toString() {
        return titlu + " - " + autor + " (" + categorie + ")";
    }

    public void semneaza() {
        this.semnat = true;
    }
    public boolean esteSemnata() {
        return semnat;
    }

}

// Clasa Utilizator
class Utilizator {
    private String nume;
    private List<Carte> cartiImprumutate;
    private Map<Carte, Date> istoricImprumuturi = new HashMap<>();
    private int NumarCartiImprumutate;
    private boolean EsteAdmin;

    public Utilizator(String nume, boolean admin) {
        this.nume = nume;
        this.cartiImprumutate = new ArrayList<>();
        this.NumarCartiImprumutate = 0;
        EsteAdmin = admin;
    }

    public String getNume() { return nume; }
    public List<Carte> getCartiImprumutate() { return cartiImprumutate; }

    public boolean esteAdmin() {
        return EsteAdmin;
    }

    public void imprumutaCarte(Carte carte) {
        if(carte !=null){
            if (carte.isDisponibila()) {
                if (cartiImprumutate.size() < 3) {
                    cartiImprumutate.add(carte);
                    carte.setDisponibila(false);
                    istoricImprumuturi.put(carte, new Date());
                    System.out.println(nume + " a imprumutat cartea: " + carte.getTitlu() + " (" + carte.getAutor() + ")");
                    System.out.println("Data imprumutului: " + new Date());
                } else {
                    System.out.println(nume + " nu poate imprumuta mai mult de 3 carți.");
                }
            } else {
                System.out.println("Cartea '" + carte.getTitlu() + "' de " + carte.getAutor() + " nu este disponibila.");
            }
        }
        else{System.out.println("Cartea nu se afla la noi in biblioteca");}

    }



    public void returneazaCarte(Carte carte) {
        if (cartiImprumutate.remove(carte)) {
            carte.setDisponibila(true);
            System.out.println(nume + " a returnat " + carte.getTitlu());
        }
    }
}

// Clasa Biblioteca
class Biblioteca {
    private List<Carte> carti;

    public Biblioteca() {
        this.carti = new ArrayList<>();
    }

    public void adaugaCarte(Carte carte) {
        carti.add(carte);
    }

    public void afiseazaCartiDisponibile() {
        for (Carte carte : carti) {
            if (carte.isDisponibila()) {
                System.out.println(carte);
            }
        }
    }
    public void afiseazaCartiIndisponibile() {
        for (Carte carte : carti) {
            if (carte.isDisponibila()==false) {
                System.out.println(carte);
            }
        }
    }
    public Carte cautaCarte(String titlu) {
        for (Carte carte : carti) {
            if (carte.getTitlu().equalsIgnoreCase(titlu)) {
                return carte;
            }
        }
        return null;
    }

    // Adăugarea unei cărți, doar dacă utilizatorul este admin
    public void adaugaCarte(Carte carte, Utilizator utilizator) {
        if (utilizator.esteAdmin()) {
            carti.add(carte);
            System.out.println("Cartea " + carte.getTitlu() + " a fost adăugată de admin.");
        } else {
            System.out.println("Acces interzis: doar adminul poate adăuga cărți.");
        }
    }

    // Ștergerea unei cărți, doar dacă utilizatorul este admin
    public void eliminaCarte(Carte carte, Utilizator utilizator) {
        if (utilizator.esteAdmin()) {
            if (carti.remove(carte)) {
                System.out.println("Cartea " + carte.getTitlu() + " a fost eliminată din bibliotecă.");
            } else {
                System.out.println("Cartea nu există în bibliotecă.");
            }
        } else {
            System.out.println("Acces interzis: doar adminul poate șterge cărți.");
        }
    }

}

// Clasa Main
public class main {
    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.adaugaCarte(new Carte("Mândrie și prejudecată", "Jane Austen", "Ficțiune"));
        biblioteca.adaugaCarte(new Carte("1984", "George Orwell", "Dystopie"));
        biblioteca.adaugaCarte(new Carte("Sapiens", "Yuval Noah Harari", "Istorie"));

        Utilizator user1 = new Utilizator("Alex",false);
        biblioteca.afiseazaCartiDisponibile();

        Carte carte = biblioteca.cautaCarte("184");
        biblioteca.afiseazaCartiIndisponibile();
        user1.imprumutaCarte(carte);
        biblioteca.afiseazaCartiIndisponibile();
    }
}
