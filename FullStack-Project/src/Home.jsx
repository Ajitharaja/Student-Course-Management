import "./Home.css";

function Home() {
  return (
    <div className="page home-page">
      <header className="hero">
        <h1>Student Course Management System</h1>
        <p>Enroll in courses, track your progress, and manage your learning — all in one place.</p>
      </header>

      <section className="features">
        <div className="feature-card">
          <h3>Browse Courses</h3>
          <p>Explore a wide range of courses across categories.</p>
        </div>
        <div className="feature-card">
          <h3>Track Progress</h3>
          <p>See your completion percentage for every course.</p>
        </div>
        <div className="feature-card">
          <h3>Stay Notified</h3>
          <p>Get real-time updates on enrollments and assignments.</p>
        </div>
      </section>
    </div>
  );
}

export default Home;