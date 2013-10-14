package supermarket;

/**
 *
 */
class Product implements IProduct
{
    private String id;
    private int unitPrice;

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

    public String getId()
    {
        return id;
    }

    public int getUnitPrice()
    {
        return unitPrice;
    }
}
