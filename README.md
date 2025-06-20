# Reward Points System

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
```
#### Get All Customers Under Retailer
```http
GET /rewardpoint/retailer/customers/{retailerId}
```
#### Get All Transactions Under Retailer
```http
GET /rewardpoint/retailer/custransactions/{retailerId}
```
#### Get All Transactions by Customer
```http
GET /rewardpoint/customer/{customerId}
```
####  Customer Reward Summary by CustomerId
```http
GET /rewardpoint/customer/summary?customerId=200&startDate=2025-04-01&endDate=2025-06-30
```
####  Customer Reward Summary by RetailorId
```http 
GET rewardpoint/retailer/summary?retailerId=200&startDate=2025-04-01&endDate=2025-06-30
```
