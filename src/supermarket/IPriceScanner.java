package supermarket;

/**
 * Price scanner interface.
 */
interface IPriceScanner
{
    /**
     * Scan the items in a shopping cart.
     * @param cart  Shopping cart to scan.  Must be non-null.  On return, items successfully scanned will be removed
     *              from the cart (signified by the item quantity being decremented).
     * @return  Total cost of the items in the cart.
     */
    int scanItems(IShoppingCart cart);
}
