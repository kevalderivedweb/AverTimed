package com.example.avertimed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.avertimed.API.UserSession;
import com.example.avertimed.Adapter.CategoryAdapter;
import com.example.avertimed.Adapter.RecomadedAdapter;
import com.example.avertimed.Model.CategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractSequentialList;
import java.util.ArrayList;

public class ThirdFragment extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private ArrayList<CategoryModel> categoryModels = new ArrayList<>();
	private RecyclerView category_view;
	private RecomadedAdapter mAdapter;
	private UserSession userSession;


	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_third, container, false);

		String strtext = getArguments().getString("recomendation");
		Log.e("recomendationRes",""+strtext);

		userSession = new UserSession(getActivity());
		try {
			JSONArray jsonArray = new JSONArray(strtext);
			for(int i =0 ; i<jsonArray.length();i++){
				JSONObject object5 = jsonArray.getJSONObject(i);

				CategoryModel categoryModel = new CategoryModel();
				categoryModel.setCat_id(object5.getInt("ProductID"));
				categoryModel.setCat_name_en(object5.getString("ProductTitleEn"));
				categoryModel.setCat_name_image(object5.getString("ProductImage"));
				categoryModel.setDescription(object5.getString("DescriptionEn"));
				categoryModel.setDescription(object5.getString("DescriptionFr"));
				categoryModel.setTxt_price(object5.getString("Price"));
				categoryModels.add(categoryModel);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		category_view = view.findViewById(R.id.category_view);
		mAdapter = new RecomadedAdapter(getActivity(),categoryModels, new RecomadedAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int item) {

				Intent intent = new Intent(getActivity(),GeneralPracticeActivity.class);
				intent.putExtra("ProductId",categoryModels.get(item).getCat_id());
				startActivity(intent);
			}
		});
		category_view.setHasFixedSize(true);
		category_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
		category_view.setAdapter(mAdapter);

		return view;
	}
}