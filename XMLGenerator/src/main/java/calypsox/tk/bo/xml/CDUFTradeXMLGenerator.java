package calypsox.tk.bo.xml;

import java.io.StringWriter;
import java.rmi.RemoteException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.calypso.tk.bo.BOMessage;
import com.calypso.tk.bo.MessageFormatException;
import com.calypso.tk.bo.xml.XMLGenerator;
import com.calypso.tk.core.CalypsoServiceException;
import com.calypso.tk.core.Log;
import com.calypso.tk.core.Trade;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.service.DSConnection;
import com.calypso.tk.upload.jaxb.CalypsoUploadDocument;
import com.calypso.tk.util.ConnectException;
import com.calypso.tk.util.ConnectionUtil;

/**
 * The Class CDUFTradeXMLGenerator.
 */
public class CDUFTradeXMLGenerator implements XMLGenerator {

	/*
	 * This is the entry point to the message generation
	 * @see com.calypso.tk.bo.xml.XMLGenerator#generate(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.bo.BOMessage,
	 * com.calypso.tk.service.DSConnection)
	 */
	@Override
	public StringBuffer generate(final PricingEnv pricingEnv, final BOMessage msg, final DSConnection dsConn) throws MessageFormatException {
		Trade trade;
		try {
			trade = getTrade(dsConn, msg.getTradeId());
			CDUFDocumentBuilder builder = new CDUFDocumentBuilder();
			CalypsoUploadDocument calypsoDocument = builder.buildCalypsoDocument(pricingEnv, trade, msg);
			return marshall(calypsoDocument);
		} catch (RemoteException e) {
			Log.error(this, e.getMessage(), e);
			throw new MessageFormatException(e);
		} catch (JAXBException e) {
			Log.error(this, e.getMessage(), e);
			throw new MessageFormatException(e);
		}
	}

	/**
	 * Marshall. Convert the jaxb object into a StringBuffer containing the generated XML.
	 *
	 * @param calypsoDocument the calypso document
	 * @return the string buffer
	 * @throws JAXBException the JAXB exception
	 */
	private StringBuffer marshall(final CalypsoUploadDocument calypsoDocument) throws JAXBException {
		StringWriter strwriter = new StringWriter();
		JAXBContext jaxbContext = JAXBContext.newInstance(CalypsoUploadDocument.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(calypsoDocument, strwriter);
		String result = strwriter.toString();
		return new StringBuffer(result);
	}

	/**
	 * Get the Trade from the id.
	 *
	 * @param dsConn the DSConnection
	 * @param tradeId the trade id
	 * @return the Trade
	 * @throws RemoteException
	 */
	private Trade getTrade(final DSConnection dsConn, final int tradeId) throws RemoteException {
		return dsConn.getRemoteTrade().getTrade(tradeId);
	}

	/* *************** main para pruebas - debería borrarse ************************ */
	public static void main(final String[] args) throws ConnectException, MessageFormatException, CalypsoServiceException {
		//DSConnection dsConnection = ConnectionUtil.connect(args, "MainEntry");
		DSConnection dsConnection = ConnectionUtil.connect("admin", "calypso", "Conector", "CalypsoV14"); // EVERIS V14.0
		BOMessage msg = new BOMessage();
		int tradeId = 1716601;
		// FRA
		msg.setTradeId(tradeId); // 1680476 V14.0 Everis //669645 V14.4 BBVA
		// SWAP
		// msg.setTradeId(tradeId); //1676598 V14.0 Everis //667916 V14.4 BBVA
		// SWAP integrado 386135
		// Repo
		// msg.setTradeId(tradeId); //1681434 V14.0 Everis 1676024
		// SimpleTransfer
		// msg.setTradeId(tradeId); //1653371 V14.0 Everis, SECURITY 1681435
		final PricingEnv pricingEnv = DSConnection.getDefault().getRemoteMarketData().getPricingEnv("OFFICIAL"); // RISK V14.4 BBVA //OFFICIAL 14.0 EVERIS
		StringBuffer stringBuffer = new CDUFTradeXMLGenerator().generate(pricingEnv, msg, dsConnection);
		System.out.println("-------------------------");
		System.out.println(stringBuffer.toString());
	}
}
