package com.commonlib.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.commonlib.widget.MultiShapeView;

public class ImageLoader {
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    private ImageLoader() {
    }


    /**
     * 将资源图片转化为Uri
     *
     * @param context
     * @param resourceId
     * @return
     */
    public static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }

    /**
     * 加载网络图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param placeholder
     */
    public static void loadUrlImage(Context context, String url, ImageView imageView, Drawable placeholder) {
        Glide.with(context)
                .load(url)
                .placeholder(placeholder)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载drawable图片
     *
     * @param context
     * @param resId       资源ID
     * @param imageView
     * @param placeholder 占位图
     */
    public static void loadResImage(Context context, int resId, ImageView imageView, Drawable placeholder) {
        Glide.with(context)
                .load(resourceIdToUri(context, resId))
                .placeholder(placeholder)
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载drawable图片
     *
     * @param context
     * @param resId       资源ID
     * @param imageView
     * @param placeholder 占位图
     */
    public static void loadResImage(Context context, int resId, ImageView imageView, int placeholder) {
        Glide.with(context)
                .load(resourceIdToUri(context, resId))
                .placeholder(placeholder)
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载本地图片
     *
     * @param context
     * @param path
     * @param imageView
     * @param placeholder
     */
    public static void loadLocalImage(Context context, String path, ImageView imageView, Drawable placeholder) {
        Glide.with(context)
                .load("file://" + path)
                .placeholder(placeholder)
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载本地图片
     *
     * @param context
     * @param path
     * @param imageView
     * @param placeholder
     */
    public static void loadLocalImage(Context context, String path, ImageView imageView, int placeholder) {
        Glide.with(context)
                .load("file://" + path)
                .placeholder(placeholder)
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载网络圆型图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param placeholder
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView, Drawable placeholder) {
        Glide.with(context)
                .load(url)
                .placeholder(placeholder)
                .crossFade()
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    /**
     * 加载drawable圆型图片
     *
     * @param context
     * @param resId
     * @param imageView
     * @param placeholder
     */
    public static void loadCircleResImage(Context context, int resId, ImageView imageView, Drawable placeholder) {
        Glide.with(context)
                .load(resourceIdToUri(context, resId))
                .placeholder(placeholder)
                .crossFade()
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    /**
     * 加载本地圆型图片
     *
     * @param context
     * @param path
     * @param imageView
     * @param placeholder
     */
    public void loadCircleLocalImage(Context context, String path, ImageView imageView, Drawable placeholder) {
        Glide.with(context)
                .load("file://" + path)
                .placeholder(placeholder)
                .crossFade()
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    public static void loadProcessImage(Context context, String url, final ImageView imageView, final int placeHolder, final int failRes) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .fitCenter()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        imageView.setImageResource(placeHolder);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        imageView.setImageResource(failRes);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                    }
                });
    }

    /**
     * @param context
     * @param url
     * @param imageView
     */
    public static void setImageBitmap(Context context, String url, final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                    }
                });
    }

    /**
     * @param context
     * @param url
     * @param imageView
     */
    public static void setShapeViewBitmap(Context context, String url, final MultiShapeView imageView, final Drawable startPlaceHolder) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        imageView.setImageDrawable(startPlaceHolder);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                    }
                });
    }


}
