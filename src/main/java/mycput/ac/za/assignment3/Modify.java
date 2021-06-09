/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycput.ac.za.assignment3;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * @author E7240
 */
public class Modify {

    ObjectInputStream input;
    static ArrayList<Customer> CustomerList = new ArrayList<>();
    static ArrayList<Supplier> SupplierList = new ArrayList<>();
    private int rentCount;
    private int cantRent;

    public void openFile() {
        try {
            input = new ObjectInputStream(new FileInputStream("stakeholder.ser"));
        } catch (IOException ioe) {
            System.out.println("Error opening File: " + ioe.getMessage());
            System.exit(1);
        }
    }

    public void closeFile() {
        try {
            input.close();
        } catch (IOException ioe) {
            System.out.println("Error closing file: " + ioe.getMessage());
            System.exit(0);
        }
    }

    public void intoList() {
        try {
            while (true) {
                Object c = input.readObject();
                if (c instanceof Supplier) {
                    SupplierList.add((Supplier) c);
                } else {
                    CustomerList.add((Customer) c);
                }
            }
        } catch (EOFException eof) {
            System.out.println("end of file reached");
        } catch (ClassNotFoundException cof) {
            System.out.println("class not found");
        } catch (IOException iof) {
            System.out.println("Inputs not found");
        } finally {
            closeFile();
        }
    }
    public static Comparator<Customer> Cuscomparator = new Comparator<Customer>() {
        @Override
        public int compare(Customer c1, Customer c2) {
            String cust1 = c1.getStHolderId();
            String cust2 = c2.getStHolderId();

            //ascending order
            return cust1.compareTo(cust2);
        }
    };
    public static Comparator<Supplier> Supcomparator1 = new Comparator<Supplier>() {

        @Override
        public int compare(Supplier s1, Supplier s2) {
            String supp1 = s1.getStHolderId();
            String supp2 = s2.getStHolderId();

            //ascending order
            return supp1.compareTo(supp2);
        }
    };

    public void writeSupplierTotext() {
        try {
            FileWriter fw = new FileWriter("supplierOutFile.txt");
            BufferedWriter buff = new BufferedWriter(fw);
            buff.write(String.format("================== SUPPLIERS ==========================\n"));
            buff.write(String.format("%-12s%-12s%-12s%-12s\n", "ID", "Name", "Prod Type", "Description"));
            buff.write("=======================================================" + "\n");
            for (int x = 0; x < SupplierList.size(); x++) {
                buff.write(String.format("%-12s%-12s%-12s%-12s\n", SupplierList.get(x).getStHolderId(), SupplierList.get(x).getName(), SupplierList.get(x).getProductType(), SupplierList.get(x).getProductDescription()));
            }
            buff.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void Rent() {
        for (int i = 0; i < CustomerList.size(); i++) {
            if (CustomerList.get(i).getCanRent()) {
                rentCount++;
            } else {
                cantRent++;
            }
        }
    }

    public void writeCustomerToText() {
        try {
            FileWriter fw = new FileWriter("customerOutFile.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("======================== CUSTOMERS ==============================" + "\n");
            bw.write(String.format("%-12s\t%-12s\t%-12s\t%-12s\t%-12s\n", "ID", "Name", "Surname", "Date of Birth", "Age"));
            bw.write("=================================================================" + "\n");

            for (int i = 0; i < CustomerList.size(); i++) {

                bw.write(String.format("%-12s\t%-12s\t%-12s\t%-12s\t%-12s\n", CustomerList.get(i).getStHolderId(), CustomerList.get(i).getFirstName(), CustomerList.get(i).getSurName(), CustomerList.get(i).getDateOfBirth(), determineAge(i))); //CustomerList.get(i).get()));

            }
            bw.write("\nNumber of customers that can rent  "+ rentCount);
            bw.write("\nNumber of customers that cannot rent  "+ cantRent);

            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int determineAge(int cus) {

        try {
            String custAge = CustomerList.get(cus).getDateOfBirth();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            Date da = sdf.parse(custAge);
            Calendar c = Calendar.getInstance();
            c.setTime(da);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int date = c.get(Calendar.DATE);
            LocalDate l1 = LocalDate.of(year, month, date);
            LocalDate now = LocalDate.now();
            Period age = Period.between(l1, now);
            return age.getYears();

        } catch (ParseException e) {
            System.out.println("Sytem Parse Error");
            return 0;
        }
    }

    public void changeDateForm() {
        try {
            for (int x = 0; x < CustomerList.size(); x++) {
                String date = CustomerList.get(x).getDateOfBirth();

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date D = format.parse(date);

                SimpleDateFormat formatNew = new SimpleDateFormat("dd MMM yyyy ");
                CustomerList.get(x).setDateOfBirth(formatNew.format(D));
            }

        } catch (Exception e) {

        }
    }

    public static void main(String args[]) {
        Modify obj = new Modify();
        obj.openFile();
        obj.intoList();
        Collections.sort(CustomerList, Cuscomparator);
        Collections.sort(SupplierList, Supcomparator1);
        obj.changeDateForm();
        obj.Rent();
        obj.writeSupplierTotext();
        obj.writeCustomerToText();

    }
}
//end main    

