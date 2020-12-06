package com.example.brec;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brec.model.Book;
import com.example.brec.model.Item;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragmentRec extends Fragment implements View.OnClickListener{
    private RecyclerView mRvBooks;
    private RecyclerView.LayoutManager mLayoutManager;

    private BookAdapter mBookAdapter;

    private EditText mEtKeyword;
    private Button mBtnSearch;
    private String result;

    private InputMethodManager mInputMethodManager;

    public SearchFragmentRec() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_rec, container,
                false);
        setupRecyclerView(root);
        setupSearchView(root);
        return root;
    }

    private void setupRecyclerView(View view) {
        mRvBooks = view.findViewById(R.id.rv_movies);
        mRvBooks.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRvBooks.setLayoutManager(mLayoutManager);

        // 어댑터 설정
        ArrayList<Item> books = new ArrayList<>();
        mBookAdapter = new BookAdapter(getContext(), books);
        mRvBooks.setAdapter(mBookAdapter);

        // 구분선 추가
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                new LinearLayoutManager(getContext()).getOrientation());
        mRvBooks.addItemDecoration(dividerItemDecoration);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setupSearchView(View view) {
        mEtKeyword = view.findViewById(R.id.et_keyword);
        mBtnSearch = view.findViewById(R.id.btn_search);
        mBtnSearch.setOnClickListener((View.OnClickListener) this);
        mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:

                String keywords = mEtKeyword.getText().toString();
                String url = "http://34.229.136.176:5000/recommend";
                ContentValues val = new ContentValues();
                val.put(keywords, "");
                //AsyncTask를통해 http수행 - arguments주의
                NetworkTask net = new NetworkTask(url, val);
                //Toast.makeText(getContext(), "실패", Toast.LENGTH_SHORT).show();
                net.execute();
                hideKeyboard();
                //startSearch(result);
                break;
        }
    }

    public void hideKeyboard() {
        mInputMethodManager.hideSoftInputFromWindow(mRvBooks.getWindowToken(), 0);
    }

    public void showEmptyFieldMessage() {
        Toast.makeText(getContext(), "원하는 키워드를 입력해주세요", Toast.LENGTH_SHORT).show();
    }

    public void showNotFoundMessage(String keyword) {
        Toast.makeText(getContext(), "해당 키워드에 대한 추천 도서를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
    }

    // 검색어가 입력되었는지 확인 후 영화 가져오기
    public void startSearch(String title) {
        if (title.isEmpty()) {
            showEmptyFieldMessage();
        } else {
            mLayoutManager.scrollToPosition(0);
            getBooks(title);
        }
    }

    // 영화 가져오기
    public void getBooks(final String title) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<Book> call = apiInterface.getBooks(title, 1, 1);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if(response.isSuccessful()) {
                    ArrayList<Item> books = new ArrayList(response.body().getItems());
                    if (books.size() == 0) {
                        mBookAdapter.clearItems();
                        showNotFoundMessage(title);
                    } else {
                        mBookAdapter.clearAndAddItems(books);
                    }
                }else{
                    Log.e("검색실패", response.message());
                }
            }
            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e("검색실패", t.getMessage());
            }
        });
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result; //요청결과 저장
            RequestHttp requestHttp = new RequestHttp();
            result = requestHttp.request(url, values);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            getBooks(s);
            result = s;

        }
    }
}
