package calypsox.tk.bo.xml;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.calypso.tk.core.Tenor;
import com.calypso.tk.refdata.RateIndex;

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
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getResetLag(int, boolean, com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetResetLag() {
		fail("Not yet implemented");		
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getRateIndexDayCount(com.calypso.tk.refdata.RateIndex)}.
	 */
	@Test
	public void testGetRateIndexDayCount() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getHolidayCodeTypeFromVector(java.util.Vector)}.
	 */
	@Test
	public void testGetHolidayCodeTypeFromVector() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getCallableBy(com.calypso.tk.refdata.SecFinanceCallableBy)}.
	 */
	@Test
	public void testGetCallableBy() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getDateRule(com.calypso.tk.core.DateRule)}.
	 */
	@Test
	public void testGetDateRule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getLegalEntity(int)}.
	 */
	@Test
	public void testGetLegalEntity() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#parseStringToInteger(java.lang.String)}.
	 */
	@Test
	public void testParseStringToInteger() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getDateRoll(com.calypso.tk.core.DateRoll)}.
	 */
	@Test
	public void testGetDateRoll() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getCompoundMethod(com.calypso.tk.product.util.CompoundMethod)}.
	 */
	@Test
	public void testGetCompoundMethod() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getFrequency(com.calypso.tk.core.Frequency)}.
	 */
	@Test
	public void testGetFrequency() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getDayCount(com.calypso.tk.core.DayCount)}.
	 */
	@Test
	public void testGetDayCount() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.AbstractCDUFProductBuilder#getDisplayValue(com.calypso.tk.core.DisplayValue)}.
	 */
	@Test
	public void testGetDisplayValue() {
		fail("Not yet implemented");
	}

}
