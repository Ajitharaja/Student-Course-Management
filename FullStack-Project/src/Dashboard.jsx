import "./Dashboard.css";

function Dashboard() {
  return (
    <div className="page dashboard-page">
      <h1>My Dashboard</h1>

      <div className="stats">
        <div className="stat-card">
          <h3>3</h3>
          <p>Courses Enrolled</p>
        </div>
        <div className="stat-card">
          <h3>1</h3>
          <p>Completed</p>
        </div>
        <div className="stat-card">
          <h3>2</h3>
          <p>In Progress</p>
        </div>
      </div>

      <div className="course-list">
        <div className="course-item">
          <h4>Full Stack Development</h4>
          <div className="progress-bar">
            <div className="progress-fill" style={{ width: "60%" }}></div>
          </div>
          <span>60% complete</span>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;