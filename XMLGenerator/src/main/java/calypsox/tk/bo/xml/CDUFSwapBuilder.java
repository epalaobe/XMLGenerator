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
		fillLegs(swap, swapLegList); //required

		jaxbSwap.setSettlementCurrency(swap.getCurrency());
		jaxbSwap.setExerciseType(swap.getExerciseType());
		jaxbSwap.setFxRate(swap.getInitialFXRate()); //required //nillable
		jaxbSwap.setIndexResetDate(getIndexResetDate(swap)); //required //nillable
		//Revisar
		jaxbSwap.setMToMAdjFirst(swap.getAdjustFirstFlowB()); //required //nillable Menu
		jaxbSwap.setFXResetHolidays(getFXResetHolidays(swap));
		jaxbSwap.setFXResetOffset(getFXResetOffset(swap));
		jaxbSwap.setSettlementFxReset(getSettlementFxReset(swap));
		jaxbSwap.setSettlementFxResetHoliday(getSettlementFxResetHoliday(swap));
		jaxbSwap.setSettlementFxResetOffSet(getSettlementFxResetOffset(swap));
		
		// TODO: jaxbSwap.setMarkToMarket(); //required //nillable
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
		jaxbSwapLeg.setTenor(getTenor(swapLeg.getRateIndex())); //required
		jaxbSwapLeg.setCurrency(swapLeg.getCurrency());
		jaxbSwapLeg.setSpecificFirstDate(getXmlGregorianCalendarFromDate(swapLeg.getFirstStubDate())); //required
		jaxbSwapLeg.setSpecificLastDate(getXmlGregorianCalendarFromDate(swapLeg.getLastStubDate())); //required
		jaxbSwapLeg.setStartDate(getXmlGregorianCalendarFromDate(swapLeg.getStartDate()));
		jaxbSwapLeg.setEndDate(getXmlGregorianCalendarFromDate(swapLeg.getEndDate()));
		jaxbSwapLeg.setInterestCompounding(String.valueOf(swapLeg.getCompoundB())); //required
		jaxbSwapLeg.setInterestCompoundingMethod(getCompoundMethod(swapLeg.getCompoundMethod())); //required
		jaxbSwapLeg.setInterestCompoundingFrequency(getFrequency(swapLeg.getCompoundFrequency())); //required
		jaxbSwapLeg.setResetRoll(getDateRoll(swapLeg.getResetDateRoll()));
		jaxbSwapLeg.setResetOffsetBusDayB(swapLeg.getResetOffsetBusDayB());
		jaxbSwapLeg.setResetTiming(swapLeg.getResetTiming());
		jaxbSwapLeg.setResetDateRule(getDateRule(swapLeg.getResetDateRule()));
		@SuppressWarnings("unchecked")
		Vector<String> resetHolidays = swapLeg.getResetHolidays();
		jaxbSwapLeg.setResetHolidays(getHolidayCodeTypeFromVector(resetHolidays));
		jaxbSwapLeg.setResetLag(getResetLag(swapLeg.getResetOffset(), swapLeg.getDefaultResetOffsetB(), swapLeg.getRateIndex())); //required
		jaxbSwapLeg.setRatesRounding(swapLeg.getParamValue("RATE_ROUNDING")); //required
		jaxbSwapLeg.setRatesRoundingDecPlaces(parseStringToInteger(swapLeg.getParamValue("RATE_ROUNDING_DEC"))); //required
		jaxbSwapLeg.setFixedAmount(swapLeg.getFixedAmount());
		jaxbSwapLeg.setResetMethod(swapLeg.getAveragingResetMethod()); //required
		jaxbSwapLeg.setFixedRate(swapLeg.getFixedRate()); //Rate de la pata fija
		jaxbSwapLeg.setBehavioralMaturity(getTenorName(swapLeg.getBehavioralMaturity()));
		jaxbSwapLeg.setCompoundDateRule(getDateRule(swapLeg.getCompoundDateRule()));
		jaxbSwapLeg.setCompoundWithSpreadB(getCompoundWithSpreadB(swapLeg));
		jaxbSwapLeg.setCouponDateRule(getDateRule(swapLeg.getCouponDateRule()));
		jaxbSwapLeg.setCouponOffsetBusDayB(swapLeg.getCouponOffsetBusDayB());
		jaxbSwapLeg.setCouponPaymentAtEnd(swapLeg.getCouponPaymentAtEndB());
		jaxbSwapLeg.setDiscountMethod(swapLeg.getDiscountMethodAsString());
		jaxbSwapLeg.setIncludeFirstB(swapLeg.getIncludeFirstB());
		jaxbSwapLeg.setIncludeLastB(swapLeg.getIncludeLastB());
		jaxbSwapLeg.setInflationCalculationMethod(swapLeg.getInflationCalcMethod());
		jaxbSwapLeg.setIntermediateCurrency(swapLeg.getIntermediateCurrency());
		//////////// Revisar:
		jaxbSwapLeg.setRate(swapLeg.getAmortRate());//required 
		jaxbSwapLeg.setFirstRate(String.valueOf(swapLeg.getFirstResetRate())); //required
		jaxbSwapLeg.setFirstReset(swapLeg.getManualInitFixing().getName()); //required 

		jaxbSwapLeg.setResetFrequency(getFrequency(swapLeg.getCouponFrequency())); //required Payment Frequency
		if (swapLeg.getParamValue("ROUNDING") != null) {
			jaxbSwapLeg.setAmountsRounding(swapLeg.getParamValue("ROUNDING"));
		} else {
			jaxbSwapLeg.setAmountsRounding(swapLeg.getDefaultRounding()); //required
		}

		// TODO: jaxbSwapLeg.setFloatingRateReset(); //required //fijacion de la tasa variable  Euribor 6 Meses VALOR DEL CALCULO TOTAL del Rate
		
		swapLegList.add(jaxbSwapLeg);
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
