package calypsox.tk.bo.xml;

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

import com.calypso.tk.core.CashFlow;
import com.calypso.tk.core.CashFlowSet;
import com.calypso.tk.core.FlowGenerationException;
import com.calypso.tk.core.JDate;
import com.calypso.tk.core.Log;
import com.calypso.tk.core.Trade;
import com.calypso.tk.core.TradeBundle;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.upload.jaxb.CalypsoTrade;
import com.calypso.tk.upload.jaxb.HolidayCode;
import com.calypso.tk.upload.jaxb.Keyword;

public abstract class AbstractCDUFTradeBuilder implements CDUFTradeBuilder {

    /**
     * Variable for twentyFourHourTimeToMilliseconds method
     */
    private static final int HUNDRED = 100;
    /**
     * Variable for twentyFourHourTimeToMilliseconds method
     */
    private static final int SIXTY = 60;
    /**
     * Variable for twentyFourHourTimeToMilliseconds method
     */
    private static final int SIXTY_THOUSAND = 60000;
	
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
		// calypsoTrade.setStartDate(trade.get); 
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
		calypsoTrade.setProductSubType(trade.getProductSubType()); // required 
		calypsoTrade.setTradeId(Integer.valueOf(trade.getId())); // required 
		calypsoTrade.setTradeKeywords(getTradeKeywords(trade));
		// TODO: calypsoTrade.setTemplateName(); 
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
	}

	/**
	 * @param legalEntity the legal entity
	 * @return the String with legal entity country name.
	 */
	String getCounterPartyCountry(final com.calypso.tk.core.LegalEntity legalEntity) {
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
	JDate getTradeDateJDate(final com.calypso.tk.core.JDatetime jDateTime){
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
	com.calypso.tk.upload.jaxb.TradeKeywords getTradeKeywords(final Trade trade) {
		com.calypso.tk.upload.jaxb.TradeKeywords keywords = new com.calypso.tk.upload.jaxb.TradeKeywords();
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
	 * Get de cash flows of product
	 * 
	 * @param pricingEnv the pricing enviroment
	 * @param product the product
	 * @return the CashFlows of product
	 */
	com.calypso.tk.upload.jaxb.CashFlows getCashflows(final PricingEnv pricingEnv, final com.calypso.tk.core.Product product) {
		com.calypso.tk.upload.jaxb.CashFlows cashflows = new com.calypso.tk.upload.jaxb.CashFlows();
		List<com.calypso.tk.upload.jaxb.Cashflow> cfList = cashflows.getCashFlow();

		CashFlowSet cfSet = null;
		if(product != null){
			if (product.getCustomFlowsB()) {
				cfSet = product.getFlows();
			} else {
				try {
					if(pricingEnv != null && JDate.getNow() != null){
						cfSet = product.generateFlows(JDate.getNow());
						product.calculateAll(cfSet, pricingEnv, JDate.getNow());
					}
				} catch (FlowGenerationException e) {
					Log.error(this, e.getMessage(), e);
				}
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
				jaxbCashflow.setFlowType(cashflow.getType());  //727
                //cashflow.getFXRate();  726
                //cashflow.getFXResetDate();  725
				// cashflow.getId(); 720
				//cashflow.getNotionalCurrency(); 722
				cfList.add(jaxbCashflow);
			}
		}
		return cashflows;
	}

	/**
	 * Get the direction of trade
	 * 
	 * @param quantity the quantity
	 * @return buy if quantity is higher and sell if is smaller.
	 */
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
	String getBook(final com.calypso.tk.core.Book book) {
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
	String getCounterParty(final com.calypso.tk.core.LegalEntity legalEntity) {
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
				for (int i=0; i<holidays.size(); i++) {
					String holiday = holidays.get(i);
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
		return (((time / HUNDRED) * SIXTY) + (time % HUNDRED)) * SIXTY_THOUSAND;
	}

}