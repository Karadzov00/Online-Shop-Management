/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        Connection conn = DB.getInstance().getConnection();

        
        String queryUpdate =
                "update System\n" +
                "set CurrentTime = DATEADD(DAY, ?, (\n" +
                "select CurrentTime \n" +
                "from System\n" +
                "where IdSystem=1))\n" +
                "where IdSystem=1";
        
        try(PreparedStatement ps = conn.prepareStatement(queryUpdate);) {
            ps.setInt(1, i);
            ps.executeUpdate(); 
            System.out.println(i+" days have passed in system");
           
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String querySelect = 
                "select CurrentTime from System\n" +
                "where IdSystem=1"; 
        try(PreparedStatement ps1 = conn.prepareStatement(querySelect);
            ResultSet rs = ps1.executeQuery();) { 
            
            if(rs.next()){
                Calendar calendar = Calendar.getInstance(); 
                calendar.setTime(rs.getDate("CurrentTime"));
                System.out.println("current time after days passed is "+calendar.getTime());
                return calendar; 
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; 
    }

    @Override
    public Calendar getCurrentTime() {
        Connection conn = DB.getInstance().getConnection();
        String query =
                "select top 1 CurrentTime\n" +
                "from System";
        
        try(PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery()) {
            if(rs.next()){
                Calendar calendar = Calendar.getInstance(); 
                calendar.setTime(rs.getDate("CurrentTime"));
                System.out.println("current time is "+calendar.getTime());
                return calendar; 
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; 
        
    }

    @Override
    public void eraseAll() {
        Connection conn = DB.getInstance().getConnection();
        String query =
                "delete from Articles\n" +
                "delete from Cities\n" +
                "delete from Customers\n" +
                "delete from Distances\n" +
                "delete from OrderArticle\n" +
                "delete from Shops\n" +
                "delete from [System]\n" +
                "delete from Transactions";
        
        try(PreparedStatement ps = conn.prepareStatement(query);) { 
            ps.executeUpdate();
            System.out.println("data in database cleared");
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
