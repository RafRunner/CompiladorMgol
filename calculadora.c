#include <stdio.h>
#include <string.h>

typedef char literal[256];

int main() {
/*----Variaveis temporarias----*/
int T0;
double T1;
int T2;
double T3;
int T4;
double T5;
int T6;
double T7;
/*------------------------------*/
double A;
double B;
int operacao;
double resultado;



printf("Bem vindo à calculadora MGOL. É possível fazer operações do tipo A <operação> B\n");
printf("Digite A: ");
scanf("%lf", &A);
printf("Digite B: ");
scanf("%lf", &B);
printf("Digite a operação (+ = 1, - = 2, * = 3, / = 4): ");
scanf("%d", &operacao);
T0 = operacao == 1;
if (T0) {
T1 = A + B;
resultado = T1;
}
T2 = operacao == 2;
if (T2) {
T3 = A - B;
resultado = T3;
}
T4 = operacao == 3;
if (T4) {
T5 = A * B;
resultado = T5;
}
T6 = operacao == 4;
if (T6) {
T7 = A / B;
resultado = T7;
}
printf("Resultado: ");
printf("%lf", resultado);
printf("\n");

return 0;
}
