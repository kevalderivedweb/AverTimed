package com.example.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class RegistrationRequest extends StringRequest {

    private Map<String, String> parameters;

    public RegistrationRequest(String FirstName, String LastName, String Email, String Password, String ConfirmPassword, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"register", listener, null);
        parameters = new HashMap<>();
        parameters.put("FirstName", FirstName);
        parameters.put("LastName", LastName);
        parameters.put("Email", Email);
        parameters.put("Password", Password);
        parameters.put("ConfirmPassword", ConfirmPassword);
        parameters.put("UserType", "Client");

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
