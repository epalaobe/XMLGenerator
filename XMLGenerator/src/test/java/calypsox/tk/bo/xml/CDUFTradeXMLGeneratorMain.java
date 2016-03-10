package calypsox.tk.bo.xml;

import java.io.FileWriter;
import java.io.IOException;

import com.calypso.tk.bo.BOMessage;
import com.calypso.tk.bo.MessageFormatException;
import com.calypso.tk.core.CalypsoServiceException;
import com.calypso.tk.core.JDatetime;
import com.calypso.tk.core.Trade;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.mo.TradeFilter;
import com.calypso.tk.service.DSConnection;
import com.calypso.tk.util.ConnectionUtil;
import com.calypso.tk.util.MessageArray;
import com.calypso.tk.util.TradeArray;

public class CDUFTradeXMLGeneratorMain {

	private static final String[] productType = { "FRA", "Repo", "SimpleTransfer", "Swap" };

	private static String OUTPUT_DIR = "c:\\tmp\\xml";
	private static String SQL = "product_desc.product_type  IN (\'TYPE\')";

	public static void main(final String[] args) throws Exception {
		DSConnection dsConnection = ConnectionUtil.connect(args, "MainEntry");
		final PricingEnv pricingEnv = DSConnection.getDefault().getRemoteMarketData().getPricingEnv("default"); // RISK
																													// V14.0

		generateMsgFromTrades(dsConnection, pricingEnv);
		// generateMsgFromMsgs(dsConnection, pricingEnv);
	}

	private static void generateMsgFromTrades(final DSConnection dsConnection, final PricingEnv pricingEnv)
			throws CalypsoServiceException, MessageFormatException, IOException {
		BOMessage msg = new BOMessage();

		TradeFilter tf = new TradeFilter();
		for (String type : productType) {
			String sql = SQL.replaceAll("TYPE", type);
			tf.setSQLWhereClause(sql);
			TradeArray array = dsConnection.getRemoteTrade().getTrades(tf, new JDatetime());

			int n = array.size();
			for (int i = 0; i < n; i++) {
				Trade trade = array.get(i);

				msg.setTradeId(trade.getId());
				StringBuffer stringBuffer = new CDUFTradeXMLGenerator().generate(pricingEnv, msg, dsConnection);

				FileWriter writer = new FileWriter(
						OUTPUT_DIR + '\\' + trade.getProductType() + '_' + trade.getId() + ".xml");
				writer.write(stringBuffer.toString());
				writer.close();
				System.out.println("Generado " + type + ": "+ (i+1) + " de " + n);
			}
		}
	}

	private static void generateMsgFromMsgs(final DSConnection dsConnection, final PricingEnv pricingEnv)
			throws CalypsoServiceException, MessageFormatException, IOException {
		MessageArray array = dsConnection.getRemoteBackOffice().getMessages("TEMPLATE_NAME=\'rtce_template.txt\'");

		int n = array.size();
		for (int i = 0; i < n; i++) {
			BOMessage msg = array.get(i);

			StringBuffer stringBuffer = new CDUFTradeXMLGenerator().generate(pricingEnv, msg, dsConnection);

			FileWriter writer = new FileWriter(
					OUTPUT_DIR + '\\' + msg.getProductType() + '_' + msg.getTradeId() + ".xml");
			writer.write(stringBuffer.toString());
			writer.close();
			System.out.println("Generado " + i + " de " + n);
		}
	}

}
