
package projekt1;

/**
 *
 * @author Wojciech
 */
public class Car {
    final String id;
    final int przebieg;
    private int cena;
    final int masa;
    
    Car(String id, int przebieg, int cena, int masa) {
        this.id = id;
        this.przebieg = przebieg;
        this.cena = cena;
        this.masa = masa;
    }
    
    Car(int przebieg, int cena, int masa){
        this("", przebieg, cena, masa);
    }
    
    void nowaCena(int nowaCena){
        cena = nowaCena;    
    }
    
    int getCena(){
        return cena;
    }

}
