package supermarket;

import java.util.Arrays;

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
        Supermarket supermarket = new Supermarket(
            Arrays.asList(
                new Product ("A", 20),
                new Product ("B", 50),
                new Product ("C", 30)
            )
        );

        System.out.println("total at checkout is " + supermarket.checkout("ABC"));
    }
}
