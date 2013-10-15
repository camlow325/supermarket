package supermarket;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
public class BaseUnitPriceRuleTest
{
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    /**
     * Validate that an IllegalArgumentException is thrown when a null cart is passed into a BaseUnitPriceRule
     * object's process() method.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullCartInProcessCallThrowsIllegalArgumentException()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        new BaseUnitPriceRule().process(null, inventory);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a null inventory lookup is passed into an
     * BaseUnitPriceRule object's process() method.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullLookupInProcessCallThrowsIllegalArgumentException()
    {
        final IShoppingCart cart = context.mock(IShoppingCart.class);
        new BaseUnitPriceRule().process(cart, null);
    }

    /**
     * Validate that items can be properly scanned when the cart is not empty.
     */
    @Test
    public void testCanScanItemsWithNonEmptyCart()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);

        final BaseUnitPriceRule baseUnitPriceRule = new BaseUnitPriceRule();

        final IItem firstItem = context.mock(IItem.class, "item 1");
        final IItem secondItem = context.mock(IItem.class, "item 2");

        final IProduct firstProduct = context.mock(IProduct.class, "product 1");
        final IProduct secondProduct = context.mock(IProduct.class, "product 2");

        final Collection<IItem> cartItems = new ArrayList<IItem>();
        cartItems.add(firstItem);
        cartItems.add(secondItem);

        context.checking(new Expectations() {{
            oneOf(cart).getItems();
            will(returnValue(cartItems));

            oneOf(firstItem).getQuantity();
            will(returnValue(5));

            oneOf(firstItem).getProductId();
            will(returnValue("A"));

            oneOf(inventory).getProduct("A");
            will(returnValue(firstProduct));

            oneOf(firstProduct).getUnitPrice();
            will(returnValue(20));

            oneOf(cart).setItemQuantity("A", 0);

            oneOf(secondItem).getQuantity();
            will(returnValue(2));

            oneOf(secondItem).getProductId();
            will(returnValue("B"));

            oneOf(inventory).getProduct("B");
            will(returnValue(secondProduct));

            oneOf(secondProduct).getUnitPrice();
            will(returnValue(40));

            oneOf(cart).setItemQuantity("B", 0);
        }});

        Assert.assertEquals("Unexpected total returned from checkout", 180,
                baseUnitPriceRule.process(cart, inventory));
    }

    /**
     * Validate that a cart with no items cannot be scanned.
     */
    @Test
    public void testCannotScanItemsWhenCartEmpty()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);

        final BaseUnitPriceRule baseUnitPriceRule = new BaseUnitPriceRule();

        final Collection<IItem> cartItems = new ArrayList<IItem>();

        context.checking(new Expectations() {{
            oneOf(cart).getItems();
            will(returnValue(cartItems));
        }});

        Assert.assertEquals("Unexpected total returned from checkout", 0,
                baseUnitPriceRule.process(cart, inventory));
    }

    /**
     * Validate that items having no quantity cannot be scanned.
     */
    @Test
    public void testCannotScanItemsHavingNoQuantity()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);

        final BaseUnitPriceRule baseUnitPriceRule = new BaseUnitPriceRule();

        final IItem firstItem = context.mock(IItem.class, "item 1");
        final IItem secondItem = context.mock(IItem.class, "item 2");
        final IProduct secondProduct = context.mock(IProduct.class, "product 2");

        final Collection<IItem> cartItems = new ArrayList<IItem>();
        cartItems.add(firstItem);
        cartItems.add(secondItem);

        context.checking(new Expectations() {{
            oneOf(cart).getItems();
            will(returnValue(cartItems));

            oneOf(firstItem).getQuantity();
            will(returnValue(0));

            oneOf(secondItem).getQuantity();
            will(returnValue(2));

            oneOf(secondItem).getProductId();
            will(returnValue("B"));

            oneOf(inventory).getProduct("B");
            will(returnValue(secondProduct));

            oneOf(secondProduct).getUnitPrice();
            will(returnValue(40));

            oneOf(cart).setItemQuantity("B", 0);
        }});

        Assert.assertEquals("Unexpected total returned from checkout", 80,
                baseUnitPriceRule.process(cart, inventory));
    }

    /**
     * Validate that items not in inventory cannot be scanned.
     */
    @Test
    public void testCannotScanItemsNotInInventory()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);

        final BaseUnitPriceRule baseUnitPriceRule = new BaseUnitPriceRule();

        final IItem firstItem = context.mock(IItem.class, "item 1");
        final IItem secondItem = context.mock(IItem.class, "item 2");
        final IProduct secondProduct = context.mock(IProduct.class, "product 2");

        final Collection<IItem> cartItems = new ArrayList<IItem>();
        cartItems.add(firstItem);
        cartItems.add(secondItem);

        context.checking(new Expectations() {{
            oneOf(cart).getItems();
            will(returnValue(cartItems));

            oneOf(firstItem).getQuantity();
            will(returnValue(2));

            oneOf(firstItem).getProductId();
            will(returnValue("A"));

            oneOf(inventory).getProduct("A");
            will(returnValue(null));

            oneOf(secondItem).getQuantity();
            will(returnValue(5));

            oneOf(secondItem).getProductId();
            will(returnValue("B"));

            oneOf(inventory).getProduct("B");
            will(returnValue(secondProduct));

            oneOf(secondProduct).getUnitPrice();
            will(returnValue(30));

            oneOf(cart).setItemQuantity("B", 0);
        }});

        Assert.assertEquals("Unexpected total returned from checkout", 150,
                baseUnitPriceRule.process(cart, inventory));
    }
}
