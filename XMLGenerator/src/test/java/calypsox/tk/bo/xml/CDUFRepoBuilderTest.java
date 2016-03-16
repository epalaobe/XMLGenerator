/**
 * 
 */
package calypsox.tk.bo.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Vector;

import org.junit.Test;

import com.calypso.tk.bo.BOCache;
import com.calypso.tk.bo.CacheImpl;
import com.calypso.tk.core.DateRule;
import com.calypso.tk.core.DayCount;
import com.calypso.tk.core.Frequency;
import com.calypso.tk.core.JDate;
import com.calypso.tk.core.Tenor;
import com.calypso.tk.core.Trade;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.product.Bond;
import com.calypso.tk.product.Cash;
import com.calypso.tk.product.Collateral;
import com.calypso.tk.product.Equity;
import com.calypso.tk.product.JGBRepo;
import com.calypso.tk.product.Repo;
import com.calypso.tk.refdata.RateIndex;
import com.calypso.tk.refdata.SecFinanceCallableBy;
import com.calypso.tk.refdata.User;
import com.calypso.tk.service.DSConnection;
import com.calypso.tk.service.RemoteReferenceData;
import com.calypso.tk.upload.jaxb.CalypsoTrade;

/**
 * Test Class for CDUFRepoBuilder.
 */
public class CDUFRepoBuilderTest {

	/**
	 * Test method for {@link calypsox.tk.bo.xml.CDUFRepoBuilder#fillTradeHeader(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Trade, com.calypso.tk.upload.jaxb.CalypsoTrade)}.
	 */
	@Test
	public final void testFillTradeHeader() {
		PricingEnv pricingEnv = new PricingEnv();
		Trade trade = new Trade();
		JDate jdate = JDate.getNow();
		Cash cash = new Cash();
		Repo repo  = new Repo();
		
		cash.setPrincipal(1000.0);
		cash.setStartDate(jdate);
		repo.setCash(cash);
		repo.setCustomFlowsB(true);
		trade.setProduct(repo);

		CalypsoTrade calypsoTrade = new CalypsoTrade();
		assertEquals((Double)0.0 , (Double)calypsoTrade.getTradeNotional());
		assertNull(calypsoTrade.getStartDate());

		CDUFRepoBuilder builder = new CDUFRepoBuilder();
		builder.fillTradeHeader(pricingEnv, trade, calypsoTrade);

		assertNotNull(calypsoTrade.getTradeNotional());
		assertNotNull(calypsoTrade.getStartDate());
		assertEquals((Double)1000.0, (Double)calypsoTrade.getTradeNotional());
		assertEquals(jdate.getDayOfMonth(), calypsoTrade.getStartDate().getDay());
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.CDUFRepoBuilder#fillProduct(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Trade, com.calypso.tk.upload.jaxb.Product)}.
	 */
	@Test
	public final void testFillProduct() {
		mock(BOCache.class);
		CacheImpl mockedCacheImp = mock(CacheImpl.class);
		
		DSConnection mockedDS = mock(DSConnection.class);
		RemoteReferenceData mockedRRD = mock(RemoteReferenceData.class);
		when(mockedDS.getRemoteReferenceData()).thenReturn(mockedRRD);
		User user = new User();
		user.setName("UserNameTest");
		when(mockedDS.getUser()).thenReturn(user.getName());
		DSConnection.setDefault(mockedDS);
		
		PricingEnv pricingEnv = new PricingEnv();
		Trade trade = new Trade();
		JDate jdate = JDate.getNow();
		Cash cash = new Cash();
		Repo repo  = new JGBRepo();
		RateIndex rateIndex = new RateIndex("EUR", "RateIndex", new Tenor(Tenor.INTRADAY_LABEL), "Source");
		Frequency frequency = Frequency.F_DAILY;
		DayCount dayCount = DayCount.D_30_365;
		DateRule dateRule = new DateRule();
		Bond bond = new Bond();
		Collateral collateral = new Collateral();
		Vector<Collateral> collaterals = new Vector<Collateral>();
		
		bond.setId(5);
		bond.setName("Bond");
		bond.setCurrency("EUR");
		dateRule.setName("NameDateRule");
		rateIndex.setDayCount(dayCount);
		collateral.setSecurity(bond);
		collaterals.add(collateral);
		cash.setPrincipal(1000.0);
		cash.setStartDate(jdate);
		cash.setCurrency("EUR");
		cash.setRateIndex(rateIndex);
		cash.setPaymentFrequency(frequency);
		cash.setFixedRate(2.0);
		cash.setFixedDayCount(dayCount);
		repo.setCash(cash);
		repo.setAllocationType("allocationType");
		repo.setCallableBy(SecFinanceCallableBy.BOTH);
		repo.setSubType("BSB");
		repo.setFillType("fillType");
		repo.setRepriceFrequency(dateRule);
		repo.setCollaterals(collaterals);
		trade.setProduct(repo);
		
		String paramString2 = "RepoTOGGLE_CLEAN_PRICE_IN_DECIMAL";
		when(mockedCacheImp.getProperty(mockedDS, mockedDS.getUser(), paramString2)).thenReturn("0");
		BOCache.setImpl(mockedCacheImp);
		
		com.calypso.tk.upload.jaxb.Product jaxbProduct = new com.calypso.tk.upload.jaxb.Product();
		assertNull(jaxbProduct.getInterestRateSwap());
		CDUFRepoBuilder builder = new CDUFRepoBuilder();
		builder.fillProduct(pricingEnv, trade, jaxbProduct);
		com.calypso.tk.upload.jaxb.Repo jaxbRepo = jaxbProduct.getRepo();
		
		assertNotNull(jaxbRepo);
		assertNotNull(jaxbRepo.getAllocationType());
		assertEquals("allocationType", jaxbRepo.getAllocationType());
		assertNotNull(jaxbRepo.getCallableBy());
		assertEquals(SecFinanceCallableBy.BOTH.toString(), jaxbRepo.getCallableBy());
		assertNotNull(jaxbRepo.getDirection());
		assertEquals("Repo", jaxbRepo.getDirection());
		assertNotNull(jaxbRepo.getFillType());
		assertEquals("fillType", jaxbRepo.getFillType());
		assertNotNull(jaxbRepo.getFundingDetails());
		assertEquals("EUR", jaxbRepo.getFundingDetails().getCurrency());
		assertEquals(cash.getRateIndex().getName(), jaxbRepo.getFundingDetails().getRateIndex());
		assertEquals(Tenor.INTRADAY_LABEL, jaxbRepo.getFundingDetails().getTenor());
		assertEquals((Double)1000.0, (Double)jaxbRepo.getFundingDetails().getPrincipal());
		assertEquals((Double)cash.getFixedRate(), (Double)jaxbRepo.getFundingDetails().getFundingRate());
		assertNotNull(jaxbRepo.getNoticeDays());
		assertNotNull(jaxbRepo.getRepoDetails());
		assertFalse(jaxbRepo.getRepoDetails().isForceDAPB());
		assertEquals(repo.getInterestDispatchMethod(), jaxbRepo.getRepoDetails().getInterestDispatch());
		assertEquals((Double)0.0, (Double)jaxbRepo.getRepoDetails().getMinimumParAmount());
		assertEquals(repo.getRepriceFrequency().getName(), jaxbRepo.getRepoDetails().getRepriceFrequency());
		assertNotNull(jaxbRepo.getSubstitutionDetails());
		assertEquals(repo.isAutoCollateralAllocation(), jaxbRepo.getSubstitutionDetails().isAutoAllocB());
		assertEquals(repo.allowModifyMoney(), jaxbRepo.getSubstitutionDetails().isModifMoneyB());
		assertEquals(repo.getSubstitutionB(), jaxbRepo.getSubstitutionDetails().isSubstitutableB());
		assertEquals(repo.getSubstitutionLimit(), jaxbRepo.getSubstitutionDetails().getSubstitutionLimit());
		assertNotNull(jaxbRepo.getOpenTerm());
		assertEquals("TERM", jaxbRepo.getOpenTerm());
		assertNotNull(jaxbRepo.getSecurityDetails());
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.CDUFRepoBuilder#fillProduct(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Trade, com.calypso.tk.upload.jaxb.Product)}.
	 */
	@Test
	public final void testFillProduct2() {
		mock(BOCache.class);
		CacheImpl mockedCacheImp = mock(CacheImpl.class);
		
		DSConnection mockedDS = mock(DSConnection.class);
		RemoteReferenceData mockedRRD = mock(RemoteReferenceData.class);
		when(mockedDS.getRemoteReferenceData()).thenReturn(mockedRRD);
		User user = new User();
		user.setName("UserNameTest");
		when(mockedDS.getUser()).thenReturn(user.getName());
		DSConnection.setDefault(mockedDS);
		
		PricingEnv pricingEnv = new PricingEnv();
		Trade trade = new Trade();
		JDate jdate = JDate.getNow();
		Cash cash = new Cash();
		Repo repo  = new Repo();
		RateIndex rateIndex = new RateIndex("EUR", "RateIndex", new Tenor(Tenor.INTRADAY_LABEL), "Source");
		Frequency frequency = Frequency.F_DAILY;
		DayCount dayCount = DayCount.D_30_365;
		DateRule dateRule = new DateRule();
		Equity equity = new Equity();
		Collateral collateral = new Collateral();
		Vector<Collateral> collaterals = new Vector<Collateral>();
		
		equity.setId(5);
		equity.setName("Bond");
		equity.setCurrency("EUR");
		dateRule.setName("NameDateRule");
		rateIndex.setDayCount(dayCount);
		collateral.setSecurity(equity);
		collaterals.add(collateral);
		
		cash.setPrincipal(1000.0);
		cash.setStartDate(jdate);
		cash.setCurrency("EUR");
		cash.setRateIndex(rateIndex);
		cash.setPaymentFrequency(frequency);
		cash.setFixedRate(2.0);
		cash.setFixedDayCount(dayCount);
		cash.setOpenTermB(true);
		
		repo.setCash(cash);
		repo.setAllocationType("allocationType");
		repo.setCallableBy(SecFinanceCallableBy.BOTH);
		repo.setSubType("BSB");
		repo.setFillType("fillType");
		repo.setRepriceFrequency(dateRule);
		repo.setCollaterals(collaterals);
		
		trade.setProduct(repo);
		
		String paramString2 = "RepoTOGGLE_CLEAN_PRICE_IN_DECIMAL";
		when(mockedCacheImp.getProperty(mockedDS, mockedDS.getUser(), paramString2)).thenReturn("0");
		BOCache.setImpl(mockedCacheImp);
		
		com.calypso.tk.upload.jaxb.Product jaxbProduct = new com.calypso.tk.upload.jaxb.Product();
		assertNull(jaxbProduct.getInterestRateSwap());
		
		CDUFRepoBuilder builder = new CDUFRepoBuilder();
		builder.fillProduct(pricingEnv, trade, jaxbProduct);
		
		com.calypso.tk.upload.jaxb.Repo jaxbRepo = jaxbProduct.getRepo();
		
		assertNotNull(jaxbRepo);
		assertNotNull(jaxbRepo.getAllocationType());
		assertEquals("allocationType", jaxbRepo.getAllocationType());
		assertNotNull(jaxbRepo.getCallableBy());
		assertEquals(SecFinanceCallableBy.BOTH.toString(), jaxbRepo.getCallableBy());
		assertNotNull(jaxbRepo.getDirection());
		assertEquals("Repo", jaxbRepo.getDirection());
		assertNotNull(jaxbRepo.getFillType());
		assertEquals("fillType", jaxbRepo.getFillType());
		assertNotNull(jaxbRepo.getFundingDetails());
		assertEquals("EUR", jaxbRepo.getFundingDetails().getCurrency());
		assertEquals(cash.getRateIndex().getName(), jaxbRepo.getFundingDetails().getRateIndex());
		assertEquals(Tenor.INTRADAY_LABEL, jaxbRepo.getFundingDetails().getTenor());
		assertEquals((Double)1000.0, (Double)jaxbRepo.getFundingDetails().getPrincipal());
		assertNotNull(jaxbRepo.getNoticeDays());
		assertNotNull(jaxbRepo.getRepoDetails());
		assertFalse(jaxbRepo.getRepoDetails().isForceDAPB());
		assertEquals(repo.getInterestDispatchMethod(), jaxbRepo.getRepoDetails().getInterestDispatch());
		assertEquals((Double)0.0, (Double)jaxbRepo.getRepoDetails().getMinimumParAmount());
		assertEquals(repo.getRepriceFrequency().getName(), jaxbRepo.getRepoDetails().getRepriceFrequency());
		assertNotNull(jaxbRepo.getSubstitutionDetails());
		assertEquals(repo.isAutoCollateralAllocation(), jaxbRepo.getSubstitutionDetails().isAutoAllocB());
		assertEquals(repo.allowModifyMoney(), jaxbRepo.getSubstitutionDetails().isModifMoneyB());
		assertEquals(repo.getSubstitutionB(), jaxbRepo.getSubstitutionDetails().isSubstitutableB());
		assertEquals(repo.getSubstitutionLimit(), jaxbRepo.getSubstitutionDetails().getSubstitutionLimit());
		assertNotNull(jaxbRepo.getOpenTerm());
		assertEquals("OPEN", jaxbRepo.getOpenTerm());
		assertNotNull(jaxbRepo.getSecurityDetails());
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.CDUFRepoBuilder#fillProduct(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Trade, com.calypso.tk.upload.jaxb.Product)}.
	 */
	@Test
	public final void testFillProduct3() {
		mock(BOCache.class);
		CacheImpl mockedCacheImp = mock(CacheImpl.class);
		
		DSConnection mockedDS = mock(DSConnection.class);
		RemoteReferenceData mockedRRD = mock(RemoteReferenceData.class);
		when(mockedDS.getRemoteReferenceData()).thenReturn(mockedRRD);
		User user = new User();
		user.setName("UserNameTest");
		when(mockedDS.getUser()).thenReturn(user.getName());
		DSConnection.setDefault(mockedDS);
		
		PricingEnv pricingEnv = new PricingEnv();
		Trade trade = new Trade();
		Repo repo  = new Repo();
		RateIndex rateIndex = new RateIndex("EUR", "RateIndex", new Tenor(Tenor.INTRADAY_LABEL), "Source");
		DayCount dayCount = DayCount.D_30_365;
		DateRule dateRule = new DateRule();
		Equity equity = new Equity();
		Collateral collateral = new Collateral();
		Vector<Collateral> collaterals = new Vector<Collateral>();
	
		equity.setId(5);
		equity.setName("Bond");
		equity.setCurrency("EUR");
		dateRule.setName("NameDateRule");
		rateIndex.setDayCount(dayCount);
		collateral.setSecurity(equity);
		collaterals.add(collateral);
		repo.setAllocationType("allocationType");
		repo.setCallableBy(SecFinanceCallableBy.BOTH);
		repo.setSubType("BSB");
		repo.setFillType("fillType");
		repo.setRepriceFrequency(dateRule);
		repo.setCollaterals(collaterals);
		repo.setContinuous(true);
		trade.setProduct(repo);
		
		String paramString2 = "RepoTOGGLE_CLEAN_PRICE_IN_DECIMAL";
		when(mockedCacheImp.getProperty(mockedDS, mockedDS.getUser(), paramString2)).thenReturn("0");
		BOCache.setImpl(mockedCacheImp);
		
		com.calypso.tk.upload.jaxb.Product jaxbProduct = new com.calypso.tk.upload.jaxb.Product();
		assertNull(jaxbProduct.getInterestRateSwap());
		CDUFRepoBuilder builder = new CDUFRepoBuilder();
		builder.fillProduct(pricingEnv, trade, jaxbProduct);
		com.calypso.tk.upload.jaxb.Repo jaxbRepo = jaxbProduct.getRepo();
		
		assertNotNull(jaxbRepo);
		assertNotNull(jaxbRepo.getAllocationType());
		assertEquals("allocationType", jaxbRepo.getAllocationType());
		assertNotNull(jaxbRepo.getCallableBy());
		assertEquals(SecFinanceCallableBy.BOTH.toString(), jaxbRepo.getCallableBy());
		assertNotNull(jaxbRepo.getDirection());
		assertEquals("Repo", jaxbRepo.getDirection());
		assertNotNull(jaxbRepo.getFillType());
		assertEquals("fillType", jaxbRepo.getFillType());
		assertNotNull(jaxbRepo.getFundingDetails());
		assertNotNull(jaxbRepo.getNoticeDays());
		assertNotNull(jaxbRepo.getRepoDetails());
		assertFalse(jaxbRepo.getRepoDetails().isForceDAPB());
		assertEquals(repo.getInterestDispatchMethod(), jaxbRepo.getRepoDetails().getInterestDispatch());
		assertEquals((Double)0.0, (Double)jaxbRepo.getRepoDetails().getMinimumParAmount());
		assertEquals(repo.getRepriceFrequency().getName(), jaxbRepo.getRepoDetails().getRepriceFrequency());
		assertNotNull(jaxbRepo.getSubstitutionDetails());
		assertEquals(repo.isAutoCollateralAllocation(), jaxbRepo.getSubstitutionDetails().isAutoAllocB());
		assertEquals(repo.allowModifyMoney(), jaxbRepo.getSubstitutionDetails().isModifMoneyB());
		assertEquals(repo.getSubstitutionB(), jaxbRepo.getSubstitutionDetails().isSubstitutableB());
		assertEquals(repo.getSubstitutionLimit(), jaxbRepo.getSubstitutionDetails().getSubstitutionLimit());
		assertNotNull(jaxbRepo.getOpenTerm());
		assertEquals("CONTINUOUS", jaxbRepo.getOpenTerm());
		assertNotNull(jaxbRepo.getSecurityDetails());
	}

}
