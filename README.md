# ☕ The Coffee Shop Barista Dilemma

A smart **order queuing and scheduling system** designed to optimize customer wait times, ensure fairness, and balance workload among baristas during peak hours in a café environment.

---

## 📌 Problem Statement

Bean & Brew café faces heavy morning rush (7–10 AM) with **200–300 customers** and only **3 baristas**.

The challenge:

* Reduce **average waiting time**
* Ensure **no customer waits more than 10 minutes**
* Maintain **fairness in order serving**
* Optimize **barista workload distribution**

Traditional FIFO (First-Come-First-Served) fails because:

> Simple orders (like Cold Brew) get delayed behind complex ones (like Mocha)

---

## 🚀 Solution Overview

This project implements a **Dynamic Priority Queue with Predictive Scheduling** that:

* Prioritizes orders intelligently
* Adapts in real-time
* Balances efficiency and fairness

---

## ⚙️ Key Features

### 🧠 Smart Priority Scheduling

Each order gets a **priority score (0–100)** based on:

* ⏱️ Wait Time (40%)
* ⚡ Order Complexity (25%)
* 👤 Loyalty Status (10%)
* 🚨 Urgency (25%)

Scores are recalculated every **30 seconds**.

---

### ⚖️ Fairness Enforcement

* Limits how many later customers can be served first
* Adds penalty if fairness threshold is exceeded
* Maintains transparency for customers

---

### 🚨 Emergency Handling

* Orders waiting > 8 minutes get **priority boost**
* Alerts triggered near 10-minute threshold
* Immediate assignment to next available barista

---

### 👨‍🍳 Workload Balancing

* Tracks each barista’s workload ratio
* Overloaded → prefers short orders
* Underutilized → takes complex orders

---

## 📊 System Parameters

| Category        | Details               |
| --------------- | --------------------- |
| Operating Hours | 7:00 AM – 10:00 AM    |
| Baristas        | 3                     |
| Avg Customers   | 250                   |
| Arrival Pattern | Poisson (λ = 1.4/min) |

---

## ☕ Menu & Preparation Time

| Drink             | Time  | Frequency | Price |
| ----------------- | ----- | --------- | ----- |
| Cold Brew         | 1 min | 25%       | ₹120  |
| Espresso          | 2 min | 20%       | ₹150  |
| Americano         | 2 min | 15%       | ₹140  |
| Cappuccino        | 4 min | 20%       | ₹180  |
| Latte             | 4 min | 12%       | ₹200  |
| Specialty (Mocha) | 6 min | 8%        | ₹250  |

---

## 🧮 Algorithm Approach

### 🔹 Dynamic Priority Queue

* Orders stored in a priority queue
* Highest score gets assigned first

### 🔹 Predictive Scheduling

* Estimates future wait times
* Assigns orders proactively

### 🔹 Real-Time Assignment

* Baristas pull highest-priority order
* System continuously adapts

---

## 📈 Performance Results

| Metric              | Smart System           | FIFO    |
| ------------------- | ---------------------- | ------- |
| Avg Wait Time       | **4.8 min**            | 6.2 min |
| Timeout Rate        | **2.3%**               | 8.5%    |
| Workload Balance    | **98%**                | Lower   |
| Fairness Violations | 23% (mostly justified) | High    |

---

## 🏗️ Project Structure

```
coffee-shop-barista-dilemma/
│── src/
│   ├── models/          # Order, Barista, Queue models
│   ├── services/        # Scheduling logic
│   ├── utils/           # Helper functions
│   ├── simulation/      # Monte Carlo simulation
│── README.md
│── pom.xml / package.json
```

---

## ▶️ How to Run

### 1. Clone the Repository

```bash
git clone https://github.com/deveshkrjha/The-Coffee-Shop-Barista-Dilemma.git
cd The-Coffee-Shop-Barista-Dilemma
```

### 2. Run the Application

(Depending on your tech stack)

#### Java (if Spring Boot / Maven)

```bash
mvn clean install
mvn spring-boot:run
```

#### Node.js (if applicable)

```bash
npm install
npm start
```

---

## 🎯 Learning Outcomes

* Micro-level **queue optimization**
* Real-world **system design thinking**
* Trade-offs between:

    * Efficiency ⚡
    * Fairness ⚖️
    * Customer psychology 🧠
* Hands-on with:

    * Scheduling algorithms
    * Priority queues
    * Simulation techniques

---

## 💡 Future Improvements

* Add **machine learning** for demand prediction
* Real-time dashboard for baristas
* Mobile app for customer tracking
* Integration with POS systems

---

## 👨‍💻 Author

**Devesh Kumar**
GitHub: https://github.com/deveshkrjha

---

## ⭐ If you like this project

Give it a ⭐ on GitHub and share your feedback!
