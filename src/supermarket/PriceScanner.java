package supermarket;

import java.util.Iterator;

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
     * @param priceRules  Price rules to seed into the scanner.  Must be non-null and have at least one element.
     */
    public PriceScanner(IInventoryLookup inventory, Iterable<IPriceRule> priceRules)
    {
        if (inventory == null)
        {
            throw new IllegalArgumentException("inventory cannot be null");
        }

        this.inventory = inventory;

        setPriceRules(priceRules);
    }

    /**
     * Set the price rules which can be used by the scanner.  Price rules previously in use will be purged from the
     * scanner before the new price rules are added.
     * @param priceRules  Price rules to seed into the scanner.  Must be non-null and have at least one element.
     */
    public void setPriceRules(Iterable<IPriceRule> priceRules)
    {
        if (priceRules == null)
        {
            throw new IllegalArgumentException("priceRules cannot be null");
        }

        Iterator<IPriceRule> priceRuleIterator = priceRules.iterator();
        if (priceRuleIterator == null || !priceRuleIterator.hasNext())
        {
            throw new IllegalArgumentException("priceRules must have at least one element");
        }

        this.priceRules = priceRules;
    }

    /**
     * Scan the items in a shopping cart.
     * @param cart  Shopping cart to scan.  Must be non-null.  On return, items successfully scanned will be removed
     *              from the cart (signified by the item quantity being decremented).
     * @return  Total cost of the items in the cart.
     */
    @Override
    public int scanItems(IShoppingCart cart)
    {
        if (cart == null)
        {
            throw new IllegalArgumentException("cart cannot be null");
        }

        int total = 0;

        for (IPriceRule priceRule : priceRules)
        {
            total += priceRule.process(cart, this);
        }

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

        if (id.isEmpty())
        {
            throw new IllegalArgumentException("id cannot be empty");
        }

        return inventory.getProduct(id);
    }
}