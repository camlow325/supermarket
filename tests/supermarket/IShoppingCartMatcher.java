package supermarket;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
class IShoppingCartMatcher extends TypeSafeMatcher<IShoppingCart>
{
    private final IShoppingCart expectedCart;

    public IShoppingCartMatcher(IShoppingCart expectedCart)
    {
        this.expectedCart = expectedCart;
    }

    @Override
    protected boolean matchesSafely(IShoppingCart actualCart)
    {
        return getMismatchMessage(actualCart).isEmpty();
    }

    @Override
    protected void describeMismatchSafely(IShoppingCart actualCart, Description mismatchDescription)
    {
        mismatchDescription.appendText(getMismatchMessage(actualCart));
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

    @Override
    public void describeTo(Description description)
    {
        description.appendText("IShoppingCartMatcher");
    }

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
}
