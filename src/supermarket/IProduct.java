package supermarket;

/**
 * Product interface.
 */
public interface IProduct
{
    /**
     * Get the id of the product.
     * @return  Id of the product.  Will be non-null and non-empty.
     */
    String getId();

    /**
     * Get the unit price of the product.
     * @return  Unit price of the product.
     */
    int getUnitPrice();
}
