package twitter4j.api;

import twitter4j.TwitterException;
import twitter4j.models.Granularity;
import twitter4j.models.TwitterSegmentationType;
import twitter4j.models.ads.*;
import twitter4j.responses.BaseAdsListResponse;
import twitter4j.responses.BaseAdsListResponseIterable;
import twitter4j.responses.BaseAdsResponse;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public interface TwitterAdsStatApi {

    BaseAdsListResponseIterable<TwitterEntityStatistics> fetchStatsSync(String accountId, TwitterEntityType twitterEntity, Collection<String> ids, Long startTime, Long endTime, boolean withDeleted, Granularity granularity, TwitterAdObjective objective, Placement placement) throws TwitterException;

    BaseAdsResponse<JobDetails> createAsyncJob(String accountId, TwitterEntityType twitterEntityType, Collection<String> ids, Long startTime, Long endTime, boolean withDeleted, Granularity granularity, TwitterAdObjective twitterAdObjective, Placement placement, Optional<TwitterSegmentationType> twitterSegmentationType) throws TwitterException;

    BaseAdsListResponseIterable<JobDetails> getJobExecutionDetails(String accountId, Collection<String> jobIds) throws TwitterException;

    BaseAdsListResponse<TwitterEntityStatistics> fetchJobDataAsync(String dataUrl) throws TwitterException;
}
