package com.purchase.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class HomeRequest extends StringRequest {

    private Map<String, String> parameters;

    public HomeRequest(Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, ServerUtils.BASE_URL+"get-home", listener, null);

    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
