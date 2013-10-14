package supermarket;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 */
public class BuyXGetYFreePriceRuleTest
{
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test(expected=IllegalArgumentException.class)
    public void testNullProductIdInConstructorThrowsIllegalArgumentException()
    {
        new BuyXGetYFreePriceRule(null, 1, 2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testZeroQuantityToBuyInConstructorThrowsIllegalArgumentException()
    {
        new BuyXGetYFreePriceRule("A", 0, 2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNegativeQuantityToBuyInConstructorThrowsIllegalArgumentException()
    {
        new BuyXGetYFreePriceRule("A", -1, 2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testZeroQuantityFreeInConstructorThrowsIllegalArgumentException()
    {
        new BuyXGetYFreePriceRule("A", 1, 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNegativeQuantityFreeInConstructorThrowsIllegalArgumentException()
    {
        new BuyXGetYFreePriceRule("A", 1, -1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNullCartInProcessCallThrowsIllegalArgumentException()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        new BuyXGetYFreePriceRule("A", 1, 1).process(null, inventory);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNullLookupInProcessCallThrowsIllegalArgumentException()
    {
        final IShoppingCart cart = context.mock(IShoppingCart.class);
        new BuyXGetYFreePriceRule("A", 1, 1).process(cart, null);
    }

    @Test
    public void testCanProcessWithMatchingItemInCartAndInventoryWhichHasQuantityMeetingRule()
    {
        final String productId = "B";
        final int productUnitCost = 50;
        final int cartItemQuantity = 5;

        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);
        final IItem item = context.mock(IItem.class);

        context.checking(new Expectations() {{
            oneOf(cart).getItem(productId);
            will(returnValue(item));

            oneOf(item).getQuantity();
            will(returnValue(cartItemQuantity));

            oneOf(inventory).getProduct(productId);
            will(returnValue(new Product(productId, productUnitCost)));

            oneOf(cart).setItemQuantity(productId, 0);
        }});

        BuyXGetYFreePriceRule rule = new BuyXGetYFreePriceRule(productId, 3, 2);
        Assert.assertEquals("Unexpected subtotal returned from process", 150,
                rule.process(cart, inventory));
    }

    @Test
    public void testCanProcessWithNoMatchingItemInCart()
    {
        final String productId = "B";

        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);

        context.checking(new Expectations() {{
            oneOf(cart).getItem(productId);
            will(returnValue(null));
        }});

        BuyXGetYFreePriceRule rule = new BuyXGetYFreePriceRule(productId, 3, 2);
        Assert.assertEquals("Unexpected subtotal returned from process", 0,
                rule.process(cart, inventory));
    }

    @Test
    public void testCanProcessWithMatchingItemInCartWhichHasNoQuantity()
    {
        final String productId = "B";
        final int cartItemQuantity = 0;

        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);
        final IItem item = context.mock(IItem.class);

        context.checking(new Expectations() {{
            oneOf(cart).getItem(productId);
            will(returnValue(item));

            oneOf(item).getQuantity();
            will(returnValue(cartItemQuantity));
        }});

        BuyXGetYFreePriceRule rule = new BuyXGetYFreePriceRule(productId, 3, 2);
        Assert.assertEquals("Unexpected subtotal returned from process", 0,
                rule.process(cart, inventory));
    }

    @Test
    public void testCanProcessWithMatchingItemInCartWhichHasNonZeroQuantityButIsNotInInventory()
    {
        final String productId = "B";

        final int cartItemQuantity = 5;

        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);
        final IItem item = context.mock(IItem.class);

        context.checking(new Expectations() {{
            oneOf(cart).getItem(productId);
            will(returnValue(item));

            oneOf(item).getQuantity();
            will(returnValue(cartItemQuantity));

            oneOf(inventory).getProduct(productId);
            will(returnValue(null));
        }});

        BuyXGetYFreePriceRule rule = new BuyXGetYFreePriceRule(productId, 3, 2);
        Assert.assertEquals("Unexpected subtotal returned from process", 0,
                rule.process(cart, inventory));
    }

    @Test
    public void testCanProcessWithMatchingItemInCartAndInventoryWhichHasQuantityBelowRule()
    {
        final String productId = "B";
        final int productUnitCost = 50;
        final int cartItemQuantity = 4;

        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);
        final IItem item = context.mock(IItem.class);

        context.checking(new Expectations() {{
            oneOf(cart).getItem(productId);
            will(returnValue(item));

            oneOf(item).getQuantity();
            will(returnValue(cartItemQuantity));

            oneOf(inventory).getProduct(productId);
            will(returnValue(new Product(productId, productUnitCost)));

            oneOf(cart).setItemQuantity(productId, cartItemQuantity);
        }});

        BuyXGetYFreePriceRule rule = new BuyXGetYFreePriceRule(productId, 3, 2);
        Assert.assertEquals("Unexpected subtotal returned from process", 0,
                rule.process(cart, inventory));
    }

    @Test
    public void testCanProcessWithMatchingItemInCartAndInventoryWhichHasQuantityAboveRule()
    {
        final String productId = "B";
        final int productUnitCost = 50;
        final int cartItemQuantity = 13;

        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);
        final IItem item = context.mock(IItem.class);

        context.checking(new Expectations() {{
            oneOf(cart).getItem(productId);
            will(returnValue(item));

            oneOf(item).getQuantity();
            will(returnValue(cartItemQuantity));

            oneOf(inventory).getProduct(productId);
            will(returnValue(new Product(productId, productUnitCost)));

            oneOf(cart).setItemQuantity(productId, 3);
        }});

        BuyXGetYFreePriceRule rule = new BuyXGetYFreePriceRule(productId, 3, 2);
        Assert.assertEquals("Unexpected subtotal returned from process", 300,
                rule.process(cart, inventory));
    }
}
