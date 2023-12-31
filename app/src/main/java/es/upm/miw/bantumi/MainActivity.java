package es.upm.miw.bantumi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import es.upm.miw.bantumi.model.ScoreViewModel;
import es.upm.miw.bantumi.model.Score;
import es.upm.miw.bantumi.model.BantumiViewModel;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MiW";
    JuegoBantumi juegoBantumi;
    BantumiViewModel bantumiVM;
    int numInicialSemillas;
    ScoreViewModel scoreViewModel;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Instancia el ViewModel y el juego, y asigna observadores a los huecos
        numInicialSemillas = getResources().getInteger(R.integer.intNumInicialSemillas);
        bantumiVM = new ViewModelProvider(this).get(BantumiViewModel.class);
        juegoBantumi = new JuegoBantumi(bantumiVM, JuegoBantumi.Turno.turnoJ1, numInicialSemillas);
        crearObservadores();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String newPlayer1 = this.preferences.getString(
                "namePlayer1",
                getString(R.string.txtPlayer1)
        );
        String newPlayer2 = this.preferences.getString(
                "namePlayer2",
                getString(R.string.txtPlayer2)
        );

        Log.i(LOG_TAG, "namePlayer1 = " + newPlayer1);
        Log.i(LOG_TAG, "namePlayer2 = " + newPlayer2);

        TextView textViewPlayer1 = findViewById(R.id.tvPlayer1);
        TextView textViewPlayer2 = findViewById(R.id.tvPlayer2);

        textViewPlayer1.setText(newPlayer1);
        textViewPlayer2.setText(newPlayer2);

    }

    /**
     * Crea y subscribe los observadores asignados a las posiciones del tablero.
     * Si se modifica el contenido del tablero -> se actualiza la vista.
     */
    private void crearObservadores() {
        for (int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
            int finalI = i;
            bantumiVM.getNumSemillas(i).observe(    // Huecos y almacenes
                    this,
                    integer -> mostrarValor(finalI, juegoBantumi.getSemillas(finalI)));
        }
        bantumiVM.getTurno().observe(   // Turno
                this,
                turno -> marcarTurno(juegoBantumi.turnoActual())
        );
    }

    /**
     * Indica el turno actual cambiando el color del texto
     *
     * @param turnoActual turno actual
     */
    private void marcarTurno(@NonNull JuegoBantumi.Turno turnoActual) {
        TextView tvJugador1 = findViewById(R.id.tvPlayer1);
        TextView tvJugador2 = findViewById(R.id.tvPlayer2);
        switch (turnoActual) {
            case turnoJ1:
                tvJugador1.setTextColor(getColor(R.color.white));
                tvJugador1.setBackgroundColor(getColor(android.R.color.holo_blue_light));
                tvJugador2.setTextColor(getColor(R.color.black));
                tvJugador2.setBackgroundColor(getColor(R.color.white));
                break;
            case turnoJ2:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador1.setBackgroundColor(getColor(R.color.white));
                tvJugador2.setTextColor(getColor(R.color.white));
                tvJugador2.setBackgroundColor(getColor(android.R.color.holo_blue_light));
                break;
            default:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador2.setTextColor(getColor(R.color.black));
        }
    }

    /**
     * Muestra el valor <i>valor</i> en la posición <i>pos</i>
     *
     * @param pos posición a actualizar
     * @param valor valor a mostrar
     */
    private void mostrarValor(int pos, int valor) {
        String num2digitos = String.format(Locale.getDefault(), "%02d", pos);
        // Los identificadores de los huecos tienen el formato casilla_XX
        int idBoton = getResources().getIdentifier("casilla_" + num2digitos, "id", getPackageName());
        if (0 != idBoton) {
            TextView viewHueco = findViewById(idBoton);
            viewHueco.setText(String.valueOf(valor));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public void RestoreMatch(){
        try {
            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput("match.txt")));
            String turn = fin.readLine();
            switch (turn) {
                case "turnoJ1":
                    juegoBantumi.setTurno(JuegoBantumi.Turno.turnoJ1);
                    break;
                case "turnoJ2":
                    juegoBantumi.setTurno(JuegoBantumi.Turno.turnoJ2);
                    break;
                default:
                    throw new RuntimeException("No se ha podido leer el turno");
            }
            for(int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
                juegoBantumi.setSemillas(i,Integer.parseInt(fin.readLine()));
            }
            Snackbar.make(
                    findViewById(android.R.id.content),
                    R.string.txtPartidaCargada,
                    Snackbar.LENGTH_LONG
            ).show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressLint({"NonConstantResourceId", "StaticFieldLeak"})
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcAjustes:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.opcReiniciarPartida:
                new ResetAlertDialog().show(getSupportFragmentManager(), "ALERT_DIALOG");
                return true;
            case R.id.opcGuardarPartida:
                FileOutputStream fos;
                try {
                    fos = openFileOutput("match.txt", Context.MODE_PRIVATE);
                    fos.write(this.juegoBantumi.serializa().getBytes());
                    fos.close();
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        R.string.txtPartidaGuardada,
                        Snackbar.LENGTH_LONG
                    ).show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            case R.id.opcRecuperarPartida:
                new RestoreAlertDialog().show(getSupportFragmentManager(), "ALERT_DIALOG");
                return true;
            case R.id.opcMejoresResultados:
                Intent intent = new Intent(MainActivity.this, ScoresActivity.class);
                startActivity(intent);
                return true;
            case R.id.opcAcercaDe:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.aboutTitle)
                        .setMessage(R.string.aboutMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return true;
            default:
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtSinImplementar),
                        Snackbar.LENGTH_LONG
                ).show();
        }
        return true;
    }

    /**
     * Acción que se ejecuta al pulsar sobre cualquier hueco
     *
     * @param v Vista pulsada (hueco)
     */
    public void huecoPulsado(@NonNull View v) {
        String resourceName = getResources().getResourceEntryName(v.getId()); // pXY
        int num = Integer.parseInt(resourceName.substring(resourceName.length() - 2));
        Log.i(LOG_TAG, "huecoPulsado(" + resourceName + ") num=" + num);
        switch (juegoBantumi.turnoActual()) {
            case turnoJ1:
                juegoBantumi.jugar(num);
                break;
            case turnoJ2:
                juegaComputador();
                break;
            default:    // JUEGO TERMINADO
                finJuego();
        }
        if (juegoBantumi.juegoTerminado()) {
            finJuego();
        }
    }

    /**
     * Elige una posición aleatoria del campo del jugador2 y realiza la siembra
     * Si mantiene turno -> vuelve a jugar
     */
    void juegaComputador() {
        while (juegoBantumi.turnoActual() == JuegoBantumi.Turno.turnoJ2) {
            int pos = 7 + (int) (Math.random() * 6);    // posición aleatoria [7..12]
            Log.i(LOG_TAG, "juegaComputador(), pos=" + pos);
            if (juegoBantumi.getSemillas(pos) != 0 && (pos < 13)) {
                juegoBantumi.jugar(pos);
            } else {
                Log.i(LOG_TAG, "\t posición vacía");
            }
        }
    }

    /**
     * El juego ha terminado. Volver a jugar?
     */
    @SuppressLint("StaticFieldLeak")
    private void finJuego() {

        String namePlayer1 = this.preferences.getString(
                "namePlayer1",
                getString(R.string.txtPlayer1)
        );

        String namePlayer2 = this.preferences.getString(
                "namePlayer2",
                getString(R.string.txtPlayer2)
        );

        String texto = (juegoBantumi.getSemillas(6) > 6 * numInicialSemillas)
                ? ("Gana" + namePlayer1)
                : ("Gana" + namePlayer2);
        if (juegoBantumi.getSemillas(6) == 6 * numInicialSemillas) {
            texto = "¡¡¡ EMPATE !!!";
        }
        Snackbar.make(
                        findViewById(android.R.id.content),
                        texto,
                        Snackbar.LENGTH_LONG
                )
                .show();

        String formattedDateTime = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            formattedDateTime = currentDateTime.format(formatter);
        }
        String winner = (juegoBantumi.getSemillas(6) > 6 * numInicialSemillas)
                ? namePlayer1
                : namePlayer2;

        int seeds = (juegoBantumi.getSemillas(6) > 6 * numInicialSemillas)
                ? juegoBantumi.getSemillas(6)
                : juegoBantumi.getSemillas(13);

        final String finalFormattedDateTime = formattedDateTime;

        Score score = new Score(finalFormattedDateTime, winner, seeds);
        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);
        try {
            scoreViewModel.insert(score);
        }catch (Exception e) {
            Log.e(LOG_TAG,"ERROR" + e.getMessage());
            e.printStackTrace();
        }
        // terminar
        new FinalAlertDialog().show(getSupportFragmentManager(), "ALERT_DIALOG");
    }

}