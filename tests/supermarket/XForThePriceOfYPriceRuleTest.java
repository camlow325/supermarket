package supermarket;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for the XForThePriceOfYPriceRule class
 */
public class XForThePriceOfYPriceRuleTest
{
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    /**
     * Validate that an IllegalArgumentException is thrown when a null product id argument is passed into an
     * XForThePriceOfYPriceRule object's constructor.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullProductIdInConstructorThrowsIllegalArgumentException()
    {
        new XForThePriceOfYPriceRule(null, 1, 2);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when an empty product id argument is passed into a
     * XForThePriceOfYPriceRule object's constructor.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testEmptyProductIdInConstructorThrowsIllegalArgumentException()
    {
        new XForThePriceOfYPriceRule("", 1, 2);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a total quantity for rule of one is passed into an
     * XForThePriceOfYPriceRule object's constructor.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testOneTotalQuantityForRuleInConstructorThrowsIllegalArgumentException()
    {
        new XForThePriceOfYPriceRule("A", 1, 2);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a total quantity for rule of zero is passed into an
     * XForThePriceOfYPriceRule object's constructor.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testZeroTotalQuantityForRuleInConstructorThrowsIllegalArgumentException()
    {
        new XForThePriceOfYPriceRule("A", 0, 2);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a negative total quantity for rule is passed into an
     * XForThePriceOfYPriceRule object's constructor.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNegativeTotalQuantityForRuleInConstructorThrowsIllegalArgumentException()
    {
        new XForThePriceOfYPriceRule("A", -1, 2);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a quantity paid per unit price of zero is passed into an
     * XForThePriceOfYPriceRule object's constructor.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testZeroQuantityPaidPerUnitPriceInConstructorThrowsIllegalArgumentException()
    {
        new XForThePriceOfYPriceRule("A", 1, 0);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a negative quantity paid per unit price is passed
     * into an XForThePriceOfYPriceRule object's constructor.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNegativeQuantityPaidPerUnitPriceInConstructorThrowsIllegalArgumentException()
    {
        new XForThePriceOfYPriceRule("A", 1, -1);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a quantity paid per unit price is equal to that of the
     * total quantity for the rule is passed into an XForThePriceOfYPriceRule object's constructor.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testQuantityPaidPerUnitPriceEqualsTotalQuantityForRuleInConstructorThrowsIllegalArgumentException()
    {
        new XForThePriceOfYPriceRule("A", 3, 3);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a quantity paid per unit price exceeds that of the
     * total quantity for the rule is passed into an XForThePriceOfYPriceRule object's constructor.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testQuantityPaidPerUnitPriceExceedsTotalQuantityForRuleInConstructorThrowsIllegalArgumentException()
    {
        new XForThePriceOfYPriceRule("A", 3, 4);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a null cart is passed into an XForThePriceOfYPriceRule
     * object's process() method.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullCartInProcessCallThrowsIllegalArgumentException()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        new XForThePriceOfYPriceRule("A", 5, 3).process(null, inventory);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a null inventory lookup is passed into an
     * XForThePriceOfYPriceRule object's process() method.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullLookupInProcessCallThrowsIllegalArgumentException()
    {
        final IShoppingCart cart = context.mock(IShoppingCart.class);
        new XForThePriceOfYPriceRule("A", 5, 3).process(cart, null);
    }

    /**
     * Validate that when an item in the cart has sufficient quantity to meet the rule that the rule processes
     * the item quantity and cost correctly.
     */
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

        XForThePriceOfYPriceRule rule = new XForThePriceOfYPriceRule(productId, 5, 3);
        Assert.assertEquals("Unexpected subtotal returned from process", 150,
                rule.process(cart, inventory));
    }

    /**
     * Validate that when no item in the cart matches the rule's product id that nothing is processed.
     */
    @Test
    public void testCannotProcessWithNoMatchingItemInCart()
    {
        final String productId = "B";

        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);

        context.checking(new Expectations() {{
            oneOf(cart).getItem(productId);
            will(returnValue(null));
        }});

        XForThePriceOfYPriceRule rule = new XForThePriceOfYPriceRule(productId, 3, 2);
        Assert.assertEquals("Unexpected subtotal returned from process", 0,
                rule.process(cart, inventory));
    }

    /**
     * Validate that when an item in the cart matches the product id configured for the rule but the item has no
     * remaining quantity that nothing is processed.
     */
    @Test
    public void testCannotProcessWithMatchingItemInCartWhichHasNoQuantity()
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

        XForThePriceOfYPriceRule rule = new XForThePriceOfYPriceRule(productId, 3, 2);
        Assert.assertEquals("Unexpected subtotal returned from process", 0,
                rule.process(cart, inventory));
    }

    /**
     * Validate that when an item in the cart matches the product id configured for the rule but item does not exist
     * in the inventory that nothing is processed.
     */
    @Test
    public void testCannotProcessWithMatchingItemInCartWhichHasNonZeroQuantityButIsNotInInventory()
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

        XForThePriceOfYPriceRule rule = new XForThePriceOfYPriceRule(productId, 3, 2);
        Assert.assertEquals("Unexpected subtotal returned from process", 0,
                rule.process(cart, inventory));
    }

    /**
     * Validate that when an item in the cart matches the product id configured for the rule, the associated product is
     * in the inventory, the quantity of the item is greater than zero, and the quantity of the item is less than the
     * minimum necessary for the rule to be applied that nothing is processed.
     */
    @Test
    public void testCannotProcessWithMatchingItemInCartAndInventoryWhichHasQuantityBelowRule()
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

        XForThePriceOfYPriceRule rule = new XForThePriceOfYPriceRule(productId, 5, 3);
        Assert.assertEquals("Unexpected subtotal returned from process", 0,
                rule.process(cart, inventory));
    }

    /**
     * Validate that when an item in the cart has sufficient quantity to exceed two applications of the rule that the
     * rule processes the item quantity and cost correctly.
     */
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

        XForThePriceOfYPriceRule rule = new XForThePriceOfYPriceRule(productId, 5, 3);
        Assert.assertEquals("Unexpected subtotal returned from process", 300,
                rule.process(cart, inventory));
    }
}