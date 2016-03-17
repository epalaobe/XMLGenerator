package calypsox.tk.bo.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.calypso.jaxb.xml.entity10.Role;
import com.calypso.tk.core.Action;
import com.calypso.tk.core.Book;
import com.calypso.tk.core.CalypsoServiceException;
import com.calypso.tk.core.CashFlowSet;
import com.calypso.tk.core.DateRoll;
import com.calypso.tk.core.JDate;
import com.calypso.tk.core.JDatetime;
import com.calypso.tk.core.LegalEntity;
import com.calypso.tk.core.Product;
import com.calypso.tk.core.RoundingMethod;
import com.calypso.tk.core.Trade;
import com.calypso.tk.core.TradeBundle;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.product.FRA;
import com.calypso.tk.product.Swap;
import com.calypso.tk.refdata.RateIndex;
import com.calypso.tk.upload.jaxb.CalypsoTrade;
import com.calypso.tk.upload.jaxb.CashFlows;
import com.calypso.tk.upload.jaxb.Keyword;
import com.calypso.tk.upload.jaxb.TradeKeywords;

/**
 * Test Class for AbstractCDUFTradeBuilder.
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
	 * @throws CalypsoServiceException 
	 */
	@Test
	public final void testFillTradeHeader() throws CalypsoServiceException, DatatypeConfigurationException {
		
		PricingEnv pricingEnv = new PricingEnv();
		
		FRA product = new FRA();
		product.setPaymentEndDateRoll(new DateRoll());
		product.setMaturityDate(JDate.valueOf("14/03/2016"));
		product.setStartDate(JDate.valueOf("14/03/2017"));
		product.setPaymentEndHolidays(new Vector<String>());

		Trade trade = new Trade();
		trade.setAction(Action.AMEND);
		trade.setQuantity(1000.0);
		trade.setComment("TradeComment"); 
		trade.setRole(Role.GUARANTOR.toString());
		trade.setSalesPerson("TradeSalesPerson");
		trade.setTradeCurrency("EUR");
		trade.setTraderName("TradeTraderName");
		trade.setProduct(product);

		doCallRealMethod().when(this.builder).getAction(trade.getAction());
		doCallRealMethod().when(this.builder).getBuySell(trade.getQuantity());
		
		CalypsoTrade calypsoTrade = new CalypsoTrade();
		
		doCallRealMethod().when(this.builder).fillTradeHeader(pricingEnv, trade, calypsoTrade);
		this.builder.fillTradeHeader(pricingEnv, trade, calypsoTrade);
		
		assertNotNull(calypsoTrade);
		assertTrue(calypsoTrade.getAction().equals(Action.AMEND.toString()));
		assertTrue(calypsoTrade.getBuySell().equals("BUY"));
		assertTrue(calypsoTrade.getComment().equals("TradeComment"));
		assertTrue(calypsoTrade.getCounterPartyRole().equals(Role.GUARANTOR.toString()));
		assertTrue(calypsoTrade.getSalesPerson().equals("TradeSalesPerson"));
		assertTrue(calypsoTrade.getTradeCurrency().equals("EUR"));
		assertTrue(calypsoTrade.getTraderName().equals("TradeTraderName"));
		
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getCounterPartyCountry(com.calypso.tk.core.LegalEntity)}.
	 */
	@Test
	public final void testGetCounterPartyCountry() {
		LegalEntity legalEntity = new LegalEntity();
		legalEntity.setCountry("SPAIN");

		doCallRealMethod().when(this.builder).getCounterPartyCountry(legalEntity);

		String country = this.builder.getCounterPartyCountry(legalEntity);

		assertNotNull(country);
		assertTrue(country.equals("SPAIN"));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getCounterPartyCountry(com.calypso.tk.core.LegalEntity)}.
	 */
	@Test
	public final void testGetCounterPartyCountry2() {
		doCallRealMethod().when(this.builder).getCounterPartyCountry(null);
		String country = this.builder.getCounterPartyCountry(null);
		assertNull(country);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getAction(com.calypso.tk.core.Action)}.
	 */
	@Test
	public final void testGetAction() {
		Trade trade = new Trade();
		trade.setAction(Action.AMEND);

		doCallRealMethod().when(this.builder).getAction(trade.getAction());

		String action = this.builder.getAction(trade.getAction());

		assertNotNull(action);
		assertTrue(action.equals("AMEND"));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getAction(com.calypso.tk.core.Action)}.
	 */
	@Test
	public final void testGetAction2() {
		doCallRealMethod().when(this.builder).getAction(null);
		String action = this.builder.getAction(null);
		assertNull(action);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getTradeDateJDate(com.calypso.tk.core.JDatetime)}.
	 */
	@Test
	public final void testGetTradeDateJDate() {
		Trade trade = new Trade();
		JDate jDate = JDate.getNow();
		JDatetime jDateTime = new JDatetime(jDate.getDate().getTime());
		trade.setTradeDate(jDateTime);

		doCallRealMethod().when(this.builder).getTradeDateJDate(trade.getTradeDate());

		JDate jDateTradeDate = this.builder.getTradeDateJDate(trade.getTradeDate());

		assertNotNull(jDateTradeDate);
		assertTrue(jDateTradeDate.equals(jDate));
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getTradeDateJDate(com.calypso.tk.core.JDatetime)}.
	 */
	@Test
	public final void testGetTradeDateJDate2() {
		doCallRealMethod().when(this.builder).getTradeDateJDate(null);
		JDate jDateTradeDate = this.builder.getTradeDateJDate(null);
		assertNull(jDateTradeDate);
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getTradeKeywords(com.calypso.tk.core.Trade)}.
	 */
	@Test
	public final void testGetTradeKeywords() {
		Trade trade = new Trade();
		Hashtable<String, String> keywords = new Hashtable<String, String>();
		keywords.put("KeywordName", "KeywordValue");
		trade.setKeywords(keywords);

		doCallRealMethod().when(this.builder).getTradeKeywords(trade);

		TradeKeywords tradeKeywords = this.builder.getTradeKeywords(trade);

		assertNotNull(tradeKeywords);

		List<Keyword> listaKeywords = tradeKeywords.getKeyword();

		assertNotNull(listaKeywords);
		assertNotNull(listaKeywords.get(0));
		assertEquals(listaKeywords.get(0).getKeywordValue(), "KeywordValue");
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getTradeKeywords(com.calypso.tk.core.Trade)}.
	 */
	@Test
	public final void testGetTradeKeywords2() {
		Trade trade = new Trade();
		trade.setKeywords(null);

		doCallRealMethod().when(this.builder).getTradeKeywords(trade);

		TradeKeywords tradeKeywords = this.builder.getTradeKeywords(trade);

		assertNotNull(tradeKeywords);

		List<Keyword> listaKeywords = tradeKeywords.getKeyword();

		assertNotNull(listaKeywords);
		assertTrue(listaKeywords.isEmpty());
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getCashflows(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Product)}.
	 */
	@Test
	public final void testGetCashflows() {
		PricingEnv pricingEnv = new PricingEnv();
		FRA product = new FRA();
		product.setPaymentEndDateRoll(new DateRoll());
		product.setMaturityDate(JDate.valueOf("14/03/2016"));
		product.setStartDate(JDate.valueOf("14/03/2017"));
		product.setPaymentEndHolidays(new Vector<String>());
		
		CashFlows cfSet = null;
		
		doCallRealMethod().when(this.builder).getCashflows(pricingEnv, product);
		cfSet = this.builder.getCashflows(pricingEnv, product);
		
		assertNotNull(cfSet);
		assertTrue(cfSet.getCashFlow().isEmpty());
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getCashflows(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Product)}.
	 */
	@Test
	public final void testGetCashflows2() {
		PricingEnv pricingEnv = new PricingEnv();
		
		CashFlowSet cashFlowSet = new CashFlowSet();
		com.calypso.tk.core.CashFlow cashFlow = new com.calypso.tk.core.CashFlow();
		cashFlow.setAmount(1000.0);
		cashFlow.setSubId(1);
		cashFlowSet.add(cashFlow);
		
		Swap product = new Swap();
		product.setCustomFlowsB(true);
		product.setFlows(cashFlowSet);

		CashFlows cfSet = null;
		
		doCallRealMethod().when(this.builder).getCashflows(pricingEnv, product);
		cfSet = this.builder.getCashflows(pricingEnv, product);
		
		assertNotNull(cfSet.getCashFlow());
		assertEquals((Double)1000.0 ,cfSet.getCashFlow().get(0).getAmount());
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getBuySell(double)}.
	 */
	@Test
	public final void testGetBuySell_SELL() {
		Trade trade = new Trade();
		trade.setQuantity(-1000.0);

		doCallRealMethod().when(this.builder).getBuySell(trade.getQuantity());

		String buyOrSell = this.builder.getBuySell(trade.getQuantity());

		assertNotNull(buyOrSell);
		assertTrue(buyOrSell.equals("SELL"));
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getBuySell(double)}.
	 */
	@Test
	public final void testGetBuySell_BUY() {
		Trade trade = new Trade();
		trade.setQuantity(1000.0);

		doCallRealMethod().when(this.builder).getBuySell(trade.getQuantity());

		String buyOrSell = this.builder.getBuySell(trade.getQuantity());

		assertNotNull(buyOrSell);
		assertTrue(buyOrSell.equals("BUY"));
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getBook(com.calypso.tk.core.Book)}.
	 */
	@Test
	public final void testGetBook() {
		Book book = new Book();
		book.setName("TestName");

		doCallRealMethod().when(this.builder).getBook(book);

		String bookName = this.builder.getBook(book);

		assertNotNull(bookName);
		assertTrue(bookName.equals("TestName"));
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getBook(com.calypso.tk.core.Book)}.
	 */
	@Test
	public final void testGetBook2() {
		doCallRealMethod().when(this.builder).getBook(null);
		String bookName = this.builder.getBook(null);
		assertNull(bookName);
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getTradeBundle(com.calypso.tk.core.TradeBundle)}.
	 */
	@Test
	public final void testGetTradeBundle() {
		TradeBundle tradeBundle =  new TradeBundle();
		tradeBundle.setName("TestName");

		doCallRealMethod().when(this.builder).getTradeBundle(tradeBundle);

		String tradeBundleName = this.builder.getTradeBundle(tradeBundle);

		assertNotNull(tradeBundleName);
		assertTrue(tradeBundleName.equals("TestName"));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getTradeBundle(com.calypso.tk.core.TradeBundle)}.
	 */
	@Test
	public final void testGetTradeBundle2() {
		doCallRealMethod().when(this.builder).getTradeBundle(null);
		String tradeBundleName = this.builder.getTradeBundle(null);
		assertNull(tradeBundleName);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getTradeBundleType(com.calypso.tk.core.TradeBundle)}.
	 */
	@Test
	public final void testGetTradeBundleType() {
		TradeBundle tradeBundle = new TradeBundle();
		tradeBundle.setName("TestName");
		tradeBundle.setType("TestType");

		doCallRealMethod().when(this.builder).getTradeBundleType(tradeBundle);

		String tradeBundleTypeName = this.builder.getTradeBundleType(tradeBundle);

		assertNotNull(tradeBundleTypeName);
		assertTrue(tradeBundleTypeName.equals("TestType"));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getTradeBundleType(com.calypso.tk.core.TradeBundle)}.
	 */
	@Test
	public final void testGetTradeBundleType2() {
		doCallRealMethod().when(this.builder).getTradeBundleType(null);
		String tradeBundleTypeName = this.builder.getTradeBundleType(null);
		assertNull(tradeBundleTypeName);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getTradeBundleOneMessage(com.calypso.tk.core.TradeBundle)}.
	 */
	@Test
	public final void testGetTradeBundleOneMessage() {
		TradeBundle tradeBundle =  new TradeBundle();
		tradeBundle.setName("TestName");
		tradeBundle.setOneMessage(true);

		doCallRealMethod().when(this.builder).getTradeBundleOneMessage(tradeBundle);

		boolean tradeBundle1Msg = this.builder.getTradeBundleOneMessage(tradeBundle);

		assertNotNull(tradeBundle1Msg);
		assertTrue(tradeBundle1Msg);
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getTradeBundleOneMessage(com.calypso.tk.core.TradeBundle)}.
	 */
	@Test
	public final void testGetTradeBundleOneMessage2() {
		doCallRealMethod().when(this.builder).getTradeBundleOneMessage(null);
		boolean tradeBundle1Msg = this.builder.getTradeBundleOneMessage(null);
		assertFalse(tradeBundle1Msg);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getCounterParty(com.calypso.tk.core.LegalEntity)}.
	 */
	@Test
	public final void testGetCounterParty() {
		LegalEntity legalEntity = new LegalEntity();
		legalEntity.setCode("TestName");

		doCallRealMethod().when(this.builder).getCounterParty(legalEntity);

		String counterPartyName = this.builder.getCounterParty(legalEntity);

		assertNotNull(counterPartyName);
		assertTrue(counterPartyName.equals("TestName"));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getCounterParty(com.calypso.tk.core.LegalEntity)}.
	 */
	@Test
	public final void testGetCounterParty2() {
		doCallRealMethod().when(this.builder).getCounterParty(null);
		String counterPartyName = this.builder.getCounterParty(null);
		assertNull(counterPartyName);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getRoundingMethod(com.calypso.tk.core.RoundingMethod)}.
	 */
	@Test
	public final void testGetRoundingMethod() {
		RoundingMethod roundingMethod = new RoundingMethod();

		doCallRealMethod().when(this.builder).getRoundingMethod(roundingMethod);

		String roundingMethodName = this.builder.getRoundingMethod(roundingMethod);

		assertNotNull(roundingMethodName);
		assertTrue(roundingMethodName.equals("NEAREST"));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getRoundingMethod(com.calypso.tk.core.RoundingMethod)}.
	 */
	@Test
	public final void testGetRoundingMethod2() {
		doCallRealMethod().when(this.builder).getRoundingMethod(null);
		String roundingMethodName = this.builder.getRoundingMethod(null);
		assertNull(roundingMethodName);
	}

	/** 
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getHolidayCode(com.calypso.tk.core.Product)}.
	 */
	@Test
	public final void testGetHolidayCode() {
		Trade trade = mock(Trade.class);
		Product mockedProduct = mock(Product.class);
		when(trade.getProduct()).thenReturn(mockedProduct);
		RateIndex mockedRateIndex = mock(RateIndex.class);
		when(mockedProduct.getRateIndex()).thenReturn(mockedRateIndex);

		@SuppressWarnings("unchecked")
		Vector<String> mockedVector = mock(Vector.class);
		when(mockedProduct.getHolidays()).thenReturn(mockedVector);
		when(mockedVector.size()).thenReturn(1);
		doReturn("TestHoliday").when(mockedVector).get(0);

		doCallRealMethod().when(this.builder).getHolidayCode(mockedProduct);

		com.calypso.tk.upload.jaxb.HolidayCode holidayCode = this.builder.getHolidayCode(mockedProduct);

		assertNotNull(holidayCode);
		assertNotNull(holidayCode.getHoliday());
		assertNotNull(holidayCode.getHoliday().get(0));
		assertEquals("TestHoliday", holidayCode.getHoliday().get(0));
	}
	
	/** 
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getHolidayCode(com.calypso.tk.core.Product)}.
	 */
	@Test
	public final void testGetHolidayCode2() {
		FRA fra = new FRA();
		doCallRealMethod().when(this.builder).getHolidayCode(fra);
		com.calypso.tk.upload.jaxb.HolidayCode holidayCode = this.builder.getHolidayCode(fra);
		assertNull(holidayCode);
	}


	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getXmlGregorianCalendarFromDate(com.calypso.tk.core.JDate)}.
	 */
	@Test
	public final void testGetXmlGregorianCalendarFromDate() {
		JDate date=JDate.getNow();

		doCallRealMethod().when(this.builder).getXmlGregorianCalendarFromDate(date);

		XMLGregorianCalendar xmlGregorian = this.builder.getXmlGregorianCalendarFromDate(date);

		assertNotNull(xmlGregorian);
		assertNotNull(xmlGregorian.getDay());
		assertEquals(date.getDayOfMonth(), xmlGregorian.getDay());
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getXmlGregorianCalendarFromDate(com.calypso.tk.core.JDate)}.
	 */
	@Test
	public final void testGetXmlGregorianCalendarFromDate2() {
		doCallRealMethod().when(this.builder).getXmlGregorianCalendarFromDate(null);
		XMLGregorianCalendar xmlGregorian = this.builder.getXmlGregorianCalendarFromDate(null);
		assertNull(xmlGregorian);
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getXmlGregorianCalendarFromDate(com.calypso.tk.core.JDate)}.
	 */
	@Test
	public final void testGetXmlGregorianCalendarFromDate3() {
		doCallRealMethod().when(this.builder).getXmlGregorianCalendarFromDate(null);
		XMLGregorianCalendar xmlGregorian = this.builder.getXmlGregorianCalendarFromDate(null);
		assertNull(xmlGregorian);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#getXmlGregorianCalendarFromTime(int)}.
	 */
	@Test
	public final void testGetXmlGregorianCalendarFromTime() {
		int timeInMs= 1457011000;//Jan 17 1970 21:43:31 GMT+0100
		String date="17-1-1970 21:43";
		doCallRealMethod().when(this.builder).getXmlGregorianCalendarFromTime(timeInMs);

		XMLGregorianCalendar xmlGregorian=this.builder.getXmlGregorianCalendarFromTime(timeInMs);


		String dateCalendar=Integer.toString(xmlGregorian.getDay()).concat("-").concat(Integer.toString(xmlGregorian.getMonth())).concat("-").concat(Integer.toString(xmlGregorian.getYear())).concat(" ").concat(Integer.toString(xmlGregorian.getHour())).concat(":").concat(Integer.toString(xmlGregorian.getMinute()));

		assertNotNull(xmlGregorian);
		assertNotNull(xmlGregorian.getHour());
		assertEquals(date,dateCalendar);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#twentyFourHourTimeToMilliseconds(int)}.
	 */
	@Test
	public final void testTwentyFourHourTimeToMilliseconds() {
		int hour = 100;

		doCallRealMethod().when(this.builder).twentyFourHourTimeToMilliseconds(hour);

		int inMilliseconds = this.builder.twentyFourHourTimeToMilliseconds(hour);

		assertEquals(3600000, inMilliseconds);
	}

}
