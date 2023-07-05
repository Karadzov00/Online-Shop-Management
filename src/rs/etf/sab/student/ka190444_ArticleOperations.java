/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.ArticleOperations;

/**
 *
 * @author karad
 */
public class ka190444_ArticleOperations implements ArticleOperations{

    @Override
    public int createArticle(int i, String string, int i1) {
            Connection conn = DB.getInstance().getConnection();
            String query =
                    "insert into Articles (IdShop, Name, Price)values(?, ?, ?)";
            
        try(
            PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);        
        ) {
            ps.setInt(1, i);
            ps.setString(2, string);
            ps.setDouble(3, i1);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                System.out.println("a new article was created with id: "+rs.getInt(1));
                return rs.getInt(1); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; 
    }
    
}
