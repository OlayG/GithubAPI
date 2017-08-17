package com.example.admin.githubapi;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.githubapi.model.GithubRepo;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Admin on 8/16/2017.
 */

public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ViewHolder> {

    public static final String TAG = "ReposAdapter";

    GithubRepo[] repos;
    String result = "";

    public ReposAdapter(GithubRepo[] repos) {
        this.repos = repos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvProjectName, tvProjectLink;
        TextView tvName, tvFollowing, tvFollowers;
        ImageView ivProfile_image;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvFollowing = (TextView) itemView.findViewById(R.id.tvFollowing);
            tvFollowers = (TextView) itemView.findViewById(R.id.tvFollowers);
            tvProjectName = (TextView) itemView.findViewById(R.id.tvProjectName);
            tvProjectLink = (TextView) itemView.findViewById(R.id.tvProjectLink);
           /* if(viewType == 0){

                //Profile Card
                //ivProfile_image = (ImageView) itemView.findViewById(R.id.ivProfile_image);
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                tvFollowing = (TextView) itemView.findViewById(R.id.tvFollowing);
                tvFollowers = (TextView) itemView.findViewById(R.id.tvFollowers);
            } else {

                tvProjectName = (TextView) itemView.findViewById(R.id.tvProjectName);
                tvProjectLink = (TextView) itemView.findViewById(R.id.tvProjectLink);
            }*/


        }
    }

    @Override
    public int getItemViewType(int position) {

        int viewType = 1;

        if (position == 0)
            viewType = 0;

        return viewType;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){

            case 0:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false), viewType);
            default:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_list_item, parent, false), viewType);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GithubRepo githubRepo;

        switch (holder.getItemViewType()){

            case 0:
                githubRepo = repos[position];
                holder.tvName.setText(githubRepo.getName());
                holder.tvFollowing.setText(githubRepo.getName());
                holder.tvFollowers.setText(githubRepo.getName());
/*                githubRepo = repos[position];

                String name = githubRepo.getName();
                String one = githubRepo.getFullName();
                String two = githubRepo.getDescription();

                holder.tvProjectName.setText(name);
                holder.tvFollowers.setText(one);
                holder.tvFollowing.setText(two);*/

                break;

            default:
                githubRepo = repos[position - 1];

                holder.tvProjectName.setText(githubRepo.getName());
                holder.tvProjectLink.setText(githubRepo.getHtmlUrl());

                break;
        }
    }

    private void makeRestCallOkHttp(String BASE_URL) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(BASE_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result = response.body().string();
                Gson gson = new Gson();
                int size = gson.fromJson(result, GithubRepo[].class).length;
            }
        });
    }

    @Override
    public int getItemCount() {
        return repos.length + 1;
    }
}
