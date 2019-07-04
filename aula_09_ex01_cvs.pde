Table table;

void setup() {

  table = loadTable("notas.csv", "header");

  println(table.getRowCount() + " linhas na tabela."); 
  
  for (TableRow row : table.rows()) {
    
    int id      = row.getInt("id");       // dado que consta na coluna ID do arquivo CVS
    String nome = row.getString("nome");  // dado que consta na coluna NOME do arquivo CVS
    float nota1 = row.getFloat("n1");     // dado que consta na coluna N1 do arquivo CVS
    float nota2 = row.getFloat("n2");
    float nota3 = row.getFloat("n3");
    
    print  (id    + " | "); 
    print  (nome  + " | ");
    print  (nota1 + " | ");
    print  (nota2 + " | ");
    println(nota3 + " | ");
    
  }// fim do for
  
}// fim do setup
