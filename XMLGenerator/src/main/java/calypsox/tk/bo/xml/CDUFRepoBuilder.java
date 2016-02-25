package calypsox.tk.bo.xml;

import java.util.List;
import java.util.Vector;

import com.calypso.tk.core.Trade;
import com.calypso.tk.marketdata.PricingEnv;
import com.calypso.tk.secfinance.SecFinanceTkUtil;
import com.calypso.tk.upload.jaxb.BondDetail;
import com.calypso.tk.upload.jaxb.BondDetails;
import com.calypso.tk.upload.jaxb.CalypsoTrade;
import com.calypso.tk.upload.jaxb.EquityDetail;
import com.calypso.tk.upload.jaxb.EquityDetails;
import com.calypso.tk.upload.jaxb.RepoDetails;
import com.calypso.tk.upload.jaxb.RepoFunding;
import com.calypso.tk.upload.jaxb.SecurityDetails;
import com.calypso.tk.upload.jaxb.SubstitutionDetails;

/**
 * The Class CDUFProductRepo.
 */
public class CDUFRepoBuilder extends AbstractCDUFProductBuilder{

	/* (non-Javadoc)
	 * @see calypsox.tk.bo.xml.AbstractCDUFProductXML#fillProduct(com.calypso.tk.core.Product, com.calypso.tk.upload.jaxb.Product)
	 */
	@Override
	public void fillProduct(final PricingEnv pricingEnv, final Trade trade, final com.calypso.tk.upload.jaxb.Product jaxbProduct) {
		com.calypso.tk.product.Repo repo = (com.calypso.tk.product.Repo) trade.getProduct();
		com.calypso.tk.upload.jaxb.Repo jaxbRepo = new com.calypso.tk.upload.jaxb.Repo();
		jaxbProduct.setRepo(jaxbRepo);

		jaxbRepo.setRepoType(com.calypso.tk.product.Repo.getRepoType(repo)); //required
		jaxbRepo.setAllocationType(repo.getAllocationType()); //required
		jaxbRepo.setCallableBy(getCallableBy(repo.getCallableBy())); //required
		jaxbRepo.setDirection(repo.getDirection()); //required
		jaxbRepo.setFillType(repo.getFillType()); //required
		jaxbRepo.setFundingDetails(getRepoFunding(repo)); //required
		jaxbRepo.setNoticeDays(repo.getNoticeDays()); //required
		jaxbRepo.setRepoDetails(getRepoDetails(repo)); //required
		jaxbRepo.setSubstitutionDetails(getSubstitutionDetails(repo)); //required
		// TODO: jaxbRepo.setOpenTerm(repo.getCash().getsec); //required
		jaxbRepo.setSecurityDetails(getSecurityDetails(repo)); //required 
		jaxbRepo.setShowCleanPriceInDecimalB(SecFinanceTkUtil.getShowCleanPriceInDecimalUserProperty(repo.getProductFamily()));

	}

	/* (non-Javadoc)
	 * @see calypsox.tk.bo.xml.AbstractCDUFProductXML#fillTradeHeader(com.calypso.tk.core.Product, com.calypso.tk.upload.jaxb.CalypsoTrade)
	 */
	@Override
	public void fillTradeHeader(final PricingEnv pricingEnv, final Trade trade, final CalypsoTrade calypsoTrade) {
		com.calypso.tk.product.Repo repo = (com.calypso.tk.product.Repo) trade.getProduct();
		calypsoTrade.setTradeNotional(repo.getPrincipal());
		calypsoTrade.setStartDate(getXmlGregorianCalendarFromDate(repo.getStartDate()));
        calypsoTrade.setProductType("Repo");

	}


	private RepoFunding getRepoFunding(final com.calypso.tk.product.Repo repo){
		RepoFunding repoFunding = new RepoFunding();
		com.calypso.tk.product.Cash cash = repo.getCash();

		if(cash!=null){
			repoFunding.setCurrency(cash.getCurrency()); //required
			repoFunding.setDayCountConvention(repo.getDayCount().toString()); //required
			repoFunding.setRateIndex(getRateIndex(cash.getRateIndex())); //required
			repoFunding.setRateIndexSource(getRateIndexSource(cash.getRateIndex())); //required
			repoFunding.setTenor(getTenor(cash.getRateIndex())); //required
			repoFunding.setPrincipal(cash.getPrincipal());
			repoFunding.setCouponFrequency(getFrequency(cash.getPaymentFrequency())); //required
			repoFunding.setFundingType(cash.getRateType()); //required
			repoFunding.setDiscountMethod(cash.getDiscountMethodAsString());
			repoFunding.setFundingRate(cash.getFixedRate());
			repoFunding.setIndexFactor(cash.getIndexFactor());
			@SuppressWarnings("unchecked")
			Vector<String> paymentHolidays = cash.getPaymentHolidays();
			repoFunding.setPaymentHolidays(getHolidayCodeTypeFromVector(paymentHolidays));
			
			if(repo.isJGB()){
				com.calypso.tk.product.JGBRepo jgbRepo = (com.calypso.tk.product.JGBRepo)repo;
				repoFunding.setJGBType(jgbRepo.getType()); //required //nillable
				repoFunding.setBorrowRate(jgbRepo.getBorrowRate());
			}
		}
		return repoFunding;
	}

	private RepoDetails getRepoDetails(final com.calypso.tk.product.Repo repo){
		RepoDetails repoDetails = new RepoDetails();

		repoDetails.setForceDAPB(repo.getForceDAPFlag());
		repoDetails.setInterestDispatch(repo.getInterestDispatchMethod());
		repoDetails.setMinimumParAmount(repo.getMinimumParAmount());
		repoDetails.setRepriceFrequency(getDateRule(repo.getRepriceFrequency()));

		return repoDetails;

	}

	private SubstitutionDetails getSubstitutionDetails(final com.calypso.tk.product.Repo repo){
		SubstitutionDetails substitutionDetails = new SubstitutionDetails();

		substitutionDetails.setAutoAllocB(repo.isAutoCollateralAllocation());
		substitutionDetails.setModifMoneyB(repo.allowModifyMoney());
		substitutionDetails.setSubstitutableB(repo.getSubstitutionB());
		substitutionDetails.setSubstitutionFrequency(getDateRule(repo.getSubstitutionFrequency()));
		substitutionDetails.setSubstitutionLimit(repo.getSubstitutionLimit());

		return substitutionDetails;

	}
	
	private SecurityDetails getSecurityDetails(final com.calypso.tk.product.Repo repo){
		SecurityDetails securityDetails = new SecurityDetails();
		
		// TODO: securityDetails.setFXPrimaryCurrency(repo.get);
		// TODO: securityDetails.setMarginFlagB(value);
		securityDetails.setBondDetails(getBondDetails(repo));
		securityDetails.setEquityDetails(getEquityDetails(repo));
		
		return securityDetails;
	}
	
	private BondDetails getBondDetails(final com.calypso.tk.product.Repo repo){
		BondDetails bondDetails = new BondDetails();
		List<BondDetail> listBondDetails = bondDetails.getBondDetail();
		// TODO: fill list
		
		return bondDetails;
	}
	
	private EquityDetails getEquityDetails(final com.calypso.tk.product.Repo repo){
		EquityDetails equityDetails = new EquityDetails();
		List<EquityDetail> listEquityDetails = equityDetails.getEquityDetail();
		// TODO:  fill list
		
		return equityDetails;
	}

}