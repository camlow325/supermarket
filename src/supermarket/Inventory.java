package supermarket;

import java.util.Collection;
import java.util.HashMap;

/**
 * Product inventory.
 */
class Inventory implements IInventoryLookup
{
    private final HashMap<String, IProduct> productMap;

    /**
     * Class constructor
     */
    public Inventory()
    {
        productMap = new HashMap<String, IProduct>();
    }

    /**
     * Class constructor
     * @param products  Products to seed into the inventory.  May be null.
     */
    public Inventory(Iterable<IProduct> products)
    {
        this();
        setProducts(products);
    }

    /**
     * Get the product whose id matches the supplied parameter.
     * @param id  Id of the product to get.  Must be non-null and non-empty.
     * @return  The product, if one can be found.  Otherwise, null.
     */
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

    /**
     * Get all of the products in the inventory as a collection.
     * @return  Collection of the products in the inventory.
     */
    public Collection<IProduct> getProducts()
    {
        return productMap.values();
    }

    /**
     * Set the products which appear in the inventory.  Products previously in the inventory will be purged from the
     * inventory before the new products are added.
     * @param products  Products to seed into the inventory.  May be null.
     */
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
