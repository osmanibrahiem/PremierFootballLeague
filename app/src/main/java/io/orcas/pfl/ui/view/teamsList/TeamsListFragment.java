package io.orcas.pfl.ui.view.teamsList;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import io.orcas.pfl.BR;
import io.orcas.pfl.R;
import io.orcas.pfl.databinding.FragmentTeamsListBinding;
import io.orcas.pfl.ui.adapter.TeamsListAdapter;
import io.orcas.pfl.ui.view.MainActivity;
import io.orcas.pfl.ui.view.base.BaseFragment;
import io.orcas.pfl.ui.viewmodel.TeamsViewModel;

public class TeamsListFragment extends BaseFragment<FragmentTeamsListBinding, TeamsViewModel> {

    private FragmentTeamsListBinding mBinding;
    private TeamsViewModel viewModel;

    private TeamsListAdapter adapter;

    public TeamsListFragment() {
        // Required empty public constructor
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_teams_list;
    }

    @Override
    public TeamsViewModel getViewModel() {
        viewModel = ViewModelProviders.of(getActivity()).get(TeamsViewModel.class);
        return viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = getViewDataBinding();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        observersRegisters();
    }

    private void observersRegisters() {
        adapter = new TeamsListAdapter();
        viewModel.getTeamPaged().observe(getActivity(), adapter::submitList);
        viewModel.getNetworkState().observe(getActivity(), adapter::setNetworkState);
        viewModel.setNavigator((MainActivity) getActivity());
        mBinding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(viewModel.getNavigator()::openTeamDetails);
    }

}
