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
import rs.etf.sab.operations.CityOperations;

/**
 *
 * @author karad
 */
public class ka190444_CityOperations implements CityOperations {

    @Override
    public int createCity(String string) {
        Connection conn = DB.getInstance().getConnection();
        String querySelect = "select *\n" +
                            "from Cities\n" +
                            "where Name = ?"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(querySelect)) {
            stmt.setString(1, string);
            try {
                ResultSet rsSelect = stmt.executeQuery();
                if (rsSelect.next()) {
                   return -1; 
                }
                String query =
                "insert into Cities(Name) values (?)";
                try(
                    PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);        
                ) {
                    ps.setString(1, string);
                    ps.executeUpdate();
                    try(ResultSet rs = ps.getGeneratedKeys();){
                        if(rs.next()){
                            System.out.println("a new city was created with id: "+rs.getInt(1));
                            return rs.getInt(1); 
                        }
                    }catch (SQLException ex) {
                    Logger.getLogger(ka190444_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }            

                } catch (SQLException ex) {
                    Logger.getLogger(ka190444_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
            Logger.getLogger(ka190444_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; 
    }

    @Override
    public List<Integer> getCities() {
        Connection conn = DB.getInstance().getConnection();
        String query = "select IdCity\n" +
                        "from Cities";
        List<Integer> cities = new ArrayList<Integer>(); 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ResultSet rs = stmt.executeQuery()) {
            System.out.println("cities");
            while (rs.next()) {
                System.out.println(rs.getInt("IdCity"));
                cities.add(rs.getInt("IdCity"));
            }
            return cities; 
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; 
        
        
    }

    @Override
    public int connectCities(int i, int i1, int i2) {
        
        Connection conn = DB.getInstance().getConnection();
        String query = "select *\n" +
                        "from Distances\n" +
                        "where (IdCity1=? and IdCity2=?)\n" +
                        "or (IdCity1=? and IdCity2=?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, i);
            stmt.setInt(2, i1);
            stmt.setInt(3, i1);
            stmt.setInt(4, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    System.out.println("the line between these 2 cities already exists");
                    return -1; 
                }
                
                String queryInsert = "insert into Distances(idCity1, idCity2, distance) values (?,?,?)";
                try ( PreparedStatement ps = conn.prepareStatement(queryInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, i);
                    ps.setInt(2, i1);
                    ps.setInt(3, i2);
                    ps.executeUpdate();
                    
                    try(ResultSet rsInsert = ps.getGeneratedKeys();) {
                        if (rsInsert.next()) {
                            System.out.println("new line is created with id" + rsInsert.getInt(1));
                            return rsInsert.getInt(1);
                        }
                        
                    } catch (SQLException ex) {
                    Logger.getLogger(ka190444_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ka190444_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            } catch (SQLException ex) {
            Logger.getLogger(ka190444_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; 
    }

    @Override
    public List<Integer> getConnectedCities(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = 
                "select IdCity1, IdCity2\n" +
                "from Distances\n" +
                "where IdCity1=? or IdCity2=?";
        List<Integer> cities= new ArrayList<>(); 
        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, i);
            stmt.setInt(2, i);
            stmt.executeQuery(); 
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int cityId1 = rs.getInt("IdCity1");
                    int cityId2 = rs.getInt("IdCity2");
                    System.out.println("city line:"+cityId1+","+cityId2);
                    if(cityId1!=i)//we chose the other city in the line 
                        cities.add(cityId1); 
                    else if (cityId2!=i)
                        cities.add(cityId2);
                }
                return cities; 
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cities; 

    }

    @Override
    public List<Integer> getShops(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query =
                "select IdShop\n" +
                "from Shops\n" +
                "where IdCity=?"; 
        List<Integer> shops= new ArrayList<>(); 
        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, i);
            stmt.executeQuery(); 
            try ( ResultSet rs = stmt.executeQuery()) {
                System.out.println("list of shops in this city");
                while (rs.next()) {
                    System.out.println(rs.getInt(1));
                    shops.add(rs.getInt(1));
                }
                return shops; 
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null; 
    }
    
}
