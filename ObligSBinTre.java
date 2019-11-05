package Oblig3;

////////////////// ObligSBinTre /////////////////////////////////

import java.util.*;
/**
 * author Sarah Haslund-Rastad, s317473
 */

public class ObligSBinTre<T> implements Beholder<T>
{
  private static final class Node<T>   // en indre nodeklasse
  {
    private T verdi;                   // nodens verdi
    private Node<T> venstre, høyre;    // venstre og høyre barn
    private Node<T> forelder;          // forelder

    // konstruktør
    private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
    {
      this.verdi = verdi;
      venstre = v; høyre = h;
      this.forelder = forelder;
    }

    private Node(T verdi, Node<T> forelder)  // konstruktør
    {
      this(verdi, null, null, forelder);
    }

    @Override
    public String toString(){ return "" + verdi;}

  } // class Node

  private Node<T> rot;                            // peker til rotnoden
  private int antall;                             // antall noder
  private int endringer;                          // antall endringer

  private final Comparator<? super T> comp;       // komparator

  public ObligSBinTre(Comparator<? super T> c)    // konstruktør
  {
    rot = null;
    antall = 0;
    comp = c;
  }

  /**
   * Metode som legger inn T verdi i binærtre på riktig plass.
   * @param verdi
   * @return
   */
  @Override
  public final boolean leggInn(T verdi)
    {
      Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

      Node<T> p = rot, q = null;               // p starter i roten
      int cmp = 0;                             // hjelpevariabel

      while (p != null)       // fortsetter til p er ute av treet
      {
        q = p;                                 // q er forelder til p
        cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
        p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
      }

      // p er nå null, dvs. ute av treet, q er den siste vi passerte

      p = new Node<>(verdi,q);    // oppretter en ny node

      if (q == null) {
        rot = p;                  // p blir rotnode
      } else if (cmp < 0) {
        q.venstre = p;            // venstre barn til q
      } else {
        q.høyre = p;              // høyre barn til q
      }

      antall++;                                // én verdi mer i treet
      return true;                             // vellykket innlegging
  }
  
  @Override
  public boolean inneholder(T verdi)
  {
    if (verdi == null) return false;

    Node<T> p = rot;

    while (p != null)
    {
      int cmp = comp.compare(verdi, p.verdi);
      if (cmp < 0) p = p.venstre;
      else if (cmp > 0) p = p.høyre;
      else return true;
    }
    return false;
  }

  /**
   * Metode som fjerner første instanse av verdi T i inorden i binærtre. Returnerer hvorvidt fjerningen var vellykket
   * (altså om verdi T fantes i binærtre).
   * @param verdi
   * @return boolean fjernet
   */
  @Override
  public boolean fjern(T verdi)  // hører til klassen SBinTre
  {
    if (verdi == null) return false;  // treet har ingen nullverdier

    Node<T> p = rot, q = null;   // q skal være forelder til p

    while (p != null)            // leter etter verdi
    {
      int cmp = comp.compare(verdi,p.verdi);      // sammenligner
      if (cmp < 0) { q = p; p = p.venstre; }      // går til venstre
      else if (cmp > 0) { q = p; p = p.høyre; }   // går til høyre
      else break;    // den søkte verdien ligger i p
    }
    if (p == null) return false;   // finner ikke verdi

    if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
    {
      Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
      if (p == rot) {
        rot = b;
        if(rot!=null) rot.forelder = null;
      }
      else if (p == q.venstre) {
        q.venstre = b;
        if(b!=null) b.forelder = q;
      }
      else {
        q.høyre = b;
        if(b!=null) b.forelder = q;
      }
    }
    else  // Tilfelle 3)
    {
      Node<T> s = p, r = p.høyre;   // finner neste i inorden
      while (r.venstre != null)
      {
        s = r;    // s er forelder til r
        r = r.venstre;
      }

      p.verdi = r.verdi;   // kopierer verdien i r til p

      if(r.høyre!=null) r.høyre.forelder = s;
      if (s != p) {
        s.venstre = r.høyre;
      }
      else {
        s.høyre = r.høyre;
      }
    }

    antall--;   // det er nå én node mindre i treet
    return true;
  }

  /**
   * Fjerner alle instanser av T verdi i binærtre. Returnerer antall instanser av verdi (0 dersom verdi ikke fantes i
   * treet).
   * @param verdi
   * @return count
   */
  public int fjernAlle(T verdi)
  {
    boolean cont = true;
    int count = 0;
    while(cont) {
      cont = fjern(verdi);
      if(cont) count++;
    }
    return count;
  }
  
  @Override
  public int antall()
  {
    return antall;
  }

  /**
   * Metode som finner antall instanser av T verdi i binærtre
   * @param verdi
   * @return count
   */
  public int antall(T verdi)
  {
    int count = 0;
    if(!inneholder(verdi)) return count;
    else {
      Node<T> p = rot;

      while (p != null) {
        int cmp = comp.compare(verdi, p.verdi);
        if (cmp < 0) p = p.venstre;
        else if (cmp >= 0) {
          if(cmp == 0) count++;
          p = p.høyre;
        }
      }
    }
    return count;
  }
  
  @Override
  public boolean tom()
  {
    return antall == 0;
  }

  /**
   * Metode som nullstiller binærtre.
   */
  @Override
  public void nullstill()
  {
    T somSkalFjernes;
    while(!tom()) {
      Node<T> p = rot;
      while(p.venstre!=null) p = p.venstre;
      somSkalFjernes = p.verdi;
      fjernAlle(somSkalFjernes);
    }
  }

  /** Visste ikke om rekursjon var tillatt i nesteInorden, eller om det bare var forbudt i skrivUt-metoden,
  * så jeg kodet uten rekursjon for sikkerhets skyld (selv om rekursjon klart hadde gjort en kjappere jobb av det
  **/

  private static <T> Node<T> nesteInorden(Node<T> p) {
    Node<T> forrige = p;
    if (p.høyre != null) {
      p = p.høyre;
      while (p.venstre != null) p = p.venstre;
    } else {
      if(p.forelder!=null) {
        p = p.forelder;
        while (p.høyre == forrige) {
          forrige = p;
          if (p.forelder == null) {   //Hvis vi er på siste inorden
            return null;
          }
          p = p.forelder;
        }
      } else return null;
    }
    return p;
  }

  /**
   * Metode som returnerer en streng av binærtreets grener i inorden.
   * @return utstreng
   */
  @Override
  public String toString()
  {
    String utstreng = "[";
    boolean fortsett = true;
    if(!tom()) {
      Node<T> p = rot;
      while(p.venstre!=null) p = p.venstre;
      utstreng += String.format("%s", p.verdi);
      while(fortsett) {
        p = nesteInorden(p);
        if (p!=null) utstreng += String.format(", %s", p.verdi);
        else fortsett = false;
      }
    }
    utstreng += "]";
    return utstreng;
  }

  /**
   * Metode som returnerer en streng av binærtreet i motsatt inorden ved hjelp av en hjelpestakk
   * @return utstreng
   */

  public String omvendtString()
  {
    String utstreng = "[";
    boolean fortsett = true;
    if(!tom()) {
      Stakk<Node> stakk = new TabellStakk<>();
      Node<T> p = rot;
      while (p.venstre != null) p = p.venstre;
      stakk.leggInn(p);
      while (fortsett) {
        p = nesteInorden(p);
        if (p != null) stakk.leggInn(p);
        else fortsett = false;
      }
      utstreng += String.format("%s", stakk.taUt().verdi);
      while (!stakk.tom()) {
        p = stakk.taUt();
        utstreng += String.format(", %s", p.verdi);
      }
    }
    utstreng += "]";
    return utstreng;
  }
  
  public String høyreGren()
  {
    String utstreng = "[";
    if(!tom()) {
      Node<T> p = rot;
      utstreng += String.format("%s", p.verdi);
      while(!(p.høyre == null && p.venstre == null)) {
        if(p.høyre != null) p = p.høyre;
        else p = p.venstre;
        utstreng += String.format(", %s", p.verdi);
      }
    }
    utstreng += "]";
    return utstreng;
  }

  /**
   * Returnerer høyden til en vilkårlig node
   * @param p
   * @return
   */
  private static int høyde(Node<?> p)  // ? betyr vilkårlig type
  {
    int høyde = 1;
    while(p.forelder!=null) {
      p = p.forelder;
      høyde++;
    }
    return høyde;
  }

  /**
   * Returnerer en streng med den lengste greinen i et binærtre.
   * @return utstreng
   */
  public String lengstGren() {
    String utstreng = "[";
    if(!tom()) {
      Node<T> p = rot;
      Stakk<Node> stakk = new TabellStakk<>();
      while (p.venstre != null) p = p.venstre;
      int maksHøyde = høyde(p);
      Node<T> lengst = p;
      while (p != null) {
        p = nesteInorden(p);
        if (p!=null && p.venstre == null && p.høyre == null) {
          if (høyde(p) > maksHøyde) {
            maksHøyde = høyde(p);
            lengst = p;
          }
        }
      }
      stakk.leggInn(lengst);
      while(lengst!=null) {
        lengst = lengst.forelder;
        if(lengst!=null) {
          stakk.leggInn(lengst);
        }
      }
      utstreng += String.format("%s", stakk.taUt().verdi);
      while(!stakk.tom()) {
        utstreng += String.format(", %s", stakk.taUt().verdi);
      }
    }
    utstreng += "]";
    return utstreng;
  }

  /**
   * Metode som returnerer en streng-array med binærtreets greiner fra venstre til høyre.
   * @return grener
   */
  public String[] grener()
  {
    String[] grener;
    if(!tom()) {
      Node<T> p = rot;
      Stakk<Node<T>> nystakk;
      Kø<Stakk<Node<T>>> kø = new TabellKø<>();
      while (p.venstre != null) p = p.venstre;
      Node<T> siste = p;
      while (siste != null) {
        if (p != null && p.venstre == null && p.høyre == null) {
          nystakk = new TabellStakk<>();
          nystakk.leggInn(p);
          while (p != null) {
            p = p.forelder;
            if (p != null) {
              nystakk.leggInn(p);
            }
          }
          if(nystakk!=null) {
            kø.leggInn(nystakk);
          }
        }
        p = nesteInorden(siste);
        siste = p;
      }
      grener = new String[kø.antall()];
      int count = 0;

      while(!kø.tom()) {
        nystakk = kø.taUt();
        grener[count] = String.format("[%s", nystakk.taUt().verdi);
        while(!nystakk.tom()) {
          grener[count] += String.format(", %s", nystakk.taUt().verdi);
        }
        grener[count] += "]";
        count++;
      }
    } else {
      grener = new String[0];
    }
    return grener;
  }
  
  public String bladnodeverdier()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  public String postString()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  @Override
  public Iterator<T> iterator()
  {
    return new BladnodeIterator();
  }
  
  private class BladnodeIterator implements Iterator<T>
  {
    private Node<T> p = rot, q = null;
    private boolean removeOK = false;
    private int iteratorendringer = endringer;
    
    private BladnodeIterator()  // konstruktør
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }
    
    @Override
    public boolean hasNext()
    {
      return p != null;  // Denne skal ikke endres!
    }
    
    @Override
    public T next()
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }
    
    @Override
    public void remove()
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

  } // BladnodeIterator

} // ObligSBinTre
