package io.orcas.pfl.ui.view.teamDetails;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import io.orcas.pfl.BR;
import io.orcas.pfl.R;
import io.orcas.pfl.databinding.FragmentTeamDetailBinding;
import io.orcas.pfl.ui.view.base.BaseFragment;
import io.orcas.pfl.ui.viewmodel.TeamsViewModel;

public class TeamDetailsFragment extends BaseFragment<FragmentTeamDetailBinding, TeamsViewModel> {

    private TeamsViewModel viewModel;
    private FragmentTeamDetailBinding binding;

    @Override
    public int getBindingVariable() {
        return BR.team;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_team_detail;
    }

    @Override
    public TeamsViewModel getViewModel() {
        viewModel = ViewModelProviders.of(getActivity()).get(TeamsViewModel.class);
        return viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getViewDataBinding();

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewModel.getTeamDetails(getArguments().getInt("team_id"))
                .observe(this, binding::setTeam);

        binding.toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
    }

}
