# Reward Points System V1.0

Spring Boot-based RESTful API that calculates and manages reward points 

---

- Tracks transactions
- Calculates reward points  per customer per month and  total

### Reward Calculation 
- 2 points for every dollar spent over $100
- 1 point for every dollar spent between $50 and $100
- $120 purchase = 2 X 20 + 1 X 50 = 90 points

---

##  Stack
- Spring Boot
- MySQL
- ModelMapper
- Maven
- JUnit & Mockito

---

##  How to Run
- Clone the repo
- Import into any IDE as a Maven project
- Run the RewardPointsApplication class
- Use Postma to test APIs

---

## API Endpoints

####  Save Transaction
```http
POST /rewardpoint/save/transaction

Sample Request Body
 
{
  "transactionNumber": "TXN011",
  "transactionMode": "UPI",
  "amount": 150.00,
  "transactionDate": "2025-06-19T10:00:00",
  "retailerDTO": {
    "retailerId": 100
  },
  "customerDTO": {
    "customerId": 201
  }
}
```

#### Get All Customers Under Retailer
```http
GET /rewardpoint/retailer/customers/{retailerId}

Sample Response 
[
    {
        "name": "Kevin",
        "customerId": 200,
        "email": "Kevin@gmail.com",
        "contact": "9623456785",
        "retailerDTO": {
            "name": "Jack",
            "retailerId": 100,
            "email": "jack@gmail.com",
            "contact": "9876564326"
        }
    },
    {
        "name": "Ken",
        "customerId": 201,
        "email": "Ken@gmail.com",
        "contact": "3454354678",
        "retailerDTO": {
            "name": "Jack",
            "retailerId": 100,
            "email": "jack@gmail.com",
            "contact": "9876564326"
        }
    }
]
```
#### Get All Transactions Under Retailer
```http
GET /rewardpoint/retailer/custransactions/{retailerId}

Sample Response 
[
    {
        "transactionId": 1,
        "transactionNumber": "TXN001",
        "transactionMode": "UPI",
        "amount": 120.00,
        "transactionDate": "2025-06-19T10:00:00.000+00:00",
        "pointsEarned": 90,
        "customerDTO": {
            "name": "Kevin",
            "customerId": 200,
            "email": "Kevin@gmail.com",
            "contact": "9623456785",
            "retailerDTO": null
        }
    },
    {
        "transactionId": 2,
        "transactionNumber": "TXN002",
        "transactionMode": "UPI",
        "amount": 160.00,
        "transactionDate": "2025-05-19T10:00:00.000+00:00",
        "pointsEarned": 170,
        "customerDTO": {
            "name": "Kevin",
            "customerId": 200,
            "email": "Kevin@gmail.com",
            "contact": "9623456785",
            "retailerDTO": null
        }
    },
    {
        "transactionId": 3,
        "transactionNumber": "TXN003",
        "transactionMode": "UPI",
        "amount": 110.00,
        "transactionDate": "2025-05-19T10:00:00.000+00:00",
        "pointsEarned": 70,
        "customerDTO": {
            "name": "Kevin",
            "customerId": 200,
            "email": "Kevin@gmail.com",
            "contact": "9623456785",
            "retailerDTO": null
        }
    },
    {
        "transactionId": 4,
        "transactionNumber": "TXN004",
        "transactionMode": "UPI",
        "amount": 70.00,
        "transactionDate": "2025-05-19T10:00:00.000+00:00",
        "pointsEarned": 20,
        "customerDTO": {
            "name": "Kevin",
            "customerId": 200,
            "email": "Kevin@gmail.com",
            "contact": "9623456785",
            "retailerDTO": null
        }
    },
    {
        "transactionId": 5,
        "transactionNumber": "TXN005",
        "transactionMode": "UPI",
        "amount": 170.00,
        "transactionDate": "2025-04-19T10:00:00.000+00:00",
        "pointsEarned": 190,
        "customerDTO": {
            "name": "Kevin",
            "customerId": 200,
            "email": "Kevin@gmail.com",
            "contact": "9623456785",
            "retailerDTO": null
        }
    },
    {
        "transactionId": 6,
        "transactionNumber": "TXN006",
        "transactionMode": "UPI",
        "amount": 100.00,
        "transactionDate": "2025-04-19T10:00:00.000+00:00",
        "pointsEarned": 50,
        "customerDTO": {
            "name": "Kevin",
            "customerId": 200,
            "email": "Kevin@gmail.com",
            "contact": "9623456785",
            "retailerDTO": null
        }
    },
    {
        "transactionId": 7,
        "transactionNumber": "TXN007",
        "transactionMode": "UPI",
        "amount": 130.00,
        "transactionDate": "2025-04-19T10:00:00.000+00:00",
        "pointsEarned": 110,
        "customerDTO": {
            "name": "Ken",
            "customerId": 201,
            "email": "Ken@gmail.com",
            "contact": "3454354678",
            "retailerDTO": null
        }
    },
    {
        "transactionId": 8,
        "transactionNumber": "TXN008",
        "transactionMode": "UPI",
        "amount": 80.00,
        "transactionDate": "2025-05-19T10:00:00.000+00:00",
        "pointsEarned": 30,
        "customerDTO": {
            "name": "Ken",
            "customerId": 201,
            "email": "Ken@gmail.com",
            "contact": "3454354678",
            "retailerDTO": null
        }
    },
    {
        "transactionId": 9,
        "transactionNumber": "TXN009",
        "transactionMode": "UPI",
        "amount": 280.00,
        "transactionDate": "2025-05-19T10:00:00.000+00:00",
        "pointsEarned": 410,
        "customerDTO": {
            "name": "Ken",
            "customerId": 201,
            "email": "Ken@gmail.com",
            "contact": "3454354678",
            "retailerDTO": null
        }
    },
    {
        "transactionId": 10,
        "transactionNumber": "TXN010",
        "transactionMode": "UPI",
        "amount": 150.00,
        "transactionDate": "2025-06-19T10:00:00.000+00:00",
        "pointsEarned": 150,
        "customerDTO": {
            "name": "Ken",
            "customerId": 201,
            "email": "Ken@gmail.com",
            "contact": "3454354678",
            "retailerDTO": null
        }
    }
]
```
#### Get All Transactions by Customer
```http
GET /rewardpoint/customer/{customerId}

Sample Response 
[
    {
        "transactionId": 1,
        "transactionNumber": "TXN001",
        "transactionMode": "UPI",
        "amount": 120.00,
        "transactionDate": "2025-06-19T10:00:00.000+00:00",
        "pointsEarned": 90,
        "customerDTO": {
            "name": "Kevin",
            "customerId": 200,
            "email": "Kevin@gmail.com",
            "contact": "9623456785",
            "retailerDTO": null
        }
    },
    {
        "transactionId": 2,
        "transactionNumber": "TXN002",
        "transactionMode": "UPI",
        "amount": 160.00,
        "transactionDate": "2025-05-19T10:00:00.000+00:00",
        "pointsEarned": 170,
        "customerDTO": {
            "name": "Kevin",
            "customerId": 200,
            "email": "Kevin@gmail.com",
            "contact": "9623456785",
            "retailerDTO": null
        }
    }
 ]
```
####  Customer Reward Summary by CustomerId
```http
GET /rewardpoint/customer/summary?customerId=200&startDate=2025-04-01&endDate=2025-06-30

Sample Response 
{
    "customerId": 200,
    "customerName": "Kevin",
    "monthlyRewards": [
        {
            "month": "2025-04",
            "points": 240,
            "transactioList": [
                {
                    "transactionId": 5,
                    "transactionNumber": "TXN005",
                    "transactionMode": "UPI",
                    "amount": 170.00,
                    "transactionDate": "2025-04-19T10:00:00.000+00:00",
                    "pointsEarned": 190,
                    "customerDTO": null
                },
                {
                    "transactionId": 6,
                    "transactionNumber": "TXN006",
                    "transactionMode": "UPI",
                    "amount": 100.00,
                    "transactionDate": "2025-04-19T10:00:00.000+00:00",
                    "pointsEarned": 50,
                    "customerDTO": null
                }
            ]
        },
        {
            "month": "2025-05",
            "points": 260,
            "transactioList": [
                {
                    "transactionId": 2,
                    "transactionNumber": "TXN002",
                    "transactionMode": "UPI",
                    "amount": 160.00,
                    "transactionDate": "2025-05-19T10:00:00.000+00:00",
                    "pointsEarned": 170,
                    "customerDTO": null
                },
                {
                    "transactionId": 3,
                    "transactionNumber": "TXN003",
                    "transactionMode": "UPI",
                    "amount": 110.00,
                    "transactionDate": "2025-05-19T10:00:00.000+00:00",
                    "pointsEarned": 70,
                    "customerDTO": null
                },
                {
                    "transactionId": 4,
                    "transactionNumber": "TXN004",
                    "transactionMode": "UPI",
                    "amount": 70.00,
                    "transactionDate": "2025-05-19T10:00:00.000+00:00",
                    "pointsEarned": 20,
                    "customerDTO": null
                }
            ]
        }
    ],
    "totalPoints": 500
}
```
####  Customer Reward Summary by RetailorId
```http 
GET /rewardpoint/retailer/summary?retailerId=200&startDate=2025-04-01&endDate=2025-06-30 

Sample Response 
[
    {
        "customerId": 200,
        "customerName": "Kevin",
        "monthlyRewards": [
            {
                "month": "2025-04",
                "points": 240,
                "transactioList": [
                    {
                        "transactionId": 5,
                        "transactionNumber": "TXN005",
                        "transactionMode": "UPI",
                        "amount": 170.00,
                        "transactionDate": "2025-04-19T10:00:00.000+00:00",
                        "pointsEarned": 190,
                        "customerDTO": null
                    },
                    {
                        "transactionId": 6,
                        "transactionNumber": "TXN006",
                        "transactionMode": "UPI",
                        "amount": 100.00,
                        "transactionDate": "2025-04-19T10:00:00.000+00:00",
                        "pointsEarned": 50,
                        "customerDTO": null
                    }
                ]
            },
            {
                "month": "2025-05",
                "points": 260,
                "transactioList": [
                    {
                        "transactionId": 2,
                        "transactionNumber": "TXN002",
                        "transactionMode": "UPI",
                        "amount": 160.00,
                        "transactionDate": "2025-05-19T10:00:00.000+00:00",
                        "pointsEarned": 170,
                        "customerDTO": null
                    },
                    {
                        "transactionId": 3,
                        "transactionNumber": "TXN003",
                        "transactionMode": "UPI",
                        "amount": 110.00,
                        "transactionDate": "2025-05-19T10:00:00.000+00:00",
                        "pointsEarned": 70,
                        "customerDTO": null
                    },
                    {
                        "transactionId": 4,
                        "transactionNumber": "TXN004",
                        "transactionMode": "UPI",
                        "amount": 70.00,
                        "transactionDate": "2025-05-19T10:00:00.000+00:00",
                        "pointsEarned": 20,
                        "customerDTO": null
                    }
                ]
            }
        ],
        "totalPoints": 500
    },
    {
        "customerId": 201,
        "customerName": "Ken",
        "monthlyRewards": [
            {
                "month": "2025-06",
                "points": 150,
                "transactioList": [
                    {
                        "transactionId": 10,
                        "transactionNumber": "TXN010",
                        "transactionMode": "UPI",
                        "amount": 150.00,
                        "transactionDate": "2025-06-19T10:00:00.000+00:00",
                        "pointsEarned": 150,
                        "customerDTO": null
                    }
                ]
            },
            {
                "month": "2025-05",
                "points": 440,
                "transactioList": [
                    {
                        "transactionId": 8,
                        "transactionNumber": "TXN008",
                        "transactionMode": "UPI",
                        "amount": 80.00,
                        "transactionDate": "2025-05-19T10:00:00.000+00:00",
                        "pointsEarned": 30,
                        "customerDTO": null
                    },
                    {
                        "transactionId": 9,
                        "transactionNumber": "TXN009",
                        "transactionMode": "UPI",
                        "amount": 280.00,
                        "transactionDate": "2025-05-19T10:00:00.000+00:00",
                        "pointsEarned": 410,
                        "customerDTO": null
                    }
                ]
            }
        ],
        "totalPoints": 590
    }
]
```
