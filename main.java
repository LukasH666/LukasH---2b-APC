import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Konto {
    protected String inhaber;
    protected String blz;
    protected String kontonr;
    protected double kontostand;
    protected String kontoart;
    protected double kontofuehrungsgebuehren;
    protected double ueberziehungsrahmen;

    public Konto(String inhaber, String blz, String kontonr, double kontostand, String kontoart, double kontofuehrungsgebuehren, double ueberziehungsrahmen) {
        this.inhaber = inhaber;
        this.blz = blz;
        this.kontonr = kontonr;
        this.kontostand = kontostand;
        this.kontoart = kontoart;
        this.kontofuehrungsgebuehren = kontofuehrungsgebuehren;
        this.ueberziehungsrahmen = ueberziehungsrahmen;
    }

    public void einzahlen(double betrag) {
        if (betrag > 0) {
            kontostand += betrag;
            System.out.println(betrag + " EUR eingezahlt. Neuer Kontostand: " + kontostand + " EUR");
        } else {
            System.out.println("Ungültiger Betrag!");
        }
    }

    public void abheben(double betrag) {
        if (betrag > 0 && betrag <= (kontostand + ueberziehungsrahmen)) {
            kontostand -= betrag;
            System.out.println(betrag + " EUR abgehoben. Neuer Kontostand: " + kontostand + " EUR");
        } else {
            System.out.println("Abhebung nicht möglich!");
        }
    }

    public void kontoauszug() {
        System.out.println("Kontoauszug für " + inhaber + " (" + kontoart + "):");
        System.out.println("Kontostand: " + kontostand + " EUR");
        System.out.println("Kontoführungsgebühren: " + kontofuehrungsgebuehren + " EUR");
        System.out.println("Überziehungsrahmen: " + ueberziehungsrahmen + " EUR");
    }

    public void ueberweisen(Konto zielkonto, double betrag) {
        if (betrag > 0 && betrag <= (kontostand + ueberziehungsrahmen)) {
            kontostand -= betrag;
            zielkonto.kontostand += betrag;
            System.out.println(betrag + " EUR überwiesen an " + zielkonto.inhaber + ". Neuer Kontostand: " + kontostand + " EUR");
        } else {
            System.out.println("Überweisung nicht möglich!");
        }
    }
}

public class main {
    private static List<Konto> konten = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Kontoverwaltung ---");
            System.out.println("1. Konto anlegen");
            System.out.println("2. Konto auflösen");
            System.out.println("3. Einzahlen");
            System.out.println("4. Abheben");
            System.out.println("5. Kontoauszug");
            System.out.println("6. Überweisen");
            System.out.println("7. Beenden");
            System.out.print("Wählen Sie eine Option: ");
            
            int wahl = scanner.nextInt();
            switch (wahl) {
                case 1:
                    kontoAnlegen();
                    break;
                case 2:
                    kontoAufloesen();
                    break;
                case 3:
                    kontoOperation("einzahlen");
                    break;
                case 4:
                    kontoOperation("abheben");
                    break;
                case 5:
                    kontoOperation("kontoauszug");
                    break;
                case 6:
                    ueberweisen();
                    break;
                case 7:
                    System.out.println("Programm beendet.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Ungültige Auswahl!");
            }
        }
    }

    private static void kontoAnlegen() {
        System.out.print("Name des Inhabers: ");
        String inhaber = scanner.next();
        System.out.print("Bankleitzahl: ");
        String blz = scanner.next();
        System.out.print("Kontonummer: ");
        String kontonr = scanner.next();
        System.out.print("Anfangsbestand: ");
        double kontostand = scanner.nextDouble();
        System.out.print("Kontoart (Girokonto/Sparkonto/Kreditkonto): ");
        String kontoart = scanner.next();
        System.out.print("Kontoführungsgebühren: ");
        double kontofuehrungsgebuehren = scanner.nextDouble();
        System.out.print("Überziehungsrahmen: ");
        double ueberziehungsrahmen = scanner.nextDouble();
        
        konten.add(new Konto(inhaber, blz, kontonr, kontostand, kontoart, kontofuehrungsgebuehren, ueberziehungsrahmen));
        System.out.println("Konto für " + inhaber + " angelegt.");
    }

    private static void kontoAufloesen() {
        System.out.print("Kontonummer zum Auflösen: ");
        String kontonr = scanner.next();
        konten.removeIf(konto -> konto.kontonr.equals(kontonr));
        System.out.println("Konto " + kontonr + " wurde aufgelöst.");
    }

    private static void kontoOperation(String operation) {
        System.out.print("Kontonummer: ");
        String kontonr = scanner.next();
        for (Konto konto : konten) {
            if (konto.kontonr.equals(kontonr)) {
                if (operation.equals("einzahlen")) {
                    System.out.print("Betrag einzahlen: ");
                    double betrag = scanner.nextDouble();
                    konto.einzahlen(betrag);
                } else if (operation.equals("abheben")) {
                    System.out.print("Betrag abheben: ");
                    double betrag = scanner.nextDouble();
                    konto.abheben(betrag);
                } else if (operation.equals("kontoauszug")) {
                    konto.kontoauszug();
                }
                return;
            }
        }
        System.out.println("Konto nicht gefunden.");
    }

    private static void ueberweisen() {
        System.out.print("Von Kontonummer: ");
        String vonKonto = scanner.next();
        System.out.print("Zu Kontonummer: ");
        String zuKonto = scanner.next();
        System.out.print("Betrag: ");
        double betrag = scanner.nextDouble();
        
        Konto sender = null;
        Konto empfaenger = null;
        
        for (Konto konto : konten) {
            if (konto.kontonr.equals(vonKonto)) {
                sender = konto;
            }
            if (konto.kontonr.equals(zuKonto)) {
                empfaenger = konto;
            }
        }
        
        if (sender != null && empfaenger != null) {
            sender.ueberweisen(empfaenger, betrag);
        } else {
            System.out.println("Eine der Kontonummern ist nicht gültig.");
        }
    }
}
