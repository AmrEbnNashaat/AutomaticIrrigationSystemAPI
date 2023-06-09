## Irrigation System Backend API Documentation

This documentation provides an overview of the backend API for the irrigation system. The API is implemented using Java Spring Boot and utilizes a PostgreSQL database running as a container on Docker.

### Technologies Used

- Java Spring Boot
- PostgreSQL
- Docker

### Disclaimer

This project is not complete. Containerization is still on its way. However, Technical Difficulties with MacBook's M1 Chip arisen with no good image for Java Spring's JDK 20. Provided is a Postman Collection that can be used to test the API. However, Its work in progress and will be done.

### Getting Started

To start the application, follow these steps:

1. Make sure you have Java and PostgreSQL installed on your machine.
2. Start the PostgreSQL database by running `docker-compose up -d` in the project directory. This will start the PostgreSQL container and initialize the database using the `import.sql` file located in the resources folder.
3. Build and run the application using your preferred Java development environment or by running the main class `Main` from the command line.

### API Endpoints

#### Plot API

##### Get All Plots

- URL: `GET /api/v1/plot`
- Description: Retrieves all plots.
- Response: List of `Plot` objects.

##### Create a Plot

- URL: `POST /api/v1/plot`
- Description: Creates a new plot.
- Request Body: `Plot` object (JSON)
- Response: Created `Plot` object.

##### Update a Plot

- URL: `PUT /api/v1/plot/{id}`
- Description: Updates an existing plot.
- Path Variable: `id` (Integer) - ID of the plot to be updated.
- Request Body: Updated `Plot` object (JSON)
- Response: Updated `Plot` object.

##### Delete a Plot

- URL: `DELETE /api/v1/plot/{id}`
- Description: Deletes a plot.
- Path Variable: `id` (Integer) - ID of the plot to be deleted.
- Response: Success message.

##### Assign a Time Slot to a Plot

- URL: `POST /api/v1/plot/{plotId}/assign?timeSlotId={timeSlotId}`
- Description: Assigns a time slot to a plot.
- Path Variable: `plotId` (Integer) - ID of the plot.
- Query Parameter: `timeSlotId` (Integer) - ID of the time slot to be assigned.
- Response: Success message.

#### Time Slot API

##### Create a Time Slot

- URL: `POST /api/v1/timeslot`
- Description: Creates a new time slot.
- Request Body: `TimeSlot` object (JSON)
- Response: Created `TimeSlot` object.

##### Get All Time Slots

- URL: `GET /api/v1/timeslot`
- Description: Retrieves all time slots.
- Response: List of `TimeSlot` objects.

##### Update a Time Slots

- URL: `PUT /api/v1/timeslot/{id}`
- Description: Updates an existing time slot.
- Path Variable: `id` (Integer) - ID of the time slot to be updated.
- Request Body: Updated `TimeSlot` object (JSON)
- Response: Updated `TimeSlot` object.

##### Delete a Time Slot
- URL: `DELETE /api/v1/timeslot/{id}`
- Description: Deletes a time slot.
- Path Variable: `id` (Integer) - ID of the time slot to be deleted.
- Response: Success message.


### Additional Notes

- The provided APIs support basic CRUD operations for plots and time slots.
- Time slots can be assigned to plots using the `assignTimeSlotToPlot` endpoint or just running the Postman Collection endpoint for creating a TimeSlot. It will be auto assigned to a plot.
- It is recommended to add more time slots if the system runs out of available time slots to assign.
- Information about the assignment will be available through the terminal.
- in `the testing_containerzation` folder, you will find the docker-compose and the Dockerfile that is to be used to contaienrize the app. If you can help out, please do.

### Acknowledgement
Thank you for checking out my project. :)

If you have any further questions or need additional assistance, please feel free to ask.
