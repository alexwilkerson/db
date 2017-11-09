import java.sql.*;
import java.util.Scanner;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;

class TUI {
    public static String choice;
    public static Scanner input = new Scanner(System.in);
    public static boolean exit = false;
    public static String query;
    public static String description;
    public static boolean implemented = false;
    public static void main(String args[]){
        printWelcomeMsg();
        // TUI loop
        while(!exit){
            System.out.print("Please enter a number (1-26) or 'q' to quit: ");
            if (validChoice()) {
                if (choice.equals("q") || choice.equals("Q")) {
                    System.out.println();
                    System.out.println("Thank you, come again.");
                    System.exit(0);
                }
                if (isNumber(choice)) {
                    switch(Integer.parseInt(choice)) {
                        case 1:
                            implemented = true;
                            description = "1. List a company’s workers by names.";
                            query = "select per_name\n" +
                                    "from person natural join works natural join job\n" +
                                    "where comp_id = '8' and end_date is null";
                            break;
                        case 2:
                            implemented = true;
                            description = "2. List a company’s staff by salary in descending order.";
                            query = "select per_name, pay_rate\n" +
                                    "from person natural join works natural join job\n" +
                                    "where comp_id = '8' and pay_type = 'salary'\n" +
                                    "order by pay_rate desc";
                            break;
                        case 3:
                            implemented = true;
                            description = "3. List companies’ labor cost (total salaries and wage rates by 1920 hours)\nin descending order.";
                            query = "select comp_id, sum(case when pay_type = 'salary'\n" +
                                    "\tthen pay_rate else pay_rate*1920 end) as total_labor_cost\n\n" +
                                    "from job natural join works\n" +
                                    "group by comp_id\n" +
                                    "order by total_labor_cost desc";
                            break;
                        case 4:
                            implemented = true;
                            description = "4. Find all the jobs a person is currently holding and worked in the past.";
                            query = "select cate_title, job_code, start_date, end_date\n" +
                                    "from person natural join works natural join has_category\n" +
                                    "natural join job_category\n" +
                                    "where per_id = 1\n" +
                                    "order by start_date asc";
                            break;
                        case 5:
                            implemented = true;
                            description = "5. List a person’s knowledge/skills in a readable format.";
                            query = "select ks_title, ks_level, ks_description\n" +
                                    "from has_skill natural join knowledge_skill\n" +
                                    "where per_id = 1";
                            break;
                        case 6:
                            implemented = true;
                            description = "6. List the skill gap of a worker between his/her job(s) and his/her skills.";
                            query = "(select ks_code, ks_title\n" +
                                    "from required_skill natural join works natural join job\n" +
                                    "natural join knowledge_skill\n" +
                                    "where per_id = 1)\n" +
                                    "minus\n" +
                                    "(select ks_code, ks_title \n" +
                                    "from has_skill natural join knowledge_skill\n" +
                                    "where per_id = 1)";
                            break;
                        case 7:
                            implemented = true;
                            description = "7. List the required knowledge/skills of a job in a readable format.";
                            query = "select ks_code, ks_title, ks_level, ks_description\n" +
                                    "from required_skill natural join knowledge_skill\n" +
                                    "where job_code = 1";
                            if (!runQuery(query))
                                System.out.println("An error occurred.");
                            implemented = true;
                            description = "7. List the required knowledge/skills of a job category in a readable format.";
                            query = "select ks_code, ks_title, ks_level, ks_description\n" +
                                    "from skill_set natural join knowledge_skill\n" +
                                    "where cate_code = 1";
                            break;

                        case 8:
                            implemented = true;
                            description = "8. List a person’s missing knowledge/skills for a specific job in a readable\nformat.";
                            query = "(select ks_code, ks_title\n" +
                                    "from required_skill natural join job natural join knowledge_skill\n" +
                                    "where job_code = 2)\n" +
                                    "minus\n" +
                                    "(select ks_code, ks_title \n" +
                                    "from has_skill natural join knowledge_skill\n" +
                                    "where per_id = 4)";
                            break;
                        case 9:
                            implemented = true;
                            description = "9. List the courses (course id and title) that each alone teaches all the\nmissing knowledge/skills for a person to pursue a specific job.";
                            query = "with\n" +
                                    "missing_ks(ks) as\n" +
                                    "((select ks_code \n" +
                                    "from required_skill natural join job\n" +
                                    "where job_code = 1)\n" +
                                    "minus\n" +
                                    "(select ks_code\n" +
                                    "from has_skill\n" +
                                    "where per_id = 1))\n\n" +

                                    "select c_code, c_title\n" +
                                    "from course c\n" +
                                    "where not exists\n" +
                                    "((select *\n" +
                                    "from missing_ks)\n" +
                                    "minus\n" +
                                    "(select ks_code\n" +
                                    "from teaches_skill ts\n" +
                                    "where ts.c_code = c.c_code))";
                            break;
                        case 13:
                            implemented = true;
                            description = "13. List all the job categories that a person is qualified for.";
                            query = "select cate_code, cate_title\n" +
                                    "from job_category jc\n" +
                                    "where not exists\n" +
                                    "((select ks_code\n" +
                                    "from skill_set ss\n" +
                                    "where jc.cate_code = ss.cate_code)\n" +
                                    "minus\n" +
                                    "(select ks_code\n" +
                                    "from has_skill\n" +
                                    "where per_id = 2))";
                            break;
                        case 14:
                            implemented = true;
                            description = "14. Find the job with the highest pay rate for a person according to his/her\nskill qualification.";
                            query = "with pay_rate_table as\n" +
                                    "(select distinct rs.job_code\n" +
                                    "from required_skill rs\n" +
                                    "where not exists\n" +
                                    "((select ks_code\n" +
                                    "from required_skill rs2\n" +
                                    "where rs.job_code = rs2.job_code)\n" +
                                    "minus\n" +
                                    "(select ks_code \n" +
                                    "from has_skill\n" +
                                    "where per_id = 1)))\n\n" +

                                    "select job_code, cate_title,  pay_rate, pay_type\n" +
                                    "from job natural join pay_rate_table natural join has_category\n" +
                                    "natural join job_category\n" +
                                    "where pay_rate = (select max(case when pay_type = 'salary' then pay_rate else pay_rate*1920 end)\n" +
                                    "from job natural join pay_rate_table)";
                            break;
                        case 15:
                            implemented = true;
                            description = "15. List all the names along with the emails of the persons who are\nqualified for a job.";
                            query = "select per_name, email\n" +
                                    "from person p\n" +
                                    "where not exists\n" +
                                    "((select ks_code\n" +
                                    "from required_skill\n" +
                                    "where job_code = 1)\n" +
                                    "minus\n" +
                                    "(select ks_code\n" +
                                    "from has_skill hs\n" +
                                    "where hs.per_id = p.per_id))";
                            break;
                        case 16:
                            implemented = true;
                            description = "16. When a company cannot find any qualified person for a job, a secondary\nsolution is to find a person who is almost qualified to the job. Make a\n“missing-one” list that lists people who miss only one skill for a specified\njob.";
                            query = "select per_id, per_name\n" +
                                    "from person p\n" +
                                    "where  1 = (select count(*)\n" +
                                    "            from ((select ks_code\n" +
                                    "                    from required_skill\n" +
                                    "                    where job_code = 1)\n" +
                                    "            minus\n" +
                                    "            (select ks_code\n" +
                                    "            from has_skill hs\n" +
                                    "            where hs.per_id = p.per_id)))";
                            break;
                        case 21:
                            implemented = true;
                            description = "21. In a local or national crisis, we need to find all the people who once\nheld a job of the special job category identifier.";
                            query = "with unemployed(per_id) as\n" +
                                    "((select per_id from person)\n" +
                                    "minus\n" +
                                    "(select per_id from works where end_date < CURRENT_DATE))\n\n" +
                                    "select per_id\n" +
                                    "from unemployed natural join job natural join has_category\n" +
                                    "where cate_code = 1";
                            break;
                        case 22:
                            implemented = true;
                            description = "22. Find all the unemployed people who once held a job of the given job\nidentifier.";
                            query = "with unemployed(per_id) as\n" +
                                    "((select per_id from person)\n" +
                                    "minus\n" +
                                    "(select per_id from works where end_date < CURRENT_DATE))\n" +
                                    "select per_id\n" +
                                    "from unemployed natural join works\n" +
                                    "where  job_code = 8";
                            break;
                        case 23:
                            implemented = true;
                            description = "23. Find out the biggest employer in terms of number of employees or the\ntotal amount of salaries and wages paid to employees.";
                            query = "with\n" +
                                    "company_size(comp_id, employee_count) as\n" +
                                    "(select comp_id, count(*)\n" +
                                    "from job natural join works\n" +
                                    "group by comp_id)\n\n" +
                                    "select comp_id employee_count\n" +
                                    "from company_size\n" +
                                    "where employee_count = ( select max ( employee_count ) from company_size)";
                            break;
                        case 24:
                            implemented = true;
                            description = "24. Find out the job distribution among business sectors; find out the\nbiggest sector in terms of number of employees or the total amount of\nsalaries and wages paid to employees.";
                            query = "with\n" +
                                    "sector_size(primary_sector, employee_count) as\n" +
                                    "(select primary_sector, count(*)\n" +
                                    "from job natural join works natural join company\n" +
                                    "group by primary_sector)\n\n" +
                                    "select primary_sector, employee_count\n" +
                                    "from sector_size\n" +
                                    "where employee_count = ( select max ( employee_count ) from sector_size)";
                            break;
                        default:
                            System.out.println();
                            System.out.println("Not yet implemented.");
                            System.out.println();
                            break;
                    }
                    if (implemented) {
                        if (!runQuery(query))
                            System.out.println("An error occurred.");
                    } 
                }
            } else {
                System.out.println();
                System.out.println("Invalid option. Try again.");
                System.out.println();

            }
        }
    }

    public static void printWelcomeMsg() {
        System.out.println("*********************************");
        System.out.println("* SQL Statement Validator Alpha *");
        System.out.println("*   by: Bongcasan & Wilkerson   *");
        System.out.println("*********************************");
        System.out.println();
    }

    public static boolean validChoice(){
        choice = input.nextLine();
        if (isNumber(choice)) {
            int choiceNum = Integer.parseInt(choice);
            if ((choiceNum > 0) && (choiceNum <= 26))
                return true;
            else
                return false;
        }
        if (choice.equals("q") || choice.equals("Q")) {
            return true;
        }else{
            return false;
        } 
    }

    public static boolean isNumber(String input) {
        try {
            Integer.parseInt(input);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean runQuery(String q) {
        implemented = false;
        System.out.println();
        System.out.println(description);
        System.out.println();
        System.out.println(query + ";");
        System.out.println();
        try{
            // step 1 load the driver class
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // step 2 create the connection object
            Connection con=DriverManager.getConnection(
                "jdbc:oracle:thin:@dbsvcs.cs.uno.edu:1521:orcl", secrets.user, secrets.password);
            // step 3 create the statement object
            Statement stmt = con.createStatement();
            // step 4 execute query
            ResultSet rs = stmt.executeQuery(q);
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            for (int i = 1; i <= cols; i++) {
                System.out.print(String.format("%-30s", rsmd.getColumnName(i)));
            }
            System.out.println();
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    String colValue = rs.getString(i);
                    System.out.print(String.format("%-30s", colValue));
                }
                System.out.println();
    
            }
           
            // step 5 close the connection object
            con.close();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println();
            return false;
        }
        System.out.println();
        return true;
    }

}
