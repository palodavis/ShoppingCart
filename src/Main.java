import model.Product;
import model.dao.DaoFactory;
import model.dao.ProductDao;

public class Main {
    public static void main(String[] args) {
        ProductDao productDao = DaoFactory.createProductDao();
        Product newProduct = new Product(null, "Notebook", "Computing", 3500, 2);
        productDao.insert(newProduct);
        System.out.println("Inserted! Id = " + newProduct.getIdProduct());
    }
}