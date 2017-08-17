package com.example.admin.githubapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.admin.githubapi.model.GithubProfile;
import com.example.admin.githubapi.model.GithubRepo;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

    public static final String BASE_URL = "https://api.github.com/users/OlayG/repos";
    public static final String TAG = "MainActivity";

    GithubProfile githubProfile;
    ArrayList<GithubRepo> repoList;
    String result = "";

    RecyclerView.ItemAnimator itemAnimator;
    ReposAdapter reposAdapter;
    @BindView(R.id.rvReposList)
    RecyclerView rvReposList;
    /*GithubRepo[] githubRepo;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        makeRestCallOkHttp(this);

        //loadRV();
    }

    private void loadRV(GithubRepo[] githubRepo) {

        itemAnimator = new DefaultItemAnimator();
        rvReposList.setLayoutManager(new LinearLayoutManager(this));
        rvReposList.setItemAnimator(itemAnimator);

        reposAdapter = new ReposAdapter(githubRepo);
        rvReposList.setAdapter(reposAdapter);
    }

    private void makeRestCallOkHttp(final Activity activity) {
        final WeakReference<Activity> activityWeakRef = new WeakReference<Activity>(activity);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
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

                final GithubRepo[] githubRepo = gson.fromJson(result, GithubRepo[].class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadRV(githubRepo);
                    }
                });

                Log.d(TAG, "onResponse: " + githubRepo[0].getFullName());
            }
        });
    }
}
