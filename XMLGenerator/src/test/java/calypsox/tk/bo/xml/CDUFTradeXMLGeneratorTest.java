/**
 * 
 */
package calypsox.tk.bo.xml;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.calypso.tk.bo.BOMessage;
import com.calypso.tk.bo.MessageFormatException;
import com.calypso.tk.core.CalypsoServiceException;
import com.calypso.tk.core.Trade;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.product.FRA;
import com.calypso.tk.service.DSConnection;
import com.calypso.tk.service.RemoteReferenceData;
import com.calypso.tk.service.RemoteTrade;

/**
 * @author aalonsop@everis.com
 */
/**
 * @author epalaobe
 *
 */
public class CDUFTradeXMLGeneratorTest {

	public CDUFTradeXMLGeneratorTest() {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	/**
	 * Test method for {@link calypsox.tk.bo.xml.CDUFTradeXMLGenerator#generate(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.bo.BOMessage, com.calypso.tk.service.DSConnection)}.
	 */
	@Test
	public final void testGenerate() {

		DSConnection mockedDS = mock(DSConnection.class);
		RemoteReferenceData mockedRRD = mock(RemoteReferenceData.class);
		when(mockedDS.getRemoteReferenceData()).thenReturn(mockedRRD);
		RemoteTrade mockedRT = mock(RemoteTrade.class);
		when(mockedDS.getRemoteTrade()).thenReturn(mockedRT);
		DSConnection.setDefault(mockedDS);

		PricingEnv pricingEnv = new PricingEnv();

		BOMessage msg = new BOMessage();
		msg.setTradeId(0);

		Trade trade = new Trade();
		trade.setId(0);
		trade.setProduct(new FRA());
		try {
			when(mockedRT.getTrade(0)).thenReturn(trade);
		} catch (CalypsoServiceException e1) {
			fail("Throw calypso service exception");
		}

		CDUFTradeXMLGenerator builder = new CDUFTradeXMLGenerator();
		StringBuffer xml = null;
		try {
			 xml = builder.generate(pricingEnv, msg, mockedDS);
		} catch (MessageFormatException e) {
			fail("Throw message format exception");
		}
		
		assertNotNull(xml);
	}
	
	/**
	 * Test method for {@link calypsox.tk.bo.xml.CDUFTradeXMLGenerator#generate(com.calypso.tk.marketdata.PricingEnv, com.calypso.tk.bo.BOMessage, com.calypso.tk.service.DSConnection)}.
	 */
	@Test
	public final void testGenerateRemoteException() {

		/*CalypsoServiceException e = new CalypsoServiceException("CalypsoServiceException");
		DSConnection mockedDS = mock(DSConnection.class);
		RemoteReferenceData mockedRRD = mock(RemoteReferenceData.class);
		when(mockedDS.getRemoteReferenceData()).thenReturn(mockedRRD);
		RemoteTrade mockedRT = mock(RemoteTrade.class);
		when(mockedDS.getRemoteTrade()).thenReturn(mockedRT);
		DSConnection.setDefault(mockedDS);

		PricingEnv pricingEnv = new PricingEnv();

		BOMessage msg = new BOMessage();
		msg.setTradeId(0);

		Trade trade = new Trade();
		trade.setId(0);
		trade.setProduct(new FRA());
		
		try {
			when(mockedRT.getTrade(0)).thenThrow(e);
		} catch (CalypsoServiceException e1) {
			fail("Throw calypso service exception");
		}

		boolean messageFortmatException = false;
		
		CDUFTradeXMLGenerator builder = new CDUFTradeXMLGenerator();
		try {
			 builder.generate(pricingEnv, msg, mockedDS);
		} catch (MessageFormatException e2) {
			messageFortmatException = true;
		}
		
		assertTrue(messageFortmatException);*/
	}

}
