package com.okokkid.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.okokkid.event.WXEvent;
import com.okokkid.util.Log;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.greenrobot.eventbus.EventBus;

/**
 * author： xuyafan
 * description:
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WXAPIWrapper.getWxapi(this).handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        WXAPIWrapper.getWxapi(this).handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq arg0) {
        Log.i("WXEntryActivity onReq:" + arg0);
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {//分享
            Log.i("ansen", "微信分享操作.....");
            WXEvent weiXin = new WXEvent(2, resp.errCode, "");
            EventBus.getDefault().post(weiXin);
        } else if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {//登陆
            Log.i("ansen", "微信登录操作.....");
            SendAuth.Resp authResp = (SendAuth.Resp) resp;
            WXEvent weiXin = new WXEvent(1, resp.errCode, authResp.code);
            EventBus.getDefault().post(weiXin);
        }
        finish();
    }
}
