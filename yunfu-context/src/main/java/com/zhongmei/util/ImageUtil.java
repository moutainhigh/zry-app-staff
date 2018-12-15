package com.zhongmei.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {
    private Bitmap bitmap;

    public static void resize(Bitmap bitmap, File outputFile, int maxWidth, int maxHeight) {
        try {
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            if (bitmapWidth > maxHeight || bitmapHeight > maxWidth) {
                float widthScale = (float) maxWidth * 1.0F / (float) bitmapWidth;
                float heightScale = (float) maxHeight * 1.0F / (float) bitmapHeight;
                float scale = Math.min(widthScale, heightScale);
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
            }

            FileOutputStream out = new FileOutputStream(outputFile);

            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            } catch (Exception var19) {
                var19.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (Exception var18) {
                    var18.printStackTrace();
                }

            }
        } catch (IOException var21) {
            var21.printStackTrace();
        }

    }

    private ImageUtil(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static ImageUtil setBitmap(Bitmap bitmap) {
        return new ImageUtil(bitmap);
    }

    public ImageUtil setAlpha(int alpha) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(alpha);
        Bitmap newBitmap = Bitmap.createBitmap(this.bitmap.getWidth(), this.bitmap.getHeight(), this.bitmap.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(this.bitmap, 0.0F, 0.0F, paint);
        this.bitmap = newBitmap;
        return this;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public Drawable getDrawable() {
        return new BitmapDrawable(this.bitmap);
    }

    public ImageUtil.StateListDrawableWrapper createStateListDrawable() {
        ImageUtil.StateListDrawableWrapper stateListDrawableWrapper = new ImageUtil.StateListDrawableWrapper(this);
        return stateListDrawableWrapper;
    }

    public class StateListDrawableWrapper {
        private ImageUtil imageUtil;
        private StateListDrawable stateListDrawable;

        public StateListDrawableWrapper(ImageUtil imageUtil) {
            this.imageUtil = imageUtil;
            this.stateListDrawable = new StateListDrawable();
        }

        public ImageUtil.StateListDrawableWrapper addState(int[] stateSet, Drawable drawable) {
            this.stateListDrawable.addState(stateSet, drawable);
            return this;
        }

        public ImageUtil.StateListDrawableWrapper setStatePressedAlpha(int alpha) {
            this.addState(new int[]{16842919}, ImageUtil.setBitmap(this.imageUtil.bitmap).setAlpha(alpha).getDrawable());
            this.addState(new int[]{0}, this.imageUtil.getDrawable());
            return this;
        }

        public Drawable getDrawable() {
            return this.stateListDrawable;
        }
    }
}
