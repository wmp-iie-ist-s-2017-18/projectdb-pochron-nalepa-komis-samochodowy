package projekt1;

/**
 *
 * @author Wojciech
 */
public class Projekt1 {
    
    public static void main(String[] args) {
       
        
       Car opel = new Car("RZ1234", 200000, 150000, 20);
       
       Car asd2 = new Car("RZ1", 150000, 20000, 30);
       
       Car prze = new Car(250000, 30000, 20);
       
       
       opel.nowaCena(20000);
       
       
       System.out.println(opel.getCena());
       System.out.println(opel.przebieg);
       System.out.println(opel.masa);
       System.out.println(asd2.id);
       
    }
    
}
