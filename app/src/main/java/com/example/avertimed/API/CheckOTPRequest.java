package com.example.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class CheckOTPRequest extends StringRequest {

    private Map<String, String> parameters;

    public CheckOTPRequest(String Email,String OTP, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"check-otp", listener, null);
        parameters = new HashMap<>();
        parameters.put("Email", Email);
        parameters.put("OTP", OTP);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
