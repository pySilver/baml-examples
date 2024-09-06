/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import com.boundaryml.baml_client.ApiClient;
import com.boundaryml.baml_client.ApiException;
import com.boundaryml.baml_client.Configuration;
import com.boundaryml.baml_client.model.*;
import com.boundaryml.baml_client.api.DefaultApi;

public class App {
    public static void main(String[] args) throws Exception {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        DefaultApi b = new DefaultApi(defaultClient);

        // Example 1: see 01-extract-receipt.baml
        {
            var image = new BamlImage(new BamlImageUrl().url("https://i.redd.it/adzt4bz4llfc1.jpeg"));
            var receipt = new ExtractReceiptRequestReceipt(image); 
            var req = (new ExtractReceiptRequest().receipt(receipt));
            var resp = b.extractReceipt(req);
            System.out.println(resp);
        }

        // Example 2: see 02-extract-resume.baml
        var extractResumeRequest = new ExtractResumeRequest()
        .resumeText("""
        John Doe

        Education
        - University of California, Berkeley
        - B.S. in Computer Science
        - graduated 2020

        Skills
        - Python
        - Java
        - C++
        """);

        try {
            Resume result = b.extractResume(extractResumeRequest);
            System.out.println(result);

            var edu0 = result.getEducation().get(0);
            System.out.printf("Education: %s, %s, %s\n", edu0.getSchool(), edu0.getDegree(), edu0.getYear());
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#extractResume");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }

        // Example 3: see 03-classify-user-msg.baml
        {
            var message = new Message().userName("Alice").message("I can't access my account using my login credentials. I havent received the promised reset password email. Please help.");
            var req = new ClassifyMessageRequest().message(message);
            var resp = b.classifyMessage(req);
            System.out.printf("ClassifyMessage: %s\n", resp);
            if (resp == Category.ACCOUNT_ISSUE) {
                System.out.printf("Category: Account Issue\n");
            }
            if (resp == Category.CANCEL_ORDER) {
                System.out.printf("Category: Cancel Order\n");
            }
            if (resp == Category.QUESTION) {
                System.out.printf("Category: Question\n");
            }
            if (resp == Category.REFUND) {
                System.out.printf("Category: Refund\n");
            }
            if (resp == Category.TECHNICAL_SUPPORT) {
                System.out.printf("Category: Technical Support\n");
            }
        }

        // Example 4: working with unions, see parse-email.baml
        {
            var input = """
Dear [Your Name],

Thank you for booking with [Airline Name]! We are pleased to confirm your upcoming flight.

Flight Confirmation Details:

Booking Reference: ABC123
Passenger Name: [Your Name]
Flight Number: 789
Departure Date: September 15, 2024
Departure Time: 10:30 AM
Arrival Time: 1:45 PM
Departure Airport: John F. Kennedy International Airport (JFK), New York, NY
Arrival Airport: Los Angeles International Airport (LAX), Los Angeles, CA
Seat Number: 12A
Class: Economy
Baggage Allowance:

Checked Baggage: 1 piece, up to 23 kg
Carry-On Baggage: 1 piece, up to 7 kg
Important Information:

Please arrive at the airport at least 2 hours before your scheduled departure.
Check-in online via our website or mobile app to save time at the airport.
Ensure that your identification documents are up to date and match the name on your booking.
Contact Us:

If you have any questions or need to make changes to your booking, please contact our customer service team at 1-800-123-4567 or email us at support@[airline].com.

We wish you a pleasant journey and thank you for choosing [Airline Name].

Best regards,

[Airline Name] Customer Servic
                """;
            // parseEmail returns BookOrder | FlightConfirmation | GroceryReceipt, i.e. a union type,
            // which in Java is represented as a wrapper type with `getActualInstance()` on it
            var resp = b.parseEmail(new ParseEmailRequest().input(input)).getActualInstance();
            System.out.printf("Parsed email: %s\n", resp);

            if (resp instanceof BookOrder) {
                var order = (BookOrder) resp;
                System.out.printf("Book title: %s\n", order.getTitle());
            } else if (resp instanceof FlightConfirmation) {
                var flight = (FlightConfirmation) resp;

                var flightNum = flight.getFlightNumber().getActualInstance();
                if (flightNum instanceof String) {
                    System.out.printf("Flight number string: %s\n", (String) flightNum);
                } else if (flightNum instanceof Integer) {
                    System.out.printf("Flight number int: %s\n", (Integer) flightNum);
                }

                var departure = flight.getDeparture().getActualInstance();
                if (departure instanceof FlightEndpoint) {
                    var departureEndpoint = (FlightEndpoint) departure;
                    System.out.printf("Departure airport and city: %s in %s\n", departureEndpoint.getAirport(), departureEndpoint.getTime());
                } else if (departure instanceof String) {
                    System.out.printf("Departure airport string (i.e. airport code): %s\n", (String) departure);
                }
            } else if (resp instanceof GroceryReceipt) {
                var receipt = (GroceryReceipt) resp;
                System.out.printf("Grocery receipt: %s\n", receipt.getStoreName());
            }
        }
    }
}