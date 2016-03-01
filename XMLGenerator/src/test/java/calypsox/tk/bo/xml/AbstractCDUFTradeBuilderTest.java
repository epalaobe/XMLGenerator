package calypsox.tk.bo.xml;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.calypso.tk.core.Action;
import com.calypso.tk.core.Book;
import com.calypso.tk.core.JDate;
import com.calypso.tk.core.JDatetime;
import com.calypso.tk.core.LegalEntity;
import com.calypso.tk.core.Product;
import com.calypso.tk.core.RoundingMethod;
import com.calypso.tk.core.Trade;
import com.calypso.tk.core.TradeBundle;
import com.calypso.tk.refdata.RateIndex;

/**
 * @author epalaobe
 *
 */
public class AbstractCDUFTradeBuilderTest {

	AbstractCDUFTradeBuilder builder;

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.builder = mock(AbstractCDUFTradeBuilder.class);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#fillTradeHeader(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Trade, com.calypso.tk.upload.jaxb.CalypsoTrade)}.
	 */
	@Test
	public final void testFillTradeHeader() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getCounterPartyCountry(com.calypso.tk.core.LegalEntity)}.
	 */
	@Test
	public final void testGetCounterPartyCountry() {
		LegalEntity mockedLegalEntity = mock(LegalEntity.class);
		mockedLegalEntity.setCountry("SPAIN");

		doCallRealMethod().when(this.builder).getCounterPartyCountry(mockedLegalEntity);
		assertNotNull(this.builder.getCounterPartyCountry(mockedLegalEntity));
		assertTrue(this.builder.getCounterPartyCountry(mockedLegalEntity).equals("SPAIN"));
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getAction(com.calypso.tk.core.Action)}.
	 */
	@Test
	public final void testGetAction() {
		Trade mockedTrade = mock(Trade.class);
		mockedTrade.setAction(Action.AMEND);

		doCallRealMethod().when(this.builder).getAction(mockedTrade.getAction());
		assertNotNull(this.builder.getAction(mockedTrade.getAction()));
		assertTrue(this.builder.getAction(mockedTrade.getAction()).equals("AMEND"));
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getTradeDateJDate(com.calypso.tk.core.JDatetime)}.
	 */
	@Test
	public final void testGetTradeDateJDate() {
		Trade mockedTrade = mock(Trade.class);
		JDate jDate = JDate.getNow();
		JDatetime jDateTime = new JDatetime(jDate.getDate().getTime());
		mockedTrade.setTradeDate(jDateTime);

		doCallRealMethod().when(this.builder).getTradeDateJDate(mockedTrade.getTradeDate());
		assertNotNull(this.builder.getTradeDateJDate(mockedTrade.getTradeDate()));
		assertTrue(this.builder.getTradeDateJDate(mockedTrade.getTradeDate()).equals(jDate));
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getTradeKeywords(com.calypso.tk.core.Trade)}.
	 */
	@Test
	public final void testGetTradeKeywords() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getReconventionList(com.calypso.tk.core.Product)}.
	 */
	@Test
	public final void testGetReconventionList() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getReconventionParameters(com.calypso.tk.product.reconvention.Reconvention)}.
	 */
	@Test
	public final void testGetReconventionParameters() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getCashflows(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Product)}.
	 */
	@Test
	public final void testGetCashflows() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getBuySell(double)}.
	 */
	@Test
	public final void testGetBuySell() {
		Trade mockedTrade = mock(Trade.class);
		mockedTrade.setQuantity(-1000.0);

		doCallRealMethod().when(this.builder).getBuySell(mockedTrade.getQuantity());
		assertNotNull(this.builder.getBuySell(mockedTrade.getQuantity()));
		assertTrue(this.builder.getBuySell(mockedTrade.getQuantity()).equals("SELL"));

		mockedTrade.setQuantity(1000.0);

		doCallRealMethod().when(this.builder).getBuySell(mockedTrade.getQuantity());
		assertNotNull(this.builder.getBuySell(mockedTrade.getQuantity()));
		assertTrue(this.builder.getBuySell(mockedTrade.getQuantity()).equals("BUY"));
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getBook(com.calypso.tk.core.Book)}.
	 */
	@Test
	public final void testGetBook() {
		Book mockedBook = mock(Book.class);
		doCallRealMethod().when(mockedBook).setName("TestName");
		mockedBook.setName("TestName");
		doCallRealMethod().when(this.builder).getBook(mockedBook);
		doCallRealMethod().when(mockedBook).getName();
		assertNotNull(this.builder.getBook(mockedBook));
		assertTrue(this.builder.getBook(mockedBook).equals("TestName"));
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getTradeBundle(com.calypso.tk.core.TradeBundle)}.
	 */
	@Test
	public final void testGetTradeBundle() {
		TradeBundle mockedTradeBundle = mock(TradeBundle.class);
		mockedTradeBundle.setName("TestName");

		doCallRealMethod().when(this.builder).getTradeBundle(mockedTradeBundle);
		assertNotNull(this.builder.getTradeBundle(mockedTradeBundle));
		assertTrue(this.builder.getTradeBundle(mockedTradeBundle).equals("TestName"));
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getTradeBundleType(com.calypso.tk.core.TradeBundle)}.
	 */
	@Test
	public final void testGetTradeBundleType() {
		TradeBundle mockedTradeBundle = mock(TradeBundle.class);
		mockedTradeBundle.setType("TestType");

		doCallRealMethod().when(this.builder).getTradeBundleType(mockedTradeBundle);
		assertNotNull(this.builder.getTradeBundleType(mockedTradeBundle));
		assertTrue(this.builder.getTradeBundleType(mockedTradeBundle).equals("TestType"));
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getTradeBundleOneMessage(com.calypso.tk.core.TradeBundle)}.
	 */
	@Test
	public final void testGetTradeBundleOneMessage() {
		TradeBundle mockedTradeBundle = mock(TradeBundle.class);
		mockedTradeBundle.setOneMessage(true);

		doCallRealMethod().when(this.builder).getTradeBundleOneMessage(mockedTradeBundle);
		assertNotNull(this.builder.getTradeBundleOneMessage(mockedTradeBundle));
		assertTrue(this.builder.getTradeBundleOneMessage(mockedTradeBundle));
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getCounterParty(com.calypso.tk.core.LegalEntity)}.
	 */
	@Test
	public final void testGetCounterParty() {
		LegalEntity mockedLegalEntity = mock(LegalEntity.class);
		mockedLegalEntity.setCode("TestName");

		doCallRealMethod().when(this.builder).getCounterParty(mockedLegalEntity);
		assertNotNull(this.builder.getCounterParty(mockedLegalEntity));
		assertTrue(this.builder.getCounterParty(mockedLegalEntity).equals("TestName"));
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getRoundingMethod(com.calypso.tk.core.RoundingMethod)}.
	 */
	@Test
	public final void testGetRoundingMethod() {
		RoundingMethod roundingMethod = new RoundingMethod();
		doCallRealMethod().when(this.builder).getRoundingMethod(roundingMethod);
		assertNotNull(this.builder.getRoundingMethod(roundingMethod));
		assertTrue(this.builder.getRoundingMethod(roundingMethod).equals("NEAREST"));
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getReconventionType(com.calypso.tk.product.reconvention.ReconventionType)}.
	 */
	@Test
	public final void testGetReconventionType() {
		fail("Not yet implemented"); // TODO
	}

	/** 
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getHolidayCode(com.calypso.tk.core.Product)}.
	 */
	@Test
	public final void testGetHolidayCode() {
		Trade mockedTrade = mock(Trade.class);
		Product mockedProduct = mock(Product.class);
		when(mockedTrade.getProduct()).thenReturn(mockedProduct);
		RateIndex mockedRateIndex = mock(RateIndex.class);
		when(mockedProduct.getRateIndex()).thenReturn(mockedRateIndex);
		
		@SuppressWarnings("unchecked")
		Vector<String> mockedVector = mock(Vector.class);
		when(mockedProduct.getHolidays()).thenReturn(mockedVector);
		doReturn("TestHoliday").when(mockedVector).get(0);

		doCallRealMethod().when(this.builder).getHolidayCode(mockedProduct);
		assertNotNull(this.builder.getHolidayCode(mockedProduct));
		//TODO
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getXmlGregorianCalendarFromDate(com.calypso.tk.core.JDate)}.
	 */
	@Test
	public final void testGetXmlGregorianCalendarFromDate() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getXmlGregorianCalendarFromTime(int)}.
	 */
	@Test
	public final void testGetXmlGregorianCalendarFromTime() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#twentyFourHourTimeToMilliseconds(int)}.
	 */
	@Test
	public final void testTwentyFourHourTimeToMilliseconds() {
		int hour = 100;
		int inMilliseconds = 3600000;
		doCallRealMethod().when(this.builder).twentyFourHourTimeToMilliseconds(hour);
		assertNotNull(this.builder.twentyFourHourTimeToMilliseconds(hour));
		assertTrue(this.builder.twentyFourHourTimeToMilliseconds(hour)==inMilliseconds);
	}

}
