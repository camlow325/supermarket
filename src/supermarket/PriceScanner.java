package supermarket;

/**
 * Scan shopping cart items to compute a total price.
 */
class PriceScanner implements IPriceScanner, IInventoryLookup
{
    private IInventoryLookup inventory;
    private Iterable<IPriceRule> priceRules;

    /**
     * Class constructor.
     * @param inventory  Product information finder.  Must be non-null.
     */
    public PriceScanner(IInventoryLookup inventory)
    {
        if (inventory == null)
        {
            throw new IllegalArgumentException("inventory cannot be null");
        }

        this.inventory = inventory;
    }

    /**
     * Set the price rules which can be used by the scanner.  Price rules previously in use will be purged from the
     * scanner before the new price rules are added.
     * @param priceRules  Price rules to seed into the scanner.  May be null.
     */
    public void setPriceRules(Iterable<IPriceRule> priceRules)
    {
        this.priceRules = priceRules;
    }

    /**
     * Scan the items in a shopping cart.
     * @param cart  Shopping cart to scan.  On return, items successfully scanned will be removed from the cart
     *              (signified by the item quantity being decremented).
     * @return  Total cost of the items in the cart.
     */
    @Override
    public int scanItems(IShoppingCart cart)
    {
        if (cart == null)
        {
            throw new IllegalArgumentException("inventory cannot be null");
        }

        int total = getTotalForPriceRules(cart);
        // For remaining items in the cart, compute item total using unit price.
        total += getTotalPriceForUnitPriceRule(cart);

        return total;
    }

    /**
     * Get the product whose id matches the supplied parameter.
     * @param id  Id of the product to get.  Must be non-null and non-empty.
     * @return  The product, if one can be found.  Otherwise, null.
     */
    @Override
    public IProduct getProduct(String id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("id cannot be null");
        }

        return inventory.getProduct(id);
    }

    private int getTotalForPriceRules(IShoppingCart cart)
    {
        int total = 0;

        if (priceRules != null)
        {
            for (IPriceRule priceRule : priceRules)
            {
                total += priceRule.process(cart, this);
            }
        }

        return total;
    }

    private int getTotalPriceForUnitPriceRule(IShoppingCart cart)
    {
        int total = 0;

        for (IItem item : cart.getItems())
        {
            int quantity = item.getQuantity();
            if (quantity > 0)
            {
                String productId = item.getProductId();
                IProduct product = getProduct(productId);
                // TODO: Consider what to do if this fails.  Failure would indicate that the product to which
                //  this item applies is no longer in the product inventory.  Should throw exception, write
                //  error to log, ...?
                if (product != null)
                {
                    total += (product.getUnitPrice() * quantity);
                    cart.setItemQuantity(productId, 0);
                }
            }
        }
        return total;
    }
}