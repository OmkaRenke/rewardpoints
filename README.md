#  Reward Points System V1.0

A Spring Boot-based RESTful API that tracks customer purchases and calculates monthly and total reward points based on transaction amounts.

---

## Reward Points Calculation

-  **2 points** for every dollar spent **over $100**
-  **1 point** for every dollar spent **between $50 and $100**
  
**Example:**  
`$120 purchase → (2 × 20) + (1 × 50) = 90 points`

---

##  Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- ModelMapper
- Maven
- JUnit 5 & Mockito
- Postman (for API testing)

---

##  How to Run This Application

### 1. Clone the Repository
```bash
git clone https://github.com/OmkaRenke/rewardpoints
cd rewardpoints
```

### 2. Create MySQL Database
```sql
CREATE DATABASE reward_points_db;
```

### 3. Update `application.properties`
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/reward_points_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

server.port=8080
```

### 4. Run the Application
Run the `RewardPointsApplication.java` class from your IDE  
or use terminal:
```bash
mvn spring-boot:run
```

---

##  APIs

###  1. Register Customer
**POST** `/rewardpoint/register/customer`

**Request Body**
```json
{
  "name": "jennie",
  "email": "jennie@gmail.com",
  "contact": "9562927792"
}
```

**Response**
```json
Customer registered successfully:104
```

---

### 2. Save Transaction
**POST** `/rewardpoint/save/transaction`

**Request Body**
```json
{
  "transactionNumber": "TXN04",
  "transactionMode": "UPI",
  "amount": 150.00,
  "transactionDate": "2025-05-19T10:00:00",
  "customerDTO": {
    "customerId": 104
  }
}
```

**Response**
```
Transaction saved successfully:7
```

---

###  3. Get All Transactions for a Customer
**GET** `/rewardpoint/customer/{customerId}`

**Example:** `/rewardpoint/customer/100`

**Response**
```json
[
  {
    "transactionId": 1,
    "transactionNumber": "TXN1001",
    "transactionMode": "CARD",
    "amount": 120.00,
    "transactionDate": "2025-04-10T04:30:00.000+00:00",
    "pointsEarned": 90
  },
  {
    "transactionId": 2,
    "transactionNumber": "TXN1002",
    "transactionMode": "UPI",
    "amount": 75.00,
    "transactionDate": "2025-05-15T09:00:00.000+00:00",
    "pointsEarned": 25
  },
  {
    "transactionId": 3,
    "transactionNumber": "TXN1003",
    "transactionMode": "CASH",
    "amount": 45.00,
    "transactionDate": "2025-06-20T03:45:00.000+00:00",
    "pointsEarned": 0
  },
  {
    "transactionId": 8,
    "transactionNumber": "TXN05",
    "transactionMode": "UPI",
    "amount": 190.00,
    "transactionDate": "2025-05-19T10:00:00.000+00:00",
    "pointsEarned": 230
   }
]
```

---

###  4. Get Monthly & Total Reward Summary
**GET** `/rewardpoint/customer/summary?customerId=100&startDate=2025-04-01&endDate=2025-06-30`

**Response**
```json
{
    "customerId": 100,
    "customerName": "Kevin",
    "monthlyRewards": [
        {
            "month": "2025 - June",
            "points": 0,
            "transactioList": [
                {
                    "transactionId": 3,
                    "transactionNumber": "TXN1003",
                    "transactionMode": "CASH",
                    "amount": 45.00,
                    "transactionDate": "2025-06-20T03:45:00.000+00:00",
                    "pointsEarned": 0
                }
            ]
        },
        {
            "month": "2025 - May",
            "points": 255,
            "transactioList": [
                {
                    "transactionId": 2,
                    "transactionNumber": "TXN1002",
                    "transactionMode": "UPI",
                    "amount": 75.00,
                    "transactionDate": "2025-05-15T09:00:00.000+00:00",
                    "pointsEarned": 25
                },
                {
                    "transactionId": 8,
                    "transactionNumber": "TXN05",
                    "transactionMode": "UPI",
                    "amount": 190.00,
                    "transactionDate": "2025-05-19T10:00:00.000+00:00",
                    "pointsEarned": 230
                }
            ]
        },
        {
            "month": "2025 - April",
            "points": 90,
            "transactioList": [
                {
                    "transactionId": 1,
                    "transactionNumber": "TXN1001",
                    "transactionMode": "CARD",
                    "amount": 120.00,
                    "transactionDate": "2025-04-10T04:30:00.000+00:00",
                    "pointsEarned": 90
                }
            ]
        }
    ],
    "totalPoints": 345
}
```

---