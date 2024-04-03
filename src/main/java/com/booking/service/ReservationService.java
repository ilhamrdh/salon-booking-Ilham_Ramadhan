package com.booking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.booking.models.Customer;
import com.booking.models.Employee;
import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;

public class ReservationService {
    private static final Scanner input = new Scanner(System.in);

    public static void createReservation(List<Person> personList, List<Service> serviceList,
            List<Reservation> reservationList) {
        System.out.println("Membuat reservation");
        System.out.println("=====================");
        System.out.print("Masukan id customer: ");
        String customerId = input.nextLine();
        Customer customer = personList.stream()
                .filter(person -> person instanceof Customer)
                .map(preson -> (Customer) preson)
                .filter(preson -> preson.getId().equals(customerId))
                .findFirst()
                .orElse(null);

        if (customer == null) {
            System.err.println("Id customer tidak ditemukan");
            return;
        }

        System.out.println("Service yang tersedia");
        for (int i = 0; i < serviceList.size(); i++) {
            System.out.println(
                    (i + 1) + ". " + serviceList.get(i).getServiceName() + " - " + serviceList.get(i).getPrice());
        }

        System.out.print("Masukan nomor service (Pisahkan dengan tanda koma (,)): ");
        String numberService = input.nextLine();
        String[] numbers = numberService.split(",");

        List<Service> selectedService = new ArrayList<>();
        double totalReservationPrice = 0.0;

        for (String number : numbers) {
            int idx = Integer.parseInt(number.trim()) - 1;
            if (idx >= 0 && idx < serviceList.size()) {
                selectedService.add(serviceList.get(idx));
                totalReservationPrice += serviceList.get(idx).getPrice();
            }
        }

        System.out.print("Masukan id employee: ");
        String employeeId = input.nextLine();
        Employee employee = personList.stream()
                .filter(preson -> preson instanceof Employee)
                .map(preson -> (Employee) preson)
                .filter(preson -> preson.getId().equals(employeeId))
                .findFirst()
                .orElse(null);

        if (employee == null) {
            System.err.println("Id employee tidak ditemukan");
            return;
        }

        System.out.print("Masukan tahapan reservasi (In Process, Finish, Canceled): ");
        String workstage = input.nextLine();
        if (!ValidationService.validateWorkstage(workstage)) {
            return;
        }

        Reservation reservation = Reservation.builder()
                .reservationId("RESV - " + (reservationList.size() + 1))
                .customer(customer)
                .employee(employee)
                .services(selectedService)
                .workstage(workstage)
                .reservationPrice(totalReservationPrice)
                .build();

        reservationList.add(reservation);
        System.out.println("Reservation berhasil dibuat");
    }

    public static void editReservationWorkstage(List<Reservation> reservationList) {
        System.out.println("Mengubah wrokstage");
        System.out.print("Masukan id reservasi: ");
        String reservationId = input.nextLine();
        Reservation reservation = reservationList.stream()
                .filter(resv -> resv.getReservationId().equals(reservationId))
                .findFirst()
                .orElse(null);

        if (reservation == null) {
            System.err.println("Reservation tidak ada");
        }

        System.out.print("Masukan workstage baru (Finish, Canceled): ");
        String workstage = input.nextLine();
        if (!ValidationService.validateWorkstage(workstage)) {
            return;
        }

        if ("Finish".equalsIgnoreCase(workstage)) {
            double price = reservation.getReservationPrice();
            Customer customer = reservation.getCustomer();

            if (price != 0.0 && customer != null) {
                double currentWallet = customer.getWallet();

                if (currentWallet < price) {
                    System.err.println("Saldo tidak mencukupi");
                    return;
                }

                double wallet = currentWallet - price;
                customer.setWallet(wallet);
            } else {
                System.err.println("Customer tidak ditemukan");
                return;
            }
        }

        reservation.setWorkstage(workstage);
        System.out.println("Workstage berhasil di perbaharui");
    }

    public static Customer getCustomerByCustomerId(List<Person> personList, String id) {
        return personList.stream()
                .filter(person -> person instanceof Customer)
                .map(person -> (Customer) person)
                .filter(customer -> customer.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Silahkan tambahkan function lain, dan ubah function diatas sesuai kebutuhan
}
