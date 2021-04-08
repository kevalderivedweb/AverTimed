package com.purchase.avertimed.API;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.purchase.avertimed.FavDatabaseModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class PlaceOrderRequest extends StringRequest {


    private Map<String, String> parameters;

    public PlaceOrderRequest(List<FavDatabaseModel> databaseModels, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"get-place-order", listener, null);
        parameters = new HashMap<>();


        for(int i = 0; i<databaseModels.size(); i++){
            Log.e("Product---Qt",databaseModels.get(i).getId()+"---"+databaseModels.get(i).getQT());
            parameters.put("ProductID[]"+i, databaseModels.get(i).getId());
            parameters.put("Qty[]"+i, databaseModels.get(i).getQT());
        }



    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {

        Log.e("Params",""+parameters);
        return parameters;
    }

}
