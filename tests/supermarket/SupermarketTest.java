package supermarket;

import org.junit.Test;
import org.junit.Assert;

/**
 *
 */
public class SupermarketTest
{
    @Test
    public void testCheckout()
    {
        Assert.assertEquals(0, new Supermarket().checkout(null));
    }
}
