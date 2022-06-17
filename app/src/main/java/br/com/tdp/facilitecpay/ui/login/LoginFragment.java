package br.com.tdp.facilitecpay.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import br.com.tdp.facilitecpay.R;
import br.com.tdp.facilitecpay.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private FragmentLoginBinding binding;
    //private Spinner spinnerRepresentantes;
    //private EditText edtTextSenha;
    //private ImageButton ibtnMenu;
    private Button btnMenu;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LoginViewModel homeViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);


        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //spinnerRepresentantes = getView().findViewById(R.id.spinnerRepresentantes);
        //edtTextSenha = getView().findViewById(R.id.editPassRepresentantes);
        btnMenu = root.findViewById(R.id.menu);
        btnMenu.setOnClickListener(this);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else{
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
}