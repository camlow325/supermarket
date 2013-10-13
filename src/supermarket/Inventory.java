package supermarket;

import java.util.Collection;
import java.util.HashMap;

/**
 *
 */
class Inventory implements IInventory
{
    private final HashMap<String, IProduct> productMap;

    public Inventory()
    {
        productMap = new HashMap<String, IProduct>();
    }

    public Inventory(Iterable<IProduct> products)
    {
        this();
        setProducts(products);
    }

    @Override
    public IProduct getProduct(String id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("id must not be null");
        }

        if (id.isEmpty())
        {
            throw new IllegalArgumentException("id must not be empty");
        }

        return productMap.get(id);
    }

    @Override
    public Collection<IProduct> getProducts()
    {
        return productMap.values();
    }

    @Override
    public void setProducts(Iterable<IProduct> products)
    {
        productMap.clear();
        if (products != null)
        {
            for (IProduct product : products)
            {
                if (product != null)
                {
                    productMap.put(product.getId(), product);
                }
            }
        }
    }
}
