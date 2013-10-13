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

        setPrice(unitPrice);

        this.id = id;
        this.unitPrice = unitPrice;
    }

    public String getId()
    {
        return id;
    }

    public int getPrice()
    {
        return unitPrice;
    }

    public void setPrice(int unitPrice)
    {
        if (unitPrice < 0)
        {
            throw new IllegalArgumentException("price is negative");
        }

        this.unitPrice = unitPrice;
    }
}
