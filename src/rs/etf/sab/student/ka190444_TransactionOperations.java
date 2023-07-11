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
import rs.etf.sab.operations.TransactionOperations;

/**
 *
 * @author karad
 */
public class ka190444_TransactionOperations implements TransactionOperations {

    @Override
    public BigDecimal getBuyerTransactionsAmmount(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select sum(t.Amount)\n" +
                        "from Transactions t join Orders o\n" +
                        "on t.IdOrder=o.IdOrder\n" +
                        "where o.IdCustomer=?\n" +
                        "group by o.IdCustomer"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    System.out.println("sum of all transactions for buyer:"+rs.getBigDecimal(1));
                    return rs.getBigDecimal(1); 
                }
                else{
                    return new BigDecimal(0); 
                }
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1); 
    }

    @Override
    public BigDecimal getShopTransactionsAmmount(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select sum(amount)\n" +
                        "from Transactions \n" +
                        "where IdShop=?"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    System.out.println("sum of all transactions for shop:"+rs.getBigDecimal(1));
                    return rs.getBigDecimal(1); 
                }
                else{
                    return new BigDecimal(0); 
                }
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1); 
    }

    @Override
    public List<Integer> getTransationsForBuyer(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select t.IdTransaction\n" +
                        "from Transactions t join Orders o\n" +
                        "on t.IdOrder = o.IdOrder\n" +
                        "where o.IdCustomer=?"; 
        List<Integer> transactions = new ArrayList<>(); 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                System.out.println("transactions for buyer:");
                while(rs.next()) {
                    transactions.add(rs.getInt(1));
                    System.out.println(rs.getInt(1));
                }
                return transactions; 
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; 
    }

    @Override
    public int getTransactionForBuyersOrder(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select IdTransaction\n" +
                        "from Transactions\n" +
                        "where IdOrder=?"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    System.out.println("transaction for order is:"+rs.getInt(1));
                    return rs.getInt(1); 
                }
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; 
    }

    @Override
    public int getTransactionForShopAndOrder(int i, int i1) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select IdTransaction\n" +
                        "from Transactions\n" +
                        "where IdOrder=? and IdShop=?"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            stmt.setInt(2, i1);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    System.out.println("transaction for order is:"+rs.getInt(1));
                    return rs.getInt(1); 
                }
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; 
    }

    @Override
    public List<Integer> getTransationsForShop(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select IdTransaction \n" +
                        "from Transactions\n" +
                        "where IdShop=?"; 
        List<Integer> transactions = new ArrayList<>(); 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                System.out.println("transactions for shop:");
                while(rs.next()) {
                    transactions.add(rs.getInt(1));
                    System.out.println(rs.getInt(1));
                }
                return transactions; 
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Calendar getTimeOfExecution(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query =  "select ExecutionTime\n" +
                        "from Transactions\n" +
                        "where IdTransaction=?"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    System.out.println("transaction execution time is:"+rs.getDate(1));
                    Calendar calendar = Calendar.getInstance(); 
                    calendar.setTime(rs.getDate(1));
                    return calendar; 
                }
                else 
                    return null; 
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; 
    }

    @Override
    public BigDecimal getAmmountThatBuyerPayedForOrder(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select sum(Amount)\n" +
                        "from Transactions t join Orders o\n" +
                        "on t.IdOrder=o.IdOrder\n" +
                        "where o.IdOrder=?"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    System.out.println("amount for order is:"+rs.getBigDecimal(1));
                    return rs.getBigDecimal(1); 
                }
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1); 
    }

    @Override
    public BigDecimal getAmmountThatShopRecievedForOrder(int i, int i1) {//int orderId, int shopId
        Connection conn = DB.getInstance().getConnection();
        String query = "select 0.95*sum(Amount)\n" +
                        "from Transactions\n" +
                        "where IdShop=? and IdOrder=?"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i1);
            stmt.setInt(2, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    System.out.println("amount for order is:"+rs.getBigDecimal(1));
                    return rs.getBigDecimal(1); 
                }
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1); 
    }

    @Override
    public BigDecimal getTransactionAmount(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select Amount\n" +
                        "from Transactions\n" +
                        "where IdTransaction=?"; 
        try (
            PreparedStatement stmt = conn.prepareStatement(query);  ) {
            stmt.setInt(1, i);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    System.out.println("amount for transaction is:"+rs.getBigDecimal(1));
                    return rs.getBigDecimal(1); 
                }
            } catch (SQLException ex) {
                Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ka190444_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1); 
    }

    @Override
    public BigDecimal getSystemProfit() {//0.95*(sum(all orders))
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
