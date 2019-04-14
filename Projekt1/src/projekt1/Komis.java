/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package projekt1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import projekt1.db.Funkcje;
import projekt1.gui.Cars;

/**
 *
 * @author Wojciech
 */
public class Komis {
    public static void main(String[] args) {
        Connection con;
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/komis", "komis", "komis");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Błąd połączenia z bazą danych");
            return;
        }
        
        Cars carsWindow = new Cars(con);
        carsWindow.setVisible(true);
        carsWindow.updateData();
    }
}
