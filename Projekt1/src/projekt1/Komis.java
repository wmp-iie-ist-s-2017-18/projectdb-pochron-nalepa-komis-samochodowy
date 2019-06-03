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
import javax.swing.UIManager;
import projekt1.db.Funkcje;
import projekt1.gui.Cars;

/**
 *
 * @author Wojciech
 */
public class Komis {
    public static void createTabels(Connection con) throws SQLException {
        Statement statement = con.createStatement();
        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS public.komis (" +
"    id integer NOT NULL GENERATED BY DEFAULT AS IDENTITY,\n" +
"    miasto text NOT NULL,\n" +
"    adres text  NOT NULL,\n" +
"    kierownik text  NOT NULL,\n" +
"    telefon text NOT NULL,\n" +
"    CONSTRAINT komis_pkey PRIMARY KEY (id)\n" +
")");
        
       statement.executeUpdate("CREATE TABLE IF NOT EXISTS public.samochody\n" +
"(\n" +
"    vin text NOT NULL,\n" +
"    marka text  NOT NULL,\n" +
"    model text  NOT NULL,\n" +
"    rok_produkcji numeric NOT NULL,\n" +
"    przebieg numeric NOT NULL,\n" +
"    kolor text NOT NULL,\n" +
"    komis integer NOT NULL,\n" +
"    info text NOT NULL,\n" +
"    CONSTRAINT samochody_pkey PRIMARY KEY (vin),\n" +
"    CONSTRAINT komis FOREIGN KEY (komis)\n" +
"        REFERENCES public.komis (id) MATCH SIMPLE\n" +
"        ON UPDATE NO ACTION\n" +
"        ON DELETE NO ACTION\n" +
")"
               );
       
       statement.executeUpdate("CREATE OR REPLACE FUNCTION public.char_value(\n" +
"	c text)\n" +
"    RETURNS integer\n" +
"    LANGUAGE 'plpgsql'\n" +
"\n" +
"    COST 100\n" +
"    VOLATILE \n" +
"AS $BODY$BEGIN \n" +
"IF (SELECT (c ~ '[0-9]+') = true) THEN\n" +
"RETURN (SELECT CAST (c as INTEGER));\n" +
"END IF;\n" +
"\n" +
"CASE c\n" +
" WHEN 'A' THEN return 1;\n" +
" WHEN 'B' THEN return 2;\n" +
" WHEN 'C' THEN return 3;\n" +
" WHEN 'D' THEN return 4;\n" +
" WHEN 'E' THEN return 5;\n" +
" WHEN 'F' THEN return 6;\n" +
" WHEN 'G' THEN return 7;\n" +
" WHEN 'H' THEN return 8;\n" +
" WHEN 'J' THEN return 1;\n" +
" WHEN 'K' THEN return 2;\n" +
" WHEN 'L' THEN return 3;\n" +
" WHEN 'M' THEN return 4;\n" +
" WHEN 'N' THEN return 5;\n" +
" WHEN 'P' THEN return 7;\n" +
" WHEN 'R' THEN return 9;\n" +
" WHEN 'S' THEN return 2;\n" +
" WHEN 'T' THEN return 3;\n" +
" WHEN 'U' THEN return 4;\n" +
" WHEN 'V' THEN return 5;\n" +
" WHEN 'W' THEN return 6;\n" +
" WHEN 'X' THEN return 7;\n" +
" WHEN 'Y' THEN return 8;\n" +
" WHEN 'Z' THEN return 9;\n" +
" ELSE RETURN -1;\n" +
"\n" +
"END CASE;\n" +
"END;\n" +
"$BODY$;");
       
       statement.executeUpdate("CREATE OR REPLACE FUNCTION public.calculate_control_digit(\n" +
"	vin text)\n" +
"    RETURNS \"char\"\n" +
"    LANGUAGE 'plpgsql'\n" +
"\n" +
"    COST 100\n" +
"    VOLATILE \n" +
"AS $BODY$DECLARE\n" +
"cv integer;\n" +
"c char;\n" +
"control_digit integer;\n" +
"weights integer[] := '{8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2}';\n" +
"BEGIN\n" +
"\n" +
"control_digit := 0;\n" +
"FOR i IN 1..17 LOOP\n" +
"	if i = 9 then continue;\n" +
"	end if;\n" +
"	c := substring(vin, i, 1);\n" +
"	select char_value(c) into cv;\n" +
"	control_digit := control_digit + cv * weights[i];\n" +
"END LOOP;\n" +
"\n" +
"control_digit := control_digit % 11;\n" +
"\n" +
"if (control_digit = 10) then\n" +
"  return 'X';\n" +
"  \n" +
"else return CAST (control_digit AS text);\n" +
"end if;														\n" +
"						\n" +
"		\n" +
"END\n" +
"$BODY$;");
       statement.executeUpdate("CREATE OR REPLACE FUNCTION public.check_vin(\n" +
"	vin text)\n" +
"    RETURNS boolean\n" +
"    LANGUAGE 'plpgsql'\n" +
"\n" +
"    COST 100\n" +
"    VOLATILE \n" +
"AS $BODY$DECLARE\n" +
"res boolean;\n" +
"control_digit char;\n" +
"BEGIN\n" +
"SELECT calculate_control_digit(vin) into control_digit;\n" +
"IF (control_digit = SUBSTR(vin, 9, 1)) THEN \n" +
"	RETURN TRUE;							\n" +
"ELSE						\n" +
"	RETURN FALSE;\n" +
"END IF;						\n" +
"\n" +
"END$BODY$;");
       
       statement.executeUpdate("CREATE OR REPLACE FUNCTION public.check_samochod()\n" +
"    RETURNS trigger\n" +
"    LANGUAGE 'plpgsql'\n" +
"    COST 100\n" +
"    VOLATILE NOT LEAKPROOF \n" +
"AS $BODY$BEGIN\n" +
"NEW.vin := upper(NEW.vin);\n" +
"IF NOT check_vin(NEW.vin) THEN\n" +
"RAISE EXCEPTION 'INVALID VIN';\n" +
"END IF;\n" +
"RETURN NEW;\n" +
"END;$BODY$;");
       
       statement.executeUpdate("DROP TRIGGER IF EXISTS new_row on \"public\".\"samochody\";");
       
       statement.executeUpdate("CREATE TRIGGER  new_row\n" +
"    BEFORE INSERT\n" +
"    ON public.samochody\n" +
"    FOR EACH ROW\n" +
"    EXECUTE PROCEDURE public.check_samochod();");
       
       statement.executeUpdate("CREATE TABLE IF NOT EXISTS public.dokumenty\n" +
"(\n" +
"    id integer NOT NULL GENERATED BY DEFAULT AS IDENTITY,\n" +
"    vin text NOT NULL,\n" +
"    nazwa_dokumentu text  NOT NULL,\n" +
"    CONSTRAINT dokumenty_pkey PRIMARY KEY (id),\n" +
"    CONSTRAINT dokumenty_vin_fkey FOREIGN KEY (vin)\n" +
"        REFERENCES public.samochody (vin) MATCH SIMPLE\n" +
"        ON UPDATE NO ACTION\n" +
"        ON DELETE NO ACTION)");
       
    }
    
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        Connection con;
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/komis", "komis", "komis");
            createTabels(con);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Błąd połączenia z bazą danych: " + e);
            return;
        }
        
        Cars carsWindow = new Cars(con);
        carsWindow.setVisible(true);
        carsWindow.updateData();
    }
}
