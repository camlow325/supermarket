package supermarket;

/**
 * Pricing rule for charging the base unit price for each item remaining in the shopping cart.
 */
class BaseUnitPriceRule implements IPriceRule
{
    /**
     * Class constructor
     */
    public BaseUnitPriceRule()
    {
    }

    /**
     * Process this rule against the items in the shopping cart.
     * @param cart  Shopping cart containing items to process.  Must be non-null.  For each item in the cart which has
     *              a non-zero quantity and whose associated product can be found in inventory, the quantity will be
     *              decremented to zero.
     * @param lookup  Product information finder.  Must be non-null.  Used to determine the unit price of the product
     *                when the rule is applied.
     * @return  Cost of the items claimed by this rule.  For example, if the shopping cart has 3 of product "A" (with a
     * corresponding unit price of 2) and 5 of product "B" (with a corresponding unit price of 1), the total cost would
     * be 11 : (3 * 2) + (5 * 1).
     */
    @Override
    public int process(IShoppingCart cart, IInventoryLookup lookup)
    {
        if (cart == null)
        {
            throw new IllegalArgumentException("cart cannot be null");
        }

        if (lookup == null)
        {
            throw new IllegalArgumentException("lookup cannot be null");
        }

        int total = 0;

        for (IItem item : cart.getItems())
        {
            int quantity = item.getQuantity();
            if (quantity > 0)
            {
                String productId = item.getProductId();
                IProduct productInInventory = lookup.getProduct(productId);

                // TODO: Consider what to do if this fails.  Failure would indicate that the product to which
                //  this item applies is no longer in the product inventory.  Should throw exception, write
                //  error to log, ...?
                if (productInInventory != null)
                {
                    total += (productInInventory.getUnitPrice() * quantity);
                    cart.setItemQuantity(productId, 0);
                }
            }
        }

        return total;
    }
}