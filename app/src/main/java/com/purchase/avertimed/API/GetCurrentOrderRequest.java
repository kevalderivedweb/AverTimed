package com.purchase.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class GetCurrentOrderRequest extends StringRequest {

    private Map<String, String> parameters;

    public GetCurrentOrderRequest(String MethodName,int page,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, ServerUtils.BASE_URL+MethodName+"?page="+page, listener, null);

    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
