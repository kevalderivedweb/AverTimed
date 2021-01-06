package com.example.avertimed.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class RequestForQTRequest extends StringRequest {

    private Map<String, String> parameters;

    public RequestForQTRequest(String Name, String Email, String PhoneNo,String QT,String Message,String CategoryID,String SubCategoryId,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"request-for-quotation", listener, null);
        parameters = new HashMap<>();
       // parameters.put("OldPassword ", OldPassword );
        parameters.put("Name", Name);
        parameters.put("Email", Email);
        parameters.put("PhoneNo", PhoneNo);
        parameters.put("CategoryID", "1");
        parameters.put("SubCategoryID", "1");
        parameters.put("Quantity", QT);
        parameters.put("Message", Message);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
