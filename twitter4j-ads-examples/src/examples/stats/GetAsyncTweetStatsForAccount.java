package examples.stats;


import examples.BaseAdsTest;
import twitter4j.*;
import twitter4j.api.TwitterAdsStatApi;
import twitter4j.models.Granularity;
import twitter4j.models.ads.*;
import twitter4j.responses.BaseAdsListResponse;
import twitter4j.responses.BaseAdsListResponseIterable;
import twitter4j.responses.BaseAdsResponse;
import twitter4j.util.TwitterAdUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class GetAsyncTweetStatsForAccount extends BaseAdsTest {

    public static void main(String[] args) {
        TwitterAds twitterAdsInstance = getTwitterAdsInstance();
        TwitterAdsStatApi statApi = twitterAdsInstance.getStatApi();
        long until = 0;
        long since = 0;
        try {
            BaseAdsResponse<JobDetails> twitterAsyncJob = statApi.createAsyncJob("1b83s0", TwitterEntityType.CAMPAIGN, Arrays.asList("4u3mr"), since, until, Boolean.TRUE, Granularity.TOTAL, TwitterAdObjective.VIDEO_VIEWS, Placement.ALL_ON_TWITTER, null);
            BaseAdsListResponseIterable<JobDetails> jobExecutionDetails;
            boolean flag;
            long timeOut = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2);
            do {
                flag = false; //continue iterating as long as status of job of job is either queued, uploading or processing
                TwitterAdUtil.reallySleep(10000L);
                jobExecutionDetails = statApi.getJobExecutionDetails("1b83s0", Arrays.asList(twitterAsyncJob.getData().getJobId()));

                for (BaseAdsListResponse<JobDetails> base : jobExecutionDetails) {
                    List<JobDetails> baselist = base.getData();
                    for (JobDetails jd : baselist) {
                        if ((jd != null) && (jd.getStatus() != TwitterAsyncQueryStatus.SUCCESS)) {
                            flag = true;
                        }
                    }
                }
            } while (flag && System.currentTimeMillis() <= timeOut);

            List<TwitterEntityStatistics> twitterEntityStatsList = Arrays.asList();

            for (BaseAdsListResponse<JobDetails> base : jobExecutionDetails) {
                List<JobDetails> baselist = base.getData();
                for (JobDetails jd : baselist) {
                    BaseAdsListResponse<TwitterEntityStatistics> allTwitterEntityStat = statApi.fetchJobDataAsync(jd.getUrl());
                    if(allTwitterEntityStat == null || allTwitterEntityStat.getData() == null){
                        continue;
                    }
                    twitterEntityStatsList.addAll(allTwitterEntityStat.getData());
                }
                System.out.println(twitterEntityStatsList.size());
            }
        } catch (TwitterException e) {
            System.err.println(e.getErrorMessage());
        }
    }
}