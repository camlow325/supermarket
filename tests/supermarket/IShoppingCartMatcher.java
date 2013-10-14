package supermarket;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *  Matcher for IShoppingCart equality
 */
class IShoppingCartMatcher extends TypeSafeMatcher<IShoppingCart>
{
    private final IShoppingCart expectedCart;

    /**
     * Class constructor
     * @param expectedCart  Cart against which a test object is to be compared.
     */
    public IShoppingCartMatcher(IShoppingCart expectedCart)
    {
        this.expectedCart = expectedCart;
    }

    /**
     * Attempts to match the supplied actualCart against the expectedCart provided in the constructor.
     * @param actualCart  Actual cart to be compared against the expectedCart supplied in the constructor.
     * @return  True if the two carts have matching content.  Otherwise, false.
     */
    @Override
    protected boolean matchesSafely(IShoppingCart actualCart)
    {
        return getMismatchMessage(actualCart).isEmpty();
    }

    /**
     * Adds text to the supplied mismatchDescription pertaining to the reason that the supplied actualCart does
     * not match the expectedCart supplied in the constructor.
     * @param actualCart  Actual cart to be compared against the expectedCart supplied in the constructor.
     * @param mismatchDescription  Description of the mismatch.
     */
    @Override
    protected void describeMismatchSafely(IShoppingCart actualCart, Description mismatchDescription)
    {
        mismatchDescription.appendText(getMismatchMessage(actualCart));
    }

    /**
     * Adds text for the description of this matcher class
     * @param description  Description of the matcher.
     */
    @Override
    public void describeTo(Description description)
    {
        description.appendText("IShoppingCartMatcher");
    }

    /**
     * Compare the expectedItems to the actualItems.  Items compared for length and content but the position of
     * individual items within the collection may differ while still allowing for a successful result.
     * @param expectedItems  Items that the actualItems are expected to match.
     * @param actualItems  Items to be compared to the expectedItems.
     * @return  True if the items match.  Otherwise, false.
     */
    public static String getItemComparisonFailureMessage(Collection<IItem> expectedItems, Collection<IItem> actualItems)
    {
        StringBuilder builder = new StringBuilder(": ");

        if (expectedItems == null)
        {
            if (actualItems != null)
            {
                builder.append("Found some actual items but none expected; ");
            }
        }
        else
        {
            if (actualItems == null)
            {
                builder.append("Found no actual items but some were expected");
            }
            else if (expectedItems.size() != actualItems.size())
            {
                builder.append("Found different item counts.  Expected: ");
                builder.append(expectedItems.size());
                builder.append("Actual: ");
                builder.append(actualItems.size());
                builder.append("; ");
            }
            else
            {
                List<IItem> cloneOfExpectedItems = new ArrayList<IItem>(expectedItems);
                loopThroughCartItems(builder, cloneOfExpectedItems, actualItems);

                for (IItem expectedItem : cloneOfExpectedItems)
                {
                    builder.append("Did not find expected item in cart items for product id '");
                    builder.append(expectedItem.getProductId());
                    builder.append("'; ");
                }
            }
        }

        return builder.substring(0, builder.length() - 2);
    }

    private static void loopThroughCartItems(StringBuilder builder, List<IItem> expectedItems,
                                             Collection<IItem> cartItems)
    {
        int i = -1;
        for (IItem cartItem : cartItems)
        {
            i++;

            if (cartItem == null)
            {
                builder.append("Found null item at cart position ");
                builder.append(i);
                builder.append("; ");
                continue;
            }

            for (int j = 0; j < expectedItems.size(); j++)
            {
                IItem expectedItem = expectedItems.get(j);

                if (expectedItem.getProductId().equals(cartItem.getProductId()))
                {
                    if (expectedItem.getQuantity() != cartItem.getQuantity())
                    {
                        builder.append("Found mismatched quantity for product id ");
                        builder.append(expectedItem.getProductId());
                        builder.append(".  Expected: ");
                        builder.append(expectedItem.getQuantity());
                        builder.append(", Actual: ");
                        builder.append(cartItem.getQuantity());
                        builder.append("; ");
                    }

                    expectedItems.remove(j);
                    break;
                }
            }
        }
    }

    private String getMismatchMessage(IShoppingCart actualCart)
    {
        String message = "";

        if (expectedCart == null)
        {
            if (actualCart != null)
            {
                message = "Actual cart has items but expected does not";
            }
        }
        else
        {
            if (actualCart == null)
            {
                message = "Expected cart has items but actual does not";
            }
            else
            {
                message = getItemComparisonFailureMessage(expectedCart.getItems(),
                        actualCart.getItems());
            }
        }

        return message;
    }
}