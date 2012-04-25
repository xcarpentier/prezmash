package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.annotation.Sql;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@Sql
public class StatsAggregate {

    @OneToOne
    public Candidat candidat;

    public Integer result;


    public static List<StatsAggregate> findStatsAggregate() {
        final String sql = "SELECT candidat_id , count(*) as result\n" +
                "FROM stats\n" +
                "GROUP BY candidat_id";

        RawSql rawSql =
                RawSqlBuilder
                        .parse(sql)
                        .columnMapping("candidat_id", "candidat.id")
                        .create();

        Query<StatsAggregate> query = Ebean.find(StatsAggregate.class);
        query.setRawSql(rawSql).order().asc("result");

        return query.findList();
    }


}