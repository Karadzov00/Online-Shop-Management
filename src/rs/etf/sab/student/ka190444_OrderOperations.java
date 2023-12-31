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
import java.util.Map;
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
        Connection conn = DB.getInstance().getConnection();
        String query =  "select IdItem\n" +
                        "from OrderArticle\n" +
                        "where IdOrder=?"; 
        List<Integer> items = new ArrayList<>(); 
        try (PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                System.out.println("items");
                while(rs.next()) {
                    System.out.println(rs.getInt(1));
                    items.add(rs.getInt(1));
                }
                return items; 
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; 
    }

    @Override
    public int completeOrder(int i) {
        Connection conn = DB.getInstance().getConnection();
        BigDecimal finalPrice = getFinalPrice(i); 
        List<Integer> shopCities = new ArrayList<>(); 
        Map<Integer, Map<Integer, Integer>> graph; 
        int customerCity=-1; 
        //check if customer has money 
        String query = 
                    "select c.Balance\n" +
                    "from Customers c join Orders o\n" +
                    "on c.IdCustomer=o.IdCustomer\n" +
                    "where o.IdOrder=?"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {//if result set is empty 
                    BigDecimal balance = rs.getBigDecimal(1); 
                    if(balance.compareTo(finalPrice)<0){
                        System.out.println("there's not enough money, order can't be completed");
                        return -1;
                    }
                }
                
                //take money from customer's balance
                String queryUpdateBalance =
                        "update Customers \n" +
                        "set Balance=Balance-?\n" +
                        "where IdCustomer=(\n" +
                        "select c.IdCustomer from Customers c\n" +
                        "join Orders o on c.IdCustomer=o.IdCustomer\n" +
                        "where o.IdOrder=?\n" +
                        ")"; 
                try ( PreparedStatement psBalance = conn.prepareStatement(queryUpdateBalance)) {
                    psBalance.setBigDecimal(1, finalPrice);
                    psBalance.setInt(2, i);
                    if(psBalance.executeUpdate()==1){
                        System.out.println("balance of user updated ");
                        
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //make new transactions for every shop from the order 
                String queryShopSums = 
                        "select s.IdShop, \n" +
                        "SUM(a.Price*oa.Amount*((100-s.Discount)/cast(100 as decimal)))\n" +
                        "as totalSum\n" +
                        "from OrderArticle oa join Articles a \n" +
                        "on oa.IdArticle=a.IdArticle\n" +
                        "join Shops s on s.IdShop=a.IdShop\n" +
                        "join Orders o on oa.IdOrder=o.IdOrder\n" +
                        "where o.IdOrder=?\n" +
                        "group by s.IdShop"; 
                try (
                    PreparedStatement psShopSums = conn.prepareStatement(queryShopSums);  ) {
                    psShopSums.setInt(1, i);
                    System.out.println("shop cities for given order");
                    try(ResultSet rsShopSums = psShopSums.executeQuery()) {
                        
                        while(rsShopSums.next()) {
                            System.out.println(rsShopSums.getInt(1));
                            //insert transaction for shop 
                            int shopId = rs.getInt("IdShop"); 
                            BigDecimal totalSum = rs.getBigDecimal("totalSum"); 
                            
                            String queryInsertTransaction = 
                                    "insert into Transactions\n" +
                                    "(Amount, IdShop, IdOrder)\n" +
                                    "values(?, ?, ?)";
                            try ( PreparedStatement psTransaction = conn.prepareStatement(queryInsertTransaction, PreparedStatement.RETURN_GENERATED_KEYS)) {
                                psTransaction.setBigDecimal(1, totalSum);
                                psTransaction.setInt(2, shopId);
                                psTransaction.setInt(3, i);
                                psTransaction.executeUpdate();
                                ResultSet rsTransaction = psTransaction.getGeneratedKeys();
                                if (rs.next()) {
                                    System.out.println("new transaction created");
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
                
                graph = DijkstraAlgorithm.formGraph(); 
                //select customer city 
                String queryCustomerCity = 
                        "select c.IdCity\n" +
                        "from Orders o join Customers c \n" +
                        "on o.IdCustomer = c.IdCustomer\n" +
                        "where IdOrder=?"; 
                try (
                    PreparedStatement psCustCity = conn.prepareStatement(queryCustomerCity);  ) {
                    psCustCity.setInt(1, i);
                    try(ResultSet rsCustCity = psCustCity.executeQuery()) {
                        if(rsCustCity.next()) {
                            System.out.println("customer city is:"+rsCustCity.getInt(1));
                            customerCity = rsCustCity.getInt(1); 
                        }
                        else 
                            return -1;
                        
                        
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
                //select shop cities 
                String queryShopCities = 
                        "select IdCity\n" +
                        "from OrderArticle oa \n" +
                        "join articles a on oa.IdArticle=a.IdArticle \n" +
                        "join Shops s on s.IdShop = a.IdShop\n" +
                        "where IdOrder=?"; 
                try (
                    PreparedStatement psShopCities = conn.prepareStatement(queryShopCities);  ) {
                    psShopCities.setInt(1, i);
                    System.out.println("shop cities for given order");
                    try(ResultSet rsShopCities = psShopCities.executeQuery()) {
                        
                        while(rsShopCities.next()) {
                            System.out.println(rsShopCities.getInt(1));
                            shopCities.add(rsShopCities.getInt(1));   
                        }
                        
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //calculate nearest shop
                int nearestShop = -1; 
                int nearestDistance = Integer.MAX_VALUE; 
                int distance;
                for(int idCity:shopCities){
                    distance = DijkstraAlgorithm.calculateShortestPath(graph, customerCity, idCity); 
                    if(distance<nearestDistance){
                        nearestDistance = distance; 
                        nearestShop=idCity; 
                    }
                }
                //step 1: send from all shops to nearest shop, write it to db
                for(int idCity:shopCities){
                    if(idCity!=nearestShop){
                        DijkstraAlgorithm.calculateShortestPathDBInsert(i, graph, idCity, nearestShop);  
                    }
                }
                //step 2: send from nearest shop to customer city 
                DijkstraAlgorithm.calculateShortestPathDBInsert(i, graph, nearestShop, customerCity); 
                
                
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;

    }

    @Override
    public BigDecimal getFinalPrice(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "EXECUTE SP_FINAL_PRICE ?"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    System.out.println("final price for given order is:"+rs.getBigDecimal(1));
                    return rs.getBigDecimal(1); 
                }
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public BigDecimal getDiscountSum(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "EXECUTE SP_FINAL_DISCOUNT ?"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    System.out.println("final discount for given order is:"+rs.getBigDecimal(1));
                    return rs.getBigDecimal(1); 
                }
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getState(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select State\n" +
                        "from Orders\n" +
                        "where IdOrder=?"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    System.out.println("state for selected order is:"+rs.getString(1));
                    return rs.getString(1); 
                }
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Calendar getSentTime(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select TimeSent\n" +
                        "from Orders\n" +
                        "where IdOrder=?"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    Calendar calendar = Calendar.getInstance(); 
                    calendar.setTime(rs.getDate(1));
                    System.out.println("sent time is "+calendar.getTime());
                    return calendar; 
                }
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;    
    }

    @Override
    public Calendar getRecievedTime(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select TimeReceived\n" +
                        "from Orders\n" +
                        "where IdOrder=?"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    Calendar calendar = Calendar.getInstance(); 
                    calendar.setTime(rs.getDate(1));
                    System.out.println("received time is "+calendar.getTime());
                    return calendar; 
                }
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;   
    }

    @Override
    public int getBuyer(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select IdCustomer\n" +
                        "from Orders\n" +
                        "where IdOrder=?"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    System.out.println("customer for selected order is:"+rs.getInt(1));
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
    public int getLocation(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
