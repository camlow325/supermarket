package supermarket;

/**
 *
 */
interface IPriceRule
{
    int process(IShoppingCart cart, IInventoryLookup lookup);
}
