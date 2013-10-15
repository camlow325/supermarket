package supermarket;

/**
 * Pricing rule for buying some bulk quantity of a product and getting some quantity of the product for free.
 */
class XForThePriceOfYPriceRule implements IPriceRule
{
    private String productId;
    private int totalQuantityForRule;
    private int quantityPaidPerUnitPrice;

    /**
     * Class constructor
     * @param productId  Id of the product.  Must be non-null and non-empty.
     * @param totalQuantityForRule  Total quantity of the product that can be claimed by the rule.  Must be 2 or
     *                              greater.
     * @param quantityPaidPerUnitPrice   Quantity of the product paid for at unit price.  Remaining quantity of
     *                                   the product is 'free'.  Must be no greater than 1 less than
     *                                   totalQuantityForRule.
     */
    public XForThePriceOfYPriceRule(String productId, int totalQuantityForRule, int quantityPaidPerUnitPrice)
    {
        if (productId == null)
        {
            throw new IllegalArgumentException("productId cannot be null");
        }

        if (productId.isEmpty())
        {
            throw new IllegalArgumentException("productId cannot be empty");
        }

        if (totalQuantityForRule < 2)
        {
            throw new IllegalArgumentException("totalQuantityForRule cannot be less than 2");
        }

        if (quantityPaidPerUnitPrice < 1)
        {
            throw new IllegalArgumentException("quantityPaidPerUnitPrice cannot be less than 1");
        }

        if (quantityPaidPerUnitPrice >= totalQuantityForRule)
        {
            throw new IllegalArgumentException(
                    "quantityPaidPerUnitPrice cannot be greater than totalQuantityForRule minus 1");
        }

        this.productId = productId;
        this.totalQuantityForRule = totalQuantityForRule;
        this.quantityPaidPerUnitPrice = quantityPaidPerUnitPrice;
    }

    /**
     * Process this rule against the items in the shopping cart.
     * @param cart  Shopping cart containing items to process.  Must be non-null.  If the product to which this rule
     *              applies is present in the cart, the corresponding cart item's quantity will be decremented per the
     *              amount applied by the rule.  For example, if this rule is serving as a "5 for the price of 3" for
     *              product A and 13 units of product A are in the cart, the quantity of product A will be decremented
     *              by 10.  For this case, the rule would have been applied against the cart twice -- 2 applications * 5
     *              units = 10 total.
     * @param lookup  Product information finder.  Must be non-null.  Used to determine the unit price of the product
     *                when the rule is applied.
     * @return  Cost of the items claimed by this rule.  For example, if this rule is serving as a "5 for the price of
     * 3" for product A, 13 units of product A are in the cart, and the unit price of product A is 2, the total cost
     * for the item applied to the rule would be 12 -- 2 applications of the rule * 3 units * unit price of 2 = 12
     * total.
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

        int price = 0;

        IItem cartItem = cart.getItem(productId);
        if (cartItem != null)
        {
            int quantity = cartItem.getQuantity();
            if (quantity > 0)
            {
                IProduct productInInventory = lookup.getProduct(productId);

                // TODO: Consider what to do if this fails.  Failure would indicate that the product to which
                //  this rule applies is no longer in the product inventory.  Should throw exception, write
                //  error to log, ...?
                if (productInInventory != null)
                {
                    price = (quantity / totalQuantityForRule) * quantityPaidPerUnitPrice *
                            productInInventory.getUnitPrice();
                    cart.setItemQuantity(productId, quantity % totalQuantityForRule);
                }
            }
        }

        return price;
    }
}