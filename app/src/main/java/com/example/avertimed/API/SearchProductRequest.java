package com.example.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class SearchProductRequest extends StringRequest {

    private Map<String, String> parameters;

    public SearchProductRequest(String Search,String SubCategoryID,String FromPrice,String ToPrice,String SortByPrice, int page,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"search-products?page="+page, listener, null);
        parameters = new HashMap<>();
        parameters.put("Search", Search);
        parameters.put("SubCategoryID", SubCategoryID);
        parameters.put("FromPrice", FromPrice);
        parameters.put("ToPrice", ToPrice);
        parameters.put("SortByPrice", SortByPrice);

    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
