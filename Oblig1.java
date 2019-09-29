package com.company;

import java.util.Arrays;
import java.util.Random;

public class Oblig1 {

    /**
     * Method that creates an array of length n with numbers 1, 2 ... n, and switches elements with
     * randomly generated numbers between 0 and n.
     * @param n
     * @return a
     */
    public static int[] randPerm(int n)
    {
        Random r = new Random();
        int[] a = new int[n];

        Arrays.setAll(a, i -> i + 1);

        for (int k = n - 1; k > 0; k--)
        {
            int i = r.nextInt(k+1);
            bytt(a,k,i);
        }

        return a;
    }

    public static void bytt(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void bytt(char[] c, int i, int j) {
        char temp = c[i];
        c[i] = c[j];
        c[j] = temp;
    }

    /**
     *Method that uses a total of 2n (for-loop + if-test) comparisons
     * to find the max element of an array. It makes a max total of n switches
     * when the input array is made up of numbers ordered in a descending order,
     * and the fewest (0) switches when the input array is made up of numbers in
     * an ascending order.
     * @param a
     * @return a[n-1]
     */

    public static int maks(int[] a) {
        int n = a.length;   // length of array

        if (n == 0) throw   // cannot be empty!
                new java.util.NoSuchElementException("a cannot be empty!");

        for(int i=0; i<(n-1); i++) {
            if(a[i] > a[i+1]) {
                bytt(a, i, i+1);
            }
        }
        return a[n-1];
    }

    /**
     * Method that counts the total number of switches made by the maks-algorithm for an array a.
     * @param a
     * @return bytte
     */

    public static int ombyttinger(int[] a) {
        int n = a.length; // length of array
        int bytte = 0; // number of switches

        if (n == 0) throw   // cannot be empty!
                new java.util.NoSuchElementException("a cannot be empty!");

        for(int i=0; i<(n-1); i++) {
            if(a[i] > a[i+1]) {
                bytt(a, i, i+1);
                bytte++;
            }
        }
        return bytte;
    }

    /**
     * Method that calculates the average number of switches made by the maks-algorithm
     * for an array of length n. Returns approximately n-3 for n<16, n-4 for n>16, n-5 for n=100.
     *
     * The fact that the switch algorithm uses a total of six operations (three array operations, three assignment
     * operations), makes it not that effective compared to the most effective algorithms we've previously developed.
     * @param n
     * @return avg number of switches
     */
    public static double gjennomsnittligByttinger(int n) {
        int[] a;
        long sum = 0;
        int antGenererte = 1000;

        for(int i=0; i<antGenererte; i++) {
            a = randPerm(n);
            sum += ombyttinger(a);
        }

        return (double)sum/antGenererte;
    }

    /**
     * Method that returns the number of different numbers in an array sorted in ascending order.
     * @param a
     * @return antUlike
     */

    public static int antallUlikeSortert(int[] a) {

        int antUlike = 0;
        int n = a.length;

        if(n!=0) {
            if (ombyttinger(a) != 0) {
                throw new IllegalStateException("Array not assorted ascending!");
            }

            for (int i = 0; i < (n - 1); i++) {
                if (a[i] != a[i + 1]) {
                    antUlike++;
                }
            }

            antUlike++; //method counts the times a(i) != a(i+1) (antUlike-1), not the total number of individual numbers.

        }

        return antUlike;
    }

    /**
     * Method that finds the number of unique numbers in an unsorted array.
     * @param a
     * @return antUlike
     */

    public static int antallUlikeUsortert(int[] a) {

        int n = a.length;
        boolean finnes;
        int antUlike = 0;

        // method checks for all integers i if it has encountered the same number previously. Should be a more
        // effective way of checking for duplicates, but none that I could think of without editing the array.
        for(int i=1; i<n; i++) {
            finnes = false;
            for(int j=0; j<i; j++) {
                if(a[i] == a[j]) {
                    finnes = true;
                }

            }
            if(!finnes) {
                antUlike++;
            }
        }

        if(n!=0) {
            antUlike++;
        }

        return antUlike;
    }

    /**
     * Sorts array in ascending order by recurring method on partitions in descending size
     * @param a
     * @param fra
     * @param til
     */

    public static void sorter(int a[], int fra, int til) {
        if (fra < (til-1)) {
            int partitionIndex = partisjon(a, fra, til);

            sorter(a, fra, partitionIndex);
            sorter(a, partitionIndex, til);
        }
    }

    /**
     * Method that takes last element as a pivot and checks if each other value is smaller. If that is the case, it
     * swaps the elements.
     * @param a
     * @param fra
     * @param til
     * @return
     */
    private static int partisjon(int a[], int fra, int til) {
        int pivot = a[til-1];
        int i = fra-1;

        for (int j = fra; j < til-1; j++) {
            if (a[j] <= pivot) {
                i++;

                bytt(a,i,j);
            }
        }

        bytt(a,i+1,til-1);

        return i+1;
    }

    /**
     * Method that sorts an array in ascending odd and ascending even numbers.
     * @param a
     */

    public static void delsortering(int[] a) {
        int n = a.length;
        int v = 0;
        int h = n-1;
        boolean cont = true;

        if(n!=0) {
            while (cont) {
                while (v <= h && (a[v] % 2 == 1 || a[v] % 2 == -1)) v++;
                while (v <= h && a[h] % 2 == 0) h--;

                if (v < h) bytt(a, v++, h--);
                else {
                    cont = false;
                }
            }
            System.out.println(Arrays.toString(a));
            if (v == 0 || v == n) {
                sorter(a, 0, n);
            } else {
                sorter(a, 0, v);
                sorter(a, v, n);
            }
        }
    }

    /**
     * Method that rotates the elements of a character array clockwise and counter-clockwise.
     * @param c
     */

    public static void rotasjon(char[] c) {
        int n = c.length;
        if (n > 0) {
            char last = c[n - 1]; //saves last element so the value is not lost

            for(int i=n-1; i>0; i--) {
                    c[i] = c[i-1];
            }

            // gives first character in array the value of the last element for full rotation
            c[0] = last;
        }
    }


    public static int gcd(int a, int b)
    {
        return b == 0 ? a : gcd(b, a % b);
    }

    /**
     * Method that rotates char array c k times (negative k for counter-clockwise rotation, positive
     * for clockwise rotation)
     * @param c
     * @param k
     */
    public static void rotasjon(char[] c, int k)
    {
        int n = c.length;
        if (n < 2) {
            return;         // no rotation necessary
        }
        if ((k %= n) < 0) {
            k += n;           // for opposite rotation
        }

        int s = gcd(n, k);

        for (int l = 0; l < s; l++) {
            char temp = c[l];

            for (int i = l-k, j = l; i != l; i -= k)  {
                if (i < 0) {
                    i += n;
                }
                c[j] = c[i]; j = i;
            }

            c[l + k] = temp;
        }
    }

    /**
     * Method that braids two strings together.
     * @param s
     * @param t
     * @return resultString
     */
    public static String flett(String s, String t) {
        String resultString = "";

        String[] textStrings = {s,t};
        int maxLengde = textStrings[0].length();
        int tLengde = textStrings[1].length();
        if (tLengde > maxLengde) maxLengde = tLengde;
        int count = 0;
        while (count < maxLengde) {
            for (int i = 0; i < textStrings.length; i++) {
                if (count < textStrings[i].length()) {
                    resultString += textStrings[i].charAt(count);
                }
            }
            count++;
        }
        return resultString;
    }

    /**
     * Method that braids a varying number of strings together.
     * @param s
     * @return resultString
     */
    public static String flett(String ... s) {
        String resultString = "";
        if(s.length!=0) {
            int maxLengde = s[0].length();
            for (int i = 0; i < s.length; i++) {
                if (s[i].length() > maxLengde) {
                    maxLengde = s[i].length();
                }
            }
            int count = 0;
            while(count<maxLengde) {
                for(int i=0; i<s.length; i++) {
                    if (count < s[i].length()) {
                        resultString += s[i].charAt(count);
                    }
                }
                count++;
            }
        }

        return resultString;
    }

    /**
     * Method that returns an array of the indexes of the sorted elements of array a, without changing a in itself
     * @param a
     * @return index
     */
    public static int[] indekssortering(int[] a) {
        int n = a.length;
        int[] index = new int[n];
        int[] sortert = new int[n];

        //loops creating a sorted copy of a

        for(int i=0; i<n; i++) {
            sortert[i] = a[i];
        }

        sorter(sortert,0,n);

        int temp;
        if(n>0) {
            temp = sortert[n - 1]; // arbitrarily chosen index to ensure that loop runs for i=0
        } else {
            temp = 0;
        }

        // algorithm that finds index of numbers in "a" matching the entries of its sorted array
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if (sortert[i] == a[j]) {
                    if(temp!=sortert[i]) { // ensures we're not testing duplicates in sorted array
                        index[i] = j;
                        temp = sortert[i];
                    } else {
                        // finds next instance of entry in a
                        for(int k=j+1; k<n; k++) {
                            if (a[k] == temp) {
                                index[i] = k;
                            }
                        }
                    }
                }
            }
        }
        return index;
    }

    /**
     * Method that finds the indexes of the third minimum values of an array a.
     * @param a
     * @return array with indexes of the three minimum values of array
     */
    public static int[] tredjeMin(int[] a) {
        int n = a.length;
        if (n < 3) throw      // must have at least three values
                new java.util.NoSuchElementException("a.length(" + n + ") < 3!");

        int[] indexOrder = indekssortering(Arrays.copyOfRange(a,0,3));

        int m = indexOrder[0]; //index of minimum
        int minValue = a[m]; //minimum value
        int nm = indexOrder[1]; //index of next to minimum value
        int nextMinValue = a[nm]; //next to minimum value
        int tm = indexOrder[2]; //index of third minimum value
        int thirdMinValue = a[tm]; //third miminum value


        for (int i = 3; i < n; i++) {
            if (a[i] < thirdMinValue) {
                if (a[i] < nextMinValue) {
                    if (a[i] < minValue) {
                        tm = nm;
                        thirdMinValue = nextMinValue; // new thirdMin

                        nm = m;
                        nextMinValue = minValue;        // new nextToMin

                        m = i;
                        minValue = a[m];                 // new Min

                    } else {
                        tm = nm;
                        thirdMinValue = nextMinValue; // new thirdToMin

                        nm = i;
                        nextMinValue = a[nm];           // new nextToMin
                    }

                } else {
                    tm = i;
                    thirdMinValue = a[tm];          // new thirdMin
                }
            }
        }
        return new int[] {m,nm,tm};
    }

    /**
     * Method that checks if string s is contained in string t.
     * @param s
     * @param t
     * @return inneholdt
     */
    public static boolean inneholdt(String s, String t) {
        char[] sSubstring = s.toCharArray();
        char[] tSubstring = t.toCharArray();
        int count = 0;
        boolean test = true;
        boolean inneholdt = true;

        while((count < sSubstring.length) && test) {
            test = false;
            for(int j=count;j<tSubstring.length; j++) {
                if(sSubstring[count] == tSubstring[j]) {
                    bytt(tSubstring, 0,j);
                    test = true;
                }
            }
            if(test) {
                count++;
            } else {
                inneholdt = false;
            }
        }
        return inneholdt;
    }
}
