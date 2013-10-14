package supermarket;

/**
 *
 */

// Suppressing since problem required this to be declared as public
@SuppressWarnings("WeakerAccess")
public class Supermarket
{
    private IPriceScanner priceScanner;

    public Supermarket(IPriceScanner priceScanner)
    {
        if (priceScanner == null)
        {
            throw new IllegalArgumentException("priceScanner cannot be null");
        }

        this.priceScanner = priceScanner;
    }

    public int checkout(String items)
    {
        if (items == null)
        {
            throw new IllegalArgumentException("items cannot be null");
        }

        ShoppingCart cart = new ShoppingCart(items);
        return priceScanner.scanItems(cart);
    }
}
