package com.example.proyectoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectoandroid.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;

/**
 * Capa VIEW. Solo muestra lo que el ViewModel le dice y le avisa de los clics.
 */
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AutenticacionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AutenticacionViewModel.class);

        // Sesión persistente: si ya hay usuario, nos saltamos el login
        if (viewModel.hayUsuarioActivo()) {
            volverInicio();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupListeners();
        observarViewModel();
    }

    private void setupListeners() {
        binding.button.setOnClickListener(v -> {
            String email = binding.txtEmail.getText().toString();
            String password = binding.txtContrasena.getText().toString();
            viewModel.login(email, password);
        });

        binding.lblGoToRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void observarViewModel() {
        // Errores de cada campo
        viewModel.getEmailError().observe(this, error -> binding.lblEmail.setError(error));
        viewModel.getContrasenaError().observe(this, error -> binding.lblInfoContrasena.setError(error));

        // Estado general de la operación
        viewModel.getEstadoAutenticacion().observe(this, this::mostrarEstado);
    }

    private void mostrarEstado(EstadoAutenticacion estado) {
        switch (estado.getEstado()) {
            case CARGANDO:
                showCargando(true);
                break;

            case EXITO:
                showCargando(false);
                volverInicio();
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

    private void showCargando(boolean loading) {
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.button.setEnabled(!loading);
    }

    private void volverInicio() {
        Intent intent = new Intent(this, MainActivity.class);
        // Borra el historial: al darle "atrás" no se vuelve al login
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}