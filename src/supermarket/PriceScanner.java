package supermarket;

/**
 *
 */
class PriceScanner implements IPriceScanner, IInventoryLookup
{
    private IInventoryLookup inventory;
    private Iterable<IPriceRule> priceRules;

    public PriceScanner(IInventoryLookup inventory)
    {
        if (inventory == null)
        {
            throw new IllegalArgumentException("inventory cannot be null");
        }

        this.inventory = inventory;
    }

    public void setPriceRules(Iterable<IPriceRule> priceRules)
    {
        this.priceRules = priceRules;
    }

    @Override
    public int scanItems(IShoppingCart cart)
    {
        if (cart == null)
        {
            throw new IllegalArgumentException("inventory cannot be null");
        }

        int total = 0;

        if (priceRules != null)
        {
            for (IPriceRule priceRule : priceRules)
            {
                total += priceRule.process(cart, this);
            }
        }

        for (IItem item : cart.getItems())
        {
            int quantity = item.getQuantity();
            if (quantity > 0)
            {
                IProduct product = getProduct(item.getProductId());
                if (product != null)
                {
                    total += (product.getUnitPrice() * quantity);
                }
            }
        }

        return total;
    }

    @Override
    public IProduct getProduct(String id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("id cannot be null");
        }

        return inventory.getProduct(id);
    }
}
