package supermarket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 */
class ShoppingCart implements IShoppingCart
{
    private final HashMap<String,Integer> itemMap;

    public ShoppingCart(String items)
    {
        if (items == null)
        {
            throw new IllegalArgumentException("items cannot be null");
        }

        itemMap = new HashMap<String,Integer>();
        convertItemsToMap(items);
    }

    public IItem getItem(String productId)
    {
        if (productId == null)
        {
            throw new IllegalArgumentException("productId cannot be null");
        }

        Item returnValue = null;

        Integer quantity = itemMap.get(productId);
        if (quantity != null)
        {
            returnValue = getItem(productId, quantity);
        }

        return returnValue;
    }

    public Collection<IItem> getItems()
    {
        Collection<IItem> items = new ArrayList<IItem>();

        for (Entry<String,Integer> entry : itemMap.entrySet())
        {
            items.add(getItem(entry.getKey(), entry.getValue()));
        }

        return items;
    }

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