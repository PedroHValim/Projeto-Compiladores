#include <iostream>
#include <string>
int main() {
 float x  ; std::cout << "Digite o primeiro número: " ;
 std::cin >>x;
 float y  ; std::cout << "Digite o segundo número:" ;
 std::cin >>y;
 std::cout << "Selecione uma opção: " <<std::endl ; std::cout << "1 - SOMA:" <<std::endl ; std::cout << "2 - SUBTRAÇÃO:" <<std::endl ; std::cout << "3 - MULTIPLICAÇÃO:" <<std::endl ; std::cout << "4 - DIVISÃO:" <<std::endl ; int z  ; std::cout << "Digite a operação desejada: " ;
 std::cin >>z;
 float resultado   = 10.5 ;
 if ( z == 1 ) { resultado = x + y ;
 std::cout << "Resultado :" <<std::endl ; std::cout << resultado <<std::endl ; }else if ( z == 2 ) { resultado = x - y ;
 std::cout << resultado <<std::endl ; }else if ( z == 3 ) { resultado = x * y ;
 std::cout << resultado <<std::endl ; } else { resultado = x / y ;
 std::cout << resultado <<std::endl ; } 
return 0;
} 