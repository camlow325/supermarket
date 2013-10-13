package supermarket;

import java.util.Collection;

/**
 *
 */
interface IInventory
{
    IProduct getProduct(String id);
    Collection<IProduct> getProducts();
    void setProducts(Iterable<IProduct> products);
}
