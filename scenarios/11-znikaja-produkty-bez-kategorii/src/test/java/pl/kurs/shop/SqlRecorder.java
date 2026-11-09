package pl.kurs.shop;

import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Records every SQL query Hibernate sends to the database (used only in tests). */
public class SqlRecorder implements StatementInspector {

    private static final List<String> QUERIES = Collections.synchronizedList(new ArrayList<>());

    @Override
    public String inspect(String sql) {
        QUERIES.add(sql);
        return sql;
    }

    static void clear() {
        QUERIES.clear();
    }

    static List<String> queries() {
        synchronized (QUERIES) {
            return List.copyOf(QUERIES);
        }
    }
}
