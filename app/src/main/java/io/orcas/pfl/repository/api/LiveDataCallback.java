package io.orcas.pfl.repository.api;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallback<T> implements Callback<T> {

    private OnDataFetched<ApiResponse<T>> callback;

    public LiveDataCallback(OnDataFetched<ApiResponse<T>> callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
        callback.onNewData(new ApiResponse<>(response));
    }

    @Override
    public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
        callback.onNewData(new ApiResponse<>(t));
    }

    public interface OnDataFetched<T> {
        void onNewData(T data);
    }

}
