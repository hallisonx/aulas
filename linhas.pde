int m = 180; //tamanho do lado
int l = 10;  //linha inicial
//int i = 5;

int x1 = l;
int y1 = l;
int x2 = l + m;
int y2 = l;

void setup() {
  size(200, 200);
}

void draw() {
  background(0);
  
  for(int i=0; i<=40; i+=5){
    x1++; x2++; y1--; y2--;
    line(x1+i, y1+i, x2-i, y2+i);
    line(x2-i, y2+i, x2-i, y2+m-i);
    line(x2-i, y2+m-i, x1+i, y1+m-i);
    line(x1+i, y1+m-i, x1+i, y1+i);
  }
  
  //quad(x1+5, y1+5, x2-5, y2+5, x2-5, y2+m-5, x1+5, y1+m-5);*/
}
