package com.okokkid.ui.index;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.okokkid.R;
import com.okokkid.base.BaseMainFragment;
import com.okokkid.ui.sign.RegisterActivity;
import com.okokkid.ui.sign.SignInActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author： xuyafan
 * description:
 */
public class ThirdFragment extends BaseMainFragment {


    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_signIn)
    Button btnSignIn;


    public static ThirdFragment newInstance() {
        Bundle args = new Bundle();
        ThirdFragment fragment = new ThirdFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Object setLayout() {
        return R.layout.fragment_third;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }


    @OnClick({R.id.btn_register, R.id.btn_signIn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                break;
            case R.id.btn_signIn:
                startActivity(new Intent(getActivity(), SignInActivity.class));
                break;
        }
    }
}
