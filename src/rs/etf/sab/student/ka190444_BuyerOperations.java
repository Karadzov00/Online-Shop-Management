/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.BuyerOperations;

/**
 *
 * @author karad
 */
public class ka190444_BuyerOperations implements BuyerOperations{

    @Override
    public int createBuyer(String string, int i) {
        Connection conn = DB.getInstance().getConnection();
        String query =
                "insert into Customers (Name, IdCity) values (?, ?)";
        try(
            PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);        
        ) {
            ps.setString(1, string);
            ps.setInt(2, i);
            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys();){
                if(rs.next()){
                    System.out.println("a new buyer was created with id: "+rs.getInt(1));
                    return rs.getInt(1); 
                }
            }catch (SQLException ex) {
            Logger.getLogger(ka190444_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }            
            
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; 
    }

    @Override
    public int setCity(int i, int i1) {
        Connection conn = DB.getInstance().getConnection();
        String query=
                "update Customers set IdCity=? where IdCustomer=?"; 
        try(PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, i);
            ps.setInt(2, i1);
            ps.executeUpdate(); 
            System.out.println("city with id:"+i1+" set to customer with id:"+i);
            return 1; 
           
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }

    @Override
    public int getCity(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query =
                "select IdCity\n" +
                "from Customers\n" +
                "where IdCustomer = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, i);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()){
                    System.out.println("city id for given customer:"+rs.getInt("IdCity"));
                    return rs.getInt("IdCity"); 
                }
                
            }catch (SQLException ex) {
            Logger.getLogger(ka190444_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; 
        
    }

    @Override
    public BigDecimal increaseCredit(int i, BigDecimal bd) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int createOrder(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integer> getOrders(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal getCredit(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}