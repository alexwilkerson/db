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
                            query = "select per_name\n" +
                                    "from person natural join works natural join job\n" +
                                    "where comp_id = '8' and end_date is null";
                            break;
                        case 2:
                            implemented = true;
                            query = "select per_name, pay_rate\n" +
                                    "from person natural join works natural join job\n" +
                                    "where comp_id = '8' and pay_type = 'salary'\n" +
                                    "order by pay_rate desc";
                            break;
                        case 3:
                            implemented = true;
                            query = "select comp_id, sum(case when pay_type = 'salary'\n" +
                                    "\tthen pay_rate else pay_rate*1920 end) as total_labor_cost\n\n" +
                                    "from job natural join works\n" +
                                    "group by comp_id\n" +
                                    "order by total_labor_cost desc";
                            break;
                        case 4:
                            implemented = true;
                            query = "select cate_title, job_code, start_date, end_date\n" +
                                    "from person natural join works natural join has_category\n" +
                                    "natural join job_category\n" +
                                    "where per_id = 1\n" +
                                    "order by start_date asc";
                            break;
                        case 5:
                            implemented = true;
                            query = "select ks_title, ks_level, ks_description\n" +
                                    "from has_skill natural join knowledge_skill\n" +
                                    "where per_id = 1";
                            break;
                        case 6:
                            implemented = true;
                            query = "(select ks_code, ks_title\n" +
                                    "from required_skill natural join works natural join job\n" +
                                    "natural join knowledge_skill\n" +
                                    "where per_id = 1)\n" +
                                    "minus\n" +
                                    "(select ks_code, ks_title \n" +
                                    "from has_skill natural join knowledge_skill\n" +
                                    "where per_id = 1)";
                            break;
                        case 8:
                            implemented = true;
                            query = "(select ks_code, ks_title\n" +
                                    "from required_skill natural join job natural join knowledge_skill\n" +
                                    "where job_code = 2)\n" +
                                    "minus\n" +
                                    "(select ks_code, ks_title \n" +
                                    "from has_skill natural join knowledge_skill\n" +
                                    "where per_id = 4)";
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
        System.out.println("Query: ");
        System.out.println(query);
        System.out.println();
        try{
            // step 1 load the driver class
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // step 2 create the connection object
            Connection con=DriverManager.getConnection(
                "jdbc:oracle:thin:@dbsvcs.cs.uno.edu:1521:orcl", "awilkers", "zT7RXLfP");
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
