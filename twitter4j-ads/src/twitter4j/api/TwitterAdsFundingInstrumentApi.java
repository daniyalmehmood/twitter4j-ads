package twitter4j.api;

import com.google.common.base.Optional;
import twitter4j.responses.BaseAdsListResponseIterable;
import twitter4j.responses.BaseAdsResponse;
import twitter4j.TwitterException;
import twitter4j.models.ads.FundingInstrument;
import twitter4j.models.ads.sort.FundingInstrumentSortByField;

import java.util.Collection;

public interface TwitterAdsFundingInstrumentApi {
    BaseAdsListResponseIterable<FundingInstrument> getAllFundingInstruments(String accountId, boolean withDeleted, Collection<String> fundingInstrumentIds, FundingInstrumentSortByField sortByField) throws TwitterException;

    BaseAdsResponse<FundingInstrument> getFundingInstrumentById(String accountId, String fundingInstrumentId, boolean withDeleted) throws TwitterException;

    // Overloads for optional parameters to adhere to best practices
    default BaseAdsListResponseIterable<FundingInstrument> getAllFundingInstruments(String accountId, boolean withDeleted) throws TwitterException {
        return getAllFundingInstruments(accountId, withDeleted, null, null);
    }

    default BaseAdsListResponseIterable<FundingInstrument> getAllFundingInstruments(String accountId, boolean withDeleted, Collection<String> fundingInstrumentIds) throws TwitterException {
        return getAllFundingInstruments(accountId, withDeleted, fundingInstrumentIds, null);
    }

    default BaseAdsListResponseIterable<FundingInstrument> getAllFundingInstruments(String accountId, boolean withDeleted, FundingInstrumentSortByField sortByField) throws TwitterException {
        return getAllFundingInstruments(accountId, withDeleted, null, sortByField);
    }
}
