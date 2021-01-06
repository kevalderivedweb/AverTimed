package com.example.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class ProductDetailsRequest extends StringRequest {

    private Map<String, String> parameters;

    public ProductDetailsRequest(String ProductId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"get-product-details", listener, null);
        parameters = new HashMap<>();
        parameters.put("ProductID", ProductId);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
