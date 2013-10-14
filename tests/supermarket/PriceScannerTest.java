package supermarket;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Tests for the PriceScanner class
 */
public class PriceScannerTest
{
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    /**
     * Validate that an IllegalArgumentException is thrown when a null inventory argument is passed into a PriceScanner
     * object's constructor
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullInventoryInConstructorThrowsIllegalArgumentException()
    {
        new PriceScanner(null);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a null IShoppingCart argument is passed into a
     * PriceScanner object's scanItems() method
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullShoppingCartInScanItemsCallThrowsIllegalArgumentException()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        new PriceScanner(inventory).scanItems(null);
    }

    /**
     * Validate that items can be properly scanned when no price rules are in effect and the cart is empty.
     */
    @Test
    public void testCanScanItemsWithNoPriceRulesAndEmptyCart()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);

        final PriceScanner priceScanner = new PriceScanner(inventory);

        final Collection<IItem> cartItems = new ArrayList<IItem>();

        context.checking(new Expectations() {{
            oneOf(cart).getItems();
            will(returnValue(cartItems));
        }});

        Assert.assertEquals("Unexpected total returned from checkout", 0,
                priceScanner.scanItems(cart));
    }

    /**
     * Validate that items can be properly scanned when no price rules are in effect and the cart is not empty.
     */
    @Test
    public void testCanScanItemsWithNoPriceRulesAndNonEmptyCart()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);

        final PriceScanner priceScanner = new PriceScanner(inventory);

        final Collection<IItem> cartItems = new ArrayList<IItem>();
        cartItems.add(new Item("A", 3));
        cartItems.add(new Item("B", 5));
        cartItems.add(new Item("C", 1));

        context.checking(new Expectations() {{
            oneOf(cart).getItems();
            will(returnValue(cartItems));

            oneOf(inventory).getProduct("A");
            will(returnValue(new Product("A", 20)));

            oneOf(cart).setItemQuantity("A", 0);

            oneOf(inventory).getProduct("B");
            will(returnValue(new Product("B", 50)));

            oneOf(cart).setItemQuantity("B", 0);

            oneOf(inventory).getProduct("C");
            will(returnValue(new Product("C", 30)));

            oneOf(cart).setItemQuantity("C", 0);
        }});

        Assert.assertEquals("Unexpected total returned from checkout", 340,
                priceScanner.scanItems(cart));
    }

    /**
     * Validate that items can be properly scanned when price rules are in effect and the cart is empty.
     */
    @Test
    public void testCanScanItemsWithPriceRulesAndEmptyCart()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);

        final IPriceRule firstPriceRule = context.mock(IPriceRule.class, "price rule 1");
        final IPriceRule secondPriceRule = context.mock(IPriceRule.class, "price rule 2");

        List<IPriceRule> priceRules = new ArrayList<IPriceRule>();
        priceRules.add(firstPriceRule);
        priceRules.add(secondPriceRule);

        final PriceScanner priceScanner = new PriceScanner(inventory);

        final Collection<IItem> cartItems = new ArrayList<IItem>();

        context.checking(new Expectations() {{
            oneOf(firstPriceRule).process(cart, priceScanner);
            will(returnValue(10));

            oneOf(secondPriceRule).process(cart, priceScanner);
            will(returnValue(20));

            oneOf(cart).getItems();
            will(returnValue(cartItems));
        }});

        priceScanner.setPriceRules(priceRules);
        Assert.assertEquals("Unexpected total returned from checkout", 30,
                priceScanner.scanItems(cart));
    }

    /**
     * Validate that items can be properly scanned when no price rules are in effect and the cart is not empty.
     */
    @Test
    public void testCanScanItemsWithPriceRulesAndNonEmptyCart()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);

        final IPriceRule firstPriceRule = context.mock(IPriceRule.class, "price rule 1");
        final IPriceRule secondPriceRule = context.mock(IPriceRule.class, "price rule 2");

        List<IPriceRule> priceRules = new ArrayList<IPriceRule>();
        priceRules.add(firstPriceRule);
        priceRules.add(secondPriceRule);

        final PriceScanner priceScanner = new PriceScanner(inventory);

        final Collection<IItem> cartItems = new ArrayList<IItem>();
        cartItems.add(new Item("A", 3));
        cartItems.add(new Item("B", 5));
        cartItems.add(new Item("C", 1));

        context.checking(new Expectations() {{
            oneOf(firstPriceRule).process(cart, priceScanner);
            will(returnValue(10));

            oneOf(secondPriceRule).process(cart, priceScanner);
            will(returnValue(20));

            oneOf(cart).getItems();
            will(returnValue(cartItems));

            oneOf(inventory).getProduct("A");
            will(returnValue(new Product("A", 20)));

            oneOf(cart).setItemQuantity("A", 0);

            oneOf(inventory).getProduct("B");
            will(returnValue(new Product("B", 50)));

            oneOf(cart).setItemQuantity("B", 0);

            oneOf(inventory).getProduct("C");
            will(returnValue(new Product("C", 30)));

            oneOf(cart).setItemQuantity("C", 0);
        }});

        priceScanner.setPriceRules(priceRules);
        Assert.assertEquals("Unexpected total returned from checkout", 370,
                priceScanner.scanItems(cart));
    }

    /**
     * Validate that items can be properly scanned when price rules are in effect, the cart is not empty, and
     * some of the cart items have no quantity at the point the fallback unit price rule is being applied.
     */
    @Test
    public void testCanScanItemsWithPriceRulesAndNonEmptyCartWithItemsHavingNoQuantity()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);

        final IPriceRule firstPriceRule = context.mock(IPriceRule.class, "price rule 1");
        final IPriceRule secondPriceRule = context.mock(IPriceRule.class, "price rule 2");

        List<IPriceRule> priceRules = new ArrayList<IPriceRule>();
        priceRules.add(firstPriceRule);
        priceRules.add(secondPriceRule);

        final PriceScanner priceScanner = new PriceScanner(inventory);

        final Collection<IItem> cartItems = new ArrayList<IItem>();
        cartItems.add(new Item("A", 0));
        cartItems.add(new Item("B", 5));
        cartItems.add(new Item("C", 0));

        context.checking(new Expectations() {{
            oneOf(firstPriceRule).process(cart, priceScanner);
            will(returnValue(10));

            oneOf(secondPriceRule).process(cart, priceScanner);
            will(returnValue(20));

            oneOf(cart).getItems();
            will(returnValue(cartItems));

            oneOf(inventory).getProduct("B");
            will(returnValue(new Product("B", 50)));

            oneOf(cart).setItemQuantity("B", 0);
        }});

        priceScanner.setPriceRules(priceRules);
        Assert.assertEquals("Unexpected total returned from checkout", 280,
                priceScanner.scanItems(cart));
    }

    /**
     * Validate that items can be properly scanned when price rules are in effect, the cart is not empty, and
     * some of the cart items have product ids which do not map to any products in inventory.
     */
    @Test
    public void testCanScanItemsWithPriceRulesAndNonEmptyCartWithUnrecognizedItems()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);

        final IPriceRule firstPriceRule = context.mock(IPriceRule.class, "price rule 1");
        final IPriceRule secondPriceRule = context.mock(IPriceRule.class, "price rule 2");

        List<IPriceRule> priceRules = new ArrayList<IPriceRule>();
        priceRules.add(firstPriceRule);
        priceRules.add(secondPriceRule);

        final PriceScanner priceScanner = new PriceScanner(inventory);

        final Collection<IItem> cartItems = new ArrayList<IItem>();
        cartItems.add(new Item("A", 3));
        cartItems.add(new Item("B", 5));
        cartItems.add(new Item("C", 1));

        context.checking(new Expectations() {{
            oneOf(firstPriceRule).process(cart, priceScanner);
            will(returnValue(10));

            oneOf(secondPriceRule).process(cart, priceScanner);
            will(returnValue(20));

            oneOf(cart).getItems();
            will(returnValue(cartItems));

            oneOf(inventory).getProduct("A");
            will(returnValue(new Product("A", 20)));

            oneOf(cart).setItemQuantity("A", 0);

            oneOf(inventory).getProduct("B");
            will(returnValue(null));

            oneOf(inventory).getProduct("C");
            will(returnValue(new Product("C", 30)));

            oneOf(cart).setItemQuantity("C", 0);
        }});

        priceScanner.setPriceRules(priceRules);
        Assert.assertEquals("Unexpected total returned from checkout", 120,
                priceScanner.scanItems(cart));
    }

    /**
     * Validate that items can be properly scanned twice when the pricing rules in effect change from one scan to the
     * next and the cart is not empty.
     */
    @Test
    public void testCanScanItemsTwiceWithDifferentPriceRulesAndNonEmptyCart()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);

        final IPriceRule firstPriceRule = context.mock(IPriceRule.class, "price rule 1");
        final IPriceRule secondPriceRule = context.mock(IPriceRule.class, "price rule 2");
        final IPriceRule thirdPriceRule = context.mock(IPriceRule.class, "price rule 3");

        List<IPriceRule> originalPriceRules = new ArrayList<IPriceRule>();
        originalPriceRules.add(firstPriceRule);
        originalPriceRules.add(secondPriceRule);

        List<IPriceRule> newPriceRules = new ArrayList<IPriceRule>();
        newPriceRules.add(thirdPriceRule);

        final PriceScanner priceScanner = new PriceScanner(inventory);

        final Collection<IItem> cartItems = new ArrayList<IItem>();
        cartItems.add(new Item("A", 3));
        cartItems.add(new Item("B", 5));
        cartItems.add(new Item("C", 1));

        context.checking(new Expectations() {{
            oneOf(firstPriceRule).process(cart, priceScanner);
            will(returnValue(10));

            oneOf(secondPriceRule).process(cart, priceScanner);
            will(returnValue(20));

            oneOf(cart).getItems();
            will(returnValue(cartItems));

            oneOf(inventory).getProduct("A");
            will(returnValue(new Product("A", 20)));

            oneOf(cart).setItemQuantity("A", 0);

            oneOf(inventory).getProduct("B");
            will(returnValue(new Product("B", 50)));

            oneOf(cart).setItemQuantity("B", 0);

            oneOf(inventory).getProduct("C");
            will(returnValue(new Product("C", 30)));

            oneOf(cart).setItemQuantity("C", 0);

            oneOf(thirdPriceRule).process(cart, priceScanner);
            will(returnValue(10));

            oneOf(cart).getItems();
            will(returnValue(cartItems));

            oneOf(inventory).getProduct("A");
            will(returnValue(new Product("A", 20)));

            oneOf(cart).setItemQuantity("A", 0);

            oneOf(inventory).getProduct("B");
            will(returnValue(new Product("B", 50)));

            oneOf(cart).setItemQuantity("B", 0);

            oneOf(inventory).getProduct("C");
            will(returnValue(new Product("C", 30)));

            oneOf(cart).setItemQuantity("C", 0);
        }});

        priceScanner.setPriceRules(originalPriceRules);
        Assert.assertEquals("Unexpected total returned from original checkout", 370,
                priceScanner.scanItems(cart));

        priceScanner.setPriceRules(newPriceRules);
        Assert.assertEquals("Unexpected total returned from second checkout", 350,
                priceScanner.scanItems(cart));
    }

    /**
     * Validate that items can be properly scanned twice when product unit prices being used change from one scan to the
     * next and the cart is not empty.
     */
    @Test
    public void testCanScanItemsTwiceWithDifferentProductUnitPriceAndNonEmptyCart()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);

        final IPriceRule firstPriceRule = context.mock(IPriceRule.class, "price rule 1");
        final IPriceRule secondPriceRule = context.mock(IPriceRule.class, "price rule 2");

        List<IPriceRule> priceRules = new ArrayList<IPriceRule>();
        priceRules.add(firstPriceRule);
        priceRules.add(secondPriceRule);

        final PriceScanner priceScanner = new PriceScanner(inventory);

        final Collection<IItem> cartItems = new ArrayList<IItem>();
        cartItems.add(new Item("A", 3));
        cartItems.add(new Item("B", 5));
        cartItems.add(new Item("C", 1));

        context.checking(new Expectations() {{
            oneOf(firstPriceRule).process(cart, priceScanner);
            will(returnValue(10));

            oneOf(secondPriceRule).process(cart, priceScanner);
            will(returnValue(20));

            oneOf(cart).getItems();
            will(returnValue(cartItems));

            oneOf(inventory).getProduct("A");
            will(returnValue(new Product("A", 20)));

            oneOf(cart).setItemQuantity("A", 0);

            oneOf(inventory).getProduct("B");
            will(returnValue(new Product("B", 50)));

            oneOf(cart).setItemQuantity("B", 0);

            oneOf(inventory).getProduct("C");
            will(returnValue(new Product("C", 30)));

            oneOf(cart).setItemQuantity("C", 0);

            oneOf(firstPriceRule).process(cart, priceScanner);
            will(returnValue(10));

            oneOf(secondPriceRule).process(cart, priceScanner);
            will(returnValue(20));

            oneOf(cart).getItems();
            will(returnValue(cartItems));

            oneOf(inventory).getProduct("A");
            will(returnValue(new Product("A", 10)));

            oneOf(cart).setItemQuantity("A", 0);

            oneOf(inventory).getProduct("B");
            will(returnValue(new Product("B", 20)));

            oneOf(cart).setItemQuantity("B", 0);

            oneOf(inventory).getProduct("C");
            will(returnValue(new Product("C", 30)));

            oneOf(cart).setItemQuantity("C", 0);
        }});

        priceScanner.setPriceRules(priceRules);
        Assert.assertEquals("Unexpected total returned from original checkout", 370,
                priceScanner.scanItems(cart));

        Assert.assertEquals("Unexpected total returned from second checkout", 190,
                priceScanner.scanItems(cart));
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a null product id argument is passed into a
     * PriceScanner object's getProduct() method
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullIdInGetProductCallThrowsIllegalArgumentException()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        new PriceScanner(inventory).getProduct(null);
    }

    /**
     * Validate that a single product can be obtained from the inventory.
     */
    @Test
    public void testCanGetProduct()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);

        final PriceScanner priceScanner = new PriceScanner(inventory);

        final Product returnProduct = new Product("A", 20);
        final String idOfProductToGet = "A";

        context.checking(new Expectations() {{
            oneOf(inventory).getProduct(idOfProductToGet);
            will(returnValue(returnProduct));
        }});

        Assert.assertSame("Unexpected product returned", returnProduct,
                priceScanner.getProduct(idOfProductToGet));
    }

    /**
     * Validate that a single product cannot be obtained when it is not present in the inventory.
     */
    @Test
    public void testCannotGetProductNonexistentProduct()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);

        final PriceScanner priceScanner = new PriceScanner(inventory);

        final String idOfProductToGet = "A";

        context.checking(new Expectations() {{
            oneOf(inventory).getProduct(with(any(String.class)));
            will(returnValue(null));
        }});

        Assert.assertNull("Unexpectedly returned non-null product",
                priceScanner.getProduct(idOfProductToGet));
    }
}