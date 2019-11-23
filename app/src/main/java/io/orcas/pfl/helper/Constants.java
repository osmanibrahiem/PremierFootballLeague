package io.orcas.pfl.helper;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.orcas.pfl.model.Team;

public class Constants {

    public static final String GLIDE_TIMEOUT = "com.bumptech.glide.load.model.stream.HttpGlideUrlLoader.Timeout";

    // network
    public static final String TEAMS_ARRAY_DATA_TAG = "teams";
    public static final Type MOVIE_ARRAY_LIST_CLASS_TYPE = (new ArrayList<Team>()).getClass();
    public static final String FOOTBALL_LEAGUE_BASE_URL = "https://api.football-data.org/";
    public static final String COMPETITION_ID_PARAM = "competition_id";
    public static final String LEAGUE_TEAMS_END_POINT = "/v2/competitions/{" + COMPETITION_ID_PARAM + "}/teams";
    public static final String TEAM_ID_PARAM = "team_id";
    public static final String TEAM_END_POINT = "/v2/teams/{" + TEAM_ID_PARAM + "}";
    public static final int PREMIER_LEAGUE_COMPETITION_ID = 2021;
    public static final String API_KEY_PARAM = "X-Auth-Token";
    public static final String API_KEY = "d4f35e41e5ee4718b58eda10bc39acd3";
    public static final String LANGUAGE = "en";
    public static final int LOADING_PAGE_SIZE = 6;
    // DB
    public static final String DATA_BASE_NAME = "FOOTBALL_LEAGUE.db";
    public static final int NUMBERS_OF_THREADS = 3;

}
