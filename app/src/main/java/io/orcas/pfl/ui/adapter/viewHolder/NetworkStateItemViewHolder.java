package io.orcas.pfl.ui.adapter.viewHolder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.orcas.pfl.databinding.NetworkStateItemBinding;
import io.orcas.pfl.model.NetworkState;

public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

    private NetworkStateItemBinding binding;

    public NetworkStateItemViewHolder(@NonNull NetworkStateItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindView(NetworkState networkState) {
        binding.setNetworkState(networkState);
        if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }

        if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
            binding.errorMsg.setVisibility(View.VISIBLE);
            binding.errorMsg.setText(networkState.getMsg());
        } else {
            binding.errorMsg.setVisibility(View.GONE);
        }
    }
}
