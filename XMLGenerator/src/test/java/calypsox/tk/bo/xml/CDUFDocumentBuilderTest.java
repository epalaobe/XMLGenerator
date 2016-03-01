package calypsox.tk.bo.xml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.calypso.tk.bo.BOMessage;
import com.calypso.tk.core.Action;
import com.calypso.tk.core.CalypsoServiceException;
import com.calypso.tk.core.Trade;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.product.FRA;
import com.calypso.tk.service.DSConnection;
import com.calypso.tk.service.RemoteMarketData;
import com.calypso.tk.upload.jaxb.CalypsoTrade;
import com.calypso.tk.upload.jaxb.CalypsoUploadDocument;
import com.calypso.tk.upload.jaxb.CustomData;

/**
 * @author epalaobe@everis.com
 */
public class CDUFDocumentBuilderTest {

	/**
	 * Test method for {@link calypsox.tk.bo.xml.CDUFDocumentBuilder#buildCalypsoDocument(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.core.Trade, com.calypso.tk.bo.BOMessage)}.
	 */
	@Test
	public void testBuildCalypsoDocument() throws CalypsoServiceException {

		DSConnection mockedDS = mock(DSConnection.class);
		RemoteMarketData mockedRMD = mock(RemoteMarketData.class);
		PricingEnv mockedPENV = mock(PricingEnv.class);
		when(mockedDS.getRemoteMarketData()).thenReturn(mockedRMD);
		when(mockedRMD.getPricingEnv("OFFICIAL")).thenReturn(mockedPENV);
		DSConnection.setDefault(mockedDS);
		
		Trade trade = new Trade();
		trade.setProduct(new FRA());

		BOMessage msg = new BOMessage();
		msg.setAction(Action.AMEND);

		assertNotNull(mockedPENV);

		CDUFDocumentBuilder builder = new CDUFDocumentBuilder();

		CalypsoUploadDocument calypsoDocument = builder.buildCalypsoDocument(mockedPENV, trade, msg);

		assertNotNull(calypsoDocument);
		assertFalse(calypsoDocument.getCalypsoTrade().isEmpty());

		for(CalypsoTrade calypsoTrade : calypsoDocument.getCalypsoTrade()){
			assertNotNull(calypsoTrade.getCustomDataList());
			assertFalse(calypsoTrade.getCustomDataList().getCustomData().isEmpty());
			for(CustomData customData : calypsoTrade.getCustomDataList().getCustomData()){
				if(customData.getName().equalsIgnoreCase("MessageAction")){
					assertTrue(customData.getValue().equalsIgnoreCase("AMEND"));
				}
			}
		}
	}
}
