package org.amoseman.budgetingbackend.database.impl.sql.sqlite;


import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;

public class Generate {
    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration()
                .withJdbc(new Jdbc()
                        .withDriver("org.sqlite.JDBC")
                        .withUrl(args[0])
                )
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withName("org.jooq.meta.sqlite.SQLiteDatabase")
                                .withIncludes(".*")
                                .withExcludes("")
                        )
                        .withTarget(new Target()
                                .withPackageName("org.jooq.codegen")
                                .withDirectory("src/main/java")
                ));
        GenerationTool.generate(configuration);
    }
}
