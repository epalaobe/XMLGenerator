package calypsox.tk.bo.xml;

import java.util.List;
import java.util.Vector;

import com.calypso.tk.bo.BOMessage;
import com.calypso.tk.core.Book;
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
        customDataList.getCustomData().add(createCustomData("MessageAction", getMsgAction(msg.getAction())));
        customDataList.getCustomData().add(createCustomData("MessageSubAction", getMsgAction(msg.getSubAction())));
        customDataList.getCustomData().add(createCustomData("ProcessingOrganization", getProccesingOrg(trade.getBook())));
        customDataList.getCustomData().add(createCustomData("TradeStatus", getStatus(trade.getStatus())));
        customDataList.getCustomData().add(createCustomData("TradeEnteredUser", trade.getEnteredUser()));

        addMessageAttributes(msg, customDataList.getCustomData());
    }

    private void addMessageAttributes(final BOMessage msg, final List<CustomData> customData) {
        @SuppressWarnings("unchecked")
        Vector<String> attrs = msg.getAttributes();

        if (attrs != null) {
            for (int i = 0; i < attrs.size(); i += 2) {
                String name = attrs.get(i);
                String value = attrs.get(i + 1);
                customData.add(createCustomData(name, value));
            }
        }
    }

    private CustomData createCustomData(final String name, final String value) {
        final CustomData data = new CustomData();
        data.setName(name);
        data.setValue(value);
        return data;
    }

    private String getProccesingOrg(final Book book) {
        if ((book != null) && (book.getLegalEntity() != null)) {
            return book.getLegalEntity().getCode();
        }
        return "";
    }

    private String getMsgAction(final com.calypso.tk.core.Action action) {
        if (action != null) {
            return action.toString();
        }
        return "";
    }

    private String getStatus(final com.calypso.tk.core.Status status) {
        if (status != null) {
            return status.toString();
        }
        return "";
    }
}
