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
import rs.etf.sab.operations.OrderOperations;

/**
 *
 * @author karad
 */
public class ka190444_OrderOperations implements OrderOperations {

    @Override
    public int addArticle(int i, int i1, int i2) {// int orderId, int articleId, int count
        //check if there are enough articles in the shop 
        //add article to the existing order, if article is already there, increase its amount 
        Connection conn = DB.getInstance().getConnection();
        String querySelectAmount = 
                "select Amount\n" +
                "from Articles\n" +
                "where IdArticle=?";
        try (
            PreparedStatement stmt = conn.prepareStatement(querySelectAmount);  ) {
            stmt.setInt(1, i1);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                   //check if amount is lesser than count 
                    System.out.println("amount of selected article: "+rs.getInt(1));
                    if(i2>rs.getInt(1)){
                        System.out.println("there inst't enough of this article in shop");
                        return -1; 
                    }
                    
                    //there is enough amount of article in shop 
                    //check if there is already that article in given order
                    String querySelectArticle = 
                            "select *\n" +
                            "from OrderArticle\n" +
                            "where IdOrder=? and IdArticle=?"; 
                    try (
                        PreparedStatement ps = conn.prepareStatement(querySelectArticle);  ) {
                        ps.setInt(1, i);
                        ps.setInt(2, i1);
                        try(ResultSet rsArticle = ps.executeQuery()) {
                            if(rsArticle.next()) {
                                //article exists in that order
                                //need to update amount
                                System.out.println("article exists in given order");
                                String queryUpdate =
                                        "update OrderArticle \n" +
                                        "set Amount=Amount+?\n" +
                                        "where IdOrder=? and IdArticle=?"; 
                                        try(
                                            PreparedStatement psUpdate = conn.prepareStatement(queryUpdate)
                                        ){
                                            psUpdate.setInt(1, i2);
                                            psUpdate.setInt(2, i);
                                            psUpdate.setInt(3, i1);
                                            psUpdate.executeUpdate(); 
                                            System.out.println("article amount is increased by:"+i2);

                                        } catch (SQLException ex) {
                                            Logger.getLogger(ka190444_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                            }
                            else{
                                //article doesn't exists in that order
                                System.out.println("article doesn't exists in given order");
                                String queryInsert = "insert into OrderArticle(IdOrder, IdArticle, Amount)\n" +
                                                        "values(?, ?, ?)"; 
                                
                                try ( PreparedStatement psInsert = conn.prepareStatement(queryInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {
                                    psInsert.setInt(1, i);
                                    psInsert.setInt(2, i1);
                                    psInsert.setInt(3, i2);
                                    psInsert.executeUpdate();
                                    try(ResultSet rsInsert = psInsert.getGeneratedKeys();) {
                                        if (rsInsert.next()) {
                                            System.out.println("new item created with id:"+rsInsert.getInt(1));
                                        }
                                        
                                    } catch (SQLException ex) {
                                        Logger.getLogger(ka190444_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } catch (SQLException ex) {
                                    Logger.getLogger(ka190444_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; 
    }

    @Override
    public int removeArticle(int i, int i1) {//(int orderId, int articleId)
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from OrderArticle\n" +
                        "where IdOrder=? and IdArticle=?";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, i);
            ps.setInt(2, i1);
            ps.executeUpdate();
            System.out.println("removed article with id:"+i+" from order with id:"+i);
            return 1; 
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; 
    }

    @Override
    public List<Integer> getItems(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int completeOrder(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal getFinalPrice(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal getDiscountSum(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getState(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Calendar getSentTime(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Calendar getRecievedTime(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getBuyer(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getLocation(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
