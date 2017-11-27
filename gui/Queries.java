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
            "SELECT job_code, start_date, end_date\n" +
                    "FROM works NATURAL JOIN job\n" +
                    "WHERE per_id = 1\n" +
                    "ORDER BY start_date DESC;"),

    Q5( "Query 5",
            "List a person’s knowledge/skills in a readable format.",
            "SELECT ks_title, ks_level, ks_description\n" +
                    "FROM has_skill NATURAL JOIN knowledge_skill\n" +
                    "WHERE per_id = 1;"),
    Q6("Query 6",
            "List the skill gap of a worker between his/her job(s) AND his/her skills.",
            "(SELECT ks_code\n" +
                    "from required_skill NATURAL JOIN works \n" +
                    "WHERE per_id = 1)\n" +
                    "MINUS\n" +
                    "(SELECT ks_code\n" +
                    "FROM has_skill NATURAL JOIN knowledge_skill \n" +
                    "WHERE per_id = 1);"),
    Q7a("Query 7a",
            "List the required knowledge skill a job in a readable format.",
            "SELECT ks_code, ks_title, ks_level, ks_description\n" +
                    "FROM required_skill NATURAL JOIN knowledge_skill\n" +
                    "WHERE job_code = 1;"),
    Q7b("Query 7b",
            "List the required knowledge skill a job category in a readable format.",
            "SELECT ks_code, ks_title, ks_level, ks_description\n" +
                    "FROM skill_set NATURAL JOIN knowledge_skill\n" +
                    "WHERE cate_code = 1;"),
    Q8("Query 8",
            "List a person’s missing knowledge/skills for a specific job in a readable format.",
            "(SELECT ks_code, ks_title\n" +
                    "FROM required_skill NATURAL JOIN knowledge_skill\n" +
                    "WHERE job_code = 2)\n" +
                    "MINUS\n" +
                    "(SELECT ks_code, ks_title \n" +
                    "FROM hAS_skill NATURAL JOIN knowledge_skill \n" +
                    "WHERE per_id = 1);"),
    Q9("Query 9",
            "List the courses (course id AND title) that each alone teaches all the\n" +
                    "missing knowledge/skills for a person to pursue a specific job.",
            "WITH missing_ks(ks) AS (\n" +
                    "    (SELECT ks_code \n" +
                    "    FROM required_skill\n" +
                    "    WHERE job_code = 1)\n" +
                    "    MINUS\n" +
                    "    (SELECT ks_code\n" +
                    "    FROM has_skill\n" +
                    "    WHERE per_id = 1))\n" +
                    "SELECT c_code, c_title\n" +
                    "FROM course c\n" +
                    "WHERE NOT EXISTS(\n" +
                    "    (SELECT *\n" +
                    "    FROM missing_ks)\n" +
                    "    MINUS\n" +
                    "    (SELECT ks_code\n" +
                    "    FROM teaches_skill ts\n" +
                    "    WHERE ts.c_code = c.c_code));"),
    Q10("Query 10",
            "Suppose the skill gap of a worker AND the requirement of a desired job\n" +
                    "can be covered by one course. Find the “quickest” solution for this worker.\n" +
                    "Show the course, section information AND the completion date.",
            "WITH\n" +
                    "missing_ks(ks) AS (\n" +
                    "    (SELECT ks_code \n" +
                    "    FROM required_skill\n" +
                    "    WHERE job_code = 1)\n" +
                    "    MINUS\n" +
                    "    (SELECT ks_code\n" +
                    "    FROM has_skill\n" +
                    "    WHERE per_id = 1)),\n" +
                    "\n" +
                    "fulfilling_courses(c_code) AS (\n" +
                    "    SELECT c_code\n" +
                    "    FROM course c\n" +
                    "    WHERE NOT EXISTS (\n" +
                    "        (SELECT *\n" +
                    "        FROM missing_ks)\n" +
                    "        MINUS\n" +
                    "        (SELECT ks_code\n" +
                    "        FROM teaches_skill ts\n" +
                    "        WHERE ts.c_code = c.c_code))),\n" +
                    "\n" +
                    "fulfilling_section(c_code, complete_date) AS (\n" +
                    "    SELECT DISTINCT c_code, complete_date \n" +
                    "    FROM section NATURAL JOIN fulfilling_courses\n" +
                    "    WHERE complete_date >= TRUNC(sysdate)\n" +
                    "    )\n" +
                    "\n" +
                    "SELECT c_code, complete_date\n" +
                    "FROM fulfilling_section\n" +
                    "WHERE complete_date = (SELECT MIN(complete_date)\n" +
                    "                        FROM fulfilling_section);"),
    Q11("Query 11",
            "Find the cheapest course to make up one’s skill gap by showing the\n" +
                "course to take AND the cost (of the section price).",
            "WITH\n" +
            "missing_ks(ks) AS (\n" +
            "    (SELECT ks_code \n" +
            "    FROM required_skill\n" +
            "    WHERE job_code = 1)\n" +
            "    MINUS\n" +
            "    (SELECT ks_code\n" +
            "    FROM has_skill\n" +
            "    WHERE per_id = 1)),\n" +
            "\n" +
            "fulfilling_courses(c_code, c_title, retail_price) AS (\n" +
            "    SELECT c_code, c_title, retail_price\n" +
            "    FROM course c\n" +
            "    WHERE NOT EXISTS (\n" +
            "        (SELECT *\n" +
            "        FROM missing_ks)\n" +
            "        MINUS\n" +
            "        (SELECT ks_code\n" +
            "        FROM teaches_skill ts\n" +
            "        WHERE ts.c_code = c.c_code)))\n" +
            "\n" +
            "SELECT c_code, c_title, retail_price\n" +
            "FROM fulfilling_courses\n" +
            "WHERE retail_price = (SELECT MIN(retail_price) \n" +
            "                FROM fulfilling_courses NATURAL JOIN section);"),
    //Q12("Query 12",
    //        "If query #9 returns nothing, then find the course sets that their\n" +
    //                "combination covers all the missing knowledge/ skills for a person to pursue\n" +
    //                "a specific job. The considered course sets will not include more than three\n" +
    //                "courses. If multiple course sets are found, list the course sets (WITH their\n" +
    //                "course IDs) in the order of the ascending order of the course sets’ total\n" +
    //                "costs.",
    //        "WITH\n" +
    //                "missing_ks(ks) AS (\n" +
    //                "    (SELECT ks_code \n" +
    //                "    FROM required_skill\n" +
    //                "    WHERE job_code = 1)\n" +
    //                "    MINUS\n" +
    //                "    (SELECT ks_code\n" +
    //                "    FROM has_skill\n" +
    //                "    WHERE per_id = 1)),\n" +
    //                "\n" +
    //                "SELECT DISTINCT c_code, title\n" +
    //                "FROM course join section using (c_code) NATURAL JOIN teaches\n" +
    //                "WHERE ks_code IN ((SELECT ks_code\n" +
    //                "                   FROM job_category NATURAL JOIN required_skill\n" +
    //                "                   WHERE cate_code = '1')\n" +
    //                "                   MINUS\n" +
    //                "                  (SELECT ks_code\n" +
    //                "                   FROM person NATURAL JOIN has_skill\n" +
    //                "                   WHERE per_id = 1));"),
    Q13("Query 13",
            "List all the job categories that a person is qualified for.",
            "SELECT cate_code, cate_title\n" +
                    "FROM job_category jc\n" +
                    "WHERE NOT EXISTS (\n" +
                    "    (SELECT ks_code\n" +
                    "    FROM skill_set ss\n" +
                    "    WHERE jc.cate_code = ss.cate_code)\n" +
                    "    MINUS\n" +
                    "    (SELECT ks_code\n" +
                    "    FROM has_skill\n" +
                    "    WHERE per_id = 2));"),
    Q14("Query 14",
            "Find the job with the highest pay rate for a person according to his/her skill qualification.",
            "WITH \n" +
                    "qualified_jobs AS(\n" +
                    "    SELECT j.job_code\n" +
                    "    FROM job j\n" +
                    "    WHERE NOT EXISTS ((SELECT ks_code\n" +
                    "                        FROM required_skill rs\n" +
                    "                        WHERE j.job_code = rs.job_code)\n" +
                    "                        MINUS\n" +
                    "                        (SELECT ks_code\n" +
                    "                        FROM has_skill\n" +
                    "                        WHERE per_id = 1))),\n" +
                    "\n" +
                    "q_jobs_desc AS(\n" +
                    "    SELECT *\n" +
                    "    FROM job NATURAL JOIN qualified_jobs\n" +
                    ")\n" +
                    "\n" +
                    "SELECT job_code, pay_rate, pay_type\n" +
                    "FROM q_jobs_desc\n" +
                    "WHERE pay_rate = (SELECT MAX(CASE WHEN pay_type = 'salary' \n" +
                    "                                THEN pay_rate \n" +
                    "                                ELSE pay_rate*1920 END)\n" +
                    "                    FROM q_jobs_desc);"),
    Q15("Query 15",
            "List all the names along with the emails of the persons who are qualified for a job.",
            "SELECT per_name, email\n" +
                    "FROM person p\n" +
                    "WHERE NOT EXISTS (\n" +
                    "    (SELECT ks_code\n" +
                    "    FROM required_skill\n" +
                    "    WHERE job_code = 1)\n" +
                    "MINUS\n" +
                    "(SELECT ks_code\n" +
                    "FROM has_skill hs\n" +
                    "WHERE hs.per_id = p.per_id));\n"),
    Q16("Query 16",
            "When a company cannot find any qualified person for a job, a secondary\n" +
                    "solution is to find a person who is almost qualified to the job. Make a\n" +
                    "“missing-one” list that lists people who miss only one skill for a specified\n" +
                    "job.",
            "SELECT per_id, per_name\n" +
                    "FROM person p\n" +
                    "WHERE 1 = (SELECT COUNT(ks_code)\n" +
                    "            FROM ((SELECT ks_code\n" +
                    "                  FROM required_skill\n" +
                    "                   WHERE job_code =1)\n" +
                    "                  MINUS\n" +
                    "                 (SELECT ks_code\n" +
                    "                  FROM has_Skill hs \n" +
                    "                  WHERE hs.per_id = p.per_id)));\n"),
    Q17("Query 17",
            "List the skillID AND the number of people in the missing-one list for a\n" +
                    "given job code in the ascending order of the people counts.",
            "WITH skills_needed(ks_code) as (\n" +
                "        SELECT ks_code\n" +
                "        FROM required_skill\n" +
                "        WHERE job_code = '1'),\n" +
                "\n" +
                "missing_skills(per_id, ms_count) AS (\n" +
                "    SELECT per_id, COUNT(ks_code)\n" +
                "    FROM person p, skills_needed\n" +
                "    WHERE ks_code IN (\n" +
                "        (SELECT ks_code\n" +
                "        FROM skills_needed)\n" +
                "        MINUS\n" +
                "        (SELECT ks_code\n" +
                "        FROM has_skill\n" +
                "        WHERE per_id = p.per_id))\n" +
                "    GROUP BY per_id)\n" +
                "\n" +
                "SELECT ks_code, COUNT(per_id) AS total_ms_count\n" +
                "FROM missing_skills ms, skills_needed\n" +
                "WHERE ks_code IN (\n" +
                "    (SELECT ks_code\n" +
                "    FROM skills_needed)\n" +
                "    MINUS\n" +
                "    (SELECT ks_code\n" +
                "    FROM has_skill\n" +
                "    WHERE per_id = ms.per_id))\n" +
                "    AND ms_count = 1\n" +
                "GROUP BY ks_code\n" +
                "ORDER BY total_ms_count ASC;\n"),

    Q18("Query 18",
            "Suppose there is a new job that has nobody qualified. List the persons\n" +
                    "who miss the least number of skills that ar required for this job AND report the “least number”.",
            "WITH \n" +
                    "\n" +
                    "skills_needed(ks_code) AS (\n" +
                    "    SELECT ks_code\n" +
                    "    FROM required_skill\n" +
                    "    WHERE job_code = 1),\n" +
                    "\n" +
                    "missing_skills(per_id, ms_count) AS (\n" +
                    "    SELECT per_id, COUNT(ks_code)\n" +
                    "    FROM person p, skills_needed sn\n" +
                    "    WHERE sn.ks_code IN (\n" +
                    "        (SELECT ks_code\n" +
                    "        FROM required_skill)\n" +
                    "        MINUS\n" +
                    "        (SELECT ks_code\n" +
                    "        FROM has_skill\n" +
                    "        WHERE per_id = p.per_id))\n" +
                    "    GROUP BY per_id),\n" +
                    "\n" +
                    "min_missing_ks(min_ms_count) AS (\n" +
                    "    SELECT MIN(ms_count)\n" +
                    "    FROM missing_skills)\n" +
                    "\n" +
                    "SELECT per_id, ms_count\n" +
                    "FROM missing_skills JOIN min_missing_ks\n" +
                    "ON ms_count = min_missing_ks.min_ms_count;"),

    Q19("Query 19",
            "For a specified job category and a given small number k, make a “missing-k” list that lists the people’s IDs and\n" +
                    "the number of missing skills for the people who miss only up to k skills in the ascending order of missing skills.",
                "WITH \n" +
                        "\n" +
                        "skills_needed(ks_code) AS (\n" +
                        "    SELECT ks_code\n" +
                        "    FROM skill_set\n" +
                        "    WHERE cate_code = '1'),\n" +
                        "\n" +
                        "missing_skills(per_id, ms_count) AS (\n" +
                        "    SELECT per_id, COUNT(ks_code)\n" +
                        "    FROM person p, (SELECT ks_code\n" +
                        "                    FROM skills_needed) sn\n" +
                        "    WHERE sn.ks_code IN (\n" +
                        "        (SELECT ks_code\n" +
                        "        FROM skills_needed)\n" +
                        "        MINUS\n" +
                        "        (SELECT ks_code\n" +
                        "        FROM has_skill\n" +
                        "        WHERE per_id = p.per_id))\n" +
                        "    GROUP BY per_id)\n" +
                        "\n" +
                        "SELECT per_id, ms_count\n" +
                        "FROM missing_skills\n" +
                        "WHERE ms_count <= 3 --k\n" +
                        "ORDER BY ms_count ASC;"),
    Q20("Query 20",
            "Given a job code and its corresponding missing-k list specified in Question 19. Find every skill that is\n" +
                    "needed by at least one person in the given missing-k list. List each skillID and the number of people who need it\n" +
                    "in the descending order of the people counts.",
                "WITH \n" +
                        "\n" +
                        "skills_needed(ks_code) as (\n" +
                        "    SELECT ks_code\n" +
                        "    FROM required_skill\n" +
                        "    WHERE job_code = '1'),\n" +
                        "\n" +
                        "missing_skills(per_id, ms_count) AS (\n" +
                        "    SELECT per_id, COUNT(ks_code)\n" +
                        "    FROM person p, (SELECT ks_code\n" +
                        "                    FROM skills_needed) sn\n" +
                        "    WHERE sn.ks_code IN (\n" +
                        "        (SELECT ks_code\n" +
                        "        FROM skills_needed)\n" +
                        "        MINUS\n" +
                        "        (SELECT ks_code\n" +
                        "        FROM has_skill\n" +
                        "        WHERE per_id = p.per_id))\n" +
                        "    GROUP BY per_id),\n" +
                        "\n" +
                        "missing_people(per_id, ms_count) AS (\n" +
                        "    SELECT per_id, ms_count\n" +
                        "    FROM missing_skills\n" +
                        "    WHERE ms_count <= 3)\n" +
                        "\n" +
                        "SELECT ks_code, COUNT(per_id) as mp_count\n" +
                        "FROM missing_people p, skills_needed\n" +
                        "WHERE skills_needed.ks_code IN (\n" +
                        "    SELECT ks_code\n" +
                        "    FROM skills_needed\n" +
                        "    MINUS\n" +
                        "    SELECT ks_code\n" +
                        "    FROM has_skill\n" +
                        "    WHERE per_id = P.per_id)\n" +
                        "GROUP BY ks_code\n" +
                        "ORDER BY mp_count DESC;"),
    Q21("Query 21",
            "In a local or national crisis, we need to find all the people who once\n" +
                    "held a job of the special job category identifier.",
            "SELECT per_id\n" +
                    "FROM works NATURAL JOIN job NATURAL JOIN job_category\n" +
                    "where cate_code = 1; \n"),
    Q22("Query 22",
            "Find all the unemployed people who once held a job of the given job identifier.",
            "WITH unemployed(per_id) AS\n" +
                    "((SELECT per_id FROM person)\n" +
                    "MINUS\n" +
                    "(SELECT per_id FROM works WHERE end_date >= CURRENT_DATE))\n" +
                    "\n" +
                    "SELECT per_id\n" +
                    "FROM unemployed NATURAL JOIN works\n" +
                    "WHERE  job_code = '8';"),
    Q23("Query 23",
            "Find out the biggest employer in terms of number of employees or the\n" +
                    "total amount of salaries AND wages paid to employees.",
            "WITH \n" +
                    "company_size(comp_id, employee_COUNT) AS\n" +
                    "(SELECT comp_id, COUNT(*)\n" +
                    "FROM job NATURAL JOIN works\n" +
                    "GROUP BY comp_id)\n" +
                    "\n" +
                    "SELECT comp_id employee_COUNT\n" +
                    "FROM company_size\n" +
                    "WHERE employee_COUNT = ( SELECT MAX ( employee_count ) FROM company_size);"),
    Q24("Query 24",
            "Find out the job distribution among business sectors; find out the\n" +
                    "biggest sector in terms of number of employees or the total amount of\n" +
                    "salaries AND wages paid to employees.",
    "WITH\n" +
            "sector_size(primary_sector, employee_COUNT) AS\n" +
            "(SELECT primary_sector, COUNT(*)\n" +
            "FROM job NATURAL JOIN works NATURAL JOIN company\n" +
            "GROUP BY primary_sector)\n" +
            "\n" +
            "SELECT primary_sector, employee_COUNT\n" +
            "FROM sector_size\n" +
            "WHERE employee_COUNT = ( SELECT MAX ( employee_count ) FROM sector_size);");


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
