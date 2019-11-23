package io.orcas.pfl.repository.paging;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import io.orcas.pfl.model.Team;

public class TeamsDataSourceFactory extends DataSource.Factory<Integer, Team> {

    private static final String TAG = TeamsDataSourceFactory.class.getSimpleName();

    private MutableLiveData<TeamsPageKeyedDataSource> networkStatus;
    private TeamsPageKeyedDataSource teamsPageKeyedDataSource;

    public TeamsDataSourceFactory(Context context) {
        this.networkStatus = new MutableLiveData<>();
        teamsPageKeyedDataSource = new TeamsPageKeyedDataSource(context);
    }

    @NonNull
    @Override
    public DataSource<Integer, Team> create() {
        networkStatus.postValue(teamsPageKeyedDataSource);
        return teamsPageKeyedDataSource;
    }

    public MutableLiveData<TeamsPageKeyedDataSource> getNetworkStatus() {
        return networkStatus;
    }

}
