package com.purchase.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class NewPasswordRequest extends StringRequest {

    private Map<String, String> parameters;

    public NewPasswordRequest(String Email,String NewPassword,String ConfirmPassword, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"new-password", listener, null);
        parameters = new HashMap<>();
        parameters.put("Email", Email);
        parameters.put("NewPassword", NewPassword);
        parameters.put("ConfirmPassword", ConfirmPassword);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
