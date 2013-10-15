package supermarket;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
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
        List<IPriceRule> priceRules = new ArrayList<IPriceRule>();
        priceRules.add(context.mock(IPriceRule.class));

        new PriceScanner(null, priceRules);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a null priceRules argument is passed into a PriceScanner
     * object's constructor
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullPriceRulesInConstructorThrowsIllegalArgumentException()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        new PriceScanner(inventory, null);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a non-null priceRules argument with no elements is
     * passed into a PriceScanner object's constructor
     */
    @Test(expected=IllegalArgumentException.class)
    public void testPriceRulesWithNoElementsInConstructorThrowsIllegalArgumentException()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        new PriceScanner(inventory, new ArrayList<IPriceRule>());
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a null priceRules argument is passed into a PriceScanner
     * object's setPriceRules() method
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullPriceRulesInSetPriceRulesCallThrowsIllegalArgumentException()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        new PriceScanner(inventory, null).setPriceRules(null);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a non-null priceRules argument with no elements is
     * passed into a PriceScanner object's setPriceRules() method
     */
    @Test(expected=IllegalArgumentException.class)
    public void testPriceRulesWithNoElementsInSetPriceRulesCallThrowsIllegalArgumentException()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        new PriceScanner(inventory, new ArrayList<IPriceRule>());
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a null IShoppingCart argument is passed into a
     * PriceScanner object's scanItems() method
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullShoppingCartInScanItemsCallThrowsIllegalArgumentException()
    {
        List<IPriceRule> priceRules = new ArrayList<IPriceRule>();
        priceRules.add(context.mock(IPriceRule.class));

        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        new PriceScanner(inventory, priceRules).scanItems(null);
    }

    /**
     * Validate that items can be properly scanned.
     */
    @Test
    public void testCanScanItems()
    {
        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);
        final IShoppingCart cart = context.mock(IShoppingCart.class);

        final IPriceRule firstPriceRule = context.mock(IPriceRule.class, "price rule 1");
        final IPriceRule secondPriceRule = context.mock(IPriceRule.class, "price rule 2");

        List<IPriceRule> priceRules = new ArrayList<IPriceRule>();
        priceRules.add(firstPriceRule);
        priceRules.add(secondPriceRule);

        final PriceScanner priceScanner = new PriceScanner(inventory, priceRules);

        context.checking(new Expectations() {{
            oneOf(firstPriceRule).process(cart, priceScanner);
            will(returnValue(10));

            oneOf(secondPriceRule).process(cart, priceScanner);
            will(returnValue(20));
        }});

        Assert.assertEquals("Unexpected total returned from checkout", 30,
                priceScanner.scanItems(cart));
    }

    /**
     * Validate that items can be properly scanned twice when the pricing rules in effect change from one scan to the
     * next.
     */
    @Test
    public void testCanScanItemsTwiceWithDifferentPriceRules()
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

        final PriceScanner priceScanner = new PriceScanner(inventory, originalPriceRules);

        context.checking(new Expectations()
        {{
                oneOf(firstPriceRule).process(cart, priceScanner);
                will(returnValue(40));

                oneOf(secondPriceRule).process(cart, priceScanner);
                will(returnValue(5));

                oneOf(thirdPriceRule).process(cart, priceScanner);
                will(returnValue(27));
            }});

        Assert.assertEquals("Unexpected total returned from original checkout", 45,
                priceScanner.scanItems(cart));

        priceScanner.setPriceRules(newPriceRules);
        Assert.assertEquals("Unexpected total returned from second checkout", 27,
                priceScanner.scanItems(cart));
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a null product id argument is passed into a
     * PriceScanner object's getProduct() method
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullIdInGetProductCallThrowsIllegalArgumentException()
    {
        List<IPriceRule> priceRules = new ArrayList<IPriceRule>();
        priceRules.add(context.mock(IPriceRule.class));

        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);

        new PriceScanner(inventory, priceRules).getProduct(null);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when an empty product id argument is passed into a
     * PriceScanner object's getProduct() method
     */
    @Test(expected=IllegalArgumentException.class)
    public void testEmptyIdInGetProductCallThrowsIllegalArgumentException()
    {
        List<IPriceRule> priceRules = new ArrayList<IPriceRule>();
        priceRules.add(context.mock(IPriceRule.class));

        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);

        new PriceScanner(inventory, priceRules).getProduct("");
    }

    /**
     * Validate that a single product can be obtained from the inventory.
     */
    @Test
    public void testCanGetProduct()
    {
        List<IPriceRule> priceRules = new ArrayList<IPriceRule>();
        priceRules.add(context.mock(IPriceRule.class));

        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);

        final PriceScanner priceScanner = new PriceScanner(inventory, priceRules);

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
        List<IPriceRule> priceRules = new ArrayList<IPriceRule>();
        priceRules.add(context.mock(IPriceRule.class));

        final IInventoryLookup inventory = context.mock(IInventoryLookup.class);

        final PriceScanner priceScanner = new PriceScanner(inventory, priceRules);

        final String idOfProductToGet = "A";

        context.checking(new Expectations() {{
            oneOf(inventory).getProduct(with(any(String.class)));
            will(returnValue(null));
        }});

        Assert.assertNull("Unexpectedly returned non-null product",
                priceScanner.getProduct(idOfProductToGet));
    }
}