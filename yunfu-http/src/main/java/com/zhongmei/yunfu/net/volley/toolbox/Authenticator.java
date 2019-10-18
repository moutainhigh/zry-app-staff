

package com.zhongmei.yunfu.net.volley.toolbox;

import com.zhongmei.yunfu.net.volley.AuthFailureError;


public interface Authenticator {

    public String getAuthToken() throws AuthFailureError;


    public void invalidateAuthToken(String authToken);
}
