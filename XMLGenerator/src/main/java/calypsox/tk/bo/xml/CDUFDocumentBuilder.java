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
     * @return the calypso document
     */
    public CalypsoUploadDocument buildCalypsoDocument(final PricingEnv pricingEnv, final Trade trade, final BOMessage msg) {
        final CalypsoUploadDocument doc = new CalypsoUploadDocument();

        final CalypsoTrade calypsoTrade = new CalypsoTrade();
        doc.getCalypsoTrade().add(calypsoTrade);
        final Product product = new Product();
        calypsoTrade.setProduct(product);

        fillBOMessageHeader(msg, trade, calypsoTrade);
        final CDUFTradeBuilder tradeBuilder = CDUFTradeBuilderFactory.getInstance().getBuilder(trade);
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
    private void fillBOMessageHeader(final BOMessage msg, final Trade trade, final CalypsoTrade doc) {
        final CustomDataList customDataList = new CustomDataList();
        doc.setCustomDataList(customDataList);

        customDataList.getCustomData().add(createCustomData("CalypsoMessageId", Integer.toString(msg.getId())));
        if (msg.getAction() != null) {
            customDataList.getCustomData().add(createCustomData("MessageAction", msg.getAction().toString()));
        }
        if (msg.getSubAction() != null) {
            customDataList.getCustomData().add(createCustomData("MessageSubAction", msg.getSubAction().toString()));
        }
        customDataList.getCustomData().add(createCustomData("ProcessingOrganization", trade.getBook().getLegalEntity().getCode()));
        customDataList.getCustomData().add(createCustomData("TradeStatus", trade.getStatus().toString()));
        customDataList.getCustomData().add(createCustomData("TradeEnteredUser", trade.getEnteredUser()));
    }

    private CustomData createCustomData(final String name, final String value) {
        final CustomData data = new CustomData();
        data.setName(name);
        data.setValue(value);
        return data;
    }
}
