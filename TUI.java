import java.sql.*;
import java.util.Scanner;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

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
            List<HashMap<String,Object>> test = convertResultSetToList(rs);
            System.out.println(test);
            //while(rs.next())
            //    System.out.println(rs.getString(1));

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
    
    public static List<HashMap<String,Object>> convertResultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();

        while (rs.next()) {
            HashMap<String,Object> row = new HashMap<String, Object>(columns);
            for(int i=1; i<=columns; ++i) {
                row.put(md.getColumnName(i),rs.getObject(i));
            }
            list.add(row);
        }

        return list;
    }

}
