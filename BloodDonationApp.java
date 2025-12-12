import java.sql.*;
import java.util.Scanner;

public class BloodDonationApp { 
    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "Praveen_07";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection Successful!");

            while (true) {
                System.out.println("\n===== BLOOD DONATION MANAGEMENT =====");
                System.out.println("1. Add Donor");
                System.out.println("2. View Donors");
                System.out.println("3. Search Donor by Blood Group");
                System.out.println("4. Update Donor");
                System.out.println("5. Delete Donor");
                System.out.println("6. Reports (Statistics)"); 
                System.out.println("7. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> addDonor(con, sc);
                    case 2 -> viewDonors(con);
                    case 3 -> searchDonor(con, sc);
                    case 4 -> updateDonor(con, sc);
                    case 5 -> deleteDonor(con, sc);
                    case 6 -> reports(con);
                    case 7 -> {
                        System.out.println("Exiting...");
                        con.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }


    private static void addDonor(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter blood group: ");
        String bloodGroup = sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        System.out.print("Enter phone: ");
        String phone = sc.nextLine();

        String query = "INSERT INTO donors (name, blood_group, email, phone) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, name);
        pst.setString(2, bloodGroup);
        pst.setString(3, email);
        pst.setString(4, phone);

        int rows = pst.executeUpdate();
        if (rows > 0) System.out.println("Donor added successfully!");
    }

    private static void viewDonors(Connection con) throws SQLException {
        String query = "SELECT * FROM donors ORDER BY id";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        System.out.println("\n---- Donor List ----");
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " | " +
                               rs.getString("name") + " | " +
                               rs.getString("blood_group") + " | " +
                         
                               rs.getString("email") + " | " +
                               rs.getString("phone"));
        }
    }
    private static void searchDonor(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter blood group to search: ");
        String bloodGroup = sc.nextLine();

        String query = "SELECT * FROM donors WHERE blood_group = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, bloodGroup);
        ResultSet rs = pst.executeQuery();

        System.out.println("\n---- Search Results ----");
        boolean found = false;
        while (rs.next()) {
            found = true;
            System.out.println(rs.getInt("id") + " | " +
                               rs.getString("name") + " | " +
                               rs.getString("email") + " | " +
                               rs.getString("phone"));
        }
        if (!found) System.out.println("No donors found for blood group " + bloodGroup);
    }

    private static void updateDonor(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter donor ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter new email: ");
        String email = sc.nextLine();
        System.out.print("Enter new phone: ");
        String phone = sc.nextLine();

        String query = "UPDATE donors SET email = ?, phone = ? WHERE id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, email);
        pst.setString(2, phone);
        pst.setInt(3, id);

        int rows = pst.executeUpdate();
        if (rows > 0) System.out.println("Donor updated successfully!");
        else System.out.println("Donor ID not found!");
    }

    private static void deleteDonor(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter donor ID to delete: ");
        int id = sc.nextInt();

        String query = "DELETE FROM donors WHERE id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, id);

        int rows = pst.executeUpdate();
        if (rows > 0) System.out.println(" Donor deleted successfully!");
        else System.out.println("Donor ID not found!");
    }

    
    private static void reports(Connection con) throws SQLException {
        String query = "SELECT blood_group, COUNT(*) as total FROM donors GROUP BY blood_group";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        System.out.println("\n---- Donor Statistics ----");
        while (rs.next()) {
            System.out.println(rs.getString("blood_group") + " : " + rs.getInt("total") + " donors");
        }
    }
}