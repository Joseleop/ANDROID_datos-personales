package es.studium.practicatema02;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button btnLimpiar;
    Button btnMostrarDatos;
    EditText txtNombre;
    EditText txtApellidos;
    EditText txtEdad;
    RadioGroup rgGenero;
    Spinner spnEstadoCivil;
    Switch swHijos;
    TextView tvMensaje;
    Spinner spnIdiomas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvMensaje = findViewById(R.id.tvMensaje);
        txtNombre = findViewById(R.id.hintNombre);
        txtApellidos = findViewById(R.id.hintApellidos);
        txtEdad = findViewById(R.id.hintEdad);
        rgGenero = findViewById(R.id.rgGenero);
        spnEstadoCivil = findViewById(R.id.spnEstadoCivil);
        swHijos = findViewById(R.id.swHijos);
        tvMensaje = findViewById(R.id.tvMensaje);

        spnIdiomas = findViewById(R.id.spnIdiomas);
        String idiomaActual = getResources().getConfiguration().getLocales().get(0).getLanguage();


        if (idiomaActual.equals("es")) {
            spnIdiomas.setSelection(0);
        } else if (idiomaActual.equals("en")) {
            spnIdiomas.setSelection(1);
        } else if (idiomaActual.equals("fr")) {
            spnIdiomas.setSelection(2);
        }

        spnIdiomas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String idiomaSeleccionado = "";

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        idiomaSeleccionado = "es";
                        break;
                    case 1:
                        idiomaSeleccionado = "en";
                        break;
                    case 2:
                        idiomaSeleccionado = "fr";
                        break;
                }
                if (!idiomaSeleccionado.equals(idiomaActual)) {
                    cambiarIdioma(idiomaSeleccionado);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnLimpiar = findViewById(R.id.btnLimpiar);
        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        btnMostrarDatos = findViewById(R.id.btnMostrarDatos);
        btnMostrarDatos.setOnClickListener(v -> {

            //Hay limpiar el campo del tv porque se queda pintado con el append
            tvMensaje.setText("");
            boolean todoOk = true;

            String textoNombre = txtNombre.getText().toString().trim();
            boolean estaNombreVacio = textoNombre.isEmpty();

            String textoApellido = txtApellidos.getText().toString().trim();
            boolean estaApellidoVacio = textoApellido.isEmpty();

            String textoEdad = txtEdad.getText().toString().trim();
            boolean estaEdadVacio = textoEdad.isEmpty();

            if (estaNombreVacio || estaApellidoVacio || estaEdadVacio) {
                tvMensaje.append(getString(R.string.txtPrefijo));
                todoOk = false;
            }
            if (estaNombreVacio) {
                tvMensaje.append(getString(R.string.faltaNombre));
            }
            if (estaApellidoVacio) {
                tvMensaje.append(getString(R.string.faltaApellido));
            }
            if (estaEdadVacio) {
                tvMensaje.append(getString(R.string.faltaEdad));
            }
            if (todoOk) {
                String nombre = txtNombre.getText().toString().trim();
                String apellidos = txtApellidos.getText().toString().trim();
                String esAdulto = esMayorEdad();
                String genero = esHombreMujer();
                String hijos = tieneHijos();
                String estadoCivil = estadoCivil();
                String datosPersona = getString(R.string.datosPersona, apellidos, nombre, esAdulto, genero, hijos, estadoCivil);
                Snackbar.make(v, datosPersona, Snackbar.LENGTH_LONG).show();
                //Mostramos los datos
            }
        });
    }

    private void cambiarIdioma(String codigo) {
        Locale locale = new Locale(codigo);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        recreate();
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtApellidos.setText("");
        txtEdad.setText("");
        rgGenero.clearCheck();
        swHijos.setChecked(false);
        spnEstadoCivil.setSelection(0);
        tvMensaje.setText("");
        txtNombre.requestFocus();
    }

    private String esHombreMujer() {
        //rgGenero = findViewById(R.id.rgGenero);
        int idSeleccionado = rgGenero.getCheckedRadioButtonId();
        String genero = "";

        if (idSeleccionado == R.id.rdBtnHombre) {
            genero = getString(R.string.checkHombre);
        } else if (idSeleccionado == R.id.rdBtnMujer) {
            genero = getString(R.string.checkMujer);
        }
        else{
            genero = getString(R.string.checkOtro);
        }
        return genero;
    }

    private String tieneHijos() {
        //swHijos = findViewById(R.id.swHijos);
        boolean tieneHijos = swHijos.isChecked();
        return tieneHijos ? getString(R.string.txtSiHijos) : getString(R.string.txtNoHijos);
    }

    private String estadoCivil() {
        return spnEstadoCivil.getSelectedItem().toString().toLowerCase();
    }

    private String esMayorEdad() {
        String textoEdad = txtEdad.getText().toString().trim();
        int edadNum = Integer.parseInt(textoEdad);
        return (edadNum >= 18) ? getString(R.string.txtMayorEdad) : getString(R.string.txtMenorEdad);
    }
}