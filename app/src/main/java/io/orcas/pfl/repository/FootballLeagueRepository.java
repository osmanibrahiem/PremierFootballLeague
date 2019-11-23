package io.orcas.pfl.repository;

import android.content.Context;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.orcas.pfl.helper.Constants;
import io.orcas.pfl.model.NetworkState;
import io.orcas.pfl.model.Team;
import io.orcas.pfl.repository.api.FootballLeagueApi;
import io.orcas.pfl.repository.api.FootballLeagueClient;
import io.orcas.pfl.repository.api.LiveDataCallback;
import io.orcas.pfl.repository.paging.TeamsDataSourceFactory;
import io.orcas.pfl.repository.paging.TeamsPageKeyedDataSource;

public class FootballLeagueRepository {

    private static final String TAG = "NetWorkLogging";

    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Team>> teamsPaged;
    private MutableLiveData<Team> teamDetails;

    private FootballLeagueApi footballLeagueApiService;

    private static FootballLeagueRepository instance;

    public static FootballLeagueRepository getInstance(Context context) {
        if (instance == null) {
            instance = new FootballLeagueRepository(context);
        }
        return instance;
    }

    private FootballLeagueRepository(Context context) {
        initPaging(context);
        footballLeagueApiService = FootballLeagueClient.getInstance(context).getService();
        teamDetails = new MutableLiveData<>();
    }

    private void initPaging(Context context) {
        Executor executor = Executors.newFixedThreadPool(Constants.NUMBERS_OF_THREADS);

        TeamsDataSourceFactory dataSourceFactory = new TeamsDataSourceFactory(context);
        networkState = Transformations.switchMap(dataSourceFactory.getNetworkStatus(),
                (Function<TeamsPageKeyedDataSource, LiveData<NetworkState>>)
                        TeamsPageKeyedDataSource::getNetworkState);

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(Constants.LOADING_PAGE_SIZE)
                .setPageSize(Constants.LOADING_PAGE_SIZE)
                .build();

        LivePagedListBuilder<Integer, Team> livePagedListBuilder = new LivePagedListBuilder<>(dataSourceFactory, pagedListConfig);
        teamsPaged = livePagedListBuilder
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<Team>> getTeamsPaged() {
        return teamsPaged;
    }

    public LiveData<Team> getTeamDetails(int teamID) {
        footballLeagueApiService.getTeam(teamID)
                .enqueue(new LiveDataCallback<>(team -> teamDetails.postValue(team.body)));
        return teamDetails;
    }
}
