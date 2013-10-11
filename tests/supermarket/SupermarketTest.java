package supermarket;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 */
public class SupermarketTest
{
    @Test
    public void testConfirmReceiptOfProductsPassedToSupermarketConstructor()
    {
        List<Product> originalProducts = Arrays.asList(
                new Product ("A", 30),
                new Product ("B", 10),
                new Product ("C", 20)
        );

        Supermarket supermarket = new Supermarket(originalProducts);

        Set<Entry<String,Product>> productsSet = supermarket.getProducts();
        Assert.assertNotNull("Product set is null", productsSet);
        Assert.assertEquals("Unexpected number of products in set", originalProducts.size(), productsSet.size());

        for (Entry<String,Product> nameToProductEntry : productsSet)
        {
            Assert.assertNotNull("Name to product entry is null", nameToProductEntry);
            Product product = nameToProductEntry.getValue();
            Assert.assertEquals("Unexpected key for product value", nameToProductEntry.getKey(), product.getName());
            Assert.assertTrue("Original product list does not contain iterator value",
                originalProducts.contains(product));
        }
    }

    @Test
    public void testCheckout()
    {
        Assert.assertEquals(0, new Supermarket(null).checkout(null));
    }
}
