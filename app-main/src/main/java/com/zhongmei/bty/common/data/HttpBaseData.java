package com.zhongmei.bty.common.data;

import org.json.JSONObject;

public interface HttpBaseData<T> {

    public JSONObject toJson();

    public T toObject(JSONObject response) throws Exception;

    public T toObject(String response) throws Exception;
}
