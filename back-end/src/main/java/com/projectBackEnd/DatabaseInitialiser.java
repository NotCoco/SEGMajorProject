package main.java.com.projectBackEnd;

public class DatabaseInitialiser {
    public DatabaseInitialiser(DatabaseConnect connect,String credentials,LoginTable login, PageTable page, MedicineTable medicine) {
        connect.connectToDatabase(credentials);
        connect.queryDatabase(login.initialise());
        connect.queryDatabase(medicine.initialise());
        connect.queryDatabase(page.initialise());
        connect.disconnectFromDatabase();
    }
}
