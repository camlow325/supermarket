package supermarket;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the Item class
 */
public class ItemTest
{
    /**
     * Validate that an IllegalArgumentException is thrown when a null string argument is passed into an Item
     * object's constructor
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullIdInConstructorThrowsIllegalArgumentException()
    {
        new Item(null, 0);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when an empty string argument is passed into an Item
     * object's constructor
     */
    @Test(expected=IllegalArgumentException.class)
    public void testEmptyIdInConstructorThrowsIllegalArgumentException()
    {
        new Item("", 0);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a negative quantity argument is passed into an Item
     * object's constructor
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNegativeQuantityInConstructorThrowsIllegalArgumentException()
    {
        new Item("A", -1);
    }

    /**
     * Validate that the product id passed into an Item's constructor can be retrieved via the same object's
     * getProductId() method.
     */
    @Test
    public void testCanGetProductId()
    {
        String id = "A";
        Item item = new Item(id, 2);
        Assert.assertEquals("Unexpected id retrieved", id, item.getProductId());
    }

    /**
     * Validate that the quantity passed into an Item's constructor can be retrieved via the same object's
     * getQuantity() method.
     */
    @Test
    public void testCanGetQuantity()
    {
        int quantity = 3;
        Item item = new Item("Bogus", quantity);
        Assert.assertEquals("Unexpected quantity retrieved", quantity, item.getQuantity());
    }
}
