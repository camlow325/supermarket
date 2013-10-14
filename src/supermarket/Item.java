package supermarket;

/**
 *
 */
class Item implements IItem
{
    private String productId;
    private int quantity;

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

    public String getProductId()
    {
        return productId;
    }

    public int getQuantity()
    {
        return quantity;
    }
}
