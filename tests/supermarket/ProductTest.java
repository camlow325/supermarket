package supermarket;

import org.junit.Test;
import org.junit.Assert;

/**
 * Test for the Product class
 */
public class ProductTest
{
    /**
     * Validate that an IllegalArgumentException is thrown when a null id argument is passed into a
     * Product object's constructor.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullNameInConstructorThrowsIllegalArgumentException()
    {
        new Product(null, 0);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when an empty id argument is passed into a
     * Product object's constructor.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testEmptyNameInConstructorThrowsIllegalArgumentException()
    {
        new Product("", 0);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a negative unit price argument is passed into a
     * Product object's constructor.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNegativeUnitPriceInConstructorThrowsIllegalArgumentException()
    {
        new Product("A", -1);
    }

    /**
     * Validate that the id passed into a Product object's constructor can be retrieved via calling the getId()
     * method on the same Product object.
     */
    @Test
    public void testCanGetId()
    {
        String name = "A";
        Product product = new Product(name, 2);
        Assert.assertEquals("Unexpected name retrieved", name, product.getId());
    }

    /**
     * Validate that the unit price passed into a Product object's constructor can be retrieved via calling the
     * getUnitPrice() method on the same Product object.
     */
    @Test
    public void testCanGetUnitPrice()
    {
        int price = 2;
        Product product = new Product("A", price);
        Assert.assertEquals("Unexpected price retrieved", price, product.getUnitPrice());
    }
}