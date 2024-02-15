package twitter4j.impl;

import com.google.gson.reflect.TypeToken;
import twitter4j.*;
import twitter4j.api.TwitterAdsCampaignApi;
import twitter4j.models.ads.Campaign;
import twitter4j.models.ads.HttpVerb;
import twitter4j.models.ads.sort.CampaignSortByField;
import twitter4j.responses.BaseAdsListResponse;
import twitter4j.responses.BaseAdsListResponseIterable;
import twitter4j.responses.BaseAdsResponse;
import twitter4j.util.TwitterAdUtil;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static twitter4j.TwitterAdsConstants.*;

/**
 * User: abhay
 * Date: 4/7/16
 * Time: 12:26 PM
 */
public class TwitterAdsCampaignApiImpl implements TwitterAdsCampaignApi {

    private static final Integer MAX_REQUEST_PARAMETER_SIZE = 50;

    private final TwitterAdsClient twitterAdsClient;

    public TwitterAdsCampaignApiImpl(TwitterAdsClient twitterAdsClient) {
        this.twitterAdsClient = twitterAdsClient;
    }

    @Override
    public BaseAdsListResponseIterable<Campaign> getAllCampaigns(String accountId, Optional<Collection<String>> campaignIds,
                                                                 Optional<Collection<String>> fundingInstrumentIds, boolean withDeleted, Optional<Integer> count,
                                                                 Optional<String> cursor, Optional<CampaignSortByField> sortByField) throws TwitterException {
        TwitterAdUtil.ensureNotNull(accountId, "accountId");
        String campaignIdsAsString = campaignIds.map(TwitterAdUtil::getCsv).orElse(null);
        String fundingInstrumentIdsAsString = fundingInstrumentIds.map(TwitterAdUtil::getCsv).orElse(null);

        List<HttpParameter> params =
                getCampaignParameters(accountId, campaignIdsAsString, fundingInstrumentIdsAsString, withDeleted, count, cursor);

        sortByField.ifPresent(sort -> params.add(new HttpParameter(PARAM_SORT_BY, sort.getField())));

        String baseUrl = twitterAdsClient.getBaseAdsAPIUrl() + PREFIX_ACCOUNTS_V1 + accountId + PATH_CAMPAIGN;

        Type type = new TypeToken<BaseAdsListResponse<Campaign>>() {}.getType();
        return twitterAdsClient.executeHttpListRequest(baseUrl, params, type);
    }

    @Override
    public BaseAdsResponse<Campaign> getCampaignById(String accountId, String campaignId, boolean withDeleted) throws TwitterException {
        TwitterAdUtil.ensureNotNull(accountId, "accountId");
        TwitterAdUtil.ensureNotNull(campaignId, "campaignId");
        String baseUrl = twitterAdsClient.getBaseAdsAPIUrl() + PREFIX_ACCOUNTS_V1 + accountId + PATH_CAMPAIGN + campaignId;
        HttpParameter[] params = new HttpParameter[]{new HttpParameter(PARAM_WITH_DELETED, withDeleted)};
        Type type = new TypeToken<BaseAdsResponse<Campaign>>() {}.getType();
        return twitterAdsClient.executeHttpRequest(baseUrl, params, type, HttpVerb.GET);
    }

    @Override
    public BaseAdsResponse<Campaign> createCampaign(Campaign campaign) throws TwitterException {
        TwitterAdUtil.ensureNotNull(campaign.getAccountId(), "Account Id");
        String accountId = campaign.getAccountId();
        List<HttpParameter> params = validateCreateCampaignParameters(campaign);
        HttpParameter[] parameters = params.toArray(new HttpParameter[0]);
        String baseUrl = twitterAdsClient.getBaseAdsAPIUrl() + PREFIX_ACCOUNTS_V1 + accountId + PATH_CAMPAIGN;
        Type type = new TypeToken<BaseAdsResponse<Campaign>>() {}.getType();
        return twitterAdsClient.executeHttpRequest(baseUrl, parameters, type, HttpVerb.POST);
    }


    @Override
    public BaseAdsResponse<Campaign> updateCampaign(String accountId, String campaignId, Optional<String> name,
                                                    Long totalBudgetAmountLocalMicro, Optional<Long> dailyBudgetAmountLocalMicro, Optional<String> startTime,
                                                    Optional<String> endTime, Optional<Boolean> paused,
                                                    Optional<Boolean> standardDelivery, int frequencyCap, int durationInDays) throws TwitterException {

        List<HttpParameter> params =
                validateUpdateCampaignParameters(accountId, campaignId, name, totalBudgetAmountLocalMicro, dailyBudgetAmountLocalMicro, startTime,
                        endTime, paused, standardDelivery, frequencyCap, durationInDays);
        String baseUrl = twitterAdsClient.getBaseAdsAPIUrl() + PREFIX_ACCOUNTS_V1 + accountId + PATH_CAMPAIGN + campaignId;
        Type type = new TypeToken<BaseAdsResponse<Campaign>>() {}.getType();
        return twitterAdsClient.executeHttpRequest(baseUrl, params.toArray(new HttpParameter[0]), type, HttpVerb.PUT);

    }

    @Override
    public BaseAdsResponse<Campaign> updateCampaignStatus(String accountId, String campaignId, Boolean paused) throws TwitterException {

        TwitterAdUtil.ensureNotNull(accountId, "AccountId");
        TwitterAdUtil.ensureNotNull(campaignId, "Campaign Id");
        TwitterAdUtil.ensureNotNull(paused, "In the Status Update, Paused");

        List<HttpParameter> params = new ArrayList<>();
        params.add(new HttpParameter(PARAM_PAUSED, paused));

        String baseUrl = twitterAdsClient.getBaseAdsAPIUrl() + PREFIX_ACCOUNTS_V1 + accountId + PATH_CAMPAIGN + campaignId;
        Type type = new TypeToken<BaseAdsResponse<Campaign>>() {}.getType();
        return twitterAdsClient.executeHttpRequest(baseUrl, params.toArray(new HttpParameter[0]), type, HttpVerb.PUT);

    }

    @Override
    public BaseAdsResponse<Campaign> deleteCampaign(String accountId, String campaignId) throws TwitterException {
        TwitterAdUtil.ensureNotNull(accountId, "Account Id");
        TwitterAdUtil.ensureNotNull(campaignId, "Campaign Id");
        String baseUrl = twitterAdsClient.getBaseAdsAPIUrl() + PREFIX_ACCOUNTS_V1 + accountId + PATH_CAMPAIGN + campaignId;
        Type type = new TypeToken<BaseAdsResponse<Campaign>>() {}.getType();
        return twitterAdsClient.executeHttpRequest(baseUrl, null, type, HttpVerb.DELETE);
    }

    // -------------------------------------- Private Methods ---------------------------------------------

    private List<HttpParameter> validateCreateCampaignParameters(Campaign campaign) {
        TwitterAdUtil.ensureNotNull(campaign.getName(), "Name");
        String name = campaign.getName();
        TwitterAdUtil.ensureNotNull(campaign.getFundingInstrumentId(), "Funding Instrument ID");
        String fundingInstrumentId = campaign.getFundingInstrumentId();
        TwitterAdUtil.ensureNotNull(campaign.getStartTime(), "Start Time");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String startTime = df.format(campaign.getStartTime());
        TwitterAdUtil.ensureNotNull(campaign.getDailyBudgetInMicro(), "Daily Budget Amount");
        Long dailyBudgetAmountLocalMicro = campaign.getDailyBudgetInMicro();

        List<HttpParameter> params = new ArrayList<>();

        Long totalBudgetAmountLocalMicro = campaign.getTotalBudgetInMicro();
        if (totalBudgetAmountLocalMicro != null) {
            params.add(new HttpParameter(PARAM_TOTAL_BUDGET_AMOUNT_LOCAL_MICRO, totalBudgetAmountLocalMicro));
        }

        params.add(new HttpParameter(PARAM_NAME, name));
        params.add(new HttpParameter(PARAM_FUNDING_INSTRUMENT_ID, fundingInstrumentId));
        params.add(new HttpParameter(PARAM_START_TIME, startTime));
        params.add(new HttpParameter(PARAM_DAILY_BUDGET_AMOUNT_LOCAL_MICRO, dailyBudgetAmountLocalMicro));
        if (campaign.getEndTime() != null) {
            String endTime = df.format(campaign.getEndTime());
            params.add(new HttpParameter(PARAM_END_TIME, endTime));
        }
        if (campaign.getPaused() != null) {
            Boolean paused = campaign.getPaused();
            params.add(new HttpParameter(PARAM_PAUSED, paused));
        }
        if (campaign.getStandardDelivery() != null) {
            Boolean standardDelivery = campaign.getStandardDelivery();
            params.add(new HttpParameter(PARAM_STANDARD_DELIVERY, standardDelivery));
        }
        if (campaign.getFrequencyCap() != null) {
            params.add(new HttpParameter(PARAM_FREQUENCY_CAP, campaign.getFrequencyCap()));
        }
        if (campaign.getDurationInDays() != null) {
            params.add(new HttpParameter(PARAM_DURATION_IN_DAYS, campaign.getDurationInDays()));
        }
        return params;
    }

    private List<HttpParameter> getCampaignParameters(String accountId, String campaignIds, String fundingInstrumentIds, boolean withDeleted,
                                                      Optional<Integer> count, Optional<String> cursor) {
        TwitterAdUtil.ensureNotNull(accountId, "accountId");
        List<HttpParameter> params = new ArrayList<>();
        params.add(new HttpParameter(PARAM_WITH_DELETED, withDeleted));
        if (campaignIds != null) {
            params.add(new HttpParameter(PARAM_CAMPAIGN_IDS, campaignIds));
        }
        if (fundingInstrumentIds != null) {
            params.add(new HttpParameter(PARAM_FUNDING_INSTRUMENT_IDS, fundingInstrumentIds));
        }
        count.ifPresent(value -> params.add(new HttpParameter(PARAM_COUNT, value)));
        cursor.ifPresent(value -> params.add(new HttpParameter(PARAM_CURSOR, value)));

        return params;
    }

    private List<HttpParameter> validateUpdateCampaignParameters(String accountId, String campaignId, Optional<String> name, Long totalBudgetAmountLocalMicro,
                                                                   Optional<Long> dailyBudgetAmountLocalMicro, Optional<String> startTime,
                                                                 Optional<String> endTime, Optional<Boolean> paused,
                                                                 Optional<Boolean> standardDelivery, int frequencyCap, int durationInDays) {
        TwitterAdUtil.ensureNotNull(accountId, "AccountId");
        TwitterAdUtil.ensureNotNull(campaignId, "Campaign Id");
        List<HttpParameter> params = new ArrayList<>();
        //The Ones that can be changed to null
        params.add(new HttpParameter(PARAM_TOTAL_BUDGET_AMOUNT_LOCAL_MICRO, String.valueOf(totalBudgetAmountLocalMicro)));
        //The Ones that cannot be changed to null below
        name.ifPresent(value -> params.add(new HttpParameter(PARAM_NAME, value)));
        dailyBudgetAmountLocalMicro.ifPresent(value -> params.add(new HttpParameter(PARAM_DAILY_BUDGET_AMOUNT_LOCAL_MICRO, value)));
        startTime.ifPresent(value -> params.add(new HttpParameter(PARAM_START_TIME, value)));
        endTime.ifPresent(value -> params.add(new HttpParameter(PARAM_END_TIME, value)));
        paused.ifPresent(value -> params.add(new HttpParameter(PARAM_PAUSED, value)));
        standardDelivery.ifPresent(value -> params.add(new HttpParameter(PARAM_STANDARD_DELIVERY, value)));
        if (frequencyCap > 0) {
            params.add(new HttpParameter(PARAM_FREQUENCY_CAP, frequencyCap));
        }
        if (durationInDays > 0) {
            params.add(new HttpParameter(PARAM_DURATION_IN_DAYS, durationInDays));
        }
        return params;
    }


}
