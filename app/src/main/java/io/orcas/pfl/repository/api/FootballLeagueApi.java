package io.orcas.pfl.repository.api;

import io.orcas.pfl.helper.Constants;
import io.orcas.pfl.model.League;
import io.orcas.pfl.model.Team;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FootballLeagueApi {

    @GET(Constants.LEAGUE_TEAMS_END_POINT)
    Call<League> getLeagueTeams(@Path(Constants.COMPETITION_ID_PARAM) int competitionId);

    @GET(Constants.TEAM_END_POINT)
    Call<Team> getTeam(@Path(Constants.TEAM_ID_PARAM) int teamId);
}
