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
            throw new IllegalArgumentException("productId is null");
        }

        if (productId.isEmpty())
        {
            throw new IllegalArgumentException("productId is empty");
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

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}
