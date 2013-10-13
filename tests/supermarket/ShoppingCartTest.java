package supermarket;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class ShoppingCartTest
{
    @Test(expected=IllegalArgumentException.class)
    public void testNullItemsInConstructorThrowsIllegalArgumentException()
    {
        new ShoppingCart(null);
    }

    @Test
    public void testCanCaptureZeroItemsCorrectly()
    {
        List<IItem> expectedItems = new ArrayList<IItem>();

        ShoppingCart cart = new ShoppingCart("");
        Collection<IItem> cartItems = cart.getItems();

        validateItems(expectedItems, cartItems);
    }

    @Test
    public void testCanCaptureOneItemCorrectly()
    {
        String productId = "A";

        List<IItem> expectedItems = new ArrayList<IItem>();
        expectedItems.add(new Item(productId, 1));

        ShoppingCart cart = new ShoppingCart(productId);
        Collection<IItem> cartItems = cart.getItems();

        validateItems(expectedItems, cartItems);
    }

    @Test
    public void testCanCaptureMultipleItemsCorrectly()
    {
        List<IItem> expectedItems = new ArrayList<IItem>();
        expectedItems.add(new Item("A", 3));
        expectedItems.add(new Item("B", 5));
        expectedItems.add(new Item("C", 1));

        ShoppingCart cart = new ShoppingCart("ABBACBBAB");
        Collection<IItem> cartItems = cart.getItems();

        validateItems(expectedItems, cartItems);
    }

    @Test
    public void testCanFindItem()
    {
        String productId = "A";

        ShoppingCart cart = new ShoppingCart("ABBACBBAB");
        IItem item = cart.getItem(productId);

        validateItem(item, productId, 3);
    }

    @Test
    public void testCannotFindNonexistentItem()
    {
        ShoppingCart cart = new ShoppingCart("ABBACBBAB");
        IItem item = cart.getItem("D");

        Assert.assertNull("Item retrieved is non-null", item);
    }

    @Test
    public void testCannotFindItemWhenCartEmpty()
    {
        ShoppingCart cart = new ShoppingCart("");
        IItem item = cart.getItem("A");

        Assert.assertNull("Item retrieved is non-null", item);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNullItemsInGetItemCallThrowsIllegalArgumentException()
    {
        new ShoppingCart("").getItem(null);
    }

    @Test
    public void testCanSetItemQuantity()
    {
        String productId = "A";
        int newQuantity = 1;
        Item itemToUpdate = new Item(productId, 3);

        List<IItem> expectedItems = new ArrayList<IItem>();
        expectedItems.add(itemToUpdate);
        expectedItems.add(new Item("B", 5));
        expectedItems.add(new Item("C", 1));

        ShoppingCart cart = new ShoppingCart("ABBACBBAB");
        Assert.assertTrue("Set quantity failed", cart.setItemQuantity(productId, newQuantity));

        IItem cartItem = cart.getItem(productId);
        validateItem(cartItem, productId, newQuantity);

        itemToUpdate.setQuantity(newQuantity);
        Collection<IItem> cartItems = cart.getItems();
        validateItems(expectedItems, cartItems);
    }

    @Test
    public void testCannotSetItemQuantityForNonexistentItem()
    {
        List<IItem> expectedItems = new ArrayList<IItem>();
        expectedItems.add(new Item("A", 3));
        expectedItems.add(new Item("B", 5));
        expectedItems.add(new Item("C", 1));

        ShoppingCart cart = new ShoppingCart("ABBACBBAB");
        Assert.assertFalse("Set quantity succeeded", cart.setItemQuantity("D", 1));

        // Validate that nothing changed since quantity set should have failed
        Collection<IItem> cartItems = cart.getItems();
        validateItems(expectedItems, cartItems);
    }

    @Test
    public void testNullProductIdInSetItemQuantityThrowsIllegalArgumentException()
    {
        List<IItem> expectedItems = new ArrayList<IItem>();
        expectedItems.add(new Item("A", 3));
        expectedItems.add(new Item("B", 5));
        expectedItems.add(new Item("C", 1));

        ShoppingCart cart = new ShoppingCart("ABBACBBAB");

        try
        {
            cart.setItemQuantity(null, 23);
            Assert.fail("Expected 'IllegalArgumentException' to be thrown for null product id but none thrown");
        }
        catch (IllegalArgumentException iae)
        {
        }

        // Validate that nothing changed since quantity set should have failed
        Collection<IItem> cartItems = cart.getItems();
        validateItems(expectedItems, cartItems);
    }

    @Test
    public void testEmptyProductIdInSetItemQuantityThrowsIllegalArgumentException()
    {
        List<IItem> expectedItems = new ArrayList<IItem>();
        expectedItems.add(new Item("A", 3));
        expectedItems.add(new Item("B", 5));
        expectedItems.add(new Item("C", 1));

        ShoppingCart cart = new ShoppingCart("ABBACBBAB");

        try
        {
            cart.setItemQuantity("", 23);
            Assert.fail("Expected 'IllegalArgumentException' to be thrown for empty product id but none thrown");
        }
        catch (IllegalArgumentException iae)
        {
        }

        // Validate that nothing changed since quantity set should have failed
        Collection<IItem> cartItems = cart.getItems();
        validateItems(expectedItems, cartItems);
    }

    @Test
    public void testNegativeQuantityInSetItemQuantityThrowsIllegalArgumentException()
    {
        List<IItem> expectedItems = new ArrayList<IItem>();
        expectedItems.add(new Item("A", 3));
        expectedItems.add(new Item("B", 5));
        expectedItems.add(new Item("C", 1));

        ShoppingCart cart = new ShoppingCart("ABBACBBAB");

        try
        {
            cart.setItemQuantity("A", -5);
            Assert.fail("Expected 'IllegalArgumentException' to be thrown for negative quantity but none thrown");
        }
        catch (IllegalArgumentException iae)
        {
        }

        // Validate that nothing changed since quantity set should have failed
        Collection<IItem> cartItems = cart.getItems();
        validateItems(expectedItems, cartItems);
    }

    private void validateItem(IItem item, String expectedProductId, int expectedQuantity)
    {
        Assert.assertNotNull("Item retrieved is null", item);
        Assert.assertEquals("Unexpected product id returned for item", expectedProductId, item.getProductId());
        Assert.assertEquals("Unexpected quantity returned for item", expectedQuantity, item.getQuantity());
    }

    private void validateItems(List<IItem> expectedItems, Collection<IItem> cartItems)
    {
        Assert.assertNotNull("Items in cart null", cartItems);
        Assert.assertEquals("Unexpected number of items in cart", expectedItems.size(), cartItems.size());

        String message = getComparisonFailureMessage(expectedItems, cartItems);
        if (!message.isEmpty())
        {
            Assert.fail("Errors comparing expected items to cart items: " + message);
        }
    }

    private String getComparisonFailureMessage(List<IItem> expectedItems, Collection<IItem> cartItems)
    {
        StringBuilder builder = new StringBuilder(": ");

        int i = -1;
        for (IItem cartItem : cartItems)
        {
            i++;

            if (cartItem == null)
            {
                builder.append("Found null item at cart position ");
                builder.append(i);
                builder.append("; ");
                continue;
            }

            for (int j = 0; j < expectedItems.size(); j++)
            {
                IItem expectedItem = expectedItems.get(j);

                if (expectedItem.getProductId().equals(cartItem.getProductId()))
                {
                    if (expectedItem.getQuantity() != cartItem.getQuantity())
                    {
                        builder.append("Found mismatched quantity for product id ");
                        builder.append(expectedItem.getProductId());
                        builder.append(".  Expected: ");
                        builder.append(expectedItem.getQuantity());
                        builder.append(", Actual: ");
                        builder.append(cartItem.getQuantity());
                        builder.append("; ");
                    }

                    expectedItems.remove(j);
                    break;
                }
            }
        }

        for (IItem expectedItem : expectedItems)
        {
            builder.append("Did not find expected item in cart items for product id '");
            builder.append(expectedItem.getProductId());
            builder.append("'; ");
        }

        return builder.substring(0, builder.length() - 2);
    }
}
