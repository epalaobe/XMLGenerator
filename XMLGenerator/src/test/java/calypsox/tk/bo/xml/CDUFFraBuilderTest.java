package calypsox.tk.bo.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calypso.tk.core.JDate;
import com.calypso.tk.core.Trade;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.product.FRA;
import com.calypso.tk.upload.jaxb.CalypsoTrade;

/**
 * Test Class for CDUFFraBuilder
 * 
 * @author epalaobe
 */
public class CDUFFraBuilderTest {

	/**
	 * Test method for {@link calypsox.tk.bo.xml.CDUFFraBuilder#fillTradeHeader(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Trade, com.calypso.tk.upload.jaxb.CalypsoTrade)}.
	 */
	@Test
	public void testFillTradeHeader() {
		PricingEnv pricingEnv = new PricingEnv();
		
		Trade trade = new Trade();
		trade.setProduct(new FRA());
		
		JDate jdate = JDate.getNow();
		
		FRA fra  = (FRA) trade.getProduct();
		fra.setPrincipal(1000.0);
		fra.setStartDate(jdate);
		
		CalypsoTrade calypsoTrade = new CalypsoTrade();
		
		assertEquals((Double)0.0 , (Double)calypsoTrade.getTradeNotional());
		assertNull(calypsoTrade.getStartDate());
		
		CDUFFraBuilder builder = new CDUFFraBuilder();
		builder.fillTradeHeader(pricingEnv, trade, calypsoTrade);
		
		assertNotNull(calypsoTrade.getTradeNotional());
		assertNotNull(calypsoTrade.getStartDate());
		assertEquals((Double)1000.0, (Double)calypsoTrade.getTradeNotional());
		assertEquals(jdate.getDayOfMonth(), calypsoTrade.getStartDate().getDay());
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.CDUFFraBuilder#fillProduct(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Trade, com.calypso.tk.upload.jaxb.Product)}.
	 */
	@Test
	public void testFillProduct() {
		PricingEnv pricingEnv = new PricingEnv();
		
		Trade trade = new Trade();
		trade.setProduct(new FRA());
		
		FRA fra  = (FRA) trade.getProduct();
		fra.setDiscountMethod("Method");
		fra.setSettleInArrears(true);
		fra.setFixedRate(1.0);

		com.calypso.tk.upload.jaxb.Product jaxbProduct = new com.calypso.tk.upload.jaxb.Product();
		
		assertNull(jaxbProduct.getFRA());
		
		CDUFFraBuilder builder = new CDUFFraBuilder();
		builder.fillProduct(pricingEnv, trade, jaxbProduct);
		
		com.calypso.tk.upload.jaxb.FRA jaxbFra = jaxbProduct.getFRA();
		
		assertNotNull(jaxbFra);
		assertNotNull(jaxbFra.isSettleInArrears());
		assertNotNull(jaxbFra.getDiscountMethod());
		assertNotNull(jaxbFra.getRate());
		assertTrue(jaxbFra.isSettleInArrears());
		assertEquals(jaxbFra.getDiscountMethod(), "Method");
		assertEquals(jaxbFra.getRate(), (Double) 1.0);
	}

}
