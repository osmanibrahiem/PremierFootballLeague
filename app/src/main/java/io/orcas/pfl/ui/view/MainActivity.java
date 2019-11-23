package io.orcas.pfl.ui.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import io.orcas.pfl.R;
import io.orcas.pfl.model.Team;
import io.orcas.pfl.ui.view.teamDetails.TeamDetailsFragment;
import io.orcas.pfl.ui.view.teamsList.TeamsListFragment;
import io.orcas.pfl.ui.view.teamsList.TeamsNavigator;

public class MainActivity extends AppCompatActivity implements TeamsNavigator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragmentsContainer) != null) {

            if (savedInstanceState != null) {
                return;
            }

            TeamsListFragment fragment = new TeamsListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentsContainer, fragment).commitNow();
        }
    }

    @Override
    public void openTeamDetails(Team team) {
        TeamDetailsFragment fragment = new TeamDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("team_id", team.getId());
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentsContainer, fragment).commit();
    }
}
