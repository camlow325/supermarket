package supermarket;

import java.lang.Iterable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 */

// Adding suppression since public access is desired for the problem
@SuppressWarnings("WeakerAccess")
public class Supermarket
{
    private final HashMap<String, Product> productMap;

    public Supermarket(Iterable<Product> products)
    {
        this.productMap = new HashMap<String,Product>();
        if (products != null)
        {
            for (Product product : products)
            {
                if (product != null)
                {
                    this.productMap.put(product.getName(), product);
                }
            }
        }
    }

    Set<Entry<String,Product>> getProducts()
    {
        return Collections.unmodifiableSet(productMap.entrySet());
    }

    public int checkout(String items)
    {
        int total = 0;

        if (items != null && items.length() > 0)
        {
            total = 1;
            System.out.println("items are: " + items);
        }

        return total;
    }


}
