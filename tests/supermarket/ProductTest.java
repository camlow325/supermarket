package supermarket;

import org.junit.Test;
import org.junit.Assert;

/**
 *
 */
public class ProductTest
{
    @Test(expected=IllegalArgumentException.class)
    public void testNullNameInConstructorThrowsIllegalArgumentException()
    {
        new Product(null, 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testEmptyNameInConstructorThrowsIllegalArgumentException()
    {
        new Product("", 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNegativeUnitPriceInConstructorThrowsIllegalArgumentException()
    {
        new Product("A", -1);
    }

    @Test
    public void testCanGetId()
    {
        String name = "A";
        Product product = new Product(name, 2);
        Assert.assertEquals("Unexpected name retrieved", name, product.getId());
    }

    @Test
    public void testCanGetUnitPrice()
    {
        int price = 2;
        Product product = new Product("A", price);
        Assert.assertEquals("Unexpected price retrieved", price, product.getUnitPrice());
    }
}
