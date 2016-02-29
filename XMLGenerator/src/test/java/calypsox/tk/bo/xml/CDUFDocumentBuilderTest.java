package calypsox.tk.bo.xml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.calypso.tk.bo.BOMessage;
import com.calypso.tk.core.Action;
import com.calypso.tk.core.CalypsoServiceException;
import com.calypso.tk.core.Trade;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.product.FRA;
import com.calypso.tk.service.DSConnection;
import com.calypso.tk.upload.jaxb.CalypsoTrade;
import com.calypso.tk.upload.jaxb.CalypsoUploadDocument;
import com.calypso.tk.util.ConnectException;
import com.calypso.tk.util.ConnectionUtil;

/**
 * @author epalaobe@everis.com
 */
public class CDUFDocumentBuilderTest {

	/**
	 * Start connection to Calypso before Test
	 * 
	 * @throws ConnectException
	 */
	@BeforeClass
	public static void connectDSConnection() throws ConnectException {
		//ConnectionUtil.connect(args, "MainEntry");
        ConnectionUtil.connect("admin", "calypso", "Conector", "CalypsoV14"); // EVERIS V14.0
	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.CDUFDocumentBuilder#buildCalypsoDocument(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Trade, com.calypso.tk.bo.BOMessage)}.
	 */
	@Test
	public void testBuildCalypsoDocument() throws CalypsoServiceException {

		Trade trade = new Trade();
		trade.setProduct(new FRA());
		
		BOMessage msg = new BOMessage();
		msg.setAction(Action.AMEND);
		
		final PricingEnv pricingEnv = DSConnection.getDefault().getRemoteMarketData().getPricingEnv("OFFICIAL");

		assertNotNull(pricingEnv);
		
		CDUFDocumentBuilder builder = new CDUFDocumentBuilder();

		CalypsoUploadDocument calypsoDocument = builder.buildCalypsoDocument(pricingEnv, trade, msg);
		
		assertNotNull(calypsoDocument);
		assertFalse(calypsoDocument.getCalypsoTrade().isEmpty());
		
		for(CalypsoTrade calypsoTrade : calypsoDocument.getCalypsoTrade()){
			assertNotNull(calypsoTrade.getCustomDataList());
			assertFalse(calypsoTrade.getCustomDataList().getCustomData().isEmpty());
			//TODO: more checks
			
		}
		
		//TODO: more test
	}



}
