package calypsox.tk.bo.xml;

import com.calypso.tk.core.Trade;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.upload.jaxb.CalypsoTrade;
import com.calypso.tk.upload.jaxb.SimpleTransferSecurity;

/**
 * The Class CDUFProductSimpleTransfer.
 */
public class CDUFSimpleTransferBuilder extends AbstractCDUFProductBuilder {

	/* (non-Javadoc)
	 * @see calypsox.tk.bo.xml.AbstractCDUFProductXML#fillProduct(com.calypso.tk.core.Product, com.calypso.tk.upload.jaxb.Product)
	 */
	@Override
	public void fillProduct(final PricingEnv pricingEnv, final Trade trade, final com.calypso.tk.upload.jaxb.Product jaxbProduct) {
		com.calypso.tk.product.SimpleTransfer simpleTransfer = (com.calypso.tk.product.SimpleTransfer) trade.getProduct();
		com.calypso.tk.upload.jaxb.SimpleTransfer jaxbSimpleTransfer = new com.calypso.tk.upload.jaxb.SimpleTransfer();
		jaxbProduct.setSimpleTransfer(jaxbSimpleTransfer);

		jaxbSimpleTransfer.setLinkedId(simpleTransfer.getLinkedId());
		jaxbSimpleTransfer.setType(simpleTransfer.getType()); //required
		jaxbSimpleTransfer.setSimpleTransferType(simpleTransfer.getSubType()); //required
		jaxbSimpleTransfer.setSimpleTransferSecurity(getSimpleTransferSecurity(simpleTransfer)); //required
		// TODO: jaxbSimpleTransfer.setTradeDirection(); //required //pay o receive
		jaxbSimpleTransfer.setFromLegalEntityRole(simpleTransfer.getOrdererRole()); // To or From
		// TODO: jaxbSimpleTransfer.setFromLegalEntity(getLegalEntity(simpleTransfer.getOrdererLeId()));
	}

	/* (non-Javadoc)
	 * @see calypsox.tk.bo.xml.AbstractCDUFProductXML#fillTradeHeader(com.calypso.tk.core.Product, com.calypso.tk.upload.jaxb.CalypsoTrade)
	 */
	@Override
	public void fillTradeHeader(final PricingEnv pricingEnv, final Trade trade, final CalypsoTrade calypsoTrade) {
		com.calypso.tk.product.SimpleTransfer simpleTransfer = (com.calypso.tk.product.SimpleTransfer) trade.getProduct();
		calypsoTrade.setTradeNotional(simpleTransfer.getPrincipal());
		calypsoTrade.setProductType("SimpleTransfer");

	}

	private SimpleTransferSecurity getSimpleTransferSecurity(final com.calypso.tk.product.SimpleTransfer simpleTransfer){
		SimpleTransferSecurity simpleTransferSecurity = new SimpleTransferSecurity();
		if(simpleTransfer.getSecurity()!=null){
			// TODO: simpleTransferSecurity.setAccrual();
			simpleTransferSecurity.setDeliveryAgainstPaymentB(simpleTransfer.getIsDAP());
			simpleTransferSecurity.setDeliveryAgainstPaymentCashAmount(simpleTransfer.getDAPCashAmount());
			simpleTransferSecurity.setDeliveryAgainstPaymentCurrency(getSecurityCurrency(simpleTransfer.getSecurity()));
			// TODO: simpleTransferSecurity.setDeliveryAgainstPaymentFXRate();
			// TODO: simpleTransferSecurity.setDirtyPrice();
			simpleTransferSecurity.setNominal(simpleTransfer.getPrincipal());
			simpleTransferSecurity.setPledgedSecurityB(simpleTransfer.getIsPledgeMovementB());
			simpleTransferSecurity.setPrice(getDisplayValue(simpleTransfer.getPriceDisplayValue()));
			// TODO: simpleTransferSecurity.setQuantity();
			simpleTransferSecurity.setReturnedSecurityB(simpleTransfer.getIsReturnB());
			// TODO: simpleTransferSecurity.setSecCode();
			// TODO: simpleTransferSecurity.setSecCodeValue();
		}
		return null;
	}

	private String getSecurityCurrency(final com.calypso.tk.core.Product product){
		if (product != null) {
			return product.getCurrency();
		}
		return null;
	}

}
