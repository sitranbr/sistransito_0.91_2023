package net.sistransito.mobile.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.rey.material.widget.TextView;
import net.sistransito.R;
import net.sistransito.mobile.appconstants.AppConstants;
import net.sistransito.mobile.appobject.AppObject;
import net.sistransito.mobile.fragment.AnyPageChangeListener;
import net.sistransito.mobile.main.MainUserActivity;
import net.sistransito.mobile.network.NetworkConnection;
import net.sistransito.mobile.utility.AESEncryption;
import net.sistransito.mobile.utility.DialogMaterial;
import net.sistransito.mobile.utility.Routine;
import net.sistransito.mobile.utility.User;
import net.sistransito.mobile.viewmodel.LoginViewModel;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private AnyPageChangeListener anyPageChangeListener;
    private LoginViewModel loginViewModel;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView btnRegister, btnForgetPassword;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        anyPageChangeListener = (ActivityLogin) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        btnRegister = view.findViewById(R.id.btn_register);
        btnForgetPassword = view.findViewById(R.id.btn_forget_passsword);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnForgetPassword.setOnClickListener(this);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        setupObservers();
        return view;
    }

    private void setupObservers() {
        loginViewModel.getLoginResponse().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getSuccess() == 1) {
                saveLoginData(response);
                Intent intent = new Intent(getActivity(), MainUserActivity.class);
                startActivity(intent);
                getActivity().finish();
            } else {
                showLoginFailMgs();
            }
        });

        loginViewModel.getHasError().observe(getViewLifecycleOwner(), hasError -> {
            if (hasError) {
                showLoginFailMgs();
            }
        });
    }

    private void saveLoginData(User user) {
        String dataEncrypt = null;
        try {
            dataEncrypt = AESEncryption.encrypt(etPassword.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        LoginData userPassword = new LoginData();
        userPassword.setPassword(dataEncrypt);

        AppObject.getTinyDB(getActivity()).putBoolean(AppConstants.isLogin, true);
        AppObject.getTinyDB(getActivity()).putObject(AppConstants.user, user);
    }

    private void showLoginFailMgs() {
        DialogMaterial.getBottomSheet(getResources().getString(R.string.login_fail), Color.RED, getActivity()).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_register) {
            anyPageChangeListener.onPageChange(AppConstants.LOGIN_FRAGMENT_1);
        } else if (id == R.id.btn_login) {
            if (checkInput()) {
                if (NetworkConnection.isNetworkAvailable(getActivity())) {
                    Routine.closeKeyboard(btnLogin, getActivity());
                    loginViewModel.login(etEmail.getText().toString(), etPassword.getText().toString());
                } else {
                    DialogMaterial.getBottomSheet(getResources().getString(R.string.no_network_connection), Color.RED, getActivity()).show();
                }
            }
        } else if (id == R.id.btn_forget_passsword) {
            // Handle forget password action
        }
    }

    private boolean checkInput() {
        if (etEmail.getText().toString().equals("")) {
            etEmail.setError(getResources().getString(R.string.insert_email_text));
            etEmail.requestFocus();
            return false;
        } else if (etPassword.getText().toString().equals("")) {
            etPassword.setError(getResources().getString(R.string.insert_password_text));
            etPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}
