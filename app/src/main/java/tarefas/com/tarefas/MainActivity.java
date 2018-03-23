package tarefas.com.tarefas;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private EditText tarefa;
    private Button salvar;
    private ListView listaTaf;

    private SQLiteDatabase bancoDados;
    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<String> itens;
    private ArrayList<Integer>  ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            tarefa = findViewById(R.id.cxTarefa);
            salvar = findViewById(R.id.btnSalvar);
            listaTaf = findViewById(R.id.lvTarefas);

            bancoDados = openOrCreateDatabase("app",MODE_PRIVATE,null);
            bancoDados.execSQL(create());

            salvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        String texto = tarefa.getText().toString();
                        if(texto.equals("")){
                            Toast.makeText(MainActivity.this, "Favor preencher o campo TAREFA", Toast.LENGTH_SHORT).show();
                        }else{
                            inserirTarefa(texto);
                            Toast.makeText(MainActivity.this, "Tarefa incluída com sucesso! ", Toast.LENGTH_SHORT).show();
                            tarefa.setText("");
                            RecExibirTarefas();

                        }
                }
            });
            listaTaf.setLongClickable(true);
            listaTaf.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    deletarTarefa( ids.get( position ) );
                    return true;
                }
            });


            RecExibirTarefas();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String create(){
        String query = "CREATE TABLE IF NOT EXISTS TAREFAS(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NOME VARCHAR);";
        return query;
    }

    private void inserirTarefa(String nome){
        try {
            bancoDados.execSQL("INSERT INTO TAREFAS (NOME) VALUES ('"+nome+"')");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void RecExibirTarefas(){
        try {
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM TAREFAS ORDER BY ID DESC", null);

            int indiceColunaId = cursor.getColumnIndex("ID");
            int indiceColunaNome = cursor.getColumnIndex("NOME");

            itens = new ArrayList<String>();
            ids = new ArrayList<Integer>();


            itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_2,
                    android.R.id.text2,
                    itens);

            listaTaf.setAdapter(itensAdaptador);

            cursor.moveToFirst();

            while (cursor != null){
                Log.i("Resultado - ", "Nome: " + cursor.getString( indiceColunaNome ) );
                itens.add(cursor.getString( indiceColunaNome ));
                //ids.add( Integer.parseInt(cursor.getString(indiceColunaId)) );

                cursor.moveToNext();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deletarTarefa(Integer id){
        try {
            bancoDados.execSQL("DELETE FROM TAREFAS WHERE ID ="+id );
            Toast.makeText(MainActivity.this, "Tarefa removida com sucesso!", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
