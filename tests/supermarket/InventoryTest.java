package supermarket;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class InventoryTest
{
    @Test
    public void testNoProductsAvailableWhenNonePassedDuringConstruction()
    {
        Inventory inventory = new Inventory();
        Collection<IProduct> productsRetrieved = inventory.getProducts();
        Assert.assertNotNull("Products retrieved after creation are null", productsRetrieved);
        Assert.assertEquals("Unexpected number of products after creation", 0, productsRetrieved.size());
    }

    @Test
    public void testProductsSetOnceAfterConstructionAreRetained()
    {
        List<IProduct> originalProducts = new ArrayList<IProduct>();
        originalProducts.add(new Product("A", 30));
        originalProducts.add(new Product("B", 10));
        originalProducts.add(new Product("C", 20));

        Inventory inventory = new Inventory();
        inventory.setProducts(originalProducts);

        Collection<IProduct> productsRetrieved = inventory.getProducts();
        Assert.assertNotNull("Products retrieved after set are null", productsRetrieved);
        Assert.assertEquals("Unexpected number of products after set", originalProducts.size(),
                productsRetrieved.size());
        Assert.assertTrue("Products retrieved does not contain all original products",
                productsRetrieved.containsAll(originalProducts));
    }

    @Test
    public void testProductsSetTwiceAfterConstructionAreRetained()
    {
        List<IProduct> originalProducts = new ArrayList<IProduct>();
        originalProducts.add(new Product("A", 30));
        originalProducts.add(new Product("B", 10));
        originalProducts.add(new Product("C", 20));

        List<IProduct> newProducts = new ArrayList<IProduct>();
        originalProducts.add(new Product("D", 10));
        originalProducts.add(new Product("E", 40));

        Inventory inventory = new Inventory();
        inventory.setProducts(originalProducts);
        inventory.setProducts(newProducts);

        Collection<IProduct> productsRetrieved = inventory.getProducts();
        Assert.assertNotNull("Products retrieved after set are null", productsRetrieved);
        Assert.assertEquals("Unexpected number of products after set", newProducts.size(),
                productsRetrieved.size());
        Assert.assertTrue("Products retrieved does not contain all original products",
                productsRetrieved.containsAll(newProducts));
    }

    @Test
    public void testProductsPassedDuringConstructionAreRetained()
    {
        List<IProduct> originalProducts = new ArrayList<IProduct>();
        originalProducts.add(new Product("A", 30));
        originalProducts.add(new Product("B", 10));
        originalProducts.add(new Product("C", 20));

        Inventory inventory = new Inventory(originalProducts);

        Collection<IProduct> productsRetrieved = inventory.getProducts();
        Assert.assertNotNull("Products retrieved are null", productsRetrieved);
        Assert.assertEquals("Unexpected number of products", originalProducts.size(), productsRetrieved.size());
        Assert.assertTrue("Products retrieved does not contain all original products",
                productsRetrieved.containsAll(originalProducts));
    }

    @Test
    public void testCanFindProduct()
    {
        Product secondProduct = new Product("B", 10);

        List<IProduct> originalProducts = new ArrayList<IProduct>();
        originalProducts.add(new Product("A", 30));
        originalProducts.add(secondProduct);
        originalProducts.add(new Product("C", 20));

        Inventory inventory = new Inventory(originalProducts);
        IProduct productRetrieved = inventory.getProduct(secondProduct.getId());

        Assert.assertSame("Unexpected product found", secondProduct, productRetrieved);
    }

    @Test
    public void testCannotFindProductWhenInventoryNotEmpty()
    {
        List<IProduct> originalProducts = new ArrayList<IProduct>();
        originalProducts.add(new Product("A", 30));
        originalProducts.add(new Product("B", 10));
        originalProducts.add(new Product("C", 20));

        Inventory inventory = new Inventory(originalProducts);
        IProduct productRetrieved = inventory.getProduct("D");

        Assert.assertNull("Product unexpectedly found", productRetrieved);
    }

    @Test
    public void testCannotFindProductWhenInventoryEmpty()
    {
        Inventory inventory = new Inventory();
        IProduct productRetrieved = inventory.getProduct("A");

        Assert.assertNull("Product unexpectedly found", productRetrieved);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testAttemptToFindProductWithNullIdThrowsIllegalArgumentException()
    {
        new Inventory().getProduct(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testAttemptToFindProductWithEmptyIdThrowsIllegalArgumentException()
    {
        new Inventory().getProduct("");
    }
}