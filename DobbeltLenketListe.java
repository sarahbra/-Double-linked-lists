package Oblig2;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;


public class DobbeltLenketListe<T> implements Liste<T> {

    /**
     * Node class
     * @param <T>
     */
    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekere

        private Node(T verdi, Node<T> forrige, Node<T> neste) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null, null);
        }
    }

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

    public DobbeltLenketListe() {
        hode = hale = null;
        antall = endringer = 0;
    }

    public DobbeltLenketListe(T[] a) {
        this();
        Objects.requireNonNull(a,"Tabellen a er null!");

        boolean funnet = false;
        int i= a.length;

        if(i!=0) {
            do {
                i--;
                if (a[i] != null) {
                    funnet = true;
                }
            } while (!funnet && i>0);

            if(i>0 || a[i]!=null) {
                hode = hale = new Node<>(a[i]);
                antall++;
            }

            for (int j = i-1; j >= 0; j--) {
                if ((a[j]) != null) {
                    hode = new Node<>(a[j], null, hode);
                    hode.neste.forrige = hode;
                    antall++;
                }
            }
        }
    }

    public Liste<T> subliste(int fra, int til){
        fraTilKontroll(antall,fra,til);
        DobbeltLenketListe<T> subliste = new DobbeltLenketListe<T>();
        int count = 0;
        Node p = hode;
        if(!(fra==til)) {
            while(count<fra) {
                p = p.neste;
                count++;
            }
            subliste.leggInn((T) p.verdi);

            while(count<til-1) {
                p = p.neste;
                subliste.leggInn((T) p.verdi);
                subliste.antall++;
                count++;
            }
        }
        return subliste;
    }

    private static void fraTilKontroll(int antall, int fra, int til)
    {
        if (fra < 0)
            throw new IndexOutOfBoundsException
                    ("fra(" + fra + ") er negativ!");

        if ((fra-til) > antall)
            throw new IndexOutOfBoundsException
                    ("Subliste > antall (" + antall + ")");

        if(til>antall) {
            throw new IndexOutOfBoundsException("Til > antall (" + antall + ")");
        }

        if (fra > til)
            throw new IllegalArgumentException
                    ("fra(" + fra + ") > til(" + til + ") - illegalt intervall!");
    }

    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        return antall() == 0;
    }

    @Override
    public boolean leggInn(T verdi) {
        boolean lagtInn = false;
        Objects.requireNonNull(verdi, "Objektet som skal legges inn skal ha en verdi");
        Node<T> ny = new Node<>(verdi,hale,null);
        if(tom()) {
            hode = ny;
        } else {
            hale.neste = ny;
        }
        hale = ny;
        lagtInn = true;
        antall++;
        endringer++;
        return lagtInn;
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        Objects.requireNonNull(verdi, "Objektet som skal legges inn skal ha en verdi");
        if(indeks>antall || indeks < 0) {
            throw new IndexOutOfBoundsException("Indeks må være mellom 0 og " + (antall-1));
        }
        Node leggesInn;

        if(indeks == 0) {
            if(antall == 0) {
                leggesInn = new Node(verdi,null,null);
                hode = leggesInn;
                hale = leggesInn;
            } else if (antall == 1) {
                leggesInn = new Node(verdi, null, hale);
                hale.forrige = leggesInn;
                hode = leggesInn;
            } else {
                leggesInn = new Node(verdi, null, hode);
                hode.forrige = leggesInn;
                hode = leggesInn;
            }
        } else if (indeks == antall) {
            leggesInn = new Node(verdi, hale, null);
            hale.neste = leggesInn;
            hale = leggesInn;
        } else {
            Node p = finnNode(indeks);
            leggesInn = new Node(verdi,p.forrige,p);
            p.forrige.neste = leggesInn;
            p.forrige = leggesInn;
        }
        antall++;
        endringer++;
    }

    @Override
    public boolean inneholder(T verdi) {
        boolean inneholder = false;
        int indeks = indeksTil(verdi);
        if(indeks!=-1) {
            inneholder = true;
        }
        return inneholder;
    }

    private Node<T> finnNode(int indeks) {
        Node<T> p;
        int count;
        if(indeks >= antall/2) {
            p = hale;
            count = antall-1;
            while (count > indeks) {
                p = p.forrige;
                count--;
            }
        } else {
            p = hode;
            count = 0;
            while (count < indeks) {
                p = p.neste;
                count++;
            }
        }
        return p;
    }

    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks,false);
        T verdi = finnNode(indeks).verdi;
        return verdi;
    }

    @Override
    public int indeksTil(T verdi) {
        int returneres = -1;
        if(verdi!=null) {
            int count = 0;
            Node p = hode;
            while (count < antall) {
                if (verdi.equals(p.verdi)) {
                    if(returneres==-1) {
                        returneres = count;
                    }
                }
                p = p.neste;
                count++;
            }
        }
        return returneres;
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        indeksKontroll(indeks,false);
        Objects.requireNonNull(nyverdi, "Ikke tillatt med null-verdier i oppdater");
        Node endres = finnNode(indeks);
        T returneres = (T) endres.verdi;
        endres.verdi = nyverdi;
        endringer++;
        return returneres;
    }

    @Override
    //Her trengs det overclocking av prosessor

    public boolean fjern(T verdi) {
        if (verdi == null) return false;
        Node p = hode;
        while (p != null) {
            if (p.verdi.equals(verdi)) break;
            p = p.neste;
        }

        if (p == hode) {
            if(p == hale) {
                hale = hode = null;
            } else {
                hode = hode.neste;
                hode.forrige = null;
            }
        } else if (p == null) {
            return false;
        } else if (p==hale) {
            hale = p.forrige;
            hale.neste = null;
        } else {
            p.forrige.neste = p.neste;
            p.neste.forrige = p.forrige;
        }

        endringer++;
        antall--;

        return true;
    }

    @Override
    public T fjern(int indeks) {
        if(indeks>=antall || indeks < 0) {
            throw new IndexOutOfBoundsException("Indeks må være mellom 0 og " + (antall-1));
        }
        T temp;
        if(indeks==0) {
            temp = hode.verdi;
            if(antall==1) {
                hode = hale = null;
            } else {
                hode.neste.forrige = null;
                hode = hode.neste;
            }
        } else if (indeks==antall-1) {
            temp = hale.verdi;
            hale.forrige.neste = null;
            hale = hale.forrige;
        } else {
            Node<T> p = finnNode(indeks);
            temp = p.verdi;
            p.forrige.neste = p.neste;
            p.neste.forrige = p.forrige;
        }
        antall--;
        endringer++;
        return temp;
    }


    @Override
    //Brukte 31 ms med 10000 kjøringer på liste med 15 elementer
    /*
    public void nullstill() {
        T verdi;
        while(antall>0) {
            verdi = fjern(0);
            endringer--;
        }
        endringer++;
    }

    */

    // Brukte 22 ms på samme test. Det vil si noe raskere (men min fjern-metode er treig)
    public void nullstill() {
        Node p = hode;
        Node temp;
        int count = 0;
        while(count<antall) {
            p.verdi = null;
            p.forrige = null;
            temp = p.neste;
            p.neste = null;
            p = temp;
            count++;
        }
        endringer++;
        antall = 0;
    }

    @Override
    public String toString() {
        StringBuilder utStreng = new StringBuilder();
        utStreng.append('[');
        if (antall!=0) {
            Node<T> p = hode;
            utStreng.append(p.verdi);

            p = p.neste;

            while(p!=null) {
                utStreng.append(',').append(' ').append(p.verdi);
                p = p.neste;
            }

        }
        utStreng.append(']');
        return utStreng.toString();
    }

    public String omvendtString() {
        StringBuilder utStreng = new StringBuilder();
        utStreng.append('[');
        if (antall!=0) {
            Node<T> p = hale;
            utStreng.append(p.verdi);

            p = p.forrige;

            while(p!=null) {
                utStreng.append(',').append(' ').append(p.verdi);
                p = p.forrige;
            }
        }
        utStreng.append(']');
        return utStreng.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new DobbeltLenketListeIterator();
    }

    public Iterator<T> iterator(int indeks) {
        return new DobbeltLenketListeIterator(indeks);
    }

    private class DobbeltLenketListeIterator implements Iterator<T>
    {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator(){
            denne = hode;     // p starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks){
            if(indeks<0 || indeks>= antall)
                throw new IndexOutOfBoundsException("Indeks må være mellom 0 og " + (antall-1));
            denne = hode;
            int count = 0;
            while(count<indeks) {
                denne = denne.neste;
                count++;
            }

            fjernOK = false;
            iteratorendringer = endringer;
        }

        @Override
        public boolean hasNext(){
            return denne != null;
        }

        @Override
        public T next(){
            if(endringer!=iteratorendringer)
                throw new ConcurrentModificationException("Endringer != iteratorendringer");
            if(!hasNext())
                throw new NoSuchElementException("Ikke flere elementer i listen");
            fjernOK = true;
            T verdi = denne.verdi;
            denne = denne.neste;

            return verdi;
        }

        @Override
        public void remove() {
            if (endringer != iteratorendringer)
                throw new ConcurrentModificationException("Listen er endra");
            if (!fjernOK)
                throw new IllegalStateException("Ulovlig tilstand!");
            fjernOK = false;
            if (denne == null) {
                if (antall == 1) {
                    hode = hale = null;
                } else {
                    hale = hale.forrige;
                    hale.neste = null;
                }
            } else if (denne.forrige == hode) {
                hode = denne;
                hode.forrige = null;
            } else {
                denne.forrige.forrige.neste = denne;
                denne.forrige = denne.forrige.forrige;
            }
            antall--;
            endringer++;
            iteratorendringer++;
            }
        }// class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new NotImplementedException();
    }
} // class DobbeltLenketListe


