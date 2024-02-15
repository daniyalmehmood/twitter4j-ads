package examples.PromotedTweet;

import com.google.common.collect.Lists;
import examples.BaseAdsTest;
import twitter4j.responses.BaseAdsListResponse;
import twitter4j.responses.BaseAdsListResponseIterable;
import twitter4j.TwitterAds;
import twitter4j.TwitterException;
import twitter4j.api.TwitterAdsPromotedTweetApi;
import twitter4j.models.ads.PromotedTweets;

import java.util.List;

/**
 * User: shivraj
 * Date: 12/05/16 2:08 PM.
 */
public class GetPromotedTweetForAccount extends BaseAdsTest {

    public static void main(String[] args) {
        TwitterAds twitterAdsInstance = getTwitterAdsInstance();
        TwitterAdsPromotedTweetApi promotedTweetApi = twitterAdsInstance.getPromotedTweetApi();
        List<PromotedTweets> promotedTweetsList = Lists.newArrayList();
        try {
            BaseAdsListResponseIterable<PromotedTweets> allPromotedTweets = promotedTweetApi.getAllPromotedTweets("18ce53uo3nm", null, false, null, null, null);
            for (BaseAdsListResponse<PromotedTweets> allPromotedTweet : allPromotedTweets) {
                promotedTweetsList.addAll(allPromotedTweet.getData());
            }
            System.out.println(promotedTweetsList.size());
        } catch (TwitterException e) {
            System.err.println(e.getErrorMessage());
        }
    }
}