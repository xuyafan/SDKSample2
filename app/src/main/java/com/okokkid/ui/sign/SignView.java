package com.okokkid.ui.sign;

import com.okokkid.base.BaseView;

/**
 * author： xuyafan
 * description:
 */
public interface SignView extends BaseView {
    void checkExistSucc(String response);

    void sendSmsCodeSucc(String response);

    void registerSucc(String response);

    void loginInSucc(String response);
}
