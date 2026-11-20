import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "./AuthContext";
import { authFetch } from "./api";
import "./Dashboard.css";

function Dashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [courses, setCourses] = useState([]);
  const [progressList, setProgressList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  function handleAuthFailure() {
    logout();
    navigate("/login");
  }

  async function loadData() {
    try {
      const [coursesRes, progressRes] = await Promise.all([
        authFetch("/courses"),
        authFetch("/progress/my"),
      ]);

      const failed = [coursesRes, progressRes].some(
        (r) => r.status === 401 || r.status === 403
      );
      if (failed) {
        handleAuthFailure();
        return;
      }

      setCourses(await coursesRes.json());
      setProgressList(await progressRes.json());
      setError("");
    } catch (err) {
      setError("Could not load data. Is the backend running?");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  async function handleEnroll(courseId) {
    setMessage("");
    try {
      const res = await authFetch(`/enrollments/${courseId}`, { method: "POST" });
      if (res.status === 401 || res.status === 403) {
        handleAuthFailure();
        return;
      }
      const data = await res.json();
      if (!res.ok) {
        setMessage(data.error || "Could not enroll");
        return;
      }
      setMessage(`Enrolled in ${data.courseName}`);
      await loadData();
    } catch (err) {
      setMessage("Cannot reach the server. Is the backend running?");
    }
  }

  async function handleComplete(courseId) {
    setMessage("");
    try {
      const res = await authFetch(`/progress/${courseId}/complete`, {
        method: "POST",
      });
      if (res.status === 401 || res.status === 403) {
        handleAuthFailure();
        return;
      }
      const data = await res.json();
      if (!res.ok) {
        setMessage(data.error || "Could not update progress");
        return;
      }
      await loadData();
    } catch (err) {
      setMessage("Cannot reach the server. Is the backend running?");
    }
  }

  const enrolledIds = new Set(progressList.map((p) => p.courseId));
  const availableCourses = courses.filter((c) => !enrolledIds.has(c.id));
  const completedCount = progressList.filter(
    (p) => p.progressPercentage >= 100
  ).length;
  const ongoingCount = progressList.length - completedCount;

  return (
    <div className="page dashboard-page">
      <h1>My Dashboard</h1>
      {user && <p>Welcome, {user.name}</p>}

      <div className="stats">
        <div className="stat-card">
          <h3>{progressList.length}</h3>
          <p>Courses Enrolled</p>
        </div>
        <div className="stat-card">
          <h3>{completedCount}</h3>
          <p>Completed</p>
        </div>
        <div className="stat-card">
          <h3>{ongoingCount}</h3>
          <p>In Progress</p>
        </div>
      </div>

      {loading && <p>Loading...</p>}
      {error && <span className="error-text">{error}</span>}
      {message && <p className="info-text">{message}</p>}

      <h2>My Courses</h2>
      {!loading && progressList.length === 0 && (
        <p>You haven't enrolled in any course yet.</p>
      )}
      <div className="course-list">
        {progressList.map((p) => (
          <div className="course-item" key={p.courseId}>
            <h4>{p.courseName}</h4>
            <div className="progress-bar">
              <div
                className="progress-fill"
                style={{ width: `${p.progressPercentage}%` }}
              ></div>
            </div>
            <span>
              {p.completedModules} of {p.totalModules} modules ·{" "}
              {p.progressPercentage}% complete
            </span>
            <div className="course-actions">
              {p.progressPercentage >= 100 ? (
                <span className="done-text">Course completed</span>
              ) : (
                <button
                  className="btn"
                  onClick={() => handleComplete(p.courseId)}
                >
                  Mark module complete
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      <h2>Available Courses</h2>
      {!loading && availableCourses.length === 0 && (
        <p>No more courses to enroll in right now.</p>
      )}
      <div className="course-list">
        {availableCourses.map((course) => (
          <div className="course-item" key={course.id}>
            <h4>{course.courseName}</h4>
            <p>Instructor: {course.instructor}</p>
            <p>
              {course.duration} weeks · {course.category}
            </p>
            <p>{course.description}</p>
            <div className="course-actions">
              <button className="btn" onClick={() => handleEnroll(course.id)}>
                Enroll
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Dashboard;