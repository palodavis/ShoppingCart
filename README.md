# ShoppingCart

Desafio 1 da Compass UOL: Um sistema desenvolvido em Java para gerenciar um carrinho de compras com integração ao Banco de dados.

---
## Requisitos necessários
- Verificar se o Java e MySQL estão instalados corretamente na máquina.
- Versão do Java:
  - Java 17.
- Versão MySQL:
  - MySQL (recomendado.: MySQL Workbench 8.0.36 Community).
- Versão conector MySQL:
  - MySQL Connector/J (.jar file, versão 9.1).
---
## Etapas de Configuração
1. Adicione o arquivo `.jar` do MySQL Connector como uma biblioteca externa no projeto.
   - Para IntelliJ IDEA: Vá em `File > Project Structure > Modules > Dependencies` e clique na opção "+" e adicione o arquivo `.jar`.
   - Caso não tenha o arquivo `.jar` na máquina, pode fazer o download pelo site: https://downloads.mysql.com/archives/c-j/
   - Caso seja outra IDE, necessário colocar o arquivo `.jar` nas dependências (bibliotecas) externas.
2. Crie um banco de dados MySQL chamado `shoppingcartjdbc`.
2. Execute o script do arquivo `database_shoppingCart.sql` no banco de dados para criar as tabelas.
3. Edite o arquivo [`db.properties`](./db.properties) com as credencias do seu banco de dados: (usuário e senha) e a URL correspondente ao BD.
---
## Executar o projeto
1. Tenha todos os requisitos necessários instalados.
2. Verificar se o MySql está em execução.
2. Compilar e executar o projeto na IDE (Recomendado: Intellij IDEA).
3. Testar as funcionalidades verificando a integração com o BD.

