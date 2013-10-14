package supermarket;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the Program class
 */
public class ProgramTest
{
    /**
     * Validate that the total cost is computed correctly for the supplied cart item content.  This is an
     * integration test.
     */
    @Test
    public void testCallCheckoutWithSomeItems()
    {
        Assert.assertEquals("Unexpected result from checkout", 240,
                Program.checkout("ABBACBBAB"));
    }

    /**
     * Validate that the total cost is computed correctly for the supplied cart item content.  This is an
     * integration test.
     */
    @Test
    public void testCallCheckoutWithMoreItems()
    {
        Assert.assertEquals("Unexpected result from checkout", 540,
                Program.checkout("BACABBACBBABBBCBBBA"));
    }


    /**
     * Code coverage for call to main with a good argument.
     */
    @Test
    public void testCallMainWithSomeItems()
    {
        Program.main(new String[] {"ABBACBBAB"} );
    }

    /**
     * Code coverage for call to main with a good argument.
     */
    @Test
    public void testCallMainWithMoreItems()
    {
        Program.main(new String[] { "BACABBACBBABBBCBBBA"} );
    }

    /**
     * Code coverage for call to main with an invalid null argument.
     */
    @Test
    public void testCallMainWithNullArgs()
    {
        Program.main(null);
    }

    /**
     * Code coverage for call to main with an invalid set of empty arguments.
     */
    @Test
    public void testCallMainWithEmptyArgs()
    {
        Program.main(new String[]{});
    }

    /**
     * Code coverage for call to main with too many arguments.
     */
    @Test
    public void testCallMainWithTooManyArgs()
    {
        Program.main(new String[]{"ABC", "A"});
    }
}
