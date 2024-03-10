package twitter4j.api;

import twitter4j.responses.BaseAdsListResponse;
import twitter4j.TwitterException;
import twitter4j.models.ads.preview.TwitterPreviewInfo;
import twitter4j.models.ads.preview.TwitterPreviewTarget;

import java.util.List;

public interface TwitterAdsPreviewApi {

    /**
     * @param accountId            The identifier for the leveraged account.
     * @param status               The line item identifier of the line item to delete.
     * @param userId               The line item identifier of the line item to delete.
     * @param cardId               The line item identifier of the line item to delete.
     * @param twitterPreviewTarget The line item identifier of the line item to delete.
     * @return tweet preview object built from the provided parameters
     * @throws TwitterException
     * @see <a href="https://dev.twitter.com/ads/reference/get/accounts/%3Aaccount_id/tweet/preview">https://dev.twitter.com/ads/reference/get/accounts/%3Aaccount_id/tweet/preview</a>
     */
    BaseAdsListResponse<TwitterPreviewInfo> getUnpublishedPromotedTweetPreview(String accountId, String status, String userId, List<String> mediaIds, String cardId, TwitterPreviewTarget twitterPreviewTarget) throws TwitterException;

    /**
     * @param accountId            The identifier for the leveraged account.
     * @param tweetId              The line item identifier of the line item to delete.
     * @param userId               The promotable user identifier to be used in promotion (as_user_id).
     * @param twitterPreviewTarget The line item identifier of the line item to delete.
     * @return tweet preview object built from an existing promoted tweet
     * @throws TwitterException
     * @see <a href="https://dev.twitter.com/ads/reference/get/accounts/%3Aaccount_id/tweet/preview/%3Atweet_id">https://dev.twitter.com/ads/reference/get/accounts/%3Aaccount_id/tweet/preview/%3Atweet_id</a>
     */
    BaseAdsListResponse<TwitterPreviewInfo> getPromotedTweetPreview(String accountId, String tweetId, String userId, TwitterPreviewTarget twitterPreviewTarget) throws TwitterException;

}
