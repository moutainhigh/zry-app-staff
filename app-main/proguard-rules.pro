-optimizationpasses 5
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes EnclosingMethod

-dontskipnonpubliclibraryclasses

-keep public class android.webkit.**
-dontwarn android.webkit.*
-keep class android.webkit.** { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class * {
  public <init>(android.content.Context);
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-dontwarn android.support.**

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-dontwarn com.flytech.**
#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#ARouter
-keep public class com.alibaba.android.arouter.routes.** { *; }
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe { *; }
-dontwarn com.alibaba.android.arouter.**

#okhttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

#libs jars
-keep class com.google.gson.** { *; }
-keep class com.qiniu.** { *; }
-keep class com.google.zxing.**{*;}
-keep class com.baoyz.swipemenulistview.**{*;}

-keep class com.pax.imglib.**{*;}
-keep class com.baidu.**{*;}
-keep class com.androidpos.sysapi.**{*;}
-dontwarn com.androidpos.sysapi.**
-keep class com.baidu.**{*;}
-keep class com.android.encryptcaculator.**{*;}
-keep class com.android.serialport.**{*;}
-keep class com.barcodeupdate.jni.**{*;}



-keep class org.apache.shiro.** { *; }
-dontwarn org.apache.shiro.**

#eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
-keep class de.greenrobot.event.**{*;}
-keepclassmembers class ** {
    public void onEvent*(**);
}

-keep class com.blueware.** { *; }
-dontwarn com.blueware.**

-dontwarn com.umeng.**

-keep class com.flytech.**{*;}
-dontwarn com.flytech.**

-keep public class com.squareup.**
-keep class com.squareup.**{*;}
-dontwarn com.squareup.**

-dontwarn org.springframework.**
#############################
-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep public class * extends android.support.v7.**
-keep interface android.support.v7.app.** { *; }
-dontwarn android.support.**

-keep class * extends java.lang.annotation.Annotation {*;}
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers enum * {
  public static **[] values();
 public static ** valueOf(java.lang.String);
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#ormlite混淆配置
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-keepclassmembers class * {
    @com.j256.ormlite.field.DatabaseField *;
}
#gson
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**
-keep class com.envy15.cherry.fragment.crossover.model.** { *; }
-dontwarn com.envy15.cherry.fragment.crossover.model.**
-keep class com.envy15.cherry.fragment.discover.model.** { *; }
-dontwarn com.envy15.cherry.fragment.discover.model.**
-keep class com.envy15.cherry.fragment.local.model.** { *; }
-dontwarn com.envy15.cherry.fragment.local.model.**
-keep class com.envy15.cherry.fragment.setting.model.** { *; }
-dontwarn com.envy15.cherry.fragment.setting.model.**

-keep class in.srain.cube.views.ptr.** { *; }
-dontwarn in.srain.cube.views.ptr.**
-keep class com.handmark.pulltorefresh.library.** { *; }
-dontwarn com.handmark.pulltorefresh.library.**

-keep class cn.wanda.ioclient.** { *; }
-dontwarn cn.wanda.ioclient.**

-keep class org.hamcrest.** { *; }
-dontwarn org.hamcrest.**

-keep class org.junit.** { *; }
-dontwarn org.junit.**

-dontwarn org.codehaus.**
-dontwarn java.nio.**
-dontwarn java.lang.invoke.**

-dontwarn org.mockito.**
-dontwarn sun.reflect.**
-dontwarn android.test.**
#annotation enum
-keep enum com.zhongmei.bty.baseservice.data.FieldType$** {
    *;
}
-keep enum com.zhongmei.bty.baseservice.data.DataBaseDescription$** {
    *;
}
#自定义
-keep public interface NoProGuard
-keep class * implements NoProGuard {
    *;
}
#网络请求及响应bean
-dontwarn com.zhongmei.bty.data.operates.message.content.**
-keep class com.zhongmei.bty.data.operates.message.content.** { *;}

-dontwarn com.zhongmei.bty.basemodule.auth.permission.message.**
-keep class com.zhongmei.bty.basemodule.auth.permission.message.** { *;}

-dontwarn com.zhongmei.bty.basemodule.autoset.message.**
-keep class com.zhongmei.bty.basemodule.autoset.message.** { *;}

-dontwarn com.zhongmei.bty.basemodule.booking.message.**
-keep class com.zhongmei.bty.basemodule.booking.message.** { *;}

-dontwarn com.zhongmei.bty.basemodule.commonbusiness.message.**
-keep class com.zhongmei.bty.basemodule.commonbusiness.message.** { *;}

-dontwarn com.zhongmei.bty.basemodule.customer.message.**
-keep class com.zhongmei.bty.basemodule.customer.message.** { *;}

-dontwarn com.zhongmei.bty.basemodule.devices.mispos.data.message.**
-keep class com.zhongmei.bty.basemodule.devices.mispos.data.message.** { *;}

-dontwarn com.zhongmei.bty.basemodule.discount.message.**
-keep class com.zhongmei.bty.basemodule.discount.message.** { *;}

-dontwarn com.zhongmei.bty.basemodule.inventory.message.**
-keep class com.zhongmei.bty.basemodule.inventory.message.** { *;}

-dontwarn com.zhongmei.bty.basemodule.orderdish.message.**
-keep class com.zhongmei.bty.basemodule.orderdish.message.** { *;}

-dontwarn com.zhongmei.bty.basemodule.pay.message.**
-keep class com.zhongmei.bty.basemodule.pay.message.** { *;}

-dontwarn com.zhongmei.bty.basemodule.print.message.**
-keep class com.zhongmei.bty.basemodule.print.message.** { *;}

-dontwarn com.zhongmei.bty.basemodule.reportcenter.message.**
-keep class com.zhongmei.bty.basemodule.reportcenter.message.** { *;}

-dontwarn com.zhongmei.bty.basemodule.shopmanager.closing.message.**
-keep class com.zhongmei.bty.basemodule.shopmanager.closing.message.** { *;}

-dontwarn com.zhongmei.bty.basemodule.shopmanager.handover.data.**
-keep class com.zhongmei.bty.basemodule.shopmanager.handover.data.** { *;}

-dontwarn com.zhongmei.bty.basemodule.shopmanager.message.**
-keep class com.zhongmei.bty.basemodule.shopmanager.message.** { *;}

-dontwarn com.zhongmei.bty.basemodule.shopmanager.paymentmanager.operators.**
-keep class com.zhongmei.bty.basemodule.shopmanager.paymentmanager.operators.** { *;}

-dontwarn com.zhongmei.bty.basemodule.trade.message.**
-keep class com.zhongmei.bty.basemodule.trade.message.** { *;}

-dontwarn com.zhongmei.bty.thirdplatform.forum.bean.**
-keep class com.zhongmei.bty.thirdplatform.forum.bean.** { *;}

-dontwarn com.zhongmei.bty.commonmodule.data.operate.content.**
-keep class com.zhongmei.bty.commonmodule.data.operate.content.** { *;}

-keepclassmembers class * {
    @com.zhongmei.bty.sync.message.SyncItemMap *;
}