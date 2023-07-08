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
import java.util.ArrayList;
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
            ps.setInt(1, i1);
            ps.setInt(2, i);
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
        Connection conn = DB.getInstance().getConnection();
        String query=
                "update Customers set Balance=\n" +
                "Balance + ?\n" +
                "where IdCustomer=?";
        try(PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setBigDecimal(1, bd);
            ps.setInt(2, i);
            ps.executeUpdate(); 
            System.out.println("balance of customer updated by:"+bd);
            
            String querySelect =
                    "select Balance\n" +
                    "from Customers\n" +
                    "where IdCustomer=?";
            try (
                PreparedStatement stmt = conn.prepareStatement(querySelect);
                ) {
                stmt.setInt(1, i);
                try(ResultSet rs = stmt.executeQuery();) {
                     if(rs.next()){
                         System.out.println("credit after adding sum is "+rs.getBigDecimal("Balance"));
                         return rs.getBigDecimal("Balance"); 
                     }
                } catch (SQLException ex) {
                Logger.getLogger(ka190444_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
            Logger.getLogger(ka190444_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return null;
    }

    @Override
    public int createOrder(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query ="insert into Orders(IdCustomer, [State])values(?, 'created')";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, i);
            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys();) {
                if (rs.next()) {
                    System.out.println("new order created with id: "+rs.getInt(1));
                    return rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; 
    }

    @Override
    public List<Integer> getOrders(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query =
                "select IdOrder\n" +
                "from Orders\n" +
                "where IdCustomer=?";
        List<Integer>orders = new ArrayList<>(); 
        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, i);
            try ( ResultSet rs = stmt.executeQuery()) {
                System.out.println("orders");
                while(rs.next()) {
                    System.out.println(rs.getInt("IdOrder"));
                    orders.add(rs.getInt("IdOrder"));
                }
                return orders;
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orders; 
        
    }

    @Override
    public BigDecimal getCredit(int i) {
        Connection conn = DB.getInstance().getConnection();
        String querySelect =
                    "select Balance\n" +
                    "from Customers\n" +
                    "where IdCustomer=?";
            try (
                PreparedStatement stmt = conn.prepareStatement(querySelect);
                ) {
                stmt.setInt(1, i);
                try(ResultSet rs = stmt.executeQuery();) {
                     if(rs.next()){
                         System.out.println("credit for customer is "+rs.getBigDecimal("Balance"));
                         return rs.getBigDecimal("Balance"); 
                     }
                } catch (SQLException ex) {
                Logger.getLogger(ka190444_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
            Logger.getLogger(ka190444_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null; 
    }
    
}
