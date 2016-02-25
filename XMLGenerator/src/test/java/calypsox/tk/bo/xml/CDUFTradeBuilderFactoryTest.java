/**
 *
 */
package calypsox.tk.bo.xml;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calypso.tk.core.Trade;
import com.calypso.tk.product.Bond;
import com.calypso.tk.product.FRA;
import com.calypso.tk.product.Swap;

/**
 * @author xe36404
 */
public class CDUFTradeBuilderFactoryTest {

    /**
     * Test method for {@link calypsox.tk.bo.xml.CDUFTradeBuilderFactory#getBuilder(com.calypso.tk.core.Trade)}.
     */
    @Test
    public void testGetBuilderSwap() {
        Trade trade = new Trade();
        trade.setProduct(new Swap());

        CDUFTradeBuilder builder = CDUFTradeBuilderFactory.getBuilder(trade);
        assertNotNull(builder);
        assertTrue(builder instanceof CDUFSwapBuilder);
    }

    /**
     * Test method for {@link calypsox.tk.bo.xml.CDUFTradeBuilderFactory#getBuilder(com.calypso.tk.core.Trade)}.
     */
    @Test
    public void testGetBuilderFra() {
        Trade trade = new Trade();
        trade.setProduct(new FRA());

        CDUFTradeBuilder builder = CDUFTradeBuilderFactory.getBuilder(trade);
        assertNotNull(builder);
        assertTrue(builder instanceof CDUFFraBuilder);
    }
    
    /**
     * Test method for {@link calypsox.tk.bo.xml.CDUFTradeBuilderFactory#getBuilder(com.calypso.tk.core.Trade)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetBuilderBond() {
        Trade trade = new Trade();
        trade.setProduct(new Bond());

        CDUFTradeBuilder builder = CDUFTradeBuilderFactory.getBuilder(trade);
    }

}
