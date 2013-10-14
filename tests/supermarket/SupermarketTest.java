package supermarket;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Tests for the Supermarket class
 */
public class SupermarketTest
{
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    /**
     * Validate that an IllegalArgumentException is thrown when a null IPriceScanner argument is passed into a
     * Supermarket object's constructor.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullPriceScannerInConstructorThrowsIllegalArgumentException()
    {
        new Supermarket(null);
    }

    /**
     * Validate that an IllegalArgumentException is thrown when a null items argument is passed into a
     * Supermarket object's checkout() method.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullItemsInCheckoutCallThrowsIllegalArgumentException()
    {
        final IPriceScanner priceScanner = context.mock(IPriceScanner.class);
        new Supermarket(priceScanner).checkout(null);
    }

    /**
     * Validate that a checkout is successful when no items are in the cart.
     */
    @Test
    public void testCanCheckoutWithNoItems()
    {
        final int expectedReturnValue = 32;

        final IPriceScanner priceScanner = context.mock(IPriceScanner.class);
        final IShoppingCart expectedShoppingCart = context.mock(IShoppingCart.class);
        final Collection<IItem> cartItems = new ArrayList<IItem>();

        context.checking(new Expectations() {{
            atLeast(1).of(expectedShoppingCart).getItems();
            will(returnValue(cartItems));

            oneOf(priceScanner).scanItems(with(ShoppingCartEquals(expectedShoppingCart)));
            will(returnValue(expectedReturnValue));
        }});

        Assert.assertEquals("Unexpected total returned from checkout", expectedReturnValue,
                new Supermarket(priceScanner).checkout(""));
    }

    /**
     * Validate that a checkout is successful when one item is in the cart.
     */
    @Test
    public void testCanCheckoutWithOneItem()
    {
        final int expectedReturnValue = 92;

        final IPriceScanner priceScanner = context.mock(IPriceScanner.class);
        final IShoppingCart expectedShoppingCart = context.mock(IShoppingCart.class);

        final Collection<IItem> cartItems = new ArrayList<IItem>();
        cartItems.add(new Item("D", 1));

        context.checking(new Expectations() {{
            atLeast(1).of(expectedShoppingCart).getItems();
            will(returnValue(cartItems));

            oneOf(priceScanner).scanItems(with(ShoppingCartEquals(expectedShoppingCart)));
            will(returnValue(expectedReturnValue));
        }});

        Assert.assertEquals("Unexpected total returned from checkout", expectedReturnValue,
                new Supermarket(priceScanner).checkout("D"));
    }

    /**
     * Validate that a checkout is successful when multiple items are in the cart.
     */
    @Test
    public void testCanCheckoutWithMultipleItems()
    {
        final int expectedReturnValue = 15;

        final IPriceScanner priceScanner = context.mock(IPriceScanner.class);
        final IShoppingCart expectedShoppingCart = context.mock(IShoppingCart.class);

        final Collection<IItem> cartItems = new ArrayList<IItem>();
        cartItems.add(new Item("A", 3));
        cartItems.add(new Item("B", 5));
        cartItems.add(new Item("C", 1));

        context.checking(new Expectations() {{
            atLeast(1).of(expectedShoppingCart).getItems();
            will(returnValue(cartItems));

            oneOf(priceScanner).scanItems(with(ShoppingCartEquals(expectedShoppingCart)));
            will(returnValue(expectedReturnValue));
        }});

        Assert.assertEquals("Unexpected total returned from checkout", expectedReturnValue,
                new Supermarket(priceScanner).checkout("ABBACBBAB"));
    }

    @Factory
    private static Matcher<IShoppingCart> ShoppingCartEquals(IShoppingCart cart)
    {
        return new IShoppingCartMatcher(cart);
    }
}