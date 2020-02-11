package main.java.com.projectBackEnd;

public class BackEndMain {

    public static void main(String []args){
        // Create a Database object
        System.out.println("Hello World");
        String credentials = ""; //localhost
        DatabaseInitialiser init = new DatabaseInitialiser(
            new SQLConnect(), credentials, new SQLLoginTable(), new SQLPageTable(), new SQLMedicineTable()
        ); //Sets up local host database.
    }

}
