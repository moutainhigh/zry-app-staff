package com.zhongmei.bty.customer.util;

import android.content.Context;
import android.util.Log;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.net.volley.Request.Method;
import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.Response.Listener;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SystemUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CustomerRequestManager {
    private static final String TAG = CustomerRequestManager.class.getSimpleName();

//	private static final String UPGRADE_MEMBER = "/CalmRouter/memberController/upgradeMember";

//	private static final String NEW_UPGRADE_MEMBER = "/CalmRouter/v1/member/upgradeMember";

    private static final String MEMBER_STATISTICS = "/CalmRouter/memberController/findMemberConsumeInfoForIpad";

//	private static final String INTEGRAL = "/CalmRouter/memberController/findMemberIntegral";

//	private static final String CUSTOMER_INFO = "/CalmRouter/memberController/findMemberInfoForAccountCoupons";

//	private static final String CHARGE_RECODE = "/CalmRouter/memberController/findMemberValuecard";

    // private static final String CHARGING =
    // "/CalmRouter/memberController/addMemberValuecard";
//	private static final String CHARGING = "/CalmRouter/memberController/v1/addMemberValuecard";

    private static CustomerRequestManager sCustomerRequestManager = new CustomerRequestManager();

    private CustomerRequestManager() {

    }

    public static CustomerRequestManager getInstance() {
        return sCustomerRequestManager;
    }

    public static JSONObject post(JSONObject json) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", json);
        jsonObject.put("versionCode", SystemUtils.getVersionCode());
        return jsonObject;
    }

    public static String genRequest(String path, String json) {
        try {
            Log.d("genRequest", json);
            return ShopInfoCfg.getInstance().getServerKey() + path + "?&content="
                    + URLEncoder.encode(json, "UTF-8") + "&versionCode=" + SystemUtils.getVersionCode()
                    + "&versionName=" + SystemUtils.getVersionName() + "&macAddress="
                    + SystemUtils.getMacAddress() + "&shopID="
                    + ShopInfoCfg.getInstance().shopId + "&deviceID="
                    + SystemUtils.getMacAddress();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }

//	public static String genChargingRequest(String path, String json) {
//		return MainApplication.getInstance().getShopInfo().get(ShopInfo.SERVER_KEY) + path + "?&content=" + json
//			+ "&versionCode=" + SystemUtils.getVersionCode() + "&versionName="
//			+ SystemUtils.getVersionName() + "&macAddress=" + SystemUtils.getMacAddress()
//			+ "&shopID=" + MainApplication.getInstance().getShopInfo().get(ShopInfo.ID_KEY);
//	}
//
//	public static String genChargingRequestDv(String path, String json) {
//		String encode = json;
//		try {
//			encode = URLEncoder.encode(json, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			Log.e(TAG,"",e);
//		}
//		return MainApplication.getInstance().getShopInfo().get(ShopInfo.SERVER_KEY) + path + "?&content=" + encode
//			+ "&versionCode=" + SystemUtils.getVersionCode() + "&versionName="
//			+ SystemUtils.getVersionName() + "&deviceID=" + SystemUtils.getMacAddress()
//			+ "&shopID=" + MainApplication.getInstance().getShopInfo().get(ShopInfo.ID_KEY);
//	}

    //change get to post by weixiong
    private void executeCustmerStringRequest(String urlHead, JSONObject json, String hinting,
                                             Listener<String> successListener, ErrorListener errorListener, Context fm) {

        String value = genRequest(urlHead, json.toString());
//		Log.i("executeCustmerStringRequest", value);
        CustomerCalmStringRequest request =
                new CustomerCalmStringRequest(Method.POST, value, successListener, errorListener);
        request.executeRequest("1", hinting, fm);
//		addRequest(request);
    }
	
	/*private void executeCustmerCharingStringRequest(String urlHead, JSONObject json, String hinting,
		Listener<String> successListener, ErrorListener errorListener, FragmentManager fm) {
		
		String value = genChargingRequestDv(urlHead, json.toString());
//		Log.i("executeCustmerCharingStringRequest", value);
		CustomerCalmStringRequest request =
			new CustomerCalmStringRequest(Method.GET, value, successListener, errorListener);
		request.executeRequest("1", hinting, fm);
//		addRequest(request);
	}*/

//	private void executeStringRequest(String urlHead, JSONObject json, String hinting,
//		Listener<String> successListener, ErrorListener errorListener, FragmentManager fm) {
//		String value = genRequest(urlHead, json.toString());
//		Log.e("login", value);
//		CalmStringRequest request = new CalmStringRequest(Method.GET, value, successListener, errorListener);
//		request.executeRequest("1", hinting, fm);
//		addRequest(request);
//	}
	
	/*public void charging(String customerId, String cashValue, String serverId, Listener<String> successListener,
		ErrorListener errorListener, FragmentManager fm) {
		
		JSONObject json = new JSONObject();
		try {
			json.put("memberId", customerId);
			json.put(ShopInfo.GROUP_ID_KEY, MainApplication.getInstance().getShopInfo().get(ShopInfo.GROUP_ID_KEY));
			json.put("bankValuecard", "0");
			json.put("cashValuecard", cashValue);
			json.put("userId", AuthUserCache.getAuthUser().getName());
			json.put("valuecard", cashValue);
			json.put("serverId", serverId);
			// json.put("deviceID",
			// SystemUtils.getMacAddress());
		} catch (Exception e) {
			Log.e(TAG,"",e);
		}
		
		Log.e("chargingJson", json.toString());
		executeCustmerCharingStringRequest(CHARGING,
			json,
			MainApplication.getInstance().getString(R.string.customer_transaction),
			successListener,
			errorListener,
			fm);
		
	}*/
	
	/*public void getChargeRecode(String customerId, Listener<String> successListener, ErrorListener errorListener,
		FragmentManager fm) {
		
		JSONObject json = new JSONObject();
		try {
			json.put("memberId", customerId);
			json.put("commercialGroupId", MainApplication.getInstance().getShopInfo().get(ShopInfo.GROUP_ID_KEY));
		} catch (Exception e) {
			Log.e(TAG,"",e);
		}
		
		executeCustmerStringRequest(CHARGE_RECODE,
			json,
			MainApplication.getInstance().getString(R.string.customer_load_charging),
			successListener,
			errorListener,
			fm);
	}*/

//	public void CustomerLogin(String phone, Listener<String> successListener, ErrorListener errorListener,
//		FragmentManager fm) {
//
//		JSONObject json = new JSONObject();
//		try {
//			json.put("commercialID", MainApplication.getInstance().getShopInfo().get(ShopInfo.ID_KEY));
//			json.put("phone", phone);
//			json.put("brandID", MainApplication.getInstance().getShopInfo().get(ShopInfo.GROUP_ID_KEY));
//		} catch (Exception e) {
//			Log.e(TAG,"",e);
//		}
//		executeStringRequest(CUSTOMER_INFO,
//			json,
//			MainApplication.getInstance().getString(R.string.customer_veryfing),
//			successListener,
//			errorListener,
//			fm);
//	}
	
	/*public void getIntegral(String serverId, Listener<String> successListener, ErrorListener errorListener,
		FragmentManager fm) {
		
		JSONObject json = new JSONObject();
		try {
			json.put("memberId", serverId);
			json.put("commercialGroupId", MainApplication.getInstance().getShopInfo().get(ShopInfo.GROUP_ID_KEY));
		} catch (Exception e) {
			Log.e(TAG,"",e);
		}
		executeCustmerStringRequest(INTEGRAL,
			json,
			MainApplication.getInstance().getString(R.string.customer_load_integer),
			successListener,
			errorListener,
			fm);
	}*/
	
	/*public void getCustomerInfo(String mPhone, Listener<String> successListener, ErrorListener errorListener,
		FragmentManager fm) {
		
		JSONObject json = new JSONObject();
		try {
			json.put("commercialID", MainApplication.getInstance().getShopInfo().get(ShopInfo.ID_KEY));
			json.put("phone", mPhone);
			json.put("brandID", MainApplication.getInstance().getShopInfo().get(ShopInfo.ID_KEY));
		} catch (Exception e) {
			Log.e(TAG,"",e);
		}
		
		executeCustmerStringRequest(CUSTOMER_INFO,
			json,
			MainApplication.getInstance().getString(R.string.customer_loading),
			successListener,
			errorListener,
			fm);
	}*/

    public void getMemberStatistics(String serverId, Listener<String> successListener, ErrorListener errorListener,
                                    Context fm) {

        JSONObject json = new JSONObject();
        try {
            json.put("memberId", serverId);
            json.put("commercialGroupId", ShopInfoCfg.getInstance().commercialGroupId);
            json.put("commercialId", ShopInfoCfg.getInstance().shopId);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }

        executeCustmerStringRequest(MEMBER_STATISTICS,
                json,
                MainApplication.getInstance().getString(R.string.customer_get_info),
                successListener,
                errorListener,
                fm);

    }
	
	/*public void upgradeToMember(Customer customer, String password, Listener<String> successListener,
		ErrorListener errorListener, FragmentManager fm) {
		
		JSONObject json = new JSONObject();
		try {
			json.put("phone", customer.get(Customer.MOBILE_KEY));
			json.put("customerSyncFlag", customer.get(Customer.SERVER_ID_KEY));
			json.put("sex", customer.get(Customer.SEX_KEY));
			json.put("birthday", DateTimeUtils.formatDate(customer.getLongObject(Customer.BIRTHDAY_KEY)));
			json.put("isAcceptSubscription", "");
			json.put("name", customer.get(Customer.NAME_KEY));
			json.put("userId", AuthUserCache.getAuthUser().getName());
			json.put("commercialGroupId", MainApplication.getInstance().getShopInfo().get(ShopInfo.GROUP_ID_KEY));
			json.put("memberCard", "");
			MD5 md5 = new MD5();
			json.put("password", md5.getMD5ofStr(password));
		} catch (Exception e) {
			Log.e(TAG,"",e);
		}
		
		// change UPGRADE_MEMBER to NEW_UPGRADE_MEMBER by weixiong
		executeCustmerStringRequest(NEW_UPGRADE_MEMBER,
			json,
			MainApplication.getInstance().getString(R.string.customer_transaction),
			successListener,
			errorListener,
			fm);
	}*/
	
	/*public void createCustomer(Customer customer, Listener<JSONObject> successListener, ErrorListener errorListener,
		FragmentManager fm) {
		
		String url = "/CalmRouter/v1/customer/create";
		
		customer.set(Customer.SERVER_ID_KEY, SystemUtils.genOnlyIdentifier());
		
		try {
			executeJsonRequest(url,
				customer.toJson(),
				MainApplication.getInstance().getString(R.string.customer_transaction),
				successListener,
				errorListener,
				fm);
		} catch (JSONException e) {
			Log.e(TAG,"",e);
		}
	}*/

//	public void updateCustomer(CustomerNew customer, Listener<JSONObject> successListener, ErrorListener errorListener,
//							   FragmentManager fm) {
//
//		UserActionEvent.start(UserActionEvent.CUSTOMER_EDIT);
//		String url = "/CalmRouter/v1/customer/update";
////		System.out.println("请求modifyDateTime:"+customer.get(Customer.LAST_SYNC_MARKER_KEY));
//		try {
//			executeJsonRequest(url,
//				customer.toJson(),
//				MainApplication.getInstance().getString(R.string.customer_transaction),
//				successListener,
//				errorListener,
//				fm);
//		} catch (JSONException e) {
//			Log.e(TAG,"",e);
//		}
//	}

//	protected void executeJsonRequest(String urlHead, JSONObject json, String hinting,
//		Listener<JSONObject> successListener, ErrorListener errorListener, FragmentManager fm) {
//		CalmRequest<?> request =
//			new CalmJsonRequest(Method.POST, MainApplication.getInstance().getShopInfo().get(ShopInfo.SERVER_KEY)
//				+ urlHead, json, successListener, errorListener);
//		Log.e("customerJson", json + "");
//		Log.d("urlHead", urlHead);
//		Log.d("executeJsonRequest", json.toString());
//		request.executeRequest("1", hinting, fm);
////		addRequest(request);
//	}
}
