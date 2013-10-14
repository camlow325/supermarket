package supermarket;

/**
 * Product item.
 */
class Item implements IItem
{
    private String productId;
    private int quantity;

    /**
     * Class constructor
     * @param productId  Product id of the item.  Value cannot be null or empty.
     * @param quantity  Quantity of the item.  Value cannot be less than zero.
     */
    public Item(String productId, int quantity)
    {
        if (productId == null)
        {
            throw new IllegalArgumentException("productId cannot be null");
        }

        if (productId.isEmpty())
        {
            throw new IllegalArgumentException("productId cannot be empty");
        }

        if (quantity < 0)
        {
            throw new IllegalArgumentException("quantity cannot be negative");
        }

        this.productId = productId;
        this.quantity = quantity;
    }

    /**
     * Get the product id of the item.
     * @return  The product id.  Will be non-null and non-empty.
     */
    public String getProductId()
    {
        return productId;
    }

    /**
     * Get the quantity of the item.
     * @return  The quantity.
     */
    public int getQuantity()
    {
        return quantity;
    }
}
