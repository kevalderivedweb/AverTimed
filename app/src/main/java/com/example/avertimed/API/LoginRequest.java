package com.example.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class LoginRequest extends StringRequest {

    private Map<String, String> parameters;

    public LoginRequest(String Email, String Password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"login", listener, null);
        parameters = new HashMap<>();
        parameters.put("Email", Email);
        parameters.put("Password", Password);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
