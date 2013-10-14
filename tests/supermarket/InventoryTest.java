package supermarket;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Tests for the inventory class.
 */
public class InventoryTest
{
    /**
     * Validate that no products can be obtained when none are passed during construction of an Inventory object.
     */
    @Test
    public void testCannotGetProductsWhenNonePassedDuringConstruction()
    {
        Inventory inventory = new Inventory();
        Collection<IProduct> productsRetrieved = inventory.getProducts();
        Assert.assertNotNull("Products retrieved after creation are null", productsRetrieved);
        Assert.assertEquals("Unexpected number of products after creation", 0, productsRetrieved.size());
    }

    /**
     * Validate that products can be obtained when set once after construction of an Inventory object.
     */
    @Test
    public void testCanGetProductsSetOnceAfterConstruction()
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

    /**
     * Validate that products can be obtained when set twice after construction of an Inventory object.
     */
    @Test
    public void testCanGetProductsSetTwiceAfterConstruction()
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

    /**
     * Validate that products can be obtained when passed during construction of an Inventory object.
     */
    @Test
    public void testCanGetProductsPassedDuringConstruction()
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

    /**
     * Validate that a single product can be obtained from an Inventory object.
     */
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

    /**
     * Validate that no product can be obtained when the Inventory is not empty but the id of the product to
     * be obtained is not in the Inventory.
     */
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

    /**
     * Validate that no product can be obtained when the Inventory is empty.
     */
    @Test
    public void testCannotFindProductWhenInventoryEmpty()
    {
        Inventory inventory = new Inventory();
        IProduct productRetrieved = inventory.getProduct("A");

        Assert.assertNull("Product unexpectedly found", productRetrieved);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a null argument is passed into an Inventory
     * object's getProduct() method
     */
    @Test(expected=IllegalArgumentException.class)
    public void testAttemptToFindProductWithNullIdThrowsIllegalArgumentException()
    {
        new Inventory().getProduct(null);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when an empty string argument is passed into an Inventory
     * object's getProduct() method
     */
    @Test(expected=IllegalArgumentException.class)
    public void testAttemptToFindProductWithEmptyIdThrowsIllegalArgumentException()
    {
        new Inventory().getProduct("");
    }
}