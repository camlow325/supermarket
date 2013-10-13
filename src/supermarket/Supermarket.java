package supermarket;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class Supermarket
{
    private IInventory inventory;

    public Supermarket(IInventory inventory)
    {
        if (inventory == null)
        {
            throw new IllegalArgumentException("inventory cannot be null");
        }

        this.inventory = inventory;
    }

    public int checkout(String items)
    {
        int total = 0;

        if (items != null)
        {
            total = 1;
            System.out.println("items are: " + items);
        }

        return total;
    }

    /**
     * Program main
     * @param args Ignored
     */
    public static void main(String[] args)
    {
        List<IProduct> originalProducts = new ArrayList<IProduct>();
        originalProducts.add(new Product("A", 20));
        originalProducts.add(new Product("B", 50));
        originalProducts.add(new Product("C", 30));

        Inventory inventory = new Inventory(originalProducts);

        Supermarket supermarket = new Supermarket(inventory);
        System.out.println("total at checkout is " + supermarket.checkout("ABC"));
    }
}
