/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;

/**
 *
 * @author karad
 */
public class ka190444_GeneralOperations implements GeneralOperations {

    @Override
    public void setInitialTime(Calendar clndr) {
            Connection conn = DB.getInstance().getConnection();
            Date date = clndr.getTime(); 
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            String query =
                    "IF EXISTS (SELECT 1 FROM System)\n" +
                    "    UPDATE [System] SET CurrentTime = ?\n" +
                    "ELSE\n" +
                    "    INSERT INTO [System] (IdSystem, CurrentTime) VALUES (1, ?);";
        try(
            PreparedStatement ps = conn.prepareStatement(query)
        ){
            ps.setDate(1, sqlDate);
            ps.setDate(2, sqlDate);
            ps.executeUpdate(); 
            System.out.println("initial time has been set");
                    
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public Calendar time(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Calendar getCurrentTime() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eraseAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
