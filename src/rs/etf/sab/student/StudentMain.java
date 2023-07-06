package rs.etf.sab.student;

import rs.etf.sab.operations.*;
import org.junit.Test;
import rs.etf.sab.tests.*;

import java.util.Calendar;

public class StudentMain {

    public static void main(String[] args) {

        ArticleOperations articleOperations = new ka190444_ArticleOperations(); // Change this for your implementation (points will be negative if interfaces are not implemented).
        BuyerOperations buyerOperations = new ka190444_BuyerOperations();
        CityOperations cityOperations = new ka190444_CityOperations();
        GeneralOperations generalOperations = new ka190444_GeneralOperations();
        OrderOperations orderOperations = null;
        ShopOperations shopOperations = null;
        TransactionOperations transactionOperations = null;

        TestHandler.createInstance(
                articleOperations,
                buyerOperations,
                cityOperations,
                generalOperations,
                orderOperations,
                shopOperations,
                transactionOperations
        );

        TestRunner.runTests();
    }
}
