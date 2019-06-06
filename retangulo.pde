int vai = 0;
int vem = 150;

int tam = 50;
int linha = 10;

void setup() {
  size(200, 200);
}

void draw() {
  background(0);

  if(vai < 150){
    rect (vai++,linha,tam,tam); 
    print(vai); 
  }else {
    rect (vem--,linha,tam,tam);
    print(" | "+vem);
  }
  
  
  if(vem==0){
      vai = 0;
      vem = 150;
      linha+=5;
    }
  
}
