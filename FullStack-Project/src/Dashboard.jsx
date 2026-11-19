import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "./AuthContext";
import { authFetch } from "./api";
import "./Dashboard.css";

function Dashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [courses, setCourses] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadCourses() {
      try {
        const response = await authFetch("/courses");

        if (response.status === 401 || response.status === 403) {
          logout();
          navigate("/login");
          return;
        }

        const data = await response.json();
        setCourses(data);
      } catch (err) {
        setError("Could not load courses. Is the backend running?");
      } finally {
        setLoading(false);
      }
    }

    loadCourses();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <div className="page dashboard-page">
      <h1>My Dashboard</h1>
      {user && <p>Welcome, {user.name}</p>}

      <div className="stats">
        <div className="stat-card">
          <h3>{courses.length}</h3>
          <p>Courses Available</p>
        </div>
      </div>

      <h2>Available Courses</h2>
      {loading && <p>Loading courses...</p>}
      {error && <span className="error-text">{error}</span>}
      {!loading && !error && courses.length === 0 && <p>No courses yet.</p>}

      <div className="course-list">
        {courses.map((course) => (
          <div className="course-item" key={course.id}>
            <h4>{course.courseName}</h4>
            <p>Instructor: {course.instructor}</p>
            <p>
              {course.duration} weeks · {course.category}
            </p>
            <p>{course.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Dashboard;