package supermarket;

/**
 *  Price rule interface
 */
interface IPriceRule
{
    /**
     * Process this rule against the items in the shopping cart.
     * @param cart  Shopping cart containing items to process.  Must be non-null.  If the product(s) to which this rule
     *              applies is/are present in the cart, the corresponding cart item quantities will be decremented per
     *              the amount applied by the rule.
     * @param lookup  Product information finder.  Must be non-null.
     * @return  Cost of the items claimed by this rule.
     */
    int process(IShoppingCart cart, IInventoryLookup lookup);
}
