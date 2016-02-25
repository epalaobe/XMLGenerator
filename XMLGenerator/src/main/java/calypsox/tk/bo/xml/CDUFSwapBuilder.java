package calypsox.tk.bo.xml;

import java.util.List;
import java.util.Vector;

import com.calypso.tk.core.Trade;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.product.CashSettleInfo;
import com.calypso.tk.product.Swap;
import com.calypso.tk.product.util.quotableReset.FXResetPurpose;
import com.calypso.tk.upload.jaxb.CalypsoTrade;
import com.calypso.tk.upload.jaxb.CashSettlementInfo;

/**
 * The Class CDUFProductSwap.
 */
public class CDUFSwapBuilder extends AbstractCDUFProductBuilder {

	/*
	 * (non-Javadoc)
	 * @see calypsox.tk.bo.xml.CDUFTradeBuilder#fillProduct(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Trade,
	 * com.calypso.tk.upload.jaxb.Product)
	 */
	@Override
	public void fillProduct(final PricingEnv pricingEnv, final Trade trade, final com.calypso.tk.upload.jaxb.Product jaxbProduct) {
		com.calypso.tk.product.Swap swap = (com.calypso.tk.product.Swap) trade.getProduct();
		com.calypso.tk.upload.jaxb.InterestRateSwap jaxbSwap = new com.calypso.tk.upload.jaxb.InterestRateSwap();
		jaxbProduct.setInterestRateSwap(jaxbSwap);

		jaxbSwap.setExerciseType(swap.getExerciseType());
		jaxbSwap.setFxRate(swap.getInitialFXRate()); //required //nillable
		// TODO: jaxbSwap.setMarkToMarket(); //required //nillable
		jaxbSwap.setIndexResetDate(getIndexResetDate(swap)); //required //nillable
		// TODO: jaxbSwap.setMToMAdjFirst(swap.getAdjustFirstFlowB()); //required //nillable
		List<com.calypso.tk.upload.jaxb.SwapLeg> swapLegList = jaxbSwap.getSwapLeg();
		fillLegs(swap, swapLegList); //required


		// TODO: jaxbSwap.setAutoAdjustStubB(value);
		// TODO: jaxbSwap.setDiscounted(value);
		// TODO: jaxbSwap.setExerciseSchedule(value);
		// TODO: jaxbSwap.setForwardStartNotionalAdjustment(value);
		// TODO: jaxbSwap.setFXResetHolidays(swap.getFXResetOverride(FXResetPurpose.PrincipalAdjustment).getHolidays());
		// TODO: jaxbSwap.setFXResetOffset(swap.getFXResetOverride(FXResetPurpose.PrincipalAdjustment).getResetOffset());
		// TODO: jaxbSwap.setNegociatedPrice(value);
		// TODO: jaxbSwap.setPaySideFXReset(value);
		// TODO: jaxbSwap.setSettlementCurrency(value);
		// TODO: jaxbSwap.setSettlementFxReset(value);
		// TODO: jaxbSwap.setSettlementFxResetHoliday(value);
		// TODO: jaxbSwap.setSettlementFxResetOffSet();
	}

	/*
	 * (non-Javadoc)
	 * @see calypsox.tk.bo.xml.AbstractCDUFTradeBuilder#fillTradeHeader(com.calypso.tk.marketdata.PricingEnv,
	 * com.calypso.tk.core.Trade, com.calypso.tk.upload.jaxb.CalypsoTrade)
	 */
	@Override
	public void fillTradeHeader(final PricingEnv pricingEnv, final Trade trade, final CalypsoTrade calypsoTrade) {
		super.fillTradeHeader(pricingEnv, trade, calypsoTrade);
		com.calypso.tk.product.Swap swap = (com.calypso.tk.product.Swap) trade.getProduct();
		calypsoTrade.setTradeNotional(swap.getPrincipal());
		calypsoTrade.setStartDate(getXmlGregorianCalendarFromDate(swap.getStartDate()));
		calypsoTrade.setProductType("InterestRateSwap");

		fillCashSettleInfo(calypsoTrade, swap);
	}

	private void fillCashSettleInfo(final CalypsoTrade calypsoTrade, final com.calypso.tk.product.Swap swap) {
		@SuppressWarnings("unchecked")
		Vector<CashSettleInfo> tradeCashSettleInfo = swap.getCashSettleInfo();
		if (!tradeCashSettleInfo.isEmpty()) {

			CashSettlementInfo value = new CashSettlementInfo();
			for (CashSettleInfo settleInfo : tradeCashSettleInfo) {
				value.setAgreement(settleInfo.getAgreement()); 
				value.setCashSettlementMethod(settleInfo.getSettleMethod());
				value.setExerciseParty(settleInfo.getExerciseParty());
				value.setExerciseType(settleInfo.getEventType());
				value.setExerciseDateBusinessDays(settleInfo.getExerciseDateBusDays());
				value.setGenerationFrequency(getFrequency(settleInfo.getGenFrequency()));
				@SuppressWarnings("unchecked")
				Vector<String> genHolidays = settleInfo.getGenHolidays();
				value.setGenerationHolidays(getHolidayCodeTypeFromVector(genHolidays));
				value.setExerciseDateConvention(getDateRoll(settleInfo.getExerciseDateConvention()));
				value.setPaymentDateConvention(getDateRoll(settleInfo.getPaymentDateConvention()));
				value.setExpirationTime(getXmlGregorianCalendarFromTime(twentyFourHourTimeToMilliseconds(settleInfo.getExpirationTime())));
				value.setValuationTime(getXmlGregorianCalendarFromTime(twentyFourHourTimeToMilliseconds(settleInfo.getValuationTime())));
				value.setEarliestExerciseTime(getXmlGregorianCalendarFromTime(twentyFourHourTimeToMilliseconds(settleInfo.getFirstExerciseTime())));
				value.setValuationDateBusinessDays(settleInfo.getValuationDateBusDays());
				value.setRateSource(settleInfo.getRateSource());
				value.setQuotationType(settleInfo.getQuoteType());
				value.setPaymentDateBusinessDays(settleInfo.getPaymentDateBusDays());
			}
			calypsoTrade.setCashSettlementInfo(value);
		}
	}

	private void fillLegs(final Swap swap, final List<com.calypso.tk.upload.jaxb.SwapLeg> swapLegList) {
		fillLeg("Pay", swap.getPayLeg(), swapLegList);
		fillLeg("Rec", swap.getReceiveLeg(), swapLegList);
	}
	
	private void fillLeg(final String payRec, final com.calypso.tk.product.SwapLeg swapLeg, final List<com.calypso.tk.upload.jaxb.SwapLeg> swapLegList) {
		com.calypso.tk.upload.jaxb.SwapLeg jaxbSwapLeg = new com.calypso.tk.upload.jaxb.SwapLeg();
		jaxbSwapLeg.setPayRec(payRec);
		jaxbSwapLeg.setLegType(swapLeg.getLegType());
		jaxbSwapLeg.setAmount(Double.valueOf(swapLeg.getPrincipal()));
		jaxbSwapLeg.setRateIndex(getRateIndex(swapLeg.getRateIndex()));
		jaxbSwapLeg.setRateIndexSource(getRateIndexSource(swapLeg.getRateIndex()));
		jaxbSwapLeg.setSpread(swapLeg.getSpread());
		// TODO: jaxbSwapLeg.setRate()//required //Rate en cuanto se contrata
		//jaxbSwapLeg.setFixedRate(swapLeg.getAmortRate()); //Rate de la pata fija 4%
		jaxbSwapLeg.setTenor(getTenor(swapLeg.getRateIndex())); //required
		jaxbSwapLeg.setCurrency(swapLeg.getCurrency());
		jaxbSwapLeg.setSpecificFirstDate(getXmlGregorianCalendarFromDate(swapLeg.getFirstStubDate())); //required
		jaxbSwapLeg.setSpecificLastDate(getXmlGregorianCalendarFromDate(swapLeg.getLastStubDate())); //required
		jaxbSwapLeg.setStartDate(getXmlGregorianCalendarFromDate(swapLeg.getStartDate()));
		jaxbSwapLeg.setEndDate(getXmlGregorianCalendarFromDate(swapLeg.getEndDate()));
		// TODO: jaxbSwapLeg.setInterestCompounding(getCompoundMethod(swapLeg.getCompoundMethod())); //required
		jaxbSwapLeg.setInterestCompoundingMethod(getCompoundMethod(swapLeg.getCompoundMethod())); //required
		jaxbSwapLeg.setInterestCompoundingFrequency(getFrequency(swapLeg.getCompoundFrequency())); //required
		jaxbSwapLeg.setResetRoll(getDateRoll(swapLeg.getResetDateRoll()));
		jaxbSwapLeg.setResetOffsetBusDayB(swapLeg.getResetOffsetBusDayB());
		jaxbSwapLeg.setResetTiming(swapLeg.getResetTiming());
		jaxbSwapLeg.setResetDateRule(getDateRule(swapLeg.getResetDateRule()));
		@SuppressWarnings("unchecked")
        Vector<String> resetHolidays = swapLeg.getResetHolidays();
		jaxbSwapLeg.setResetHolidays(getHolidayCodeTypeFromVector(resetHolidays));
		// TODO: jaxbSwapLeg.setResetFrequency(); //required
		jaxbSwapLeg.setResetLag(getResetLag(swapLeg.getResetOffset(), swapLeg.getDefaultResetOffsetB(), swapLeg.getRateIndex())); //required
		// TODO: jaxbSwapLeg.setResetMethod(); //required
		// TODO: jaxbSwapLeg.setFloatingRateReset(); //required //fijacion de la tasa variable  Euribor 6 Meses VALOR DEL CALCULO TOTAL
		// TODO: jaxbSwapLeg.setAmountsRounding(); //required
		jaxbSwapLeg.setRatesRounding(swapLeg.getParamValue("RATE_ROUNDING")); //required
		jaxbSwapLeg.setRatesRoundingDecPlaces(parseStringToInteger(swapLeg.getParamValue("RATE_ROUNDING_DEC"))); //required
		// TODO: jaxbSwapLeg.setFirstReset(swapLeg.get); //required //dia de fijacion del la pata variable. START - RESETLAG
		jaxbSwapLeg.setFirstRate(String.valueOf(swapLeg.getFirstResetRate())); //required //Fijacion de primer flujo pata variable. 
		jaxbSwapLeg.setFixedAmount(swapLeg.getFixedAmount());
		swapLegList.add(jaxbSwapLeg);
	}

	private boolean getIndexResetDate(final com.calypso.tk.product.Swap swap){
		if(swap.getFXResetOverride(FXResetPurpose.PrincipalAdjustment)!=null){
			return swap.getFXResetOverride(FXResetPurpose.PrincipalAdjustment).getUseIndexResetDateB();
		}
		return false;
	}
}
