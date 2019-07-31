package jdbc;

/**
 * Conectando com BD usando JDBC.
 *
 * Primeiramente baixe o arquivo da implementacao JDBC (JDBC driver) e o adicione ao seu
 * classpath (via IDE ou via CLASSPATH).
 * No caso do MySQL, o arquivo eh chamado mysql-connector e pode ser obtido na pagina
 * do MySQL.
 * No caso do Derby (Java DB) o arquivo e o derby-client.jar, encontrado juntamente com
 * outros jars na instalacao do BD.
 *
 * O exemplo abaixo utiliza o MySQL. Criei o database 'mydatabase' e tabelas e colunas
 * conforme descritas na query string.
 * Veja + sobre o driver em http://dev.mysql.com/doc/refman/6.0/en/connector-j-reference.html
 *
 * Veja comentarios nas instrucoes para outros detalhes.
 *
 * Note que esta forma de obter conexao nao eh adequada para uma aplicacao multi-usuario. Para tal,
 * consulte 'connection pool' e 'data source'
 *
 * Para este exemplo foi criado o banco de dados MYDATABASE, a tabela USUARIO e as colunas ID, NOME e EMAIL. 
 */

// classes da API JDBC estao em java.sql
import java.sql.*;

public class DatabaseDemo {
    
    public static void main(String[] args) {
        DatabaseDemo instance = new DatabaseDemo();
        instance.testConnection();
    }

    public void testConnection() {
        try {
            // Com esta chamada carregamos a classe do Driver. Cada BD tera uma classe espeficica.
            // Como estou usando o MySQL, segue a classe necessaria.
            // Veja: http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Class.html#forName(java.lang.String)
            Class.forName("com.mysql.jdbc.Driver");

        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }


        Connection connection = null;
        try {
            // Qdo um driver e carregado, ele se registra em uma classe chamada DriverManager. E desta forma
            // podemos solicitar uma conexao com nosso database
            //
            // jdbc: eh o protocolo.
            // mysql: representa o subprotocolo, identifica o driver a ser utilizado
            // o resto eh especifico do database. no caso do MySQL, IP, porta, database e parametros.
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase?connectTimeout=300&charset=utf8", "root", "");

            // Statement representa uma instrucao SQL. Pode ser criada de forma estatica (java.sql.Statement) ou
            // dinamica e precompilada (java.sql.PreparedStatement).
            // Atraves desta interface eh possivel realizar nao so select mas tambem update, insert e delete.
            // Statement e PreparedStatement retornam uma referencia ResultSet quando realizamos uma query.
            // Esta interface permite percorrer os registros que foram obtidos via query.

            // Exemplo usando PreparedStatement
            PreparedStatement statement = connection.prepareStatement("SELECT id, nome, email FROM usuario WHERE id = ?");
            int paramIdx = 1; // indice de ? sempre iniciando com 1
            int paramValue = 1;
            statement.setInt(paramIdx, paramValue);
            ResultSet resultSet = statement.executeQuery();

            // Exemplo usando Statement
            //Statement statement = connection.createStatement();
            //ResultSet resultSet = statement.executeQuery("SELECT id, nome, email FROM usuario");

            // ResultSet sempre retorna valido (not null) porem podera retornar vazio. next() eh responsavel por
            // informar true enquanto tivermos registros para iterar.
            while (resultSet.next()) {

                // o valor da coluna pode ser obtido pelo indice, que comeca com 1 (id), 2(nome) etc.
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);

                // ou entao voce pode obter o valor pelo nome da coluna retornada.
                String email = resultSet.getString("email");

                // geralmente criamos objetos a partir dos valores retornados
                User user = new User(id, name, email);
                System.out.println(user);
            }

            // fechar resultset e statement aberto
            resultSet.close();
            statement.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // e NUNCA esquecer de fechar a conexao.
            try{
                if (connection != null) {
                    connection.close();
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private class User {
        private int id;
        private String name;
        private String email;

        private User(int id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public String toString() {
            return String.format("{User { id : %d, name : %s, email : %s}}", id, name, email);
        }
    }
}
