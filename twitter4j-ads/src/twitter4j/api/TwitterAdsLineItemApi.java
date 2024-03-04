package twitter4j.api;

import com.google.common.base.Optional;
import twitter4j.responses.BaseAdsListResponseIterable;
import twitter4j.responses.BaseAdsResponse;
import twitter4j.TwitterException;
import twitter4j.models.ads.BidType;
import twitter4j.models.ads.LineItem;
import twitter4j.models.ads.PromotedAccount;
import twitter4j.models.ads.Sentiments;
import twitter4j.models.ads.sort.LineItemsSortByField;
import twitter4j.models.ads.sort.PromotedAccountsSortByField;
import twitter4j.models.video.AssociateMediaCreativeResponse;
import twitter4j.models.video.PreRollCallToActionResponse;
import twitter4j.models.video.TwitterCallToActionType;

import java.util.Collection;


public interface TwitterAdsLineItemApi {

    BaseAdsListResponseIterable<LineItem> getAllLineItems(String accountId, Collection<String> campaignIds, Collection<String> lineItemIds,
                                                          Collection<String> fundingInstrumentIds, Integer count, boolean withDeleted,
                                                          String cursor, LineItemsSortByField sortByField) throws TwitterException;

    BaseAdsResponse<LineItem> getLineItemById(String accountId, String lineItemId, boolean withDeleted) throws TwitterException;

    BaseAdsResponse<LineItem> createLineItem(LineItem lineItem) throws TwitterException;

    BaseAdsResponse<LineItem> updateLineItem(String accountId, String lineItemId, BidType bidType, boolean automaticallySelectBid,
                                             Long bidAmountLocalMicro, Boolean paused, Sentiments includeSentiment,
                                             Boolean matchRelevantPopularQueries, String chargeBy, String bidUnit, String advertiserDomain,
                                             String[] iabCategories) throws TwitterException;

    BaseAdsResponse<LineItem> deleteLineItem(String accountId, String lineItemId) throws TwitterException;

    BaseAdsResponse<PromotedAccount> createPromotedAccounts(String accountId, String lineItemId, String userId) throws TwitterException;

    BaseAdsListResponseIterable<PromotedAccount> getPromotedAccounts(String accountId, Collection<String> promotedAccountIds,
                                                                     String lineItemId, boolean withDeleted,
                                                                     PromotedAccountsSortByField sortByField) throws TwitterException;

    BaseAdsResponse<PreRollCallToActionResponse> createCallToActionDetailsForPreRollViews(String accountId, String lineItemId,
                                                                                          TwitterCallToActionType twitterCallToActionType,
                                                                                          String callToActionUrl) throws TwitterException;

    BaseAdsResponse<AssociateMediaCreativeResponse> associateMediaCreativeWithAccount(String accountId, String lineItemId, String accountMediaId,

}
