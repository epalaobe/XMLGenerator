package calypsox.tk.bo.xml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.calypso.tk.core.CalypsoServiceException;
import com.calypso.tk.core.DateRoll;
import com.calypso.tk.core.DateRule;
import com.calypso.tk.core.DayCount;
import com.calypso.tk.core.DisplayValue;
import com.calypso.tk.core.Frequency;
import com.calypso.tk.core.LegalEntity;
import com.calypso.tk.core.Tenor;
import com.calypso.tk.product.util.CompoundMethod;
import com.calypso.tk.refdata.RateIndex;
import com.calypso.tk.refdata.RateIndexDefaults;
import com.calypso.tk.refdata.SecFinanceCallableBy;
import com.calypso.tk.service.DSConnection;
import com.calypso.tk.service.RemoteReferenceData;
import com.calypso.tk.upload.jaxb.HolidayCodeType;

/**
 * Test Class for AbstractCDUFProductBuilder.
 */
public class AbstractCDUFProductBuilderTest {

	AbstractCDUFProductBuilder builder;

	/**
	 * @throws java.lang.Exception
	 */ 
	@Before
	public void setUp() throws Exception {
		this.builder = mock(AbstractCDUFProductBuilder.class);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getTenor(com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetTenor() {
		RateIndex rateIndex = new RateIndex();
		rateIndex.setTenor(new Tenor(Tenor.INTRADAY_LABEL));
		
		doCallRealMethod().when(this.builder).getTenor(rateIndex);
		String tenorName = this.builder.getTenor(rateIndex);

		assertNotNull(tenorName);
		assertTrue(tenorName.equals(Tenor.INTRADAY_LABEL));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getTenor(com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetTenorNull() {
		doCallRealMethod().when(this.builder).getTenor(null);
		String tenorName = this.builder.getTenor(null);

		assertNull(tenorName);
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getTenor(com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetTenorNull2() {
		RateIndex rateIndex = new RateIndex();
		rateIndex.setTenor(null);
		
		doCallRealMethod().when(this.builder).getTenor(rateIndex);
		String tenorName = this.builder.getTenor(rateIndex);

		assertNull(tenorName);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getRateIndex(com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetRateIndex() {
		RateIndex rateIndex = new RateIndex();
		rateIndex.setName("RateIndexName");
		doCallRealMethod().when(this.builder).getRateIndex(rateIndex);
		String rateIndexName = this.builder.getRateIndex(rateIndex);
		assertNotNull(rateIndexName);
		assertTrue(rateIndexName.equals("RateIndexName"));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getRateIndex(com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetRateIndexNull() {
		doCallRealMethod().when(this.builder).getRateIndex(null);
		String rateIndexName = this.builder.getRateIndex(null);
		assertNull(rateIndexName);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getRateIndexHolidays(com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetRateIndexHolidays() {
		RateIndex rateIndex = new RateIndex();
		Vector<String> vacioVector = new Vector<String>();
		doCallRealMethod().when(this.builder).getRateIndexHolidays(rateIndex);
		Vector<String> holidays = this.builder.getRateIndexHolidays(rateIndex);
		assertNotNull(holidays);
		assertTrue(holidays.equals(vacioVector));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getRateIndexHolidays(com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetRateIndexHolidaysNull() {
		doCallRealMethod().when(this.builder).getRateIndexHolidays(null);
		Vector<String> holidays = this.builder.getRateIndexHolidays(null);
		assertNull(holidays);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getRateIndexSource(com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetRateIndexSource() {
		RateIndex rateIndex = new RateIndex();
		rateIndex.setSource("RateIndexSource");
		doCallRealMethod().when(this.builder).getRateIndexSource(rateIndex);
		String sourceName = this.builder.getRateIndexSource(rateIndex);
		assertNotNull(sourceName);
		assertTrue(sourceName.equals("RateIndexSource"));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getRateIndexSource(com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetRateIndexSourceNull() {
		doCallRealMethod().when(this.builder).getRateIndexSource(null);
		String sourceName = this.builder.getRateIndexSource(null);
		assertNull(sourceName);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getResetLag(int, boolean, com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetResetLag() {
		RateIndex rateIndex = new RateIndex();
		RateIndexDefaults rateIndexDefaults = new RateIndexDefaults();
		rateIndexDefaults.setResetDays(2);
		rateIndex.setDefaults(rateIndexDefaults);
		doCallRealMethod().when(this.builder).getResetLag(1, true, rateIndex);
		String resetLagValue = this.builder.getResetLag(1, true, rateIndex);
		assertNotNull(resetLagValue);
		assertTrue(resetLagValue.equals("2"));
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getResetLag(int, boolean, com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetResetLagDefault() {
		RateIndex rateIndex = new RateIndex();
		doCallRealMethod().when(this.builder).getResetLag(1, false, rateIndex);
		String resetLagValue = this.builder.getResetLag(1, false, rateIndex);
		assertNotNull(resetLagValue);
		assertTrue(resetLagValue.equals("1"));		
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getResetLag(int, boolean, com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetResetLagNull() {
		doCallRealMethod().when(this.builder).getResetLag(1, true, null);
		String resetLagValue = this.builder.getResetLag(1, true, null);
		assertNull(resetLagValue);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getRateIndexDayCount(com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetRateIndexDayCount() {
		RateIndex rateIndex = new RateIndex();
		DayCount dayCount = DayCount.D_30_365;
		rateIndex.setDayCount(dayCount);
		doCallRealMethod().when(this.builder).getRateIndexDayCount(rateIndex);
		String rateIndexName = this.builder.getRateIndexDayCount(rateIndex);
		assertNotNull(rateIndexName);
		assertTrue(rateIndexName.equals(DayCount.D_30_365.toString()));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getRateIndexDayCount(com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetRateIndexDayCountNull() {
		doCallRealMethod().when(this.builder).getRateIndexDayCount(null);
		String rateIndexName = this.builder.getRateIndexDayCount(null);
		assertNull(rateIndexName);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getHolidayCodeTypeFromVector(java.util.Vector)}.
	 */
	@Test
	public void testGetHolidayCodeTypeFromVector() {
		HolidayCodeType holidayCodeType = new HolidayCodeType();
		List<String> list = holidayCodeType.getHoliday();
		Vector<String> vector = new Vector<String>();
		vector.add("HolydayC1");
		vector.add("HolydayC2");
		list.add("HolydayC1");
		list.add("HolydayC2");
		doCallRealMethod().when(this.builder).getHolidayCodeTypeFromVector(vector);
		HolidayCodeType HolydayList = this.builder.getHolidayCodeTypeFromVector(vector);
		assertNotNull(HolydayList);
		assertTrue(HolydayList.getHoliday().equals(list));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getHolidayCodeTypeFromVector(java.util.Vector)}.
	 */
	@Test
	public void testGetHolidayCodeTypeFromVectorEmpty() {
		Vector<String> vector = new Vector<String>();
		doCallRealMethod().when(this.builder).getHolidayCodeTypeFromVector(vector);
		HolidayCodeType HolydayList = this.builder.getHolidayCodeTypeFromVector(vector);
		assertNull(HolydayList);
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getHolidayCodeTypeFromVector(java.util.Vector)}.
	 */
	@Test
	public void testGetHolidayCodeTypeFromVectorNull() {
		doCallRealMethod().when(this.builder).getHolidayCodeTypeFromVector(null);
		HolidayCodeType HolydayList = this.builder.getHolidayCodeTypeFromVector(null);
		assertNull(HolydayList);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getCallableBy(com.calypso.tk.refdata.SecFinanceCallableBy)}.
	 */
	@Test
	public void testGetCallableBy() {
		SecFinanceCallableBy secFinanceCallebleBy = SecFinanceCallableBy.LENDER;
		doCallRealMethod().when(this.builder).getCallableBy(secFinanceCallebleBy);
		String label = this.builder.getCallableBy(secFinanceCallebleBy);
		assertNotNull(label);
		assertTrue(label.equals(SecFinanceCallableBy.LENDER.getLabel()));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getCallableBy(com.calypso.tk.refdata.SecFinanceCallableBy)}.
	 */
	@Test
	public void testGetCallableByNull() {
		doCallRealMethod().when(this.builder).getCallableBy(null);
		String label = this.builder.getCallableBy(null);
		assertNull(label);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getDateRule(com.calypso.tk.core.DateRule)}.
	 */
	@Test
	public void testGetDateRule() {
		DateRule dateRule = new DateRule();
		dateRule.setName("DateRuleName");
		doCallRealMethod().when(this.builder).getDateRule(dateRule);
		String dateRuleName = this.builder.getDateRule(dateRule);
		assertNotNull(dateRuleName);
		assertTrue(dateRuleName.equals("DateRuleName"));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getDateRule(com.calypso.tk.core.DateRule)}.
	 */
	@Test
	public void testGetDateRuleNull() {
		doCallRealMethod().when(this.builder).getDateRule(null);
		String dateRuleName = this.builder.getDateRule(null);
		assertNull(dateRuleName);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getLegalEntity(int)}.
	 * @throws CalypsoServiceException 
	 */
	@Test
	public void testGetLegalEntity() throws CalypsoServiceException {
		DSConnection mockedDS = mock(DSConnection.class);
		RemoteReferenceData mockedRRD = mock(RemoteReferenceData.class);
		LegalEntity LE = new LegalEntity();
		LE.setId(1); 
		LE.setCode("LECode");

		when(mockedDS.getRemoteReferenceData()).thenReturn(mockedRRD);
		
		when(mockedRRD.getLegalEntity(1)).thenReturn(LE); 
		
		DSConnection.setDefault(mockedDS);

		doCallRealMethod().when(this.builder).getLegalEntity(1);
		String LECodeName = this.builder.getLegalEntity(1);

		assertNotNull(LECodeName);
		assertTrue(LECodeName.equals("LECode"));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getLegalEntity(int)}. 
	 * @throws CalypsoServiceException 
	 */
	@Test
	public void testGetLegalEntityException() throws CalypsoServiceException{
		CalypsoServiceException e = new CalypsoServiceException("CalypsoServiceException");
		DSConnection mockedDS = mock(DSConnection.class);
		RemoteReferenceData mockedRRD = mock(RemoteReferenceData.class);
		when(mockedDS.getRemoteReferenceData()).thenReturn(mockedRRD);
		DSConnection.setDefault(mockedDS);
		
		when(mockedDS.getRemoteReferenceData().getLegalEntity(1)).thenThrow(e);
		doCallRealMethod().when(this.builder).getLegalEntity(1);
		String LECodeName = this.builder.getLegalEntity(1);
		assertNull(LECodeName);
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getLegalEntity(int)}.
	 * @throws CalypsoServiceException 
	 */
	@Test
	public void testGetLegalEntityNull() throws CalypsoServiceException {
		DSConnection mockedDS = mock(DSConnection.class);
		RemoteReferenceData mockedRRD = mock(RemoteReferenceData.class);
		when(mockedDS.getRemoteReferenceData()).thenReturn(mockedRRD);
		DSConnection.setDefault(mockedDS);

		doCallRealMethod().when(this.builder).getLegalEntity(1);
		String LECodeName = this.builder.getLegalEntity(1);

		assertNull(LECodeName);
	}
	

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#parseStringToInteger(java.lang.String)}.
	 */
	@Test
	public void testParseStringToInteger() {
		String str = "1";
		doCallRealMethod().when(this.builder).parseStringToInteger(str);
		int parseStringToIntegerInt = this.builder.parseStringToInteger(str);
		assertNotNull(parseStringToIntegerInt);
		assertEquals(1, parseStringToIntegerInt);
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#parseStringToInteger(java.lang.String)}.
	 */
	@Test
	public void testParseStringToIntegerException() {
		String str = "aaa";
		doCallRealMethod().when(this.builder).parseStringToInteger(str);
		int result = this.builder.parseStringToInteger(str);
		assertEquals(0, result);
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#parseStringToInteger(java.lang.String)}.
	 */
	@Test
	public void testParseStringToIntegerNull() {
		doCallRealMethod().when(this.builder).parseStringToInteger(null);
		int result = this.builder.parseStringToInteger(null);
		assertEquals(0, result);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getDateRoll(com.calypso.tk.core.DateRoll)}.
	 */
	@Test
	public void testGetDateRoll() {
		DateRoll dateRoll = DateRoll.R_END_MONTH;
		doCallRealMethod().when(this.builder).getDateRoll(dateRoll);
		String dateRollName = this.builder.getDateRoll(dateRoll);
		assertNotNull(dateRollName);
		assertTrue(dateRollName.equals(DateRoll.R_END_MONTH.toString()));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getDateRoll(com.calypso.tk.core.DateRoll)}.
	 */
	@Test
	public void testGetDateRollNull() {
		doCallRealMethod().when(this.builder).getDateRoll(null);
		String dateRollName = this.builder.getDateRoll(null);
		assertNull(dateRollName);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getCompoundMethod(com.calypso.tk.product.util.CompoundMethod)}.
	 */
	@Test
	public void testGetCompoundMethod() {
		CompoundMethod compoundMethod = CompoundMethod.FLAT;
		doCallRealMethod().when(this.builder).getCompoundMethod(compoundMethod);
		String compoundMethodName = this.builder.getCompoundMethod(compoundMethod);
		assertNotNull(compoundMethodName);
		assertTrue(compoundMethodName.equals("Flat"));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getCompoundMethod(com.calypso.tk.product.util.CompoundMethod)}.
	 */
	@Test
	public void testGetCompoundMethodNull() {
		doCallRealMethod().when(this.builder).getCompoundMethod(null);
		String compoundMethodName = this.builder.getCompoundMethod(null);
		assertNull(compoundMethodName);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getFrequency(com.calypso.tk.core.Frequency)}.
	 */
	@Test
	public void testGetFrequency() {
		Frequency frequency = Frequency.F_DAILY;
		doCallRealMethod().when(this.builder).getFrequency(frequency);
		String frequencyName = this.builder.getFrequency(frequency);
		assertNotNull(frequencyName);
		assertTrue(frequencyName.equals(Frequency.F_DAILY.toString()));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getFrequency(com.calypso.tk.core.Frequency)}.
	 */
	@Test
	public void testGetFrequencyNull() {
		doCallRealMethod().when(this.builder).getFrequency(null);
		String frequencyName = this.builder.getFrequency(null);
		assertNull(frequencyName);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getDayCount(com.calypso.tk.core.DayCount)}.
	 */
	@Test
	public void testGetDayCount() {
		DayCount dayCount = DayCount.D_30_365;
		doCallRealMethod().when(this.builder).getDayCount(dayCount);
		String dayCountName = this.builder.getDayCount(dayCount);
		assertNotNull(dayCountName);
		assertTrue(dayCountName.equals(DayCount.D_30_365.toString()));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getDayCount(com.calypso.tk.core.DayCount)}.
	 */
	@Test
	public void testGetDayCountNull() {
		doCallRealMethod().when(this.builder).getDayCount(null);
		String dayCountName = this.builder.getDayCount(null);
		assertNull(dayCountName);
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getDisplayValue(com.calypso.tk.core.DisplayValue)}.
	 */
	@Test
	public void testGetDisplayValue() {
		DisplayValue mockedDisplayValue = mock(DisplayValue.class);
		when(mockedDisplayValue.get()).thenReturn(2.1);
		doCallRealMethod().when(this.builder).getDisplayValue(mockedDisplayValue);
		Double dpd = this.builder.getDisplayValue(mockedDisplayValue);
		assertNotNull(dpd);
		assertEquals((Double) 2.1, dpd);
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getDisplayValue(com.calypso.tk.core.DisplayValue)}.
	 */
	@Test
	public void testGetDisplayValueNull() {
		doCallRealMethod().when(this.builder).getDisplayValue(null);
		Double dpd = this.builder.getDisplayValue(null);
		assertNull(dpd);
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getTenorName(com.calypso.tk.core.Tenor)}.
	 */
	@Test
	public void testGetTenorName() {
		Tenor tenor = new Tenor(Tenor.INTRADAY_LABEL);
		
		doCallRealMethod().when(this.builder).getTenorName(tenor);
		String tenorName = this.builder.getTenorName(tenor);
		String tenorNameNull = this.builder.getTenorName(null);
		
		assertNotNull(tenorName);
		assertNull(tenorNameNull);
		assertTrue(tenorName.equals(Tenor.INTRADAY_LABEL));
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getTenorName(com.calypso.tk.core.Tenor)}.
	 */
	@Test
	public void testGetTenorNameNull() {
		doCallRealMethod().when(this.builder).getTenorName(null);
		String tenorName = this.builder.getTenorName(null);
		assertNull(tenorName);
	}

}
