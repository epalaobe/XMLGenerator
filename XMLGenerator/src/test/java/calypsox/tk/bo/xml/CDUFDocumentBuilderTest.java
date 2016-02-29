package calypsox.tk.bo.xml;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.calypso.tk.bo.BOMessage;
import com.calypso.tk.core.CalypsoServiceException;
import com.calypso.tk.core.Trade;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.product.FRA;
import com.calypso.tk.service.DSConnection;
import com.calypso.tk.upload.jaxb.CalypsoUploadDocument;
import com.calypso.tk.util.ConnectException;
import com.calypso.tk.util.ConnectionUtil;

/**
 * @author epalaobe@everis.com
 */
public class CDUFDocumentBuilderTest {

	@BeforeClass
	public static void connectDSConnection() throws ConnectException {
		//ConnectionUtil.connect(args, "MainEntry");
        ConnectionUtil.connect("admin", "calypso", "Conector", "CalypsoV14"); // EVERIS V14.0
	}

	@Test
	public void testBuildCalypsoDocument() throws CalypsoServiceException {

		Trade trade = new Trade();
		trade.setProduct(new FRA());
		BOMessage msg = new BOMessage();
		PricingEnv pricingEnv = DSConnection.getDefault().getRemoteMarketData().getPricingEnv("OFFICIAL");

		CDUFDocumentBuilder builder = new CDUFDocumentBuilder();

		CalypsoUploadDocument calypsoDocument = builder.buildCalypsoDocument(pricingEnv, trade, msg);
		
		assertNotNull(calypsoDocument);
		
		//TODO:
	}



}
