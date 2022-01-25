import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static spark.Spark.*;

public class JSONServerAgImJDBC {
    ObjectMapper om = new ObjectMapper();
    Statement statement;

    public static void main(String[] args) {
        new JSONServerAgImJDBC().run();
    }

    public void run() {

        try {
            dbConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Start embedded server at this port
        port(8088);
        // Configure resources
        get("/all", (request, response) -> {
            String query;

            query = "SELECT * FROM macchine";
            ResultSet rs = statement.executeQuery(query);

            ArrayList<macchina> l = new ArrayList<>();
            while (rs.next()) {
                l.add(new macchina(rs.getInt("id_macchina"), rs.getString("brand"),
                        rs.getString("modello"), rs.getString("condizione"), rs.getInt("kilometraggio"), rs.getInt("cavalli"), rs.getInt("prezzo")));

            }
            System.out.println(l);
            return om.writeValueAsString(l);


        });

        get("/:id_macchina", (request, response) -> {
            String id_macchina = request.params(":id_macchina");
            String query;

            query = String.format("SELECT * FROM macchine where id_macchina = '%s' ", id_macchina);
            ResultSet rs = statement.executeQuery(query);
            if (!rs.next()) {
                response.status(404);
                return om.writeValueAsString("{status: failed}");
            }
            macchina r = new macchina(rs.getInt("id_macchina"), rs.getString("brand"),
                    rs.getString("modello"), rs.getString("condizione"), rs.getInt("kilometraggio"), rs.getInt("cavalli"), rs.getInt("prezzo"));

            return om.writeValueAsString(r);
        });
        delete("/:id_macchina", (request, response) -> {
            String id_macchina = request.params(":id_macchina");
            String query;

            query = String.format("DELETE FROM macchine where id_macchina = '%s' ", id_macchina);

            int rs = statement.executeUpdate(query);
            if (rs == 1)
                return om.writeValueAsString(String.format("delete macchina con id %s effettuato con successo", id_macchina));
            return String.format("macchina con id %s non presente nel database", id_macchina);
        });


        get("/brand/:name", (request, response) -> {
            String name = request.params(":name");
            String query;

            query = String.format("SELECT * FROM macchine where brand = '%s' ", name);
            ResultSet rs = statement.executeQuery(query);
            ArrayList<macchina> l = new ArrayList<>();
            while (rs.next()) {
                l.add(new macchina(rs.getInt("id_macchina"), rs.getString("brand"),
                        rs.getString("modello"), rs.getString("condizione"), rs.getInt("kilometraggio"), rs.getInt("cavalli"), rs.getInt("prezzo")));

            }
            return om.writeValueAsString(l);
        });


        post("/add", (request, response) -> {
            int id_macchina = Integer.parseInt(request.queryParams("id_macchina"));
            String brand = request.queryParams("brand");
            String modello = request.queryParams("modello");
            String condizione = request.queryParams("condizione");
            int kilometraggio = Integer.parseInt(request.queryParams("kilometraggio"));
            int cavalli = Integer.parseInt(request.queryParams("cavalli"));
            int prezzo = Integer.parseInt(request.queryParams("prezzo"));

            macchina ai = new macchina(id_macchina, brand, modello, condizione, kilometraggio, cavalli, prezzo);
            String query = String.format(
                    "INSERT INTO macchine VALUES ('%d', '%s', '%s', '%s', %d, %d,'%d')", id_macchina,
                    brand, modello, condizione, kilometraggio, cavalli, prezzo);
            statement.executeUpdate(query);

            response.status(201);
            return om.writeValueAsString(ai);
        });

        put("/update/:id_macchina", (request, response) -> {
            macchina car1 = om.readValue(request.body(), macchina.class);
            Integer id_macchina = car1.id_macchina;
            String brand = car1.brand;
            String modello = car1.modello;
            String condizione = car1.condizione;
            Integer kilometraggio = car1.kilometraggio;
            Integer cavalli = car1.cavalli;
            Integer prezzo = car1.prezzo;


            String querySet = "";
            boolean first = false;
            if (brand != null) {
                querySet += String.format("brand= '%s'", brand);
                first = true;
            }

            if (modello != null) {
                if (first)
                    querySet += " , ";
                querySet += String.format("modello= '%s'", modello);
                first = true;
            }
            if (condizione != null) {
                if (first)
                    querySet += " , ";
                querySet += String.format("condizione= '%s'", condizione);
                first = true;
            }
            if (kilometraggio != 0) {
                if (first)
                    querySet += " , ";
                querySet += String.format("kilometraggio= '%s'", kilometraggio);
                first = true;
            }
            if (cavalli != 0) {
                if (first)
                    querySet += " , ";
                querySet += String.format("cavalli= '%s'", cavalli);
                first = true;
            }
            if (prezzo != null) {
                if (first)
                    querySet += " , ";
                querySet += String.format("prezzo= '%s'", prezzo);
            }
            String query = "UPDATE `macchine` SET " + querySet +
                    " WHERE `macchine`.`id_macchina` = " + request.params(":id_macchina");
            System.out.println(query);
            statement.executeUpdate(query);

            response.status(404);
            return om.writeValueAsString("macchina modificata correttemente");
        });

    }

    public void dbConnection() throws SQLException {
/*
        //SQLite connection
        DBManager.setConnection(
                DBManager.JDBC_Driver_SQLite,
                DBManager.JDBC_URL_SQLite);
        statement = DBManager.getConnection().createStatement();
*/
        // MySQL connection
        DBManager.setConnection(
                DBManager.JDBC_Driver_MySQL,
                DBManager.JDBC_URL_MySQL);
        statement = DBManager.getConnection().createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);

/*
        //H2 connection
        DBManager.setConnection(
                DBManager.JDBC_Driver_H2,
                DBManager.JDBC_URL_H2);
        statement = DBManager.getConnection().createStatement();
*/

        try {
            /*
             * Simple query for testing that everything is OK. If an exception raised, the
             * db is deleted and created from scratch.
             */
            statement.executeQuery("SELECT * FROM macchine LIMIT 1");
        } catch (SQLException e) {
            statement.executeUpdate("DROP TABLE IF EXISTS macchine");
            statement.executeUpdate("CREATE TABLE macchine ("
                    + "id_macchina INT(11) not null PRIMARY KEY, "
                    + "brand VARCHAR(20) not null, "
                    + "modello VARCHAR(30) not null, "
                    + "condizione enum('usata','nuova') DEFAULT NULL, "
                    + "kilometraggio INT(11) not null, "
                    + "cavalli INT(11) not null, "
                    + "prezzo INT(11) not null )");

            statement.executeUpdate(

                    "INSERT INTO `macchine` (`id_macchina`, `brand`, `modello`, `condizione`, `kilometraggio`, `cavalli`, `prezzo`) VALUES(12, 'Audi', 'A8', 'nuova', 0, 800, 70000)");
            statement.executeUpdate(
                    "INSERT INTO `macchine` (`id_macchina`, `brand`, `modello`, `condizione`, `kilometraggio`, `cavalli`, `prezzo`) VALUES(17, 'Ferrari', '812 SUPERFAST', 'usata', 0, 500, 300000)");
            statement.executeUpdate(
                    "INSERT INTO `macchine` (`id_macchina`, `brand`, `modello`, `condizione`, `kilometraggio`, `cavalli`, `prezzo`) VALUES(19, 'Lamborghini', 'Urus', 'usata', 900, 200, 200000)");

        }
    }
}