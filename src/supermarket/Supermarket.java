package supermarket;

/**
 *
 */

// Adding suppression since public access is desired for the problem
@SuppressWarnings("WeakerAccess")
public class Supermarket
{
    public int checkout(String items)
    {
        int total = 0;

        if (items != null && items.length() > 0)
        {
            total = 1;
            System.out.println("items are: " + items);
        }

        return total;
    }
}
