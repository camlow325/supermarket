package supermarket;

import java.util.Collection;

/**
 * Shopping cart interface
 */
interface IShoppingCart
{
    /**
     * Get a shopping cart item by its product id
     * @param productId  Product id.  Value cannot be null or empty.
     * @return  Matching item, if one is available.  Otherwise, null.
     */
    IItem getItem(String productId);

    /**
     * Get all of the items in the shopping cart in the form of a collection.
     * @return Collection of items.  Collection will be read-only and be a copy of the data held by the shopping cart.
     * Changes to the items returned in the list have no effect on the cart.
     */
    Collection<IItem> getItems();

    /**
     * Change the quantity of an item in the cart.  Will not insert a new item in the cart if no prior item
     * corresponding to the productId was present.
     * @param productId Product id of the item whose quantity should be changed.  Value cannot be null or empty.
     * @param quantity New quantity for the item.  Value cannot be less than zero.
     * @return True if the item was updated.  Otherwise, false.
     */
    boolean setItemQuantity(String productId, int quantity);
}
