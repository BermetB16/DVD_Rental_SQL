import java.sql.SQLException;

public class Demo {


    public static void main(String[] args) throws SQLException {

        DB_Connection.openConnection("dvdrentals");

        ActorBean.getActors().forEach(System.out::println);

    }
}
