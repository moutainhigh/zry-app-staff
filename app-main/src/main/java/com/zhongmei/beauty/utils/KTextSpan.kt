//package com.zhongmei.beauty.utils
//
//import android.graphics.MaskFilter
//import android.graphics.Rasterizer
//import android.graphics.drawable.Drawable
//import android.os.Parcel
//import android.os.Parcelable
//import android.text.style.*
//
///**
// * kotlin 实现spannableString
// *

// * @date 2018/6/23
// */
//class KTextSpan(val content: String) : Parcelable {
//    //所有样式
//    val styles = mutableListOf(mutableListOf<CharacterStyle>())
//    val textConstructor = mutableListOf<String>(content)
//
//    constructor(parcel: Parcel) : this(parcel.readString()) {
//    }
//
//    fun backgroundColor(colorVal: Int) {
//        styles[0].add(BackgroundColorSpan(colorVal))
//    }
//
//    fun foregroundColor(colorVal: Int) {
//        styles[0].add(ForegroundColorSpan(colorVal))
//    }
//
//    /**
//     * 模糊(BlurMaskFilter)、浮雕(EmbossMaskFilter) BlurMaskFilter
//     */
//    fun maskFilter(maskFilter: MaskFilter) {
//        styles[0].add(MaskFilterSpan(maskFilter))
//    }
//
//    /**
//     * 光栅效果 StrikethroughSpan()
//     */
//    fun rasterizer(rasterizer: Rasterizer) {
//        styles[0].add(RasterizerSpan(rasterizer))
//    }
//
//    /**
//     * 删除线（中划线）
//     */
//    fun strikethrough() {
//        styles[0].add(StrikethroughSpan())
//    }
//
//    /**     * 下划线     */
//    fun underline() {
//        styles[0].add(UnderlineSpan())
//    }
//
//    /**
//     * 设置图片 （DynamicDrawableSpan.ALIGN_BASELINE  or DynamicDrawableSpan.ALIGN_BOTTOM）
//     */
//    fun dynamicDrawable(drawable: Drawable, isAlignBaseLine: Boolean) {
//        styles[0].add(object : DynamicDrawableSpan(if (isAlignBaseLine) DynamicDrawableSpan.ALIGN_BASELINE else DynamicDrawableSpan.ALIGN_BOTTOM) {
//            override fun getDrawable(): Drawable {
//                drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
//                return drawable
//            }
//        })
//    }
//
//    /**
//     * 字体大小(像素)
//     */
//    fun absoluteSize(textSize: Int) {
//        styles[0].add(AbsoluteSizeSpan(textSize, false))
//    }
//
//    /**     * 图片     */
//    fun image(drawable: Drawable, width: Int = drawable.minimumWidth, height: Int = drawable.minimumHeight) {
//        drawable.setBounds(0, 0, width, height)
//        styles[0].add(ImageSpan(drawable))
//    }
//
//    /**
//     * ScaleXSpan 基于x轴缩放
//     */
//    fun scaleX(scaleRate: Float) {
//        styles[0].add(ScaleXSpan(scaleRate))
//    }
//
//    /**
//     *  相对大小（文本字体）
//     */
//    fun relativeSize(scanRate: Float) {
//        styles[0].add(RelativeSizeSpan(scanRate))
//    }
//
//    /**
//     *  字体样式：粗体、斜体等 Typeface
//     */
//    fun style(typeface: Int) {
//        styles[0].add(StyleSpan(typeface))
//    }
//
//    /**     * 下标（数学公式会用到）     */
//    fun subscript() {
//        styles[0].add(SubscriptSpan())
//    }
//
//    /**     * 上标（数学公式会用到）     */
//    fun superscript() {
//        styles[0].add(SuperscriptSpan())
//    }
//
//    /**     *  文本字体     */
//    fun typeface(typeface: String) {
//        styles[0].add(TypefaceSpan(typeface))
//    }
//
//    /**     * 文本超链接     */
//    fun url(linkAddress: String) {
//        styles[0].add(URLSpan(linkAddress))
//    }
//
//    operator fun plus(nextVal: KTextSpan): KTextSpan {
//        styles.addAll(nextVal.styles)
//        textConstructor.addAll(nextVal.textConstructor)
//        return this
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(content)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<KTextSpan> {
//        override fun createFromParcel(parcel: Parcel): KTextSpan {
//            return KTextSpan(parcel)
//        }
//
//        override fun newArray(size: Int): Array<KTextSpan?> {
//            return arrayOfNulls(size)
//        }
//    }
//}
