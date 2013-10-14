package supermarket;

/**
 * Product information finder interface.
 */
interface IInventoryLookup
{
    /**
     * Get the product whose id matches the supplied parameter.
     * @param id  Id of the product to get.  Must be non-null and non-empty.
     * @return  The product, if one can be found.  Otherwise, null.
     */
    IProduct getProduct(String id);
}
