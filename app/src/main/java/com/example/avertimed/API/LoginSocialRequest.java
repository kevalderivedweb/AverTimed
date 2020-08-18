package com.example.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class LoginSocialRequest extends StringRequest {
    private Map<String, String> parameters;

    public LoginSocialRequest(String method ,String FirstName,String LastName,String Email,String methodname,String method_id,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL +method, listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("FirstName", FirstName);
        parameters.put("LastName", LastName);
        parameters.put("Email", Email);
        parameters.put(methodname, method_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}