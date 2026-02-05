# 🚀 Adaptive Learning Engine (Spring Boot + MySQL)

An intelligent Learning Management System (LMS) that personalizes the student journey. Instead of a static list of lessons, this engine evaluates quiz performance in real-time to recommend the most suitable next step.

## 🌟 Key Features
* **Adaptive Recommendation Logic:** Uses a custom Service layer to map scores to difficulty tiers (Easy/Medium/Hard).
* **Full-Stack Integration:** Dynamic UI rendered via Thymeleaf, backed by a RESTful Spring Boot API.
* **Audit & Tracking:** Persists student progress and scores in a relational MySQL database.
* **Graceful Error Handling:** Global Exception Handler to manage data gaps (404/500) without system crashes.

## 🛠️ Tech Stack
* **Backend:** Java 17, Spring Boot 3, Spring Data JPA
* **Frontend:** Thymeleaf, HTML5, CSS3
* **Database:** MySQL
* **Version Control:** Git/GitHub

## 📸 How It Works
1. Students view a "Learning Path" on the dashboard.
2. After taking a quiz, the score is submitted via a POST/GET request.
3. The `CourseService` calculates the next level and redirects the user to a personalized recommendation card.
