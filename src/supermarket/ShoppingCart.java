package supermarket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Shopping cart wrapping items to be scanned.
 */
class ShoppingCart implements IShoppingCart
{
    private final HashMap<String,Integer> itemMap;

    /**
     * Class constructor
     * @param items  String with product ids representing instances of a corresponding item in a cart.  Value cannot be
     *               null.  For example, a value of 'ABBACBBAB' would indicate that three items having the id 'A', five
     *               items having the id 'B', and one item having the id 'C' are present in the cart.
     */
    public ShoppingCart(String items)
    {
        if (items == null)
        {
            throw new IllegalArgumentException("items cannot be null");
        }

        itemMap = new HashMap<String,Integer>();
        convertItemsToMap(items);
    }

    /**
     * Get a shopping cart item by its product id.
     * @param productId  Product id.  Value cannot be null or empty.
     * @return  Matching item, if one is available.  Otherwise, null.
     */
    public IItem getItem(String productId)
    {
        if (productId == null)
        {
            throw new IllegalArgumentException("productId cannot be null");
        }

        if (productId.isEmpty())
        {
            throw new IllegalArgumentException("productId cannot be empty");
        }

        Item returnValue = null;

        Integer quantity = itemMap.get(productId);
        if (quantity != null)
        {
            returnValue = getItem(productId, quantity);
        }

        return returnValue;
    }

    /**
     * Get all of the items in the shopping cart in the form of a collection.
     * @return Collection of items.  Collection will be read-only and be a copy of the data held by the shopping cart.
     * Changes to the items returned in the list have no effect on the cart.
     */
    public Collection<IItem> getItems()
    {
        Collection<IItem> items = new ArrayList<IItem>();

        for (Entry<String,Integer> entry : itemMap.entrySet())
        {
            items.add(getItem(entry.getKey(), entry.getValue()));
        }

        return Collections.unmodifiableCollection(items);
    }

    /**
     * Change the quantity of an item in the cart.  Will not insert a new item in the cart if no prior item
     * corresponding to the productId was present.
     * @param productId Product id of the item whose quantity should be changed.  Value cannot be null or empty.
     * @param quantity New quantity for the item.  Value cannot be less than zero.
     * @return True if the item was updated.  Otherwise, false.
     */
    public boolean setItemQuantity(String productId, int quantity)
    {
        if (productId == null)
        {
            throw new IllegalArgumentException("productId cannot be null");
        }

        if (productId.isEmpty())
        {
            throw new IllegalArgumentException("productId on item cannot be empty");
        }

        if (quantity < 0)
        {
            throw new IllegalArgumentException("quantity on item cannot be less than zero.  Value specified was " +
                quantity);
        }

        boolean quantitySet = false;

        if (itemMap.containsKey(productId))
        {
            itemMap.put(productId, quantity);
            quantitySet = true;
        }

        return quantitySet;
    }

    private void convertItemsToMap(String items)
    {
        if (!items.isEmpty())
        {
            String[] itemIds = items.split("");
            if (itemIds != null)
            {
                for (int i = 1; i < itemIds.length; i++)
                {
                    String itemId = itemIds[i];
                    Integer quantity = itemMap.get(itemId);
                    if (quantity != null)
                    {
                        itemMap.put(itemId, ++quantity);
                    }
                    else
                    {
                        itemMap.put(itemId, 1);
                    }
                }
            }
        }
    }

    private Item getItem(String productId, Integer quantity)
    {
        return new Item(productId, quantity);
    }
}