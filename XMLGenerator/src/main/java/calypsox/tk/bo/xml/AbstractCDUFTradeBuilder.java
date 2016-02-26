package calypsox.tk.bo.xml;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.calypso.jaxb.xml.Audit;
import com.calypso.tk.core.Book;
import com.calypso.tk.core.CashFlow;
import com.calypso.tk.core.CashFlowSet;
import com.calypso.tk.core.FlowGenerationException;
import com.calypso.tk.core.JDate;
import com.calypso.tk.core.JDatetime;
import com.calypso.tk.core.LegalEntity;
import com.calypso.tk.core.Log;
import com.calypso.tk.core.Trade;
import com.calypso.tk.core.TradeBundle;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.product.reconvention.Reconvention;
import com.calypso.tk.product.reconvention.ReconventionParameter;
import com.calypso.tk.product.reconvention.impl.ReconventionUtil;
import com.calypso.tk.upload.jaxb.CalypsoTrade;
import com.calypso.tk.upload.jaxb.CashFlows;
import com.calypso.tk.upload.jaxb.HolidayCode;
import com.calypso.tk.upload.jaxb.Keyword;
import com.calypso.tk.upload.jaxb.Parameter;
import com.calypso.tk.upload.jaxb.Parameters;
import com.calypso.tk.upload.jaxb.TradeKeywords;

public abstract class AbstractCDUFTradeBuilder implements CDUFTradeBuilder {

	/**
	 * Fill some data header that are general to all trades.
	 *
	 * @param trade the trade
	 * @param calypsoTrade the method modifies the calypsoTrade to add the trade data
	 */
	@Override
	public void fillTradeHeader(final PricingEnv pricingEnv, final Trade trade, final CalypsoTrade calypsoTrade) {
		calypsoTrade.setAction(getAction(trade.getAction())); // required
		calypsoTrade.setBuySell(getBuySell(trade.getQuantity())); // required
		calypsoTrade.setComment(trade.getComment()); // required
		calypsoTrade.setCounterPartyRole(trade.getRole()); // required
		calypsoTrade.setExternalReference(trade.getExternalReference()); // required
		calypsoTrade.setInternalReference(trade.getInternalReference()); // required
		// The cash settle info is a particular property of some products like swaps, then it should not be here but in the irs
		// generator. It is possible to call the super.fillTradeHeader() and then fill the gaps for the particular product.
		// calypsoTrade.setCashSettlementInfo()
		// Trade Notional and StartDate information is from product data, then now it is in the FRA and IRS fillTradeHeader
		// method.
		// calypsoTrade.setTradeNotional();
		// calypsoTrade.setStartDate(trade.get); //required
		calypsoTrade.setSalesPerson(trade.getSalesPerson()); // required
		calypsoTrade.setTradeBook(getBook(trade.getBook())); // required
		calypsoTrade.setTradeCounterParty(getCounterParty(trade.getCounterParty())); // required
		calypsoTrade.setTradeCurrency(trade.getTradeCurrency()); // required
		calypsoTrade.setTradeDateTime(getXmlGregorianCalendarFromDate(getTradeDateJDate(trade.getTradeDate()))); // required
		calypsoTrade.setTradeSettleDate(getXmlGregorianCalendarFromDate(trade.getSettleDate())); // required
		calypsoTrade.setTraderName(trade.getTraderName()); // required
		calypsoTrade.setHolidayCode(getHolidayCode(trade.getProduct())); // required
		calypsoTrade.setTradeBundleName(getTradeBundle(trade.getBundle())); // required
		calypsoTrade.setTradeBundleType(getTradeBundleType(trade.getBundle())); // required
		calypsoTrade.setTradeBundleOneMsg(getTradeBundleOneMessage(trade.getBundle()));
		calypsoTrade.setMarketType(trade.getMarketType());
		calypsoTrade.setMaturityDate(getXmlGregorianCalendarFromDate(trade.getMaturityDate()));
		calypsoTrade.setMirrorBook(getBook(trade.getMirrorBook()));
		calypsoTrade.setNegotiatedCurrency(trade.getNegotiatedCurrency());
		calypsoTrade.setMarketPlace(getCounterPartyCountry(trade.getCounterParty()));

		calypsoTrade.setProductSubType(trade.getProductSubType()); // required //nillable
		calypsoTrade.setTradeId(Integer.valueOf(trade.getId())); // required //nillable
		calypsoTrade.setTradeKeywords(getTradeKeywords(trade));
		
		calypsoTrade.setReconventionList(getReconventionList(trade.getProduct()));
		calypsoTrade.setCashFlows(getCashflows(pricingEnv, trade.getProduct()));
		
		// TODO: calypsoTrade.setTemplateName(); //required
		// TODO: calypsoTrade.setAllegeActionB();
		// TODO: calypsoTrade.setCancelAction();
		// TODO: calypsoTrade.setNovation();
		// TODO: calypsoTrade.setReprice();
		// TODO: calypsoTrade.setReRate();
		// TODO: calypsoTrade.setRollDetails();
		// TODO: calypsoTrade.setTermination();
		// TODO: calypsoTrade.setExercise();
		// TODO: calypsoTrade.setFeeReRate();
		// TODO: calypsoTrade.setInterestCleanup();
		// TODO: calypsoTrade.setTradeEventsInSameBundle();
		
		// Trade Status and Audit
		com.calypso.jaxb.xml.Trade trade2 = new com.calypso.jaxb.xml.Trade();
		trade.setStatus(trade.getStatus());
		trade2.setAuditInfo(getAudit(trade.getEnteredUser(), trade.getEnteredDate()));
	}


	/**
	 * @param user the EnteredBy user
	 * @param dateEntered the jdatetime
	 * @return the Audit jaxb object
	 */
	Audit getAudit(final String user, final JDatetime dateEntered){
		Audit result = new Audit();
		result.setEnteredBy(user);
		result.setDateEntered(parseJDatetimeToCalender(dateEntered));
		return result;
	}

	/**
	 * @param datetime the jdatetime
	 * @return the Calendar object with jdatetime data.
	 */
	Calendar parseJDatetimeToCalender(final JDatetime datetime){
		Calendar result = null;
		if (datetime != null) {
			result = Calendar.getInstance();
			result.setTime(datetime);
		}
		return result;
	}

	/**
	 * @param legalEntity the legal entity
	 * @return the String with legal entity country name.
	 */
	String getCounterPartyCountry(final LegalEntity legalEntity) {
		if (legalEntity != null) {
			return legalEntity.getCountry();
		}
		return null;
	}

	/**
	 * @param action the action object
	 * @return the String with action name
	 */
	String getAction(final com.calypso.tk.core.Action action) {
		if (action != null) {
			return action.toString();
		}
		return null;
	}

	/**
	 * Get JDate from JDateTime
	 * 
	 * @param jDateTime the JDateTime
	 * @return the JDate Object
	 */
	JDate getTradeDateJDate(final JDatetime jDateTime){
		if(jDateTime!=null){
			return jDateTime.getJDate(null);
		}
		return null;
	}

	/**
	 * Get all keywords values in TradeKeywords Object.
	 *
	 * @param trade the trade
	 * @return the TradeKeywords object with all keywords values.
	 */
	TradeKeywords getTradeKeywords(final Trade trade) {
		TradeKeywords keywords = new TradeKeywords();
		List<Keyword> kwList = keywords.getKeyword();

		@SuppressWarnings("unchecked")
		Map<String, String> tradeKeywords = trade.getKeywords();
		if (tradeKeywords != null) {
			Set<Entry<String, String>> kwSet = tradeKeywords.entrySet();
			for (Entry<String, String> entry : kwSet) {
				Keyword keyword = new Keyword();
				keyword.setKeywordName(entry.getKey());
				keyword.setKeywordValue(entry.getValue());
				kwList.add(keyword);
			}
		}
		return keywords;
	}

	/**
	 * Fill a ReconventionList with data product.
	 *
	 * @param product the product
	 * @return the ReconventionList of product
	 */
	com.calypso.tk.upload.jaxb.ReconventionList getReconventionList(final com.calypso.tk.core.Product product) {
		com.calypso.tk.upload.jaxb.ReconventionList reconventionList = new com.calypso.tk.upload.jaxb.ReconventionList();
		List<com.calypso.tk.upload.jaxb.ReconventionDetails> listReconventionDetails = reconventionList.getReconventionDetails();
		List<Reconvention> reconventions = ReconventionUtil.getReconventions(product);

		for (Reconvention reconvention : reconventions) {
			com.calypso.tk.upload.jaxb.ReconventionDetails reconventionDetails = new com.calypso.tk.upload.jaxb.ReconventionDetails();
			reconventionDetails.setEffectiveDate(getXmlGregorianCalendarFromDate(reconvention.getEffectiveDate()));
			reconventionDetails.setParameters(getReconventionParameters(reconvention));
			reconventionDetails.setPreScheduledB(reconvention.getIsPrescheduled());
			// TODO: reconventionDetails.setPrincipalStructure();
			reconventionDetails.setReconventionDatetime(getXmlGregorianCalendarFromDate(reconvention.getReconventionDatetime().getJDate(null)));
			reconventionDetails.setType(getReconventionType(reconvention.getReconventionType()));
			listReconventionDetails.add(reconventionDetails);
		}
		return null;
	}

	/**
	 * Fill Parameters Object with reconvention data.
	 *
	 * @param reconvention the reconvention
	 * @return the Parameters Object with reconvention data.
	 */
	Parameters getReconventionParameters(final Reconvention reconvention) {
		Parameters parameters = new Parameters();
		List<Parameter> listParameters = parameters.getParameter();
		List<ReconventionParameter<?>> reconventionParameters = reconvention.getReconventionParameters();
		for (ReconventionParameter<?> reconventionParameter : reconventionParameters) {
			Parameter parameter = new Parameter();
			parameter.setParameterName(reconventionParameter.getName());
			parameter.setParameterValue(String.valueOf(reconventionParameter.getValue()));
			listParameters.add(parameter);
		}
		return parameters;
	}

	CashFlows getCashflows(final PricingEnv pricingEnv, final com.calypso.tk.core.Product product) {
		CashFlows cashflows = new CashFlows();
		List<com.calypso.tk.upload.jaxb.Cashflow> cfList = cashflows.getCashFlow();

		CashFlowSet cfSet = null;
		if (product.getCustomFlowsB()) {
			cfSet = product.getFlows();
		} else {
			try {
				cfSet = product.generateFlows(JDate.getNow());
				product.calculateAll(cfSet, pricingEnv, JDate.getNow());
			} catch (FlowGenerationException e) {
				Log.error(this, e.getMessage(), e);
			}
		}
		if (cfSet != null) {
			Iterator<CashFlow> iterator = cfSet.iterator();
			while (iterator.hasNext()) {
				CashFlow cashflow = iterator.next();
				com.calypso.tk.upload.jaxb.Cashflow jaxbCashflow = new com.calypso.tk.upload.jaxb.Cashflow();
				jaxbCashflow.setAmount(cashflow.getAmount());
				jaxbCashflow.setDiscountFactor(cashflow.getDf());
				jaxbCashflow.setDate(getXmlGregorianCalendarFromDate(cashflow.getDate()));
				jaxbCashflow.setEndDate(getXmlGregorianCalendarFromDate(cashflow.getEndDate()));
				jaxbCashflow.setStartDate(getXmlGregorianCalendarFromDate(cashflow.getStartDate()));
				jaxbCashflow.setRoundingMethod(getRoundingMethod(cashflow.getRoundingMethod()));
				cfList.add(jaxbCashflow);
			}
		}
		return cashflows;
	}

	String getBuySell(final double quantity) {
		if (quantity > 0) {
			return "BUY";
		} else {
			return "SELL";
		}
	}

	/**
	 * @param book the book
	 * @return the String with Book name.
	 */
	String getBook(final Book book) {
		if (book != null) {
			return book.getName();
		}
		return null;
	}

	/**
	 * @param tradeBundle the TradeBundle
	 * @return the String with TradeBundle name.
	 */
	String getTradeBundle(final TradeBundle tradeBundle) {
		if (tradeBundle != null) {
			return tradeBundle.getName();
		}
		return null;
	}

	/**
	 * @param tradeBundle the TradeBundle
	 * @return the String with TradeBundle type.
	 */
	String getTradeBundleType(final TradeBundle tradeBundle) {
		if (tradeBundle != null) {
			return tradeBundle.getType();
		}
		return null;
	}

	/**
	 * @param tradeBundle the TradeBundle
	 * @return the boolean value of TradeBundle OneMessage.
	 */
	boolean getTradeBundleOneMessage(final TradeBundle tradeBundle) {
		if (tradeBundle != null) {
			return tradeBundle.getOneMessage();
		}
		return false;
	}

	/**
	 * @param legalEntity the LegalEntity
	 * @return the String with LegalEntity code
	 */
	String getCounterParty(final LegalEntity legalEntity) {
		if (legalEntity != null) {
			return legalEntity.getCode();
		}
		return null;
	}

	/**
	 * @param reoundingMethod the rounding method
	 * @return the String with rounding method name.
	 */
	String getRoundingMethod(final com.calypso.tk.core.RoundingMethod reoundingMethod) {
		if (reoundingMethod != null) {
			return reoundingMethod.toString();
		}
		return null;
	}

	/**
	 * @param reconventionType the reconvention type
	 * @return the string with reconvention type name
	 */
	String getReconventionType(final com.calypso.tk.product.reconvention.ReconventionType reconventionType) {
		if (reconventionType != null) {
			return reconventionType.toString();
		}
		return null;
	}

	/**
	 * @param product the product
	 * @return the HolidayCode with holiday data.
	 */
	HolidayCode getHolidayCode(final com.calypso.tk.core.Product product) {
		if(product.getRateIndex()!=null){
			@SuppressWarnings("unchecked")
			Vector<String> holidays = product.getHolidays();
			HolidayCode holidayCode = new HolidayCode();
			List<String> list = holidayCode.getHoliday();
			if (!holidays.isEmpty()) {
				for (String holiday : holidays) {
					list.add(holiday);
				}
				return holidayCode;
			}
		}
		return null;
	}

	/**
	 * Parse JDate to XMLGregorianCalendar
	 *
	 * @param jdate the JDate object
	 * @return the XMLGregorianCalendar object with JDate data.
	 */
	XMLGregorianCalendar getXmlGregorianCalendarFromDate(final JDate jdate) {
		if (jdate != null) {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.set(jdate.getYear(), jdate.getMonth() - 1, jdate.getDayOfMonth());
			try {
				return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
			} catch (DatatypeConfigurationException e) {
				Log.error(this, e.getMessage());
				return null;
			}
		}
		return null;
	}

	/**
	 * @param time the time in millis
	 * @return the XMLGregorianCalendar object with time data.
	 */
	XMLGregorianCalendar getXmlGregorianCalendarFromTime(final int time) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(time);
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		} catch (DatatypeConfigurationException e) {
			Log.error(this, e.getMessage());
			return null;
		}
	}

	/**
	 * @param time the Time in format int.
	 * @return the time in millis
	 */
	int twentyFourHourTimeToMilliseconds(final int time) {
		return (((time / 100) * 60) + (time % 100)) * 60000;
	}

}