package com.zhongmei.bty.basemodule.devices.display.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;

/**
 * 第二屏界面显示工具类
 *
 * @date:2015年10月14日下午6:03:45
 */
public class DisplayServiceManager {
    private static final String TAG = DisplayServiceManager.class.getSimpleName();
    //默认语言
    private static String mLanguage = "";

    public static void startService(Context context) {
		/*if(!DisplayServiceTool.isHighSystemVersion()){
			return;
		}
        mLanguage = context.getSharedPreferences(Constant.SP_FILE_NAME,
                Context.MODE_PRIVATE).getString(Constant.DEFAULTLANGUAGE,"");
		DisplayServiceTool.startService(context);*/
    }

    /**
     * 更新显示
     *
     * @Title: updateDisplay
     * @Description: TODO
     * @Param @param context
     * @Param @param payMessage TODO
     * @Return void 返回类型
     */
    public synchronized static void updateDisplay(Context context, final Parcelable object) {
        //DisplayServiceTool.updateDisplay(context,object,mLanguage);
    }


    /**
     * 界面隐藏 回到动画界面
     *
     * @Title: doCancel
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    public static void doCancel(Context context) {
        //DisplayServiceTool.doCancel(context);
    }

    public static void doDelayPause(Context context) {
        //DisplayServiceTool.doDelayPause(context);
    }

    /**
     * 每种收银方式 应付金额
     *
     * @Title: updateDisplayPay
     * @Description: TODO
     * @Param @param context
     * @Param @param amount TODO
     * @Return void 返回类型
     */
    public static void updateDisplayPay(Context context, Double amount) {
		/*String message = ShopInfoCfg.formatCash(amount);
		PayMessage mPayMessage = DisplayServiceManager.buildPayMessage(PayMessage.PAY_STATE_CASH, message);
		DisplayServiceManager.updateDisplay(context, mPayMessage);*/
    }

    /**
     * 构建paymessage对象
     *

     * @Title: buildPayMessage
     * @Description: TODO
     * @Param @param payState
     * @Param @param message
     * @Param @return TODO
     * @Return PayMessage 返回类型
     */
	/*public static PayMessage buildPayMessage(int command, String message) {
		PayMessage mPayMessage = new PayMessage();
		mPayMessage.setCommand(command);
		mPayMessage.setMessage(message);
		return mPayMessage;
	}*/

    /**
     * 构建用户信息展示类
     *

     * @Title: buildDUserInfo
     * @Description: TODO
     * @Param @param command
     * @Param @return TODO
     * @Return DisplayUserInfo 返回类型
     */
	/*public static DisplayUserInfo buildDUserInfo(int command, String input, CustomerNew customer, long integral, boolean isFirst, double payAmount) {
		DisplayUserInfo userInfo = new DisplayUserInfo();
		userInfo.setCommand(command);
		userInfo.setFirst(isFirst);
		if (command == DisplayUserInfo.COMMAND_ACCOUNT_INPUT) {
			userInfo.setAccount(input);
		} else if (command == DisplayUserInfo.COMMAND_PASSWORD_INPUT) {
			userInfo.setPassword(input);
		} else if (command == DisplayUserInfo.COMMAND_USERINFO_SHOW) {
			userInfo.setIntegral(String.valueOf(integral));
			if (customer != null) {
				if(customer.card!=null){
					return buildDUserInfo(command,customer.card,integral,true,payAmount);
				}
				userInfo.setCustomerName(customer.customerName);
				if(customer.getCustomerLevel()!=null)
					userInfo.setLevel(customer.getCustomerLevel().getLevelName());
			}
			userInfo.setPayAmount(payAmount);
			if (TextUtils.isEmpty(userInfo.getCustomerName())) {
				userInfo.setCustomerName(BaseApplication.sInstance.getString(R.string.display_user_no_name));
			}
			if (TextUtils.isEmpty(userInfo.getLevel())) {
				Resources res = BaseApplication.sInstance.getResources();
				userInfo.setLevel(res.getString(R.string.display_nothing));
			}
		} else {
			userInfo.setCommand(command);
		}

		return userInfo;
	}*/

	/*public static DisplayUserInfo buildDUserInfo(int command, CustomerNew customer, long integral, boolean isShowMoney, double payAmount) {
		DisplayUserInfo userInfo = new DisplayUserInfo();
		userInfo.setCommand(command);
		if (command == DisplayUserInfo.COMMAND_USERINFO_SHOW) {
			Resources res = BaseApplication.sInstance.getResources();
			userInfo.setIntegral(String.valueOf(integral));
			if (customer != null) {
				userInfo.setCustomerName(customer.customerName);
				if(customer.getCustomerLevel()!=null)
					userInfo.setLevel(customer.getCustomerLevel().getLevelName());
			} else {
				userInfo.setCustomerName(res.getString(R.string.display_user_no_name));
			}
			if(TextUtils.isEmpty(userInfo.getLevel())){
				userInfo.setLevel(res.getString(R.string.display_nothing));
			}
			if(isShowMoney){
				userInfo.setPayAmount(payAmount);
			}else{
				userInfo.setPayAmount(-1);
			}
		} else {
			userInfo.setCommand(command);
		}

		return userInfo;
	}*/

	/*public static DisplayUserInfo buildDUserInfo(int command, EcCard card, long integral,boolean isShowMoney,double payAmount) {
		DisplayUserInfo userInfo = new DisplayUserInfo();
		userInfo.setCommand(command);
		if (command == DisplayUserInfo.COMMAND_USERINFO_SHOW) {

			userInfo.setIntegral(String.valueOf(integral));
			if (card != null) {
				Resources res = BaseApplication.sInstance.getResources();
				userInfo.setCustomerName(card.getName());
				userInfo.setLevel(res.getString(R.string.display_nothing));
			} else {
				userInfo.setCustomerName(BaseApplication.sInstance.getString(R.string.display_user_no_name));
			}
			if(isShowMoney){
				userInfo.setPayAmount(payAmount);
			}else{
				userInfo.setPayAmount(-1);
			}
		} else {
			userInfo.setCommand(command);
		}

		return userInfo;
	}*/

//	/**
//	 *

//	 * @Title: updateCashUserInfo
//	 * @Description: 更新收银模块的用户信息
//	 * @Param  isShowMoney:是否显示金额
//	 * @Return void 返回类型
//	 */
//	public static void updateCashUserInfo(Context context,boolean isShowMoney,CashInfoManager cashInfo){
//		DisplayUserInfo dUserInfo =null;
//		long integral=cashInfo.getMemberIntegral();
//		if(cashInfo.getCustomer()!=null){
//			dUserInfo=DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, cashInfo
//				.getCustomer(), integral,isShowMoney,cashInfo.getActualAmount());
//		}else if(cashInfo.getEcCard()!=null){
//			dUserInfo=DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW,cashInfo
//				.getEcCard(), integral,isShowMoney,cashInfo.getActualAmount());
//		}
//		if(dUserInfo!=null){
//			DisplayServiceManager.updateDisplay(context, dUserInfo);
//		}
//	}

	/*public static void updateCashUserInfo(Context context,boolean isShowMoney,IPaymentInfo cashInfo){
		DisplayUserInfo dUserInfo =null;
		long integral=cashInfo.getMemberIntegral();
		if(cashInfo.getCustomer()!=null){
//			dUserInfo=DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, cashInfo
//					.getCustomer(), integral,isShowMoney,cashInfo.getActualAmount());
			dUserInfo= buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, cashInfo.getCustomer(),
					integral,isShowMoney,cashInfo.getActualAmount());
		}else if(cashInfo.getEcCard()!=null){
			dUserInfo= buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW,cashInfo
					.getEcCard(), integral,isShowMoney,cashInfo.getActualAmount());
		}
		if(dUserInfo!=null){
			DisplayServiceManager.updateDisplay(context, dUserInfo);
		}
	}*/

    /**
     * 构建二维码显示相关信息
     *

     * @Title: buildDBarcode
     * @Description: TODO
     * @Param @param command
     * @Param @return TODO
     * @Return DisplayBarcode 返回类型
     */
	/*public static DisplayBarcode buildDBarcode(int payWay, String amount, Bitmap bitmap, long payModeId) {
		DisplayBarcode dBarcode = new DisplayBarcode();
		if (payWay == 0) {
			dBarcode.setCommand(DisplayBarcode.DISPLAY_BARCODE_SACN);
		} else if (payWay == 1) {
			dBarcode.setCommand(DisplayBarcode.DISPLAY_BARCODE_DEFAULT);
		} else if(payWay==2){
			dBarcode.setCommand(DisplayBarcode.DISPLAY_BARCODE_CFAIL);
		}else{
			dBarcode.setCommand(DisplayBarcode.DISPLAY_BARCODE_PAYING);
		}
		dBarcode.setBitmap(bitmap);
		if (payModeId == PayModeId.JIN_CHENG.value()) {
			dBarcode.setIconRes(R.drawable.jin_cheng_pay_icon);
			dBarcode.setMessage(amount);
		} else {
			dBarcode.setMessage(ShopInfoCfg.formatCurrencySymbol(" " + amount));
		}
		if (payModeId == PayModeId.WEIXIN_PAY.value()) {
			dBarcode.setPayType(PayMessage.PAY_TYPE_WEIXIN);
			dBarcode.setLabel(BaseApplication.sInstance.getString(R.string.display_wechat_pay_label));
		} else if (payModeId == PayModeId.BAIFUBAO.value()) {
			dBarcode.setPayType(PayMessage.PAY_TYPE_BAIFUBAO);
			dBarcode.setLabel(BaseApplication.sInstance.getString(R.string.display_baidu_takout_pay_label));
		} else if (payModeId == PayModeId.ALIPAY.value()) {
			dBarcode.setPayType(PayMessage.PAY_TYPE_ALIPAY);
			dBarcode.setLabel(BaseApplication.sInstance.getString(R.string.display_alipay_pay_label));
		}else if(payModeId== PayModeId.DIANPING_FASTPAY.value()){
			dBarcode.setLabel(BaseApplication.sInstance.getString(R.string.display_dianping_pay_label));
		} else if (payModeId == PayModeId.JIN_CHENG.value()) {
			dBarcode.setLabel(BaseApplication.sInstance.getString(R.string.display_jin_cheng_pay_label));
		} else if (payModeId == PayModeId.MEMBER_CARD.value()) {
			dBarcode.setLabel(BaseApplication.sInstance.getString(R.string.display_member_card_pay_label));
		} else if (payModeId == PayModeId.MOBILE_PAY.value()) {
			dBarcode.setLabel(BaseApplication.sInstance.getString(R.string.display_mobile_pay_label));
		}
		return dBarcode;
	}*/

    /**
     * 更新排队数据
     *
     * @Title: doUpdateQueue
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    public static void doUpdateQueue(Context context) {
        doUpdateQueue(context, false);
    }

    public static void doUpdateQueue(Context context, boolean isClear) {
		/*DisplayQueue command = new DisplayQueue();
		command.setClear(isClear);
		updateDisplay(context, command);*/
    }

    public static void doShowQueueCallNo(Context context, String no, int command) {
		/*DisplayOnCallNo comd = new DisplayOnCallNo();
		comd.setNo(no);
		comd.setCommand(command);
		updateDisplay(context, comd);*/
    }


    /**
     * 发送初始化完成后的广播，通知第二屏查询图片，针对数据库未初始化的情形
     *
     * @param context
     */
    public static void sendBootBroadCast(Context context) {
		/*Intent intent = new Intent(PayMessage.DISPLAY_BOOT_ACTION);
		context.sendBroadcast(intent);*/
    }

    /**
     * 抹零 抹分时更新界面 update sencond screen
     *

     * @Title: doUpdateScreen
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
	/*public static void doUpdateScreen(Context context, PayMethodItem mCurrentMethod, IPaymentInfo cashInfo) {
		CustomerNew customer = cashInfo.getCustomer();
		if (customer != null) {
			DisplayUserInfo dUserInfo =
					buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW,
							customer,
							cashInfo.getMemberIntegral(), true, cashInfo.getActualAmount());
			DisplayServiceManager.updateDisplay(context, dUserInfo);
		} else {
			if (mCurrentMethod != null && mCurrentMethod.payModelGroup == PayModelGroup.VALUE_CARD) {
				DisplayUserInfo dUserInfo =
						DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_ACCOUNT_INPUT, "", null, 0, true, cashInfo.getActualAmount());
				DisplayServiceManager.updateDisplay(context, dUserInfo);
			} else {
				DisplayServiceManager.updateDisplayPay(context, cashInfo.getActualAmount());
			}
		}
	}*/

    /**
     * @Title: doUpdateUnionPay
     * @Description: 更新银联支付信息
     * @Param @param context
     * @Param @param command
     * @Param @param message
     * @Param @param payInfo 支付价格信息
     * @Return void 返回类型
     */
    public static void doUpdateUnionPay(Context context, int command, String message, String payInfo) {
		/*DisplayPayUnion unionPay = new DisplayPayUnion();
		unionPay.setCommand(command);
		unionPay.setMessage(message);
		unionPay.setPayInfo(payInfo);
		updateDisplay(context, unionPay);*/
    }


    /**
     * @Title: doUpdateUnionPay
     * @Description: TODO
     * @Param @param context
     * @Param @param command
     * @Param @param message
     * @Param @param payInfo
     * @Param @param isShowAnim 是否显示动画
     * @Return void 返回类型
     */
    public static void doUpdateUnionPay(Context context, int command, String message, String payInfo, boolean isShowAnim) {
		/*DisplayPayUnion unionPay = new DisplayPayUnion();
		unionPay.setCommand(command);
		unionPay.setMessage(message);
		unionPay.setPayInfo(payInfo);
		unionPay.setShowAnim(isShowAnim);
		updateDisplay(context, unionPay);*/
    }

    /**
     * @Title: doUpdateRecharge 充值确认框
     * @Description: TODO
     * @Param @param context
     * @Param @param command
     * @Param @param rechargeAmount
     * @Param @param memberName
     * @Return void 返回类型
     */
    public static void doUpdateRecharge(Context context, int command, String rechargeAmount, String memberName) {
		/*DisplayRecharge recharge=new DisplayRecharge();
		recharge.setCommand(command);
		if(memberName==null){
			memberName="";
		}
		recharge.setMemberName(memberName);
		recharge.setRechargeAmount(rechargeAmount);
		updateDisplay(context, recharge);*/
    }

    public static void doUpdateLoginInfo(Context context, int comm, String phone, Bitmap bitmap, boolean isRegister) {
		/*DisPlayLoginInfo disPlayLoginInfo = new DisPlayLoginInfo();
		disPlayLoginInfo.setBitmap(bitmap);
		disPlayLoginInfo.setPhone(phone);
		disPlayLoginInfo.setRegister(isRegister);
		disPlayLoginInfo.setCommand(comm);
		updateDisplay(context, disPlayLoginInfo);*/
    }

    //add 20180201 start
	/*public static DisplayBarcode buildDBarcode(int command) {
		DisplayBarcode dBarcode = new DisplayBarcode();
		dBarcode.setCommand(command);
		return dBarcode;
	}*/

    public static void startInnerScanner(Context context) {
		/*DisplayBarcode startCommd = buildDBarcode(DisplayBarcode.DISPLAY_INNER_SCANNER_START);
		updateDisplay(context, startCommd);*/
    }

    public static void stopInnerScanner(Context context) {
		/*DisplayBarcode stopCommd = buildDBarcode(DisplayBarcode.DISPLAY_INNER_SCANNER_STOP);
		updateDisplay(context, stopCommd);*/
    }
    //add 20180201 end
}
