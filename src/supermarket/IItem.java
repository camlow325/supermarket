package supermarket;

/**
 * Product item interface.
 */
public interface IItem
{
    /**
     * Get the product id of the item.
     * @return  The product id.  Will be non-null and non-empty.
     */
    String getProductId();

    /**
     * Get the quantity of the item.
     * @return  The quantity.
     */
    int getQuantity();
}
