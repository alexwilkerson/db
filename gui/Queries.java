public enum Queries {

    Q1( "Query 1",
            "List a company’s workers by name.",
            "SELECT per_id, per_name\n" +
                    "FROM person NATURAL JOIN works NATURAL JOIN job\n" +
                    "WHERE comp_id = '8' AND end_date IS NULL;"),

    Q2( "Query 2",
            "List a company's staff by salary in descending order.",
            "SELECT per_name, pay_rate\n" +
                    "FROM person NATURAL JOIN works NATURAL JOIN job\n" +
                    "WHERE comp_id = '8' AND pay_type = 'salary'\n" +
                    "ORDER BY pay_rate DESC;"),

    Q3( "Query 3",
            "List companies’ labor cost (total salaries and wage rates by 1920 hours)\n" +
                    "in descending order.\n",
            "SELECT comp_id, SUM(CASE WHEN pay_type = 'salary'\n" +
                    "    THEN pay_rate  ELSE pay_rate*1920 END) AS total_labor_cost\n" +
                    "\n" +
                    "FROM job NATURAL JOIN works\n" +
                    "GROUP BY comp_id\n" +
                    "ORDER BY total_labor_cost DESC;"),

    Q4( "Query 4",
            "Given a persons identifier, find all the jobs this person is\n" + "" +
                    "currently holding and worked in the past.",
            "SELECT per_id, job_code, start_date, end_date\n" +
                    "FROM works NATURAL JOIN job\n" +
                    "WHERE per_id = 1\n" +
                    "ORDER BY start_date DESC;"),

    Q5( "Query 5",
            "List a person’s knowledge/skills in a readable format.",
            "SELECT ks_title,ks_level, ks_description\n" +
                    "FROM has_skill NATURAL JOIN knowledge_skill\n" +
                    "WHERE per_id = 1;");

    private final String query;
    private final String desc;
    private final String sql;

    Queries(String query, String desc, String sql) {
        this.query = query;
        this.desc = desc;
        this.sql = sql;
    }

    public String getQuery() {
        return query;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }

    @Override
    public String toString() { return query; }
}
