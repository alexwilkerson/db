import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class Person {
    private String name;
    private String gender;
    private int streetNumber;
    private String streetName;
    private String aptNumber;
    private String city;
    private String state;
    private int zipCode;
    private String email;
    private Connection c;
    private String user, pass;

    Person(String name, String aptNumber, int streetNumber, String streetName, String city, String state, int zipCode, String email, String gender, String user, String pass) {
        this.name = name;
        this.gender = gender;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.aptNumber = aptNumber;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.email = email;
        this.user = user;
        this.pass = pass;
    }

    boolean validateData() {
        if (!name.equals("") || !gender.equals("") || !streetName.equals("") || city.length() > 100 || !city.equals("") || !state.equals("") || aptNumber.length() > 100 || email.length() > 100)
            return false;
        return true;
    }

    boolean createInDB() {
        try {
            String SQL = "insert into PERSON (PER_NAME,APT_NUMBER,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,EMAIL,GENDER) values (?,?,?,?,?,?,?,?,?)";

            Connection c;
            c = DBConnect.connect(user, pass);

            PreparedStatement preparedStatement = c.prepareStatement(SQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, aptNumber);
            preparedStatement.setInt(3, streetNumber);
            preparedStatement.setString(4, streetName);
            preparedStatement.setString(5, city);
            preparedStatement.setString(6, state);
            preparedStatement.setInt(7, zipCode);
            preparedStatement.setString(8, email);
            preparedStatement.setString(9, gender);

            preparedStatement.execute();

            c.close();
        } catch (SQLException e) {
            AlertBox.display("Error running SQL!", "Some sort of error occurred while trying to add Employee to database.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
