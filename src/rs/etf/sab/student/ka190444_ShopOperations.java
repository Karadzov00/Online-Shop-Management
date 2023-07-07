/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.ShopOperations;

/**
 *
 * @author karad
 */
public class ka190444_ShopOperations implements ShopOperations {

    @Override
    public int createShop(String string, String string1) {
        Connection conn = DB.getInstance().getConnection();
        String querySelect = 
                "select IdCity\n" +
                "from Cities\n" +
                "where Name=?"; 
        try ( PreparedStatement stmt = conn.prepareStatement(querySelect)) {
            stmt.setString(1, string1);//cityName
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("id of the shop's city is "+rs.getInt(1));
                    int idCity = rs.getInt(1); 
                    String queryInsert = 
                            "insert into Shops(Name, IdCity, Balance, Discount) values (?, ?, 0, 0)";
                    try ( PreparedStatement ps = conn.prepareStatement(queryInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, string);//shop name
                        ps.setInt(2, idCity);
                        ps.executeUpdate();
                        try(ResultSet rsInsert = ps.getGeneratedKeys()) {
                            if(rsInsert.next()){
                                System.out.println("a new shop added with id:"+rsInsert.getInt(1));
                                return rsInsert.getInt(1);
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
    public int setCity(int i, String string) {
        Connection conn = DB.getInstance().getConnection();
        String querySelect = 
                "select IdCity\n" +
                "from Cities\n" +
                "where Name=?"; 
        try ( PreparedStatement stmt = conn.prepareStatement(querySelect)) {
            stmt.setString(1, string);//cityName
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("id of the shop's new city is "+rs.getInt(1));
                    int idCity = rs.getInt(1); 
                    
                    String queryUpdate = "update Shops set IdCity=? where IdShop=?"; 
                    try ( PreparedStatement ps = conn.prepareStatement(queryUpdate)) {
                        ps.setInt(1, idCity);
                        ps.setInt(2, i);
                        ps.executeUpdate();
                        System.out.println("shop updated, new id city set:"+idCity);
                        return 1; 
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
    public int getCity(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select idCity from Shops where IdShop=?";
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    System.out.println("the id of the city is:"+rs.getInt(1));
                    return rs.getInt(1); 
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
    public int setDiscount(int i, int i1) {
        Connection conn = DB.getInstance().getConnection();
        String query ="update Shops set Discount=? where IdShop=?"; 
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, i1);
            ps.setInt(2, i);
            if(ps.executeUpdate()==1){
                System.out.println("shop updated, discount set to :"+i1);
                return 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; 
    }

    @Override
    public int increaseArticleCount(int i, int i1) {
        Connection conn = DB.getInstance().getConnection();
        String query ="update Articles set Amount=Amount+? where IdArticle=?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, i1);
            ps.setInt(2, i);//article id
            ps.executeUpdate(); 
            String querySelect = "select Amount from Articles where IdArticle=?";
            try (
                PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, i);
                try(ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("new amount of selected article:"+rs.getInt(1));
                        return rs.getInt(1); 
                    }
                } catch (SQLException ex) {
                Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
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
    public int getArticleCount(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select COUNT(*)\n" +
                        "from Articles\n" +
                        "where IdShop=?\n" +
                        "group by IdShop"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    System.out.println("number of articles in given shop is:"+rs.getInt(1));
                    return rs.getInt(1); 
                }
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0; 

    }

    @Override
    public List<Integer> getArticles(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select IdArticle\n" +
                        "from Articles\n" +
                        "where IdShop=?"; 
        List<Integer> articles = new ArrayList<>(); 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                System.out.println("articles");
                while(rs.next()) {
                    System.out.println(rs.getInt(1));
                    articles.add(rs.getInt(1)); 
                    
                }
                return articles; 
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; 
    }

    @Override
    public int getDiscount(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
