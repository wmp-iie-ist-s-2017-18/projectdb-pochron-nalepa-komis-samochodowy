
package projekt1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import projekt1.db.Funkcje;

/**
 *
 * @author Wojciech
 */
public class DBTest {
    
    static boolean asd()  {
        System.out.println("asdasd");
        return true;
    }
    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/komis", "komis", "komis");
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery("select * from cars;");
        while(res.next()) {
            System.out.println(res.getString("marka"));
        }
        Funkcje.createTables(con);
        
        
        
        
        
    }
}