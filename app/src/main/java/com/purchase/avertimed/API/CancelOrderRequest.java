package com.purchase.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class CancelOrderRequest extends StringRequest {

    private Map<String, String> parameters;

    public CancelOrderRequest(String MethodName, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"cancel-order", listener, null);
        parameters = new HashMap<>();
        // parameters.put("OldPassword ", OldPassword );
        parameters.put("UserOrderID", MethodName);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
