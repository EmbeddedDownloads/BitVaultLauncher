package com.bitvault.launcher.Control;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Application Control
 */
public class AppController extends Application {
    /**
     * Debuggable TAG
     */
    public static final String TAG = AppController.class
            .getSimpleName();
    /**
     * AppController reference object
     */
    private static AppController mInstance;
    /**
     * RequestQueue reference object
     */
    private RequestQueue mRequestQueue, serialRequestQueue;
    /**
     * ImageLoader reference object
     */
    private ImageLoader mImageLoader;
    int MAX_SERIAL_THREAD_POOL_SIZE = 1;
    final int MAX_CACHE_SIZE = 2 * 1024 * 1024; // 2 MB
    private static Context context;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static Context getContextInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }
        initImageLoader(getApplicationContext());
        mInstance = this;
        context = this;
    }


    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());//,new HurlStack(null, ClientSSLSocketFactory.getSocketFactory()));
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(this.mRequestQueue,
//                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Use to fetch the serial request queue
     */
    public RequestQueue getSerialRequestQueue() {
        if (serialRequestQueue == null) {
            serialRequestQueue = prepareSerialRequestQueue();
            serialRequestQueue.start();
        }
        return serialRequestQueue;
    }

    private RequestQueue prepareSerialRequestQueue() {
        Cache cache = new DiskBasedCache(getApplicationContext().getCacheDir(),
                MAX_CACHE_SIZE);
        Network network = getNetwork();
        return new RequestQueue(cache, network, MAX_SERIAL_THREAD_POOL_SIZE);
    }


    private Network getNetwork() {
//        String userAgent = "volley/0";

        return new BasicNetwork(new HurlStack());
    }

    public <T> void addToArrayRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getSerialRequestQueue().add(req);
    }
}
