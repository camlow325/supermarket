package supermarket;

/**
 *
 */
class BuyXGetYFreePriceRule implements IPriceRule
{
    private String productId;
    private int quantityToBuy;
    private int quantityFree;

    public BuyXGetYFreePriceRule(String productId, int quantityToBuy, int quantityFree)
    {
        if (productId == null)
        {
            throw new IllegalArgumentException("productId cannot be null");
        }

        if (quantityToBuy < 1)
        {
            throw new IllegalArgumentException("quantityToBuy cannot be less than 1");
        }

        if (quantityFree < 1)
        {
            throw new IllegalArgumentException("quantityToBuy cannot be less than 1");
        }

        this.productId = productId;
        this.quantityToBuy = quantityToBuy;
        this.quantityFree = quantityFree;
    }

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
                    price = (quantity / (quantityToBuy + quantityFree)) * quantityToBuy *
                            productInInventory.getUnitPrice();
                    cart.setItemQuantity(productId, quantity % (quantityToBuy + quantityFree));
                }
            }
        }

        return price;
    }
}
