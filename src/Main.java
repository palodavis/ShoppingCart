import model.Product;
import model.dao.DaoFactory;
import model.dao.ProductDao;

public class Main {
    public static void main(String[] args) {
        System.out.println("1 - Test product inserction");
        ProductDao productDao = DaoFactory.createProductDao();
        //Product newProduct = new Product(null, "Notebook", "Computing", 3500, 2);
        //productDao.insert(newProduct);
        //System.out.println("Inserted! Id = " + newProduct.getIdProduct());

        System.out.println("2 - Test product deletion");
        productDao.delete(3);
        System.out.println("Product Deleted!");

        System.out.println("3 - Test product update");
        Product product = new Product();
        product = productDao.searchId(2);
        product.setName("Laptop");
        productDao.update(product);
        System.out.println("Product Updated!");
    }
}