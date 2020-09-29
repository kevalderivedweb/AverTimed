package com.example.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class GetShippingAddressRequest extends StringRequest {

    private Map<String, String> parameters;

    public GetShippingAddressRequest(Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, ServerUtils.BASE_URL+"get-shipping-address", listener, null);

    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
