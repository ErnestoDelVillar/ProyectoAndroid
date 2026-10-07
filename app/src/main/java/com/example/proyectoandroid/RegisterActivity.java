package com.example.proyectoandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectoandroid.databinding.ActivityRegisterBinding;
import com.google.android.material.snackbar.Snackbar;

/**
 * Capa VIEW del registro. Lee los campos, se los pasa al ViewModel.
 */
public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegistroViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(RegistroViewModel.class);

        setupListeners();
        observarViewModel();
    }

    private void setupListeners() {

        binding.btnRegistrar.setOnClickListener(v -> {
            String usuario = binding.txtUsuarioRegistro.getText().toString();
            String email = binding.txtEmailRegistro.getText().toString();
            String contrasena = binding.txtContrasenaRegistro.getText().toString();
            viewModel.registrar(usuario, email, contrasena);
        });

    }

    private void observarViewModel() {
        // Errores de cada campo
        viewModel.getUsuarioError().observe(this, error -> binding.lblUsuarioRegistro.setError(error));
        viewModel.getEmailError().observe(this, error -> binding.lblEmailRegistro.setError(error));
        viewModel.getContrasenaError().observe(this, error -> binding.lblInfoContrasenaRegistro.setError(error));

        // Estado general de la operación
        viewModel.getEstadoRegistro().observe(this, this::mostrarEstado);
    }

    private void mostrarEstado(EstadoAutenticacion estado) {
        switch (estado.getEstado()) {
            case CARGANDO:
                showCargando(true);
                break;

            case EXITO:
                showCargando(false);
                Toast.makeText(this, "Cuenta creada. Ahora inicia sesión", Toast.LENGTH_LONG).show();
                finish();
                break;

            case ERROR:
                showCargando(false);
                Snackbar.make(binding.getRoot(), estado.getMensaje(), Snackbar.LENGTH_LONG).show();
                viewModel.onErrorShown();
                break;

            case INACTIVO:

            default:
                showCargando(false);
                break;
        }
    }

    private void showCargando(boolean cargando) {
        binding.progressBarRegistro.setVisibility(cargando ? View.VISIBLE : View.GONE);
        binding.btnRegistrar.setEnabled(!cargando);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}