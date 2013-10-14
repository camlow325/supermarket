package supermarket;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
class Program
{
    /**
     * Program main
     * @param args Ignored
     */
    public static void main(String[] args)
    {
        String item = parseItem(args);
        if (item != null)
        {
            checkout(item);
        }
    }

    static int checkout(String item)
    {
        List<IProduct> originalProducts = new ArrayList<IProduct>();
        originalProducts.add(new Product("A", 20));
        originalProducts.add(new Product("B", 50));
        originalProducts.add(new Product("C", 30));

        Inventory inventory = new Inventory(originalProducts);

        List<IPriceRule> priceRules = new ArrayList<IPriceRule>();
        priceRules.add(new BuyXGetYFreePriceRule("B", 3, 2));

        PriceScanner priceScanner = new PriceScanner(inventory);
        priceScanner.setPriceRules(priceRules);

        Supermarket supermarket = new Supermarket(priceScanner);
        int total = supermarket.checkout(item);
        System.out.println("total at checkout is " + total);

        return total;
    }

    private static String parseItem(String[] args)
    {
        String item = null;

        if (args == null)
        {
            System.out.println("null args specified");
        }
        else if (args.length < 1)
        {
            System.out.println("not enough args");
        }
        else if (args.length > 1)
        {
            System.out.println("too many args");
        }
        else if (args[0] == null)
        {
            System.out.println("argument specified is null");
        }
        else
        {
            item = args[0];
        }

        if (item == null)
        {
            usage();
        }

        return item;
    }

    private static void usage()
    {
        System.out.println("usage: one string with each character denoting the id");
        System.out.println("       of an associated product in a shopping cart");
    }
}
