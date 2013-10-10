package supermarket;

/**
 *
 */
public class Program
{
    /**
     * Program main
     * @param args Ignored
     */
    public static void main(String[] args)
    {
        Supermarket supermarket = new Supermarket();
        System.out.println("total at checkout is " + supermarket.checkout("ABC"));
    }
}
