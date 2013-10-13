package supermarket;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 */
public class SupermarketTest
{
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void testCheckout()
    {
        final IInventory inventory = context.mock(IInventory.class);

//        context.checking(new Expectations() {{
//            oneOf(inventory).getProduct("1234");
//            will(returnValue(new Product("sjk", 22)));
//        }});

        Assert.assertEquals(0, new Supermarket(inventory).checkout(null));
    }
}