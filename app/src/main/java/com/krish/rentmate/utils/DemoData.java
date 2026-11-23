package com.krish.rentmate.utils;

import com.krish.rentmate.R;
import com.krish.rentmate.models.Expense;
import com.krish.rentmate.models.Property;
import com.krish.rentmate.models.Roommate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemoData {

    private static List<Property> properties;
    private static List<Roommate> roommates;
    private static List<Expense> expenses;

    public static List<Property> getProperties() {
        if (properties == null) {
            properties = new ArrayList<>();
            properties.add(new Property(
                    "p1",
                    "Modern Studio near Gurgaon",
                    "Sector 56, Gurugram",
                    12000,
                    4.8f,
                    "Studio",
                    1, 1,
                    "Fully Furnished",
                    "A beautiful and compact studio apartment located near Sector 56 Metro Station. Features high-speed Wi-Fi, modern kitchenette, AC, and 24/7 security.",
                    "Wi-Fi, AC, Power Backup, Gym, Kitchenette, Security",
                    false,
                    R.drawable.ic_property_placeholder1
            ));

            properties.add(new Property(
                    "p2",
                    "Student-friendly shared apartment",
                    "Sector 62, Gurugram",
                    8500,
                    4.6f,
                    "Shared Apartment",
                    3, 2,
                    "Semi-Furnished",
                    "Spacious 3 BHK shared apartment ideal for college students and interns. Walking distance to tech parks and public transit.",
                    "Wi-Fi, Washing Machine, Balcony, Elevator, RO Water",
                    false,
                    R.drawable.ic_property_placeholder2
            ));

            properties.add(new Property(
                    "p3",
                    "Cozy private room",
                    "Sohna Road, Gurugram",
                    10000,
                    4.7f,
                    "Private Room",
                    1, 1,
                    "Fully Furnished",
                    "Quiet private bedroom with attached balcony in a premium gated community. Includes daily maid service and high-speed internet.",
                    "AC, Housekeeping, TV, Parking, Gated Community",
                    false,
                    R.drawable.ic_property_placeholder3
            ));

            properties.add(new Property(
                    "p4",
                    "Luxury 2BHK Student Residency",
                    "DLF Phase 3, Gurugram",
                    18000,
                    4.9f,
                    "Apartment",
                    2, 2,
                    "Fully Furnished",
                    "Premium 2 BHK property with modern modular kitchen, stylish interior, and rooftop lounge area.",
                    "Modular Kitchen, Rooftop Lounge, Wi-Fi, 24/7 Power, Elevator",
                    false,
                    R.drawable.ic_property_placeholder4
            ));

            properties.add(new Property(
                    "p5",
                    "Affordable PG for Boys/Girls",
                    "Sector 43, Gurugram",
                    7500,
                    4.5f,
                    "PG",
                    2, 1,
                    "Furnished",
                    "Clean and managed PG accommodation with 3 meals included daily, laundry facilities, and study area.",
                    "Meals Included, Wi-Fi, Laundry, CCTV, Study Desk",
                    false,
                    R.drawable.ic_property_placeholder5
            ));
        }
        return properties;
    }

    public static List<Roommate> getRoommates() {
        if (roommates == null) {
            roommates = new ArrayList<>();
            roommates.add(new Roommate(
                    "r1",
                    "Aarav Sharma",
                    21,
                    "Computer Science Student",
                    "Gurugram",
                    8000,
                    12000,
                    92,
                    Arrays.asList("Clean", "Non-smoker", "Night owl", "Tech Enthusiast"),
                    "Hey! I'm Aarav, a 3rd year CS student. I spend most nights coding or gaming. Looking for a chilled roommate who keeps common areas clean.",
                    false,
                    R.drawable.ic_avatar_placeholder
            ));

            roommates.add(new Roommate(
                    "r2",
                    "Ananya Roy",
                    22,
                    "Design Intern",
                    "Sector 56, Gurugram",
                    10000,
                    15000,
                    88,
                    Arrays.asList("Early bird", "Non-smoker", "Fitness", "Social"),
                    "Hi! I work as a UI designer in Sector 56. I love morning coffee, yoga, and keeping a positive home environment.",
                    false,
                    R.drawable.ic_avatar_female
            ));

            roommates.add(new Roommate(
                    "r3",
                    "Rohan Verma",
                    23,
                    "Software Engineer",
                    "DLF Phase 3, Gurugram",
                    12000,
                    18000,
                    95,
                    Arrays.asList("Quiet", "Clean", "Non-smoker", "Foodie"),
                    "Software developer working remotely 3 days a week. Respect privacy, keep things tidy, and love weekend cooking.",
                    false,
                    R.drawable.ic_avatar_placeholder
            ));

            roommates.add(new Roommate(
                    "r4",
                    "Priya Kapoor",
                    20,
                    "Business Student",
                    "Sohna Road, Gurugram",
                    7000,
                    11000,
                    85,
                    Arrays.asList("Vegetarian", "Night owl", "Music Lover", "Clean"),
                    "MBA student looking for a shared apartment near campus. Easygoing, cheerful, and loves dogs!",
                    false,
                    R.drawable.ic_avatar_female
            ));
        }
        return roommates;
    }

    public static List<Expense> getExpenses() {
        if (expenses == null) {
            expenses = new ArrayList<>();
            expenses.add(new Expense(
                    "e1",
                    "Monthly Rent",
                    12000.0,
                    "Oct 01, 2026",
                    "Rent",
                    "Paid",
                    "Krish",
                    "Split (3 people)",
                    R.drawable.ic_house
            ));

            expenses.add(new Expense(
                    "e2",
                    "Electricity Bill",
                    1850.0,
                    "Oct 05, 2026",
                    "Electricity",
                    "Pending",
                    "Aarav",
                    "Split (3 people)",
                    R.drawable.ic_flash
            ));

            expenses.add(new Expense(
                    "e3",
                    "Grocery Shopping",
                    2400.0,
                    "Oct 10, 2026",
                    "Groceries",
                    "Paid",
                    "Krish",
                    "Split (3 people)",
                    R.drawable.ic_shopping_cart
            ));

            expenses.add(new Expense(
                    "e4",
                    "Wi-Fi Bill",
                    999.0,
                    "Oct 12, 2026",
                    "Internet",
                    "Paid",
                    "Rohan",
                    "Split (3 people)",
                    R.drawable.ic_wifi
            ));

            expenses.add(new Expense(
                    "e5",
                    "Water Supply & Maintenance",
                    650.0,
                    "Oct 15, 2026",
                    "Water",
                    "Pending",
                    "Krish",
                    "Split (3 people)",
                    R.drawable.ic_water
            ));
        }
        return expenses;
    }

    public static void addExpense(Expense expense) {
        if (expenses == null) {
            getExpenses();
        }
        expenses.add(0, expense);
    }
}