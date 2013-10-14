package supermarket;

/**
 * Supermarket
 */

// Suppressing since problem required this to be declared as public
@SuppressWarnings("WeakerAccess")
public class Supermarket
{
    private IPriceScanner priceScanner;

    /**
     * Class constructor
     * @param priceScanner  Cart item scanner for computing total price.  Value cannot be null.
     */
    public Supermarket(IPriceScanner priceScanner)
    {
        if (priceScanner == null)
        {
            throw new IllegalArgumentException("priceScanner cannot be null");
        }

        this.priceScanner = priceScanner;
    }

    /**
     * Check out the items in the supplied token string to compute a total price.
     * @param items  String with product ids representing instances of a corresponding item in a cart.  Value cannot be
     *               null.  For example, a value of 'ABBACBBAB' would indicate that three items having the id 'A', two
     *               items having the id 'B', and one item having the id 'C' are present in the cart.
     * @return  Total cost of the items in the cart.
     */
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
