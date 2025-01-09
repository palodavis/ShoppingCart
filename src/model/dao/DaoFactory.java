package model.dao;

import db.DB;
import model.dao.impl.CartDaoJDBC;
import model.dao.impl.ProductDaoJDBC;
import model.dao.impl.ShoppingDaoJDBC;

public class DaoFactory {

    public static ProductDao createProductDao() {
        return new ProductDaoJDBC(DB.getConnection());
    }

    public static ShoppingDao createShoppingDao() {
        return new ShoppingDaoJDBC(DB.getConnection());
    }

    public static CartDao createCartDao() {
        return new CartDaoJDBC(DB.getConnection());
    }
}
