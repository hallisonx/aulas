void setup() {
  noLoop();
}

void draw() {
  int soma = 0;  // variável que receberá o resultado da função
  soma = somarNumeros(5, 3);   // Chamando a função
  imprimeSeparador();
  println("A soma é: " + soma);  // Exibe o valor da variável no console
  imprimeSeparador();
}

// Função que retorna um valor inteiro, resultado da soma dos parâmetros
int somarNumeros(int n1, int n2){
  return n1 + n2;
}

void imprimeSeparador(){
  for(int i=0;   i < 20;   i++){
    print("-");
  }
  println();
}
