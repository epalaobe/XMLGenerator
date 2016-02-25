package calypsox.tk.bo.xml;

import com.calypso.tk.core.Trade;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.upload.jaxb.CalypsoTrade;

public interface CDUFTradeBuilder {

    /**
     * Fill some data header that are general to all trades.
     *
     * @param trade the trade
     * @param calypsoTrade the method modifies the calypsoTrade to add the trade data
     */
    void fillTradeHeader(final PricingEnv pricingEnv, final Trade trade, final CalypsoTrade calypsoTrade);

    /**
     * Fill the body of specific product
     *
     * @param product the product
     * @param jaxbProduct the method modifies the jaxbProduct to add the product data
     */
    void fillProduct(final PricingEnv pricingEnv, final Trade trade, com.calypso.tk.upload.jaxb.Product jaxbProduct);

}
