package com.okokkid.wxapi;

import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXAPIWrapper {
    public static final String STATE_SIGNIN = "bytheway_signin";
    public static final String STATE_BIND = "bytheway_bind";

    public static final String APPID = "wx3da454b811f2b89d";
    public static final String SECRET = "wx3da454b811f2b89d";

    private static IWXAPI iwxapi;

    public static IWXAPI getWxapi(Context ctx) {
        if (iwxapi == null) {
            regToWx(ctx);
        }
        return iwxapi;
    }

    private static void regToWx(Context ctx) {
        iwxapi = WXAPIFactory.createWXAPI(ctx, APPID);
        iwxapi.registerApp(APPID);
    }
}
