import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.dbutils.BeanProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level =  AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ActorBean {

    int actor_id;
    String first_name;
    String last_name;
    String last_update;

    public static List<ActorBean> getActors() throws SQLException {
        String query = "SELECT * FROM public.actor;";
        try (ResultSet resultSet = DB_Connection.executeQuery(query)) {
            return  new BeanProcessor().toBeanList(resultSet, ActorBean.class);

        }
    }

    public static ActorBean getBy(String column, String value) throws SQLException {
        String query = "SELECT * FROM public.actor WHERE " + column + " = ?;";
        ResultSet resultSet = DB_Connection.executeQuery(query, value);
        if (!resultSet.next()) {
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, ActorBean.class);
        }
    }

}
