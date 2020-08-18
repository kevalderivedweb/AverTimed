package com.example.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class ChangePasswordRequest extends StringRequest {

    private Map<String, String> parameters;

    public ChangePasswordRequest(String OldPassword,String Password,String ConfirmPassword, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"change-password", listener, null);
        parameters = new HashMap<>();
       // parameters.put("OldPassword ", OldPassword );
        parameters.put("Password", Password);
        parameters.put("ConfirmPassword", ConfirmPassword);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
