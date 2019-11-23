package io.orcas.pfl.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import io.orcas.pfl.databinding.NetworkStateItemBinding;
import io.orcas.pfl.databinding.TeamItemBinding;
import io.orcas.pfl.model.NetworkState;
import io.orcas.pfl.model.Team;
import io.orcas.pfl.ui.adapter.viewHolder.NetworkStateItemViewHolder;
import io.orcas.pfl.ui.adapter.viewHolder.TeamItemViewHolder;

public class TeamsListAdapter extends PagedListAdapter<Team, RecyclerView.ViewHolder> {

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private NetworkState networkState;

    private OnItemClickListener onItemClickListener;

    public TeamsListAdapter() {
        super(Team.DIFF_CALLBACK);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_PROGRESS) {
            NetworkStateItemBinding headerBinding = NetworkStateItemBinding.inflate(layoutInflater, parent, false);
            return new NetworkStateItemViewHolder(headerBinding);
        } else {
            TeamItemBinding itemBinding = TeamItemBinding.inflate(layoutInflater, parent, false);
            return new TeamItemViewHolder(itemBinding, onItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TeamItemViewHolder) {
            ((TeamItemViewHolder) holder).bindTo(getItem(position));
        } else {
            ((NetworkStateItemViewHolder) holder).bindView(networkState);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.LOADED;
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    public interface OnItemClickListener {
        void onClick(Team team);
    }
}
