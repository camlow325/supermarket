package supermarket;

/**
 * Product.
 */
class Product implements IProduct
{
    private String id;
    private int unitPrice;

    /**
     * Class constructor
     * @param id  Id of the product.  Value cannot be null or empty.
     * @param unitPrice  Unit price of the product.  Value cannot be less than zero.
     */
    public Product(String id, int unitPrice)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("id cannot be null");
        }

        if (id.isEmpty())
        {
            throw new IllegalArgumentException("id cannot be empty");
        }

        if (unitPrice < 0)
        {
            throw new IllegalArgumentException("price cannot be negative");
        }

        this.id = id;
        this.unitPrice = unitPrice;
    }

    /**
     * Get the id of the product.
     * @return  Id of the product.  Will be non-null and non-empty.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Get the unit price of the product.
     * @return  Unit price of the product.
     */
    public int getUnitPrice()
    {
        return unitPrice;
    }
}