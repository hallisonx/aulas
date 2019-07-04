Table table;
Aluno aluno; //DECLARACAO DO OBJETO ALUNO A PARTIR DA CLASSE ALUNO

void setup() {

  table = loadTable("notas.csv", "header");

  println(table.getRowCount() + " linhas na tabela.");  //CHAMADA DO MÉTODO GETROWCOUNT() PARA SABER QUANTAS LINHAS TEM O ARQUIVO
  
  for (TableRow row : table.rows()) {
    
    // O QUE ANTES ERA ARMAZENADO EM VARIÁVEIS, AGORA COMPÕE OS ATRIBUTOS DE UMA CLASSE ALUNO 
    aluno = new Aluno( row.getInt("id") , row.getString("nome") , row.getFloat("n1") , row.getFloat("n2") , row.getFloat("n3") );
    
    print  (aluno.id    + " | "); // IMPRIME O ATRIBUTO ID DO OBJETO ALUNO QUE ESTÁ SENDO CONSULTADO
    print  (aluno.nome  + " | "); // IMPRIME O ATRIBUTO NOME DO OBJETO ALUNO QUE ESTÁ SENDO CONSULTADO
    print  (aluno.n1 + " | ");
    print  (aluno.n2 + " | ");
    println(aluno.n3 + " | ");
    
  }// fim do for
  
}// fim do setup

class Aluno {
  int id;
  String nome;
  float n1;
  float n2;
  float n3;
  float media;
  
  Aluno(int id, String nome, float n1, float n2, float n3){
    this.id = id;
    this.nome = nome;
    this.n1 = n1;
    this.n2 = n2;
    this.n3 = n3;
  }
  
  float obterMedia(){
    this.media = (n1 + n2 + n3) / 3;
    return  media;
  }
  
}
