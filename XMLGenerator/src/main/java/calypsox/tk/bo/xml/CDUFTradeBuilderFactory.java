package calypsox.tk.bo.xml;

import com.calypso.tk.core.Product;
import com.calypso.tk.core.Trade;

/**
 * A factory for creating CDUFTradeBuilder objects.
 */
public class CDUFTradeBuilderFactory {

	/** The instance. */
	private static CDUFTradeBuilderFactory instance = new CDUFTradeBuilderFactory();
	
	/**
	 * Gets the single instance of CDUFTradeBuilderFactory.
	 *
	 * @return single instance of CDUFTradeBuilderFactory
	 */
	public static CDUFTradeBuilderFactory getInstance() {
		return instance;
	}
	
    /**
     * Gets the builder.
     *
     * @param trade the trade
     * @return the builder
     */
    public CDUFTradeBuilder getBuilder(final Trade trade) {
        CDUFTradeBuilder object;
        final Product product = trade.getProduct();
        if (product instanceof com.calypso.tk.product.FRA) {
            object = new CDUFFraBuilder();
        } else if (product instanceof com.calypso.tk.product.Swap) {
            object = new CDUFSwapBuilder(); 
        } else if (product instanceof com.calypso.tk.product.SimpleTransfer) {
            object = new CDUFSimpleTransferBuilder();
        } else if (product instanceof com.calypso.tk.product.Repo) {
            object = new CDUFRepoBuilder();
        } else {
            throw new java.lang.UnsupportedOperationException("the product type '" + trade.getProductType() + "' is not supported");
        }
        return object;
    }

}
