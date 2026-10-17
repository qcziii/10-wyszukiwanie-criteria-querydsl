package pl.kurs.sklep;

import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Zapamiętuje każde zapytanie SQL, które Hibernate wysyła do bazy (używane tylko w testach). */
public class SqlPodglad implements StatementInspector {

    private static final List<String> ZAPYTANIA = Collections.synchronizedList(new ArrayList<>());

    @Override
    public String inspect(String sql) {
        ZAPYTANIA.add(sql);
        return sql;
    }

    static void wyczysc() {
        ZAPYTANIA.clear();
    }

    static List<String> zapytania() {
        synchronized (ZAPYTANIA) {
            return List.copyOf(ZAPYTANIA);
        }
    }
}
