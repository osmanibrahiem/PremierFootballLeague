package io.orcas.pfl.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import io.orcas.pfl.model.NetworkState;
import io.orcas.pfl.model.Team;
import io.orcas.pfl.repository.FootballLeagueRepository;
import io.orcas.pfl.ui.view.base.BaseViewModel;
import io.orcas.pfl.ui.view.teamsList.TeamsNavigator;

public class TeamsViewModel extends BaseViewModel<TeamsNavigator> {

    private FootballLeagueRepository mRepository;

    public TeamsViewModel(@NonNull Application application) {
        super(application);
        mRepository = FootballLeagueRepository.getInstance(application);
    }

    public LiveData<NetworkState> getNetworkState() {
        return mRepository.getNetworkState();
    }

    public LiveData<PagedList<Team>> getTeamPaged() {
        return mRepository.getTeamsPaged();
    }

    public LiveData<Team> getTeamDetails(int teamID) {
        return mRepository.getTeamDetails(teamID);
    }


}
