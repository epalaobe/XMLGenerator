package calypsox.tk.bo.xml;

import com.calypso.tk.bo.BOMessage;
import com.calypso.tk.core.Trade;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.upload.jaxb.CalypsoTrade;
import com.calypso.tk.upload.jaxb.CalypsoUploadDocument;
import com.calypso.tk.upload.jaxb.CustomData;
import com.calypso.tk.upload.jaxb.CustomDataList;
import com.calypso.tk.upload.jaxb.Product;

public class CDUFDocumentBuilder {

    /**
     * Build the calypso document based on the Trade's data
     *
     * @param pricingEnv the pricing env
     * @param trade the trade
     * @param msg the msg
     * @return the calypso document auditinfo
     */
    public CalypsoUploadDocument buildCalypsoDocument(final PricingEnv pricingEnv, final Trade trade, final BOMessage msg) {
        CalypsoUploadDocument doc = new CalypsoUploadDocument();

        CalypsoTrade calypsoTrade = new CalypsoTrade();
        doc.getCalypsoTrade().add(calypsoTrade);
        Product product = new Product();
        calypsoTrade.setProduct(product);
        
        fillBOMessageId(msg, calypsoTrade);
        CDUFTradeBuilder tradeBuilder = CDUFTradeBuilderFactory.getInstance().getBuilder(trade);
        tradeBuilder.fillTradeHeader(pricingEnv, trade, calypsoTrade);
        tradeBuilder.fillProduct(pricingEnv, trade, product);

        return doc;
    }

    /**
     * Fill bo message id into the CustomDataList
     *
     * @param msg the msg
     * @param doc the method modifies the doc to add the boMessageId
     */
    private void fillBOMessageId(final BOMessage msg, final CalypsoTrade doc) {
        CustomData data = new CustomData();
        data.setName("CalypsoMessageId");
        data.setValue(Integer.toString(msg.getId()));
        
        CustomDataList customDataList = new CustomDataList();
        customDataList.getCustomData().add(data);

        doc.setCustomDataList(customDataList);
    }

}
