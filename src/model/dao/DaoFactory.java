package model.dao;

import db.DB;

public class DaoFactory {

    public static ProductDao createProductDao() {
        return new ProductDaoJDBC(DB.getConnection());
    }
}
