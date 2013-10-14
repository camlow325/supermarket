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
 *
 */
public class PriceScannerTest
{
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test(expected=IllegalArgumentException.class)
    public void testNullInventoryInConstructorThrowsIllegalArgumentException()
    {
        new PriceScanner(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNullShoppingCartInScanItemsCallThrowsIllegalArgumentException()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        new PriceScanner(inventory).scanItems(null);
    }

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

            oneOf(inventory).getProduct("B");
            will(returnValue(new Product("B", 50)));

            oneOf(inventory).getProduct("C");
            will(returnValue(new Product("C", 30)));
        }});

        Assert.assertEquals("Unexpected total returned from checkout", 340,
                priceScanner.scanItems(cart));
    }

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

            oneOf(inventory).getProduct("B");
            will(returnValue(new Product("B", 50)));

            oneOf(inventory).getProduct("C");
            will(returnValue(new Product("C", 30)));
        }});

        priceScanner.setPriceRules(priceRules);
        Assert.assertEquals("Unexpected total returned from checkout", 370,
                priceScanner.scanItems(cart));
    }

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
        }});

        priceScanner.setPriceRules(priceRules);
        Assert.assertEquals("Unexpected total returned from checkout", 280,
                priceScanner.scanItems(cart));
    }

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

            oneOf(inventory).getProduct("B");
            will(returnValue(null));

            oneOf(inventory).getProduct("C");
            will(returnValue(new Product("C", 30)));
        }});

        priceScanner.setPriceRules(priceRules);
        Assert.assertEquals("Unexpected total returned from checkout", 120,
                priceScanner.scanItems(cart));
    }

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

            oneOf(inventory).getProduct("B");
            will(returnValue(new Product("B", 50)));

            oneOf(inventory).getProduct("C");
            will(returnValue(new Product("C", 30)));

            oneOf(thirdPriceRule).process(cart, priceScanner);
            will(returnValue(10));

            oneOf(cart).getItems();
            will(returnValue(cartItems));

            oneOf(inventory).getProduct("A");
            will(returnValue(new Product("A", 20)));

            oneOf(inventory).getProduct("B");
            will(returnValue(new Product("B", 50)));

            oneOf(inventory).getProduct("C");
            will(returnValue(new Product("C", 30)));
        }});

        priceScanner.setPriceRules(originalPriceRules);
        Assert.assertEquals("Unexpected total returned from original checkout", 370,
                priceScanner.scanItems(cart));

        priceScanner.setPriceRules(newPriceRules);
        Assert.assertEquals("Unexpected total returned from second checkout", 350,
                priceScanner.scanItems(cart));
    }

    @Test
    public void testCanScanItemsTwiceWithDifferentInventoryAndNonEmptyCart()
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

            oneOf(inventory).getProduct("B");
            will(returnValue(new Product("B", 50)));

            oneOf(inventory).getProduct("C");
            will(returnValue(new Product("C", 30)));

            oneOf(firstPriceRule).process(cart, priceScanner);
            will(returnValue(10));

            oneOf(secondPriceRule).process(cart, priceScanner);
            will(returnValue(20));

            oneOf(cart).getItems();
            will(returnValue(cartItems));

            oneOf(inventory).getProduct("A");
            will(returnValue(new Product("A", 10)));

            oneOf(inventory).getProduct("B");
            will(returnValue(new Product("B", 20)));

            oneOf(inventory).getProduct("C");
            will(returnValue(new Product("C", 30)));
        }});

        priceScanner.setPriceRules(priceRules);
        Assert.assertEquals("Unexpected total returned from original checkout", 370,
                priceScanner.scanItems(cart));

        Assert.assertEquals("Unexpected total returned from second checkout", 190,
                priceScanner.scanItems(cart));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNullIdInGetProductCallThrowsIllegalArgumentException()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        new PriceScanner(inventory).getProduct(null);
    }

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