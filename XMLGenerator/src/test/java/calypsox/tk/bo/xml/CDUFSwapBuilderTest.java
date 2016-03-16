/**
 * 
 */
package calypsox.tk.bo.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.Vector;

import org.junit.Test;

import com.calypso.tk.core.CallInfo;
import com.calypso.tk.core.JDate;
import com.calypso.tk.core.Trade;
import com.calypso.tk.product.CashSettleInfo;
import com.calypso.tk.product.Swap;
import com.calypso.tk.product.SwapLeg;
import com.calypso.tk.product.util.CompoundMethod;
import com.calypso.tk.product.util.quotableReset.FXResetPurpose;
import com.calypso.tk.refdata.FXResetOverrideImpl;
import com.calypso.tk.upload.jaxb.CalypsoTrade;

/**
 * Test Class for CDUFSwapBuilder.
 */
public class CDUFSwapBuilderTest {

	/**
	 * Test method for {@link calypsox.tk.bo.xml.CDUFSwapBuilder#fillTradeHeader(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Trade, com.calypso.tk.upload.jaxb.CalypsoTrade)}.
	 */
	@Test
	public final void testFillTradeHeader() {
		Trade trade = new Trade();
		trade.setProduct(new Swap());

		JDate jdate = JDate.getNow();

		Swap swap  = (Swap) trade.getProduct();
		swap.setPrincipal(1000.0);
		swap.setStartDate(jdate);

		CalypsoTrade calypsoTrade = new CalypsoTrade();

		assertEquals((Double)0.0, (Double)calypsoTrade.getTradeNotional());
		assertNull(calypsoTrade.getStartDate());

		CDUFSwapBuilder builder = new CDUFSwapBuilder();
		builder.fillTradeHeader(null, trade, calypsoTrade);

		assertNotNull(calypsoTrade.getTradeNotional());
		assertNotNull(calypsoTrade.getStartDate());
		assertEquals((Double)1000.0, (Double)calypsoTrade.getTradeNotional());
		assertEquals(jdate.getDayOfMonth(), calypsoTrade.getStartDate().getDay());

	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.CDUFSwapBuilder#fillProduct(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Trade, com.calypso.tk.upload.jaxb.Product)}.
	 */
	@Test
	public final void testFillProduct() {
		Trade trade = new Trade();
		trade.setProduct(new Swap());

		Swap swap  = (Swap) trade.getProduct();
		CallInfo callInfo = new CallInfo();
		callInfo.setExerciseType("TestType");
		swap.setCallInfo(callInfo);
		swap.setInitialFXRate(1.0);

		com.calypso.tk.upload.jaxb.Product jaxbProduct = new com.calypso.tk.upload.jaxb.Product();

		assertNull(jaxbProduct.getInterestRateSwap());

		CDUFSwapBuilder builder = new CDUFSwapBuilder();
		builder.fillProduct(null, trade, jaxbProduct);

		com.calypso.tk.upload.jaxb.InterestRateSwap jaxbSwap = jaxbProduct.getInterestRateSwap();

		assertNotNull(jaxbSwap);
		assertNotNull(jaxbSwap.getFxRate());
		assertNotNull(jaxbSwap.getExerciseType());
		assertEquals("TestType", jaxbSwap.getExerciseType());
		assertEquals((Double) 1.0 , jaxbSwap.getFxRate());
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.CDUFSwapBuilder#fillProduct(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Trade, com.calypso.tk.upload.jaxb.Product)}.
	 */
	@Test
	public final void testFillProduct2() {
		Trade trade = new Trade();
		trade.setProduct(new Swap());

		Swap swap  = (Swap) trade.getProduct();
		
		FXResetOverrideImpl fxResetOverrride = new FXResetOverrideImpl();
		fxResetOverrride.setUseIndexResetDateB(true);
		fxResetOverrride.setHolidays(new Vector<String>());
		fxResetOverrride.setResetOffset(2);
		swap.setFXResetOverride(FXResetPurpose.PrincipalAdjustment, fxResetOverrride);

		com.calypso.tk.upload.jaxb.Product jaxbProduct = new com.calypso.tk.upload.jaxb.Product();

		assertNull(jaxbProduct.getInterestRateSwap());

		CDUFSwapBuilder builder = new CDUFSwapBuilder();
		builder.fillProduct(null, trade, jaxbProduct);

		com.calypso.tk.upload.jaxb.InterestRateSwap jaxbSwap = jaxbProduct.getInterestRateSwap();

		assertNotNull(jaxbSwap);
		assertEquals((Integer)2, jaxbSwap.getFXResetOffset());
		assertNull(jaxbSwap.getFXResetHolidays());
		assertTrue(jaxbSwap.isIndexResetDate());
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.CDUFSwapBuilder#fillProduct(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Trade, com.calypso.tk.upload.jaxb.Product)}.
	 */
	@Test
	public final void testFillProduct3() {
		Trade trade = new Trade();
		trade.setProduct(new Swap());

		Swap swap  = (Swap) trade.getProduct();
		
		FXResetOverrideImpl fxResetOverrride = new FXResetOverrideImpl();
		fxResetOverrride.setHolidays(new Vector<String>());
		fxResetOverrride.setResetOffset(2);
		
		SwapLeg swapLegPay = new SwapLeg();
		swapLegPay.setLegType("Float");
		swapLegPay.setIntermediateCurrency("EUR");
		swapLegPay.setPrincipalCurrency("USD");
		swapLegPay.setCurrency("EUR");
		swapLegPay.setFXResetOverride(FXResetPurpose.IntermediateToSettlementConversion, fxResetOverrride);
		
		swap.setSwapLeg(swapLegPay, true);

		com.calypso.tk.upload.jaxb.Product jaxbProduct = new com.calypso.tk.upload.jaxb.Product();

		assertNull(jaxbProduct.getInterestRateSwap());

		CDUFSwapBuilder builder = new CDUFSwapBuilder();
		builder.fillProduct(null, trade, jaxbProduct);

		com.calypso.tk.upload.jaxb.InterestRateSwap jaxbSwap = jaxbProduct.getInterestRateSwap();

		assertNotNull(jaxbSwap);
		assertEquals("2", jaxbSwap.getSettlementFxResetOffSet());
		assertEquals("NaN|2||false|false", jaxbSwap.getSettlementFxReset().toString());
		assertNull(jaxbSwap.getSettlementFxResetHoliday());
	}

	/******** 
	 * 
	 * Testing for Private Methods
	 * 
	 * ********/


	/**
	 * Unit Test Private Method fillCashSettleInfo
	 */
	@Test
	public final void testfillCashSettleInfo() {
		Trade trade = new Trade();

		Swap swap  = spy(new Swap());
		Vector<CashSettleInfo> tradeCashSettleInfo = new Vector<CashSettleInfo>();
		CashSettleInfo settleInfo = new CashSettleInfo();
		settleInfo.setAgreement("TestAgreement");
		tradeCashSettleInfo.add(settleInfo);
		doReturn(tradeCashSettleInfo).when(swap).getCashSettleInfo();

		trade.setProduct(swap);

		CalypsoTrade calypsoTrade = new CalypsoTrade();

		assertEquals((Double)0.0, (Double)calypsoTrade.getTradeNotional());
		assertNull(calypsoTrade.getStartDate());

		CDUFSwapBuilder builder = new CDUFSwapBuilder();
		builder.fillTradeHeader(null, trade, calypsoTrade);

		assertNotNull(calypsoTrade.getCashSettlementInfo());
		assertNotNull(calypsoTrade.getCashSettlementInfo().getAgreement());
		assertEquals("TestAgreement", calypsoTrade.getCashSettlementInfo().getAgreement());
	}

	/**
	 * Unit Test Private Method fillLeg
	 */
	@Test
	public final void testfillLeg() {
		Trade trade = new Trade();
		trade.setProduct(new Swap());

		SwapLeg swapLegPay = new SwapLeg();
		swapLegPay.setLegType("Float");
		swapLegPay.setParamValue("ROUNDING", "NEAREST");
		swapLegPay.setCompoundMethod(null);
		SwapLeg swapLegRec = new SwapLeg();
		swapLegRec.setLegType("Fixed");
		swapLegRec.setCompoundMethod(CompoundMethod.SPREAD);

		Swap swap  = (Swap) trade.getProduct();
		swap.setSwapLeg(swapLegPay, true);
		swap.setSwapLeg(swapLegRec, false);


		com.calypso.tk.upload.jaxb.Product jaxbProduct = new com.calypso.tk.upload.jaxb.Product();

		assertNull(jaxbProduct.getInterestRateSwap());

		CDUFSwapBuilder builder = new CDUFSwapBuilder();
		builder.fillProduct(null, trade, jaxbProduct);

		com.calypso.tk.upload.jaxb.InterestRateSwap jaxbSwap = jaxbProduct.getInterestRateSwap();

		assertNotNull(jaxbSwap);
		assertNotNull(jaxbSwap.getSwapLeg());
		for(com.calypso.tk.upload.jaxb.SwapLeg swapLeg : jaxbSwap.getSwapLeg()){
			if(swapLeg.getPayRec().equals("Pay")){
				assertEquals("Float", swapLeg.getLegType());
				assertEquals("NEAREST", swapLeg.getAmountsRounding());
			}
			if(swapLeg.getPayRec().equals("Rec")){
				assertEquals("Fixed", swapLeg.getLegType());
				assertTrue(swapLeg.isCompoundWithSpreadB());
			}
		}
	}
}
