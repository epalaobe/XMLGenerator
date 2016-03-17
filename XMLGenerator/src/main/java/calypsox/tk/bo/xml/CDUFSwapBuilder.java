package calypsox.tk.bo.xml;

import java.util.List;
import java.util.Vector;

import com.calypso.tk.core.Trade;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.product.CashSettleInfo;
import com.calypso.tk.product.Swap;
import com.calypso.tk.product.util.CompoundMethod;
import com.calypso.tk.product.util.quotableReset.FXResetPurpose;
import com.calypso.tk.upload.jaxb.CalypsoTrade;
import com.calypso.tk.upload.jaxb.CashSettlementInfo;
import com.calypso.tk.upload.jaxb.HolidayCodeType;

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

		List<com.calypso.tk.upload.jaxb.SwapLeg> swapLegList = jaxbSwap.getSwapLeg();
		fillLegs(swap, swapLegList); 

		jaxbSwap.setSettlementCurrency(swap.getCurrency());
		jaxbSwap.setExerciseType(swap.getExerciseType());
		jaxbSwap.setFxRate(swap.getInitialFXRate());  
		jaxbSwap.setIndexResetDate(getIndexResetDate(swap));  
		//Revisar
		jaxbSwap.setMToMAdjFirst(swap.getAdjustFirstFlowB());
		jaxbSwap.setFXResetHolidays(getFXResetHolidays(swap));
		jaxbSwap.setFXResetOffset(getFXResetOffset(swap));
		jaxbSwap.setSettlementFxReset(getSettlementFxReset(swap));
		jaxbSwap.setSettlementFxResetHoliday(getSettlementFxResetHoliday(swap));
		jaxbSwap.setSettlementFxResetOffSet(getSettlementFxResetOffset(swap));
		
		// TODO: jaxbSwap.setMarkToMarket();  
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
		calypsoTrade.setCashFlows(getCashflows(pricingEnv, swap));
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
		jaxbSwapLeg.setTenor(getTenor(swapLeg.getRateIndex())); 
		jaxbSwapLeg.setCurrency(swapLeg.getCurrency());
		jaxbSwapLeg.setSpecificFirstDate(getXmlGregorianCalendarFromDate(swapLeg.getFirstStubDate())); 
		jaxbSwapLeg.setSpecificLastDate(getXmlGregorianCalendarFromDate(swapLeg.getLastStubDate())); 
		jaxbSwapLeg.setStartDate(getXmlGregorianCalendarFromDate(swapLeg.getStartDate()));
		jaxbSwapLeg.setEndDate(getXmlGregorianCalendarFromDate(swapLeg.getEndDate()));
		jaxbSwapLeg.setInterestCompounding(String.valueOf(swapLeg.getCompoundB())); 
		jaxbSwapLeg.setInterestCompoundingMethod(getCompoundMethod(swapLeg.getCompoundMethod())); 
		jaxbSwapLeg.setInterestCompoundingFrequency(getFrequency(swapLeg.getCompoundFrequency())); 
		jaxbSwapLeg.setResetRoll(getDateRoll(swapLeg.getResetDateRoll()));
		jaxbSwapLeg.setResetOffsetBusDayB(swapLeg.getResetOffsetBusDayB());
		jaxbSwapLeg.setResetTiming(swapLeg.getResetTiming());
		jaxbSwapLeg.setResetDateRule(getDateRule(swapLeg.getResetDateRule()));
		jaxbSwapLeg.setResetHolidays(getResetHolidays(swapLeg));
		jaxbSwapLeg.setResetLag(getResetLag(swapLeg.getResetOffset(), swapLeg.getDefaultResetOffsetB(), swapLeg.getRateIndex())); 
		jaxbSwapLeg.setRatesRounding(swapLeg.getParamValue("RATE_ROUNDING")); 
		jaxbSwapLeg.setRatesRoundingDecPlaces(parseStringToInteger(swapLeg.getParamValue("RATE_ROUNDING_DEC"))); 
		jaxbSwapLeg.setFixedAmount(swapLeg.getFixedAmount());
		jaxbSwapLeg.setResetMethod(swapLeg.getAveragingResetMethod()); 
		jaxbSwapLeg.setFixedRate(swapLeg.getFixedRate()); 
		jaxbSwapLeg.setBehavioralMaturity(getTenorName(swapLeg.getBehavioralMaturity()));
		jaxbSwapLeg.setCompoundDateRule(getDateRule(swapLeg.getCompoundDateRule()));
		jaxbSwapLeg.setCompoundWithSpreadB(getCompoundWithSpreadB(swapLeg));
		jaxbSwapLeg.setCouponDateRule(getDateRule(swapLeg.getCouponDateRule()));
		jaxbSwapLeg.setCouponOffsetBusDayB(swapLeg.getCouponOffsetBusDayB()); //108
		jaxbSwapLeg.setCouponPaymentAtEnd(swapLeg.getCouponPaymentAtEndB());
		jaxbSwapLeg.setDiscountMethod(swapLeg.getDiscountMethodAsString());
		jaxbSwapLeg.setIncludeFirstB(swapLeg.getIncludeFirstB());
		jaxbSwapLeg.setIncludeLastB(swapLeg.getIncludeLastB());
		jaxbSwapLeg.setInflationCalculationMethod(swapLeg.getInflationCalcMethod());
		jaxbSwapLeg.setIntermediateCurrency(swapLeg.getIntermediateCurrency());
		jaxbSwapLeg.setDayCountConvention(getDayCount(swapLeg.getDayCount())); //109
		jaxbSwapLeg.setDateRollConvention(getDateRoll(swapLeg.getCouponDateRoll())); //104
		jaxbSwapLeg.setRollDay(String.valueOf(swapLeg.getRollingDay())); //106
		jaxbSwapLeg.setSampleTiming(swapLeg.getSampleTiming()); //102
		jaxbSwapLeg.setFloatingRateReset(String.valueOf(swapLeg.getIndexFactor())); //101
		//////////// Revisar:
		jaxbSwapLeg.setRate(swapLeg.getAmortRate()); 
		jaxbSwapLeg.setFirstRate(String.valueOf(swapLeg.getFirstResetRate())); 
		jaxbSwapLeg.setFirstReset(swapLeg.getManualInitFixing().getName());  
		jaxbSwapLeg.setResetFrequency(getFrequency(swapLeg.getCouponFrequency())); // Payment Frequency
		jaxbSwapLeg.setAmountsRounding(getAmountsRounding(swapLeg));
		//swapLeg.getEmbeddedOptionType(); //105
		//swapLeg.getCouponOffset(); //107 
		//swapLeg.getCompoundSpread(); //110
		//swapLeg.getId(); //99
		//swapLeg.getCouponHolidays(); // 103
		swapLegList.add(jaxbSwapLeg);
	}

	private HolidayCodeType getResetHolidays(final com.calypso.tk.product.SwapLeg swapLeg){
		@SuppressWarnings("unchecked")
		Vector<String> resetHolidays = swapLeg.getResetHolidays();
		return getHolidayCodeTypeFromVector(resetHolidays);
	} 
	
	private String getAmountsRounding(final com.calypso.tk.product.SwapLeg swapLeg){
		if (swapLeg.getParamValue("ROUNDING") == null) {
			return swapLeg.getDefaultRounding(); 
		} else {
			return swapLeg.getParamValue("ROUNDING");
		}
	} 
	
	private boolean getCompoundWithSpreadB(final com.calypso.tk.product.SwapLeg swapLeg){
		if(swapLeg.getCompoundMethod()!=null){
			return swapLeg.getCompoundMethod().getCashFlowGeneratorName().equals(CompoundMethod.SPREAD.getDisplayName());
		}
		return false;
	}

	private boolean getIndexResetDate(final com.calypso.tk.product.Swap swap){
		if(swap.getFXResetOverride(FXResetPurpose.PrincipalAdjustment)!=null){
			return swap.getFXResetOverride(FXResetPurpose.PrincipalAdjustment).getUseIndexResetDateB();
		}
		return false;
	}

	private HolidayCodeType getFXResetHolidays(final com.calypso.tk.product.Swap swap){
		if(swap.getFXResetOverride(FXResetPurpose.PrincipalAdjustment)!=null){
			Vector<String> fxResetHolidays = swap.getFXResetOverride(FXResetPurpose.PrincipalAdjustment).getHolidays();
			return getHolidayCodeTypeFromVector(fxResetHolidays);
		}
		return null;
	}

	private Integer getFXResetOffset(final com.calypso.tk.product.Swap swap){
		if(swap.getFXResetOverride(FXResetPurpose.PrincipalAdjustment)!=null){
			return swap.getFXResetOverride(FXResetPurpose.PrincipalAdjustment).getResetOffset();
		}
		return null;
	}

	private HolidayCodeType getSettlementFxResetHoliday(final com.calypso.tk.product.Swap swap){
		if(swap.getFloatLeg()!=null && swap.getFloatLeg().getSettlementFXResetOverride()!=null){
			Vector<String> fxResetHolidays = swap.getFloatLeg().getSettlementFXResetOverride().getHolidays();
			return getHolidayCodeTypeFromVector(fxResetHolidays);
		}
		return null;
	}

	private String getSettlementFxReset(final com.calypso.tk.product.Swap swap){
		if(swap.getFloatLeg()!=null && swap.getFloatLeg().getSettlementFXResetOverride()!=null){
			return swap.getFloatLeg().getSettlementFXResetOverride().toString();
		}
		return null;
	}

	private String getSettlementFxResetOffset(final com.calypso.tk.product.Swap swap){
		if(swap.getFloatLeg()!=null && swap.getFloatLeg().getSettlementFXResetOverride()!=null){
			return String.valueOf(swap.getFloatLeg().getSettlementFXResetOverride().getResetOffset());
		}
		return null;
	}
}
