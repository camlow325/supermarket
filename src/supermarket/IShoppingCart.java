package supermarket;

import java.util.Collection;

/**
 *
 */
public interface IShoppingCart
{
    IItem getItem(String productId);
    Collection<IItem> getItems();
    boolean setItemQuantity(String productId, int quantity);
}
