package twitter4j.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import twitter4j.*;
import twitter4j.api.TwitterAdsPromotedTweetApi;
import twitter4j.models.ads.HttpVerb;
import twitter4j.models.ads.PromotedTweets;
import twitter4j.models.ads.sort.PromotedTweetsSortByField;
import twitter4j.responses.BaseAdsListResponse;
import twitter4j.responses.BaseAdsListResponseIterable;
import twitter4j.responses.BaseAdsResponse;
import twitter4j.util.TwitterAdUtil;

import com.google.gson.reflect.TypeToken;

public class TwitterAdsPromotedTweetApiImpl implements TwitterAdsPromotedTweetApi {

    private static final int MAX_REQUEST_PARAMETER_SIZE = 50;
    private final TwitterAdsClient twitterAdsClient;

    public TwitterAdsPromotedTweetApiImpl(TwitterAdsClient twitterAdsClient) {
        this.twitterAdsClient = twitterAdsClient;
    }

    @Override
    public BaseAdsListResponseIterable<PromotedTweets> getAllPromotedTweets(String accountId, Optional<String> lineItemId, boolean withDeleted,
                                                                            Optional<Integer> count, Optional<String> cursor, Optional<PromotedTweetsSortByField> sortByField) throws TwitterException {
        var params = new ArrayList<HttpParameter>();
        populateCommonParameters(params, lineItemId, count, cursor, sortByField, withDeleted);
        var baseUrl = constructBaseUrl(accountId, TwitterAdsConstants.PATH_PROMOTED_TWEETS);
        Type type = new TypeToken<BaseAdsListResponse<PromotedTweets>>(){}.getType();
        return twitterAdsClient.executeHttpListRequest(baseUrl, params, type);
    }

    @Override
    public BaseAdsResponse<PromotedTweets> getPromotedTweetsById(String accountId, String promotedTweetsId) throws TwitterException {
        var baseUrl = constructBaseUrl(accountId, TwitterAdsConstants.PATH_PROMOTED_TWEETS + promotedTweetsId);
        Type type = new TypeToken<BaseAdsResponse<PromotedTweets>>(){}.getType();
        return twitterAdsClient.executeHttpRequest(baseUrl,  new HttpParameter[0], type, HttpVerb.GET);
    }

    @Override
    public BaseAdsListResponse<PromotedTweets> createPromotedTweets(String accountId, String lineItemId, List<String> tweetIds)
            throws TwitterException {
        List<HttpParameter> params = new ArrayList<>();
        params.add(new HttpParameter(TwitterAdsConstants.PARAM_LINE_ITEM_ID, lineItemId));
        if (!tweetIds.isEmpty()) {
            TwitterAdUtil.ensureMaxSize(tweetIds, MAX_REQUEST_PARAMETER_SIZE);
            var tweetIdsAsString = String.join(",", tweetIds);
            params.add(new HttpParameter(TwitterAdsConstants.PARAM_TWEET_IDS, tweetIdsAsString));
        }
        var response = twitterAdsClient.postRequest(constructBaseUrl(accountId, TwitterAdsConstants.PATH_PROMOTED_TWEETS), params.toArray(new HttpParameter[params.size()]));
        return parseResponse(response, new TypeToken<BaseAdsListResponse<PromotedTweets>>(){}.getType());
    }

    @Override
    public BaseAdsResponse<PromotedTweets> deletePromotedTweets(String accountId, String tweetId) throws TwitterException {
        var baseUrl = constructBaseUrl(accountId, TwitterAdsConstants.PATH_PROMOTED_TWEETS + tweetId);
        Type type = new TypeToken<BaseAdsResponse<PromotedTweets>>(){}.getType();
        return twitterAdsClient.executeHttpRequest(baseUrl,  new HttpParameter[0], type, HttpVerb.DELETE);
    }

    private void populateCommonParameters(List<HttpParameter> params, Optional<String> lineItemId, Optional<Integer> count, Optional<String> cursor,
                                          Optional<PromotedTweetsSortByField> sortByField, boolean withDeleted) {
        lineItemId.ifPresent(li -> params.add(new HttpParameter(TwitterAdsConstants.PARAM_LINE_ITEM_ID, li)));
        count.ifPresent(c -> params.add(new HttpParameter(TwitterAdsConstants.PARAM_COUNT, c)));
        cursor.ifPresent(cur -> params.add(new HttpParameter(TwitterAdsConstants.PARAM_CURSOR, cur)));
        sortByField.ifPresent(sbf -> params.add(new HttpParameter(TwitterAdsConstants.PARAM_SORT_BY, sbf.getField())));
        params.add(new HttpParameter(TwitterAdsConstants.PARAM_WITH_DELETED, withDeleted));
    }

    private String constructBaseUrl(String accountId, String path) {
        return twitterAdsClient.getBaseAdsAPIUrl() + TwitterAdsConstants.PREFIX_ACCOUNTS_V1 + accountId + path;
    }

    private <T> T parseResponse(HttpResponse response, Type type) throws TwitterException {
        try {
            return (T) TwitterAdUtil.constructBaseAdsListResponse(response, response.asString(), type);
        } catch (IOException e) {
            throw new TwitterException("Failed to parse response.", e);
        }
    }
}
