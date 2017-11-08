import java.sql.*;
import java.util.Scanner;

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
                                    "where comp_id = '8' and end_date is null\n";
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
        System.out.print(query);
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
            while(rs.next())
                System.out.println(rs.getString(1));

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
