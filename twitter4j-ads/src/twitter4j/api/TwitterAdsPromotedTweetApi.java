package twitter4j.api;

import twitter4j.TwitterException;
import twitter4j.models.ads.PromotedTweets;
import twitter4j.models.ads.sort.PromotedTweetsSortByField;
import twitter4j.responses.BaseAdsListResponse;
import twitter4j.responses.BaseAdsListResponseIterable;
import twitter4j.responses.BaseAdsResponse;

import java.util.List;
import java.util.Optional;

public interface TwitterAdsPromotedTweetApi {

    /**
     * Retrieves references to the Promoted Tweets associated with one or more line items.
     *
     * @param accountId   The identifier for the leveraged account.
     * @param lineItemId  A reference to the line item, omitting returns all promoted tweets across campaigns.
     * @param withDeleted Include deleted results in the request, defaults to false.
     * @param count       Specifies the number of Promoted Tweets to retrieve, up to a max of 1000.
     * @param cursor      Cursor for page retrieval, handles paging automatically if not specified.
     * @param sortByField Sorts by supported attribute in ascending or descending order.
     * @return An iterable of {@link PromotedTweets} associated with line items.
     * @throws TwitterException If a Twitter API error occurs.
     */
    BaseAdsListResponseIterable<PromotedTweets> getAllPromotedTweets(String accountId, Optional<String> lineItemId, boolean withDeleted,
                                                                     Optional<Integer> count, Optional<String> cursor, Optional<PromotedTweetsSortByField> sortByField) throws TwitterException;

    /**
     * Retrieves references to the Promoted Tweets associated with a promotedTweetId.
     *
     * @param accountId       The identifier for the leveraged account.
     * @param promotedTweetId A reference to the promoted tweet in question.
     * @return A {@link BaseAdsResponse} containing {@link PromotedTweets} details.
     * @throws TwitterException If a Twitter API error occurs.
     */
    BaseAdsResponse<PromotedTweets> getPromotedTweetsById(String accountId, String promotedTweetId) throws TwitterException;

    /**
     * Creates Promoted Tweets for a given line item.
     *
     * @param accountId  The identifier for the leveraged account.
     * @param lineItemId The line item under which tweets will be promoted.
     * @param tweetIds   The IDs of tweets to promote.
     * @return A {@link BaseAdsListResponse} containing details of created Promoted Tweets.
     * @throws TwitterException If a Twitter API error occurs.
     */
    BaseAdsListResponse<PromotedTweets> createPromotedTweets(String accountId, String lineItemId, List<String> tweetIds) throws TwitterException;

    /**
     * Deletes a Promoted Tweet.
     *
     * @param accountId The identifier for the leveraged account.
     * @param tweetId   The ID of the tweet to delete from promotions.
     * @return A {@link BaseAdsResponse} with the Promoted Tweet marked as deleted.
     * @throws TwitterException If a Twitter API error occurs.
     */
    BaseAdsResponse<PromotedTweets> deletePromotedTweets(String accountId, String tweetId) throws TwitterException;

}
