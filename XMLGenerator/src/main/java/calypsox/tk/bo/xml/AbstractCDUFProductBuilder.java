package calypsox.tk.bo.xml;

import java.util.List;
import java.util.Vector;

import com.calypso.tk.core.CalypsoServiceException;
import com.calypso.tk.core.DateRoll;
import com.calypso.tk.core.DateRule;
import com.calypso.tk.core.LegalEntity;
import com.calypso.tk.core.Log;
import com.calypso.tk.core.Tenor;
import com.calypso.tk.refdata.RateIndex;
import com.calypso.tk.refdata.SecFinanceCallableBy;
import com.calypso.tk.service.DSConnection;
import com.calypso.tk.upload.jaxb.HolidayCodeType;

/**
 * The Class AbstractCDUFProductXML. Add some methods used for the Product part of the CDUF.
 */
public abstract class AbstractCDUFProductBuilder extends AbstractCDUFTradeBuilder {

	protected String getTenor(final RateIndex rateIndex) {
		if (rateIndex != null) {
			Tenor tenor = rateIndex.getTenor();
			if (tenor != null) {
				return tenor.toString();
			}
		}
		return null;
	}

	/**
	 * @param rateIndex the RateIndex
	 * @return the String with RateIndex name.
	 */
	protected String getRateIndex(final RateIndex rateIndex) {
		if (rateIndex != null) {
			return rateIndex.getName();
		}
		return null;
	}

	/**
	 * @param rateIndex the rate index
	 * @return the Vector<String> with rate index holidays
	 */
	@SuppressWarnings("unchecked")
	protected Vector<String> getRateIndexHolidays(final RateIndex rateIndex) {
		if (rateIndex != null) {
			return rateIndex.getHolidays();
		}
		return null;
	}

	/**
	 * @param rateIndex the RateIndex
	 * @return the String with RateIndex source name.
	 */
	protected String getRateIndexSource(final RateIndex rateIndex) {
		if (rateIndex != null) {
			return rateIndex.getSource();
		}
		return null;
	}


	/**
	 * @param resetOffset the resetOffset value
	 * @param defaultResetOffset the boolean resetoffset
	 * @param rateIndex the rateindex
	 * @return the string with ResetLag value.
	 */
	protected String getResetLag(final int resetOffset, final boolean defaultResetOffset, final RateIndex rateIndex) {
		if(defaultResetOffset){
			if(rateIndex!=null){
				return String.valueOf(rateIndex.getResetDays());
			}
			return null;
		}else{
			return String.valueOf(resetOffset);
		}
	}

	/**
	 * @param rateIndex the RateIndex
	 * @return the String with RateIndex Day Count.
	 */
	protected String getRateIndexDayCount(final RateIndex rateIndex) {
		if (rateIndex != null) {
			return rateIndex.getDayCount().toString();
		}
		return null;
	}

	/**
	 * @param vector the Vector with Holidays
	 * @return the HolidayCodeType with all holidays in the vector.
	 */
	protected HolidayCodeType getHolidayCodeTypeFromVector(final Vector<String> vector) {
		if(vector!=null){
			HolidayCodeType holidayCodeType = new HolidayCodeType();
			List<String> list = holidayCodeType.getHoliday();
			if (!vector.isEmpty()) {
				for (String holiday : vector) {
					list.add(holiday);
				}
				return holidayCodeType;
			}
		}
		return null;
	}

	/**
	 * @param secFinanceCallableBy
	 * @return the String with SecFinanceCallableBy label.
	 */
	protected String getCallableBy(final SecFinanceCallableBy secFinanceCallableBy) {
		if (secFinanceCallableBy != null) {
			return secFinanceCallableBy.getLabel();
		}
		return null;
	}

	/**
	 * @param dateRule the DateRule
	 * @return the String with DateRule name.
	 */
	protected String getDateRule(final DateRule dateRule) {
		if (dateRule != null) {
			return dateRule.getName();
		}
		return null;
	}

	/**
	 * @param leId the LegalEntity id
	 * @return the String with LegalEntity code.
	 */
	protected String getLegalEntity(final int leId) {
		try {
			LegalEntity legalEntity = DSConnection.getDefault().getRemoteReferenceData().getLegalEntity(leId);
			if (legalEntity != null) {
				return legalEntity.getCode();
			}
		} catch (CalypsoServiceException e) {
			Log.error(this, e.getMessage());
			return null;
		}
		return null;
	}

	/**
	 * @param value string value
	 * @return int value
	 */
	protected int parseStringToInteger(final String value){
		if(value!=null){
			try{
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				Log.error(this, e.getMessage());
			}
		}
		return 0;
	}

	/**
	 * @param dateRoll the date roll
	 * @return the String with date roll name
	 */
	protected String getDateRoll(final DateRoll dateRoll) {
		if (dateRoll != null) {
			return dateRoll.toString();
		}
		return null;
	}

	/**
	 * @param compoundMethod the compound method
	 * @return the string with compound method name
	 */
	protected String getCompoundMethod(final com.calypso.tk.product.util.CompoundMethod compoundMethod) {
		if (compoundMethod != null) {
			return compoundMethod.getDisplayName();
		}
		return null;
	}

	/**
	 * @param frequency the frequency
	 * @return the string with frequency name
	 */
	protected String getFrequency(final com.calypso.tk.core.Frequency frequency) {
		if (frequency != null) {
			return frequency.toString();
		}
		return null;
	}

	/**
	 * @param dayCount the day count
	 * @return the string with day count name
	 */
	protected String getDayCount(final com.calypso.tk.core.DayCount dayCount) {
		if (dayCount != null) {
			return dayCount.toString();
		}
		return null;
	}
	
	protected double getDisplayValue(final com.calypso.tk.core.DisplayValue displayValue) {
		if (displayValue != null) {
			return displayValue.get();
		}
		return 0.0D;
	}

}
