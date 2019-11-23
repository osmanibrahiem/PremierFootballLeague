package io.orcas.pfl.repository.paging;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.orcas.pfl.helper.Constants;
import io.orcas.pfl.model.NetworkState;
import io.orcas.pfl.model.Team;
import io.orcas.pfl.repository.api.FootballLeagueApi;
import io.orcas.pfl.repository.api.FootballLeagueClient;
import io.orcas.pfl.repository.api.LiveDataCallback;

public class TeamsPageKeyedDataSource extends PageKeyedDataSource<Integer, Team> {

    private static final String TAG = TeamsPageKeyedDataSource.class.getSimpleName();
    private final FootballLeagueApi footballLeagueApiService;
    private final MutableLiveData<NetworkState> networkState;

    TeamsPageKeyedDataSource(Context context) {
        footballLeagueApiService = FootballLeagueClient.getInstance(context).getService();
        networkState = new MutableLiveData<>();
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Team> callback) {
        Log.i(TAG, "Loading Initial Rang, Count " + params.requestedLoadSize);
        int pageNum = 0;
        Log.i(TAG, "Loading page " + pageNum);
        int pageSize = params.requestedLoadSize;

        networkState.postValue(NetworkState.LOADING);
        footballLeagueApiService.getLeagueTeams(Constants.PREMIER_LEAGUE_COMPETITION_ID)
                .enqueue(new LiveDataCallback<>(data -> {
                    if (data.body != null && data.body.getTeams() != null) {
                        List<Team> fullList = data.body.getTeams();

                        List<Team> teamsPage = getPageList(fullList, pageNum, pageSize);
                        Integer nextPageNum = (teamsPage.isEmpty()) ? null : pageNum + 1;

                        callback.onResult(teamsPage, pageNum, nextPageNum);
                        networkState.postValue(NetworkState.LOADED);

                    } else {
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, data.errorMessage));
                    }
                }));
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Team> callback) {
        Log.i(TAG, "Loading page " + params.key);
        Integer pageNum = params.key;
        int pageSize = params.requestedLoadSize;

        networkState.postValue(NetworkState.LOADING);
        final AtomicInteger page = new AtomicInteger(0);
        try {
            page.set(params.key);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        networkState.postValue(NetworkState.LOADING);
        footballLeagueApiService.getLeagueTeams(Constants.PREMIER_LEAGUE_COMPETITION_ID)
                .enqueue(new LiveDataCallback<>(data -> {
                    if (data.body != null && data.body.getTeams() != null && data.body.getTeams().size() > 0) {
                        List<Team> fullList = data.body.getTeams();

                        List<Team> teamsPage = getPageList(fullList, pageNum, pageSize);
                        Integer nextPageNum = (teamsPage.isEmpty()) ? null : params.key + 1;

                        callback.onResult(teamsPage, nextPageNum);
                        networkState.postValue(NetworkState.LOADED);

                    } else {
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, data.errorMessage));
                    }
                }));

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Team> callback) {

    }

    private <T> List<T> getPageList(Collection<T> c, int pageNum, int pageSize) {
        if (c == null)
            return Collections.emptyList();
        List<T> list = new ArrayList<T>(c);
        if (pageSize <= 0 || pageSize > list.size())
            pageSize = list.size();
        int numPages = (int) Math.ceil((double) list.size() / (double) pageSize);
        if (pageNum < numPages)
            return list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size()));
        return Collections.emptyList();
    }
}
