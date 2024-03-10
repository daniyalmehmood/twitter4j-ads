package twitter4j.impl;

import com.google.gson.reflect.TypeToken;
import twitter4j.*;
import twitter4j.api.TwitterAdsPreviewApi;
import twitter4j.models.ads.preview.TwitterPreviewInfo;
import twitter4j.models.ads.preview.TwitterPreviewTarget;
import twitter4j.responses.BaseAdsListResponse;
import twitter4j.util.TwitterAdUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TwitterAdsPreviewApiImpl implements TwitterAdsPreviewApi {

    private final TwitterAdsClient twitterAdsClient;

    public TwitterAdsPreviewApiImpl(TwitterAdsClient twitterAdsClient) {
        this.twitterAdsClient = twitterAdsClient;
    }

    @Override
    public BaseAdsListResponse<TwitterPreviewInfo> getUnpublishedPromotedTweetPreview(String accountId, String status,
                                                                                      String userId, List<String> mediaIds,
                                                                                      String cardId,
                                                                                      TwitterPreviewTarget twitterPreviewTarget) throws TwitterException {
        TwitterAdUtil.ensureNotNull(accountId, "account Id");
        TwitterAdUtil.ensureNotNull(status, "status");
        var url = twitterAdsClient.getBaseAdsAPIUrl() + TwitterAdsConstants.PREFIX_ACCOUNTS_V1 + accountId + TwitterAdsConstants.TWEET_PREVIEW_PATH;
        var params = new ArrayList<HttpParameter>();
        params.add(new HttpParameter(TwitterAdsConstants.PARAM_ACCOUNT_ID, accountId));
        params.add(new HttpParameter(TwitterAdsConstants.PARAM_STATUS, status));
        if (TwitterAdUtil.isNotNullOrEmpty(userId)) {
            params.add(new HttpParameter(TwitterAdsConstants.PARAM_AS_USER_ID, userId));
        }
        if (!TwitterAdUtil.isNotEmpty(mediaIds) && mediaIds.size() > 4) {
            throw new TwitterException("Media ids cannot be greater than 4");
        }
        if (twitterPreviewTarget == null) {
            twitterPreviewTarget = TwitterPreviewTarget.TWITTER_TIMELINE;
        }
        params.add(new HttpParameter(TwitterAdsConstants.TWEET_PREVIEW_TARGET, twitterPreviewTarget.name()));

        // Simplified handling based on TwitterPreviewTarget
        if (twitterPreviewTarget == TwitterPreviewTarget.PUBLISHER_NETWORK && !TwitterAdUtil.isNotEmpty(mediaIds)) {
            throw new TwitterException("For Preview of tweet using preview_target PUBLISHER_NETWORK, media ids is a compulsory field");
        }
        if (mediaIds != null && !mediaIds.isEmpty()) {
            params.add(new HttpParameter(TwitterAdsConstants.PARAM_MEDIA_IDS, TwitterAdUtil.getCsv(mediaIds)));
        }
        if (cardId != null && !cardId.isBlank()) {
            params.add(new HttpParameter(TwitterAdsConstants.PARAM_CARD_ID, cardId));
        }

        Type type = new TypeToken<BaseAdsListResponse<TwitterPreviewInfo>>(){}.getType();
        try {
            var httpResponse = twitterAdsClient.get(url, params.toArray(new HttpParameter[0]));
            return TwitterAdUtil.constructBaseAdsListResponse(httpResponse, httpResponse.asString(), type);
        } catch (IOException e) {
            throw new TwitterException("Some error occurred while adapting response for previews", e);
        }
    }

    @Override
    public BaseAdsListResponse<TwitterPreviewInfo> getPromotedTweetPreview(String accountId, String tweetId, String userId, TwitterPreviewTarget twitterPreviewTarget) throws TwitterException {
        TwitterAdUtil.ensureNotNull(accountId, "account Id");
        TwitterAdUtil.ensureNotNull(tweetId, "tweetId");
        var url = twitterAdsClient.getBaseAdsAPIUrl() + TwitterAdsConstants.PREFIX_ACCOUNTS_V1 + accountId + TwitterAdsConstants.TWEET_PREVIEW_PATH + tweetId;
        var params = new ArrayList<HttpParameter>();
        params.add(new HttpParameter(TwitterAdsConstants.PARAM_ACCOUNT_ID, accountId));
        params.add(new HttpParameter(TwitterAdsConstants.PARAM_TWEET_ID, tweetId));
        if (TwitterAdUtil.isNotNullOrEmpty(userId)) {
            params.add(new HttpParameter(TwitterAdsConstants.PARAM_AS_USER_ID, userId));
        }
        if (twitterPreviewTarget == null) {
            twitterPreviewTarget = TwitterPreviewTarget.TWITTER_TIMELINE;
        }
        params.add(new HttpParameter(TwitterAdsConstants.TWEET_PREVIEW_TARGET, twitterPreviewTarget.name()));

        Type type = new TypeToken<BaseAdsListResponse<TwitterPreviewInfo>>(){}.getType();
        try {
            var httpResponse = twitterAdsClient.get(url, params.toArray(new HttpParameter[0]));
            return TwitterAdUtil.constructBaseAdsListResponse(httpResponse, httpResponse.asString(), type);
        } catch (IOException e) {
            throw new TwitterException("Some error occurred while adapting response for previews", e);
        }
    }
}
