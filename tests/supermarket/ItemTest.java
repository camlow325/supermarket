package supermarket;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ItemTest
{
    @Test(expected=IllegalArgumentException.class)
    public void testNullIdInConstructorThrowsIllegalArgumentException()
    {
        new Item(null, 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testEmptyIdInConstructorThrowsIllegalArgumentException()
    {
        new Item("", 0);
    }

    @Test
    public void testCanGetId()
    {
        String id = "A";
        Item item = new Item(id, 2);
        Assert.assertEquals("Unexpected id retrieved", id, item.getProductId());
    }

    @Test
    public void testGetQuantity()
    {
        int quantity = 3;
        Item item = new Item("Bogus", quantity);
        Assert.assertEquals("Unexpected quantity retrieved", quantity, item.getQuantity());
    }

    @Test
    public void testSetQuantity()
    {
        int newQuantity = 5;
        Item item = new Item("Bogus", 3);

        item.setQuantity(newQuantity);
        Assert.assertEquals("Unexpected quantity retrieved after first set", newQuantity, item.getQuantity());

        newQuantity = 7;

        item.setQuantity(newQuantity);
        Assert.assertEquals("Unexpected quantity retrieved after second set", newQuantity, item.getQuantity());
    }
}
