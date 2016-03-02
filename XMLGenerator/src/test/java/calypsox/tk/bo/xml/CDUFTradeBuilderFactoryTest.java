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
import com.calypso.tk.product.Repo;
import com.calypso.tk.product.SimpleTransfer;
import com.calypso.tk.product.Swap;

/**
 * Test Class for CDUFTradeBuilderFactory
 * 
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

        CDUFTradeBuilder builder = CDUFTradeBuilderFactory.getInstance().getBuilder(trade);
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

        CDUFTradeBuilder builder = CDUFTradeBuilderFactory.getInstance().getBuilder(trade);
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

        CDUFTradeBuilderFactory.getInstance().getBuilder(trade);
    }
    
    /**
     * Test method for {@link calypsox.tk.bo.xml.CDUFTradeBuilderFactory#getBuilder(com.calypso.tk.core.Trade)}.
     */
    @Test
    public void testGetBuilderSimpleTransfer() {
        Trade trade = new Trade();
        trade.setProduct(new SimpleTransfer());

        CDUFTradeBuilder builder = CDUFTradeBuilderFactory.getInstance().getBuilder(trade);
        assertNotNull(builder);
        assertTrue(builder instanceof CDUFSimpleTransferBuilder);
    }
    
    /**
     * Test method for {@link calypsox.tk.bo.xml.CDUFTradeBuilderFactory#getBuilder(com.calypso.tk.core.Trade)}.
     */
    @Test
    public void testGetBuilderRepo() {
        Trade trade = new Trade();
        trade.setProduct(new Repo());

        CDUFTradeBuilder builder = CDUFTradeBuilderFactory.getInstance().getBuilder(trade);
        assertNotNull(builder);
        assertTrue(builder instanceof CDUFRepoBuilder);
    }

}
