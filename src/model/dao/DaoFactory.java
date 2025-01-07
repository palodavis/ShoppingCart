package model.dao;

import db.DB;

public class DaoFactory {

    public static ProductDao createProductDao() {
        return new ProductDaoJDBC(DB.getConnection());
    }

    public static ShoppingDao createShoppingDao() {
        return new ShoppingDaoJDBC(DB.getConnection());
    }
}
