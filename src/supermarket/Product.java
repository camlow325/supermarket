package supermarket;

/**
 *
 */
class Product
{
    private String name;
    private int price;

    public Product(String name, int price)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("name is null");
        }

        if (name.isEmpty())
        {
            throw new IllegalArgumentException("name is empty");
        }

        if (price < 0)
        {
            throw new IllegalArgumentException("price is negative");
        }

        this.name = name;
        this.price = price;
    }

    public String getName()
    {
        return name;
    }

    public int getPrice()
    {
        return price;
    }
}
