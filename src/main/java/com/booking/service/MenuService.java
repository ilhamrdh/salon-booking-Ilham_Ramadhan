package com.booking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.booking.models.Customer;
import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;
import com.booking.repositories.PersonRepository;
import com.booking.repositories.ServiceRepository;

public class MenuService {
    private static List<Person> personList = PersonRepository.getAllPerson();
    private static List<Service> serviceList = ServiceRepository.getAllService();
    private static List<Reservation> reservationList = new ArrayList<>();
    private static Scanner input = new Scanner(System.in);

    public static void mainMenu() {
        String[] mainMenuArr = { "Show Data", "Create Reservation", "Complete/cancel reservation",
                "Find Customer by id", "Exit" };
        String[] subMenuArr = { "Recent Reservation", "Show Customer", "Show Employee", "History Reservation",
                "History Cancel",
                "Back to main menu" };

        int optionMainMenu;
        int optionSubMenu;

        boolean backToMainMenu = false;
        boolean backToSubMenu = false;
        do {
            PrintService.printMenu("\nMain Menu", mainMenuArr);
            optionMainMenu = Integer.valueOf(input.nextLine());
            switch (optionMainMenu) {
                case 1:
                    do {
                        PrintService.printMenu("\nShow Data", subMenuArr);
                        optionSubMenu = Integer.valueOf(input.nextLine());
                        // Sub menu - menu 1
                        switch (optionSubMenu) {
                            case 1:
                                // panggil fitur tampilkan recent reservation
                                PrintService.showRecentReservation(reservationList);
                                break;
                            case 2:
                                // panggil fitur tampilkan semua customer
                                PrintService.showAllCustomer(personList);
                                break;
                            case 3:
                                // panggil fitur tampilkan semua employee
                                PrintService.showAllEmployee(personList);
                                break;
                            case 4:
                                // panggil fitur tampilkan history reservation + total keuntungan
                                PrintService.showHistoryReservation(reservationList);
                                break;
                            case 5:
                                // panggil fitur tampilkan history reservation + total keuntungan
                                PrintService.showHistoryReservationCancel(reservationList);
                                break;
                            case 0:
                                backToSubMenu = true;
                        }
                    } while (!backToSubMenu);
                    break;
                case 2:
                    // panggil fitur menambahkan reservation
                    ReservationService.createReservation(personList, serviceList, reservationList);
                    break;
                case 3:
                    // panggil fitur mengubah workstage menjadi finish/cancel
                    ReservationService.editReservationWorkstage(reservationList);
                    break;
                case 4:
                    System.out.print("Masukan id customer: ");
                    String customerId = input.nextLine();
                    if (!ValidationService.validateId(customerId, personList)) {
                        break;
                    }
                    Customer customer = ReservationService.getCustomerByCustomerId(personList, customerId);
                    System.out.println("================ Data Customer ================");
                    System.out.println("Name: " + customer.getName());
                    System.out.println("Address: " + customer.getAddress());
                    System.out.println("Member: " + customer.getMember().getMembershipName());
                    System.out.println("Wallet: " + customer.getWallet());
                    break;
                case 0:
                    backToMainMenu = true;
                    break;
            }
        } while (!backToMainMenu);

    }
}
