import java.util.*;

// Clasa Carte
class Carte {
    private String titlu;
    private String autor;
    private String categorie;
    private String descriere;
    private boolean disponibila;
    private boolean semnat;

    public Carte(String titlu, String autor, String categorie, String descriere) {
        this.titlu = titlu;
        this.autor = autor;
        this.categorie = categorie;
        this.disponibila = true;
        this.descriere = descriere;
        this.semnat = false;
    }

    public String getTitlu() { return titlu; }
    public String getAutor() { return autor; }
    public String getCategorie() { return categorie; }
    public String getDescriere() { return descriere; }
    public boolean isDisponibila() { return disponibila; }
    public void setDisponibila(boolean disponibila) { this.disponibila = disponibila; }

    public void semneaza() { this.semnat = true; }
    public boolean esteSemnata() { return semnat; }

    @Override
    public String toString() {
        return titlu + " - " + autor + " (" + categorie + ")";
    }

    public String detaliiComplete() {
        return "Titlu: " + titlu +
                "\nAutor: " + autor +
                "\nCategorie: " + categorie +
                "\nDescriere: " + descriere +
                "\nDisponibila: " + (disponibila ? "Da" : "Nu") +
                "\nSemnata: " + (semnat ? "Da" : "Nu");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Carte carte = (Carte) obj;
        return Objects.equals(titlu, carte.titlu) && Objects.equals(autor, carte.autor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titlu, autor);
    }
}

// Clasa Autor
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
            carte.semneaza();
            System.out.println(nume + " a semnat cartea: " + carte.getTitlu());
        } else {
            System.out.println("Aceasta carte nu a fost scrisa de " + nume);
        }
    }
}

// Clasa Utilizator
class Utilizator {
    protected String nume;
    protected List<Carte> cartiImprumutate;
    protected Map<Carte, Date> istoricImprumuturi;
    protected boolean esteAdmin;

    public Utilizator(String nume, boolean admin) {
        this.nume = nume;
        this.esteAdmin = admin;
        this.cartiImprumutate = new ArrayList<>();
        this.istoricImprumuturi = new HashMap<>();
    }

    public String getNume() { return nume; }
    public List<Carte> getCartiImprumutate() { return cartiImprumutate; }

    public boolean esteAdmin() {
        return esteAdmin;
    }

    public void imprumutaCarte(Carte carte) {
        if (carte == null) {
            System.out.println("Cartea nu exista in biblioteca.");
            return;
        }
        if (!carte.isDisponibila()) {
            System.out.println("Cartea '" + carte.getTitlu() + "' nu este disponibila.");
            return;
        }
        if (cartiImprumutate.size() >= 3) {
            System.out.println(nume + " nu poate imprumuta mai mult de 3 carți.");
            return;
        }

        cartiImprumutate.add(carte);
        carte.setDisponibila(false);
        istoricImprumuturi.put(carte, new Date());
        System.out.println(nume + " a imprumutat cartea: " + carte.getTitlu());
    }

    public void returneazaCarte(Carte carte) {
        if (carte == null) {
            System.out.println("Cartea nu exista in biblioteca.");
            return;
        }
        if (cartiImprumutate.remove(carte)) {
            carte.setDisponibila(true);
        } else {
            System.out.println(nume + " nu are aceasta carte.");
        }
    }

    public void afiseazaIstoricImprumuturi() {
        System.out.println("Istoric imprumuturi pentru " + nume + ":");
        for (Map.Entry<Carte, Date> entry : istoricImprumuturi.entrySet()) {
            System.out.println(entry.getKey().getTitlu() + " - " + entry.getValue());
        }
    }
}

// Clasa Utilizator
class Admin extends Utilizator {
    public Admin(String nume) {
        super(nume, true);
    }

    public void adaugaCarte(Biblioteca biblioteca, Carte carte) {
        biblioteca.adaugaCarte(carte, this);
    }

    public void eliminaCarte(Biblioteca biblioteca, Carte carte) {
        biblioteca.eliminaCarte(carte, this);
    }
}

// Clasa Biblioteca
class Biblioteca {
    private List<Carte> carti;
    private Map<Carte, String> istoricGlobalImprumuturi;

    public Biblioteca() {
        this.carti = new ArrayList<>();
        this.istoricGlobalImprumuturi = new HashMap<>();
    }

    public void adaugaCarte(Carte carte) {
        carti.add(carte);
    }

    public void adaugaCarte(Carte carte, Utilizator utilizator) {
        if (utilizator.esteAdmin()) {
            carti.add(carte);
            System.out.println("Adminul a adaugat cartea: " + carte.getTitlu());
        } else {
            System.out.println("Doar adminul poate adauga carti.");
        }
    }

    public void eliminaCarte(Carte carte, Utilizator utilizator) {
        if (utilizator.esteAdmin()) {
            if (carti.remove(carte)) {
                System.out.println("Cartea a fost eliminata: " + carte.getTitlu());
            } else {
                System.out.println("Cartea nu a fost gasita.");
            }
        } else {
            System.out.println("Doar adminul poate sterge carti.");
        }
    }

    public void afiseazaCarti() {
        for (Carte carte : carti) {
            System.out.println(carte);
        }
    }

    public void afiseazaCartiDisponibile() {
        System.out.println("Carti disponibile:");
        for (Carte carte : carti) {
            if (carte.isDisponibila()) {
                System.out.println(carte);
            }
        }
    }

    public void afiseazaCartiIndisponibile() {
        System.out.println("Carti indisponibile:");
        for (Carte carte : carti) {
            if (!carte.isDisponibila()) {
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

    public void inregistreazaImprumut(Carte carte, String numeUtilizator) {
        istoricGlobalImprumuturi.put(carte, numeUtilizator);
    }

    public void afiseazaIstoricGlobal() {
        if (istoricGlobalImprumuturi.isEmpty()) {
            System.out.println("Nicio carte nu a fost imprumutata.");
            return;
        }
        for (Map.Entry<Carte, String> entry : istoricGlobalImprumuturi.entrySet()) {
            System.out.println(entry.getKey().getTitlu() + " - imprumutata de: " + entry.getValue());
        }
    }
}

// Clasa ServiciuBiblioteca
class ServiciuBiblioteca {
    private Biblioteca biblioteca;

    public ServiciuBiblioteca(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
    }

    public void imprumutaCarte(Utilizator user, String titluCarte) {
        Carte carte = biblioteca.cautaCarte(titluCarte);
        user.imprumutaCarte(carte);
        if (carte != null && !carte.isDisponibila()) {
            biblioteca.inregistreazaImprumut(carte, user.getNume());
        }
    }


    public void returneazaCarte(Utilizator user, String titluCarte) {
        Carte carte = biblioteca.cautaCarte(titluCarte);
        user.returneazaCarte(carte);
    }

    public void afiseazaCartiDisponibile() {
        biblioteca.afiseazaCartiDisponibile();
    }

    public void afiseazaCartiIndisponibile() {
        biblioteca.afiseazaCartiIndisponibile();
    }
}

// Clasa Main
public class main {
    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        ServiciuBiblioteca serviciu = new ServiciuBiblioteca(biblioteca);

        Admin admin = new Admin("Ioana");
        Utilizator user1 = new Utilizator("Alex", false);
        Autor autor = new Autor("Agatha Christie", "agth@g.r", 30);


        admin.adaugaCarte(biblioteca, new Carte("1984", "George Orwell", "Distopie", "O carte despre viitor"));
        admin.adaugaCarte(biblioteca, new Carte("Mandrie si prejudecata", "Jane Austen", "Ficțiune", "Clasic literar"));
        admin.adaugaCarte(biblioteca, new Carte("Codul lui Da Vinci", "Dan Brown", "Thriller", "Mister"));
        admin.adaugaCarte(biblioteca, new Carte("Castelul din nori s-a sfaramat", "Steig Larsson", "Thriller", "Cartea 3 din trilogia Millenium"));
        admin.adaugaCarte(biblioteca, new Carte("Crima fara cadavru", "Agatha Christie", "Politist", "Carte din seria Miss Marple"));
        admin.adaugaCarte(biblioteca, new Carte("10 negri mititei", "Agatha Christie", "Politist", "Carte din seria Hercule Poirot"));
        admin.adaugaCarte(biblioteca, new Carte("100 de cine petrecute in familie", "Ana Popa", "Bucate", "Carte perfecta de bucate"));
        admin.adaugaCarte(biblioteca, new Carte("Why machines learn", "Anil Ananthaswamy", "Non Fictiune", "Carte despre machine learning"));

        Carte carteSemnata = biblioteca.cautaCarte("Crima fara cadavru");
        autor.adaugaCarte(carteSemnata);
        autor.semneazaCarte(carteSemnata);
        System.out.println("\n");

        biblioteca.afiseazaCarti();
        System.out.println("\n");

        serviciu.afiseazaCartiDisponibile();
        System.out.println("\n");

        serviciu.imprumutaCarte(user1, "1984");
        System.out.println("\n");

        serviciu.afiseazaCartiDisponibile();
        System.out.println("\n");

        serviciu.afiseazaCartiIndisponibile();
        System.out.println("\n");

        serviciu.returneazaCarte(user1, "1984");

        Carte carteDeSters = biblioteca.cautaCarte("Codul lui Da Vinci");
        admin.eliminaCarte(biblioteca, carteDeSters);
        System.out.println("\n");

        biblioteca.afiseazaCarti();
        System.out.println("\n");

        Carte carteDetalii = biblioteca.cautaCarte("1984");
        if (carteDetalii != null) {
            System.out.println(carteDetalii.detaliiComplete());
        }
        System.out.println("\nIstoric pentru utilizator");
        for (Map.Entry<Carte, Date> entry : user1.istoricImprumuturi.entrySet()) {
            System.out.println(entry.getKey().getTitlu() + " imprumutata pe: " + entry.getValue());
        }
        System.out.println("\nIstoric pentru biblioteca");
        biblioteca.afiseazaIstoricGlobal();

    }
}
