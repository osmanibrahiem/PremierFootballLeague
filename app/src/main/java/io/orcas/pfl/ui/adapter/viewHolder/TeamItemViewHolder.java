package io.orcas.pfl.ui.adapter.viewHolder;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.orcas.pfl.R;
import io.orcas.pfl.databinding.TeamItemBinding;
import io.orcas.pfl.model.Team;
import io.orcas.pfl.ui.adapter.TeamsListAdapter;

public class TeamItemViewHolder extends RecyclerView.ViewHolder {

    private TeamItemBinding binding;
    private TeamsListAdapter.OnItemClickListener onItemClickListener;

    public TeamItemViewHolder(@NonNull TeamItemBinding binding, TeamsListAdapter.OnItemClickListener onItemClickListener) {
        super(binding.getRoot());
        this.binding = binding;
        this.onItemClickListener = onItemClickListener;
    }

    public void bindTo(Team team) {
        binding.setTeam(team);
        binding.getRoot().setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(team);
            }
        });
    }

    @BindingAdapter("teamImage")
    public static void setImageUrl(AppCompatImageView view, String url) {
        if (url != null) {
            if (url.endsWith(".svg")) {
                SvgLoader.pluck()
                        .with((Activity) view.getContext())
                        .setPlaceHolder(R.drawable.default_crest, R.drawable.default_crest)
                        .load(url, view);
            } else {
                Glide.with(view.getContext())
                        .load(url)
                        .skipMemoryCache( true )
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.default_crest)
                        .placeholder(R.drawable.default_crest)
                        .thumbnail(0.1f)
                        .into(view);
            }
        }
    }

}
