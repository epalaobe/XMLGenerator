package calypsox.tk.bo.xml;

import java.util.Vector;

import com.calypso.tk.core.Trade;
import com.calypso.tk.core.Util;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.upload.jaxb.CalypsoTrade;

/**
 * The Class CDUFProductFRA.
 */
public class CDUFFraBuilder extends AbstractCDUFProductBuilder {

    /*
     * (non-Javadoc)
     * @see calypsox.tk.bo.xml.AbstractCDUFProductXML#fillProduct(com.calypso.tk.core.Product, com.calypso.tk.upload.jaxb.Product)
     */
    @Override
    public void fillProduct(final PricingEnv pricingEnv, final Trade trade, final com.calypso.tk.upload.jaxb.Product jaxbProduct) {
        com.calypso.tk.product.FRA fra = (com.calypso.tk.product.FRA) trade.getProduct();
        com.calypso.tk.upload.jaxb.FRA jaxbFra = new com.calypso.tk.upload.jaxb.FRA();
        jaxbProduct.setFRA(jaxbFra);
        
        jaxbFra.setDiscountMethod(fra.getDiscountMethod());
        jaxbFra.setInterpFromIndexTenor(getTenor(fra.getInterpFromIndex()));
        jaxbFra.setInterpolatedB(Boolean.valueOf(fra.getInterpolatedB()));
        jaxbFra.setInterpToIndexTenor(getTenor(fra.getInterpToIndex()));
        jaxbFra.setPaymentDateRoll(getDateRoll(fra.getPaymentDateRoll())); // required
        jaxbFra.setPaymentDayCount(getRateIndexDayCount(fra.getRateIndex())); // required
        jaxbFra.setBeginDateRoll(getDateRoll(fra.getPaymentBeginDateRoll())); // required
        jaxbFra.setEndDateRoll(getDateRoll(fra.getPaymentEndDateRoll())); // required
        @SuppressWarnings("unchecked") 
        Vector<String> paymentBeginHolidays = fra.getPaymentBeginHolidays();
        jaxbFra.setBeginHolidayCode(getHolidayCodeTypeFromVector(paymentBeginHolidays)); // required
        @SuppressWarnings("unchecked")
        Vector<String> paymentEndHolidays = fra.getPaymentEndHolidays();
        jaxbFra.setEndHolidayCode(getHolidayCodeTypeFromVector(paymentEndHolidays)); // required
        Vector<String> paymentHolidays = getRateIndexHolidays(fra.getRateIndex());
        jaxbFra.setPaymentHolidays(getHolidayCodeTypeFromVector(paymentHolidays)); // required
        jaxbFra.setRate(fra.getFixedRate()); // required //nillable
        jaxbFra.setRateIndex(getRateIndex(fra.getRateIndex())); // required //nillable
        jaxbFra.setTenor(getTenor(fra.getRateIndex())); // required //nillable
        jaxbFra.setResetLag(getResetLag(fra.getResetOffset(), fra.getDefaultResetOffsetB(), fra.getRateIndex())); // nillable
        jaxbFra.setRateIndexSource(getRateIndexSource(fra.getRateIndex()));
        jaxbFra.setConvertBasisB(Util.toBoolean(fra.getSecCode("BASIS_CONVERT_B")));
        jaxbFra.setSettleInArrears(fra.getSettleInArrears());
    }

    /*
     * (non-Javadoc)
     * @see calypsox.tk.bo.xml.AbstractCDUFProductXML#fillTradeHeader(com.calypso.tk.core.Product,
     * com.calypso.tk.upload.jaxb.CalypsoTrade)
     */
    @Override
    public void fillTradeHeader(final PricingEnv pricingEnv, final Trade trade, final CalypsoTrade calypsoTrade) {
        super.fillTradeHeader(pricingEnv, trade, calypsoTrade);
        com.calypso.tk.product.FRA fra = (com.calypso.tk.product.FRA) trade.getProduct();
        calypsoTrade.setTradeNotional(fra.getNotional());
        calypsoTrade.setStartDate(getXmlGregorianCalendarFromDate(fra.getStartDate()));
        calypsoTrade.setProductType("FRA");
   
    }

}
