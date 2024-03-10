package examples.stats;


import examples.BaseAdsTest;
import twitter4j.responses.BaseAdsListResponse;
import twitter4j.responses.BaseAdsListResponseIterable;
import twitter4j.TwitterAds;
import twitter4j.TwitterException;
import twitter4j.api.TwitterAdsStatApi;
import twitter4j.models.Granularity;
import twitter4j.models.ads.Placement;
import twitter4j.models.ads.TwitterAdObjective;
import twitter4j.models.ads.TwitterEntityStatistics;
import twitter4j.models.ads.TwitterEntityType;

import java.util.Arrays;
import java.util.List;


public class GetTweetStatsForAccount extends BaseAdsTest {

    public static void main(String[] args) {
        TwitterAds twitterAdsInstance = getTwitterAdsInstance();
        TwitterAdsStatApi statApi = twitterAdsInstance.getStatApi();
        List<TwitterEntityStatistics> twitterEntityStatsList = Arrays.asList();
        Long since = 0L;
        Long until = 0L;
        try {
            BaseAdsListResponseIterable<TwitterEntityStatistics> allTwitterEntityStats = statApi.fetchStatsSync("1b83s0", TwitterEntityType.CAMPAIGN, Arrays.asList("4u3mr"), since, until, Boolean.TRUE, Granularity.TOTAL, TwitterAdObjective.VIDEO_VIEWS, Placement.ALL_ON_TWITTER);
            for (BaseAdsListResponse<TwitterEntityStatistics> allTwitterEntityStat : allTwitterEntityStats) {
                twitterEntityStatsList.addAll(allTwitterEntityStat.getData());
            }
            System.out.println(twitterEntityStatsList.size());
        } catch (TwitterException e) {
            System.err.println(e.getErrorMessage());
        }
    }
}