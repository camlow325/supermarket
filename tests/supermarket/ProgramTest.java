package supermarket;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ProgramTest
{
    @Test
    public void testCallCheckoutWithSomeItems()
    {
        Assert.assertEquals("Unexpected result from checkout", 240,
                Program.checkout("ABBACBBAB"));
    }

    @Test
    public void testCallCheckoutWithMoreItems()
    {
        Assert.assertEquals("Unexpected result from checkout", 540,
                Program.checkout("BACABBACBBABBBCBBBA"));
    }

    @Test
    public void testCallMainWithSomeItems()
    {
        Program.main(new String[]{"ABBACBBAB"});
    }

    @Test
    public void testCallMainWithMoreItems()
    {
        Program.main(new String[] { "BACABBACBBABBBCBBBA"} );
    }

    @Test
    public void testCallMainWithNullArgs()
    {
        Program.main(null);
    }

    @Test
    public void testCallMainWithEmptyArgs()
    {
        Program.main(new String[]{});
    }

    @Test
    public void testCallMainWithTooManyArgs()
    {
        Program.main(new String[]{"ABC", "A"});
    }
}
