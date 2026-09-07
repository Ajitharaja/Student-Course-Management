import { useState } from "react";
import "./Auth.css";
import { useNavigate } from "react-router-dom";

function Login({ onLoginSuccess }) {
   const navigate = useNavigate();
  const [formData, setFormData] = useState({ email: "", password: "" });
  const [errors, setErrors] = useState({});

  function handleChange(e) {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  }

  function validate() {
  const newErrors = {};

  if (!formData.email.trim()) {
    newErrors.email = "Email is required";
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
    newErrors.email = "Enter a valid email address";
  }

  if (!formData.password) {
    newErrors.password = "Password is required";
  } else if (formData.password.length < 6) {
    newErrors.password = "Password must be at least 6 characters";
  }

  return newErrors;
}

function handleSubmit(e) {
  e.preventDefault();
  const newErrors = validate();
  setErrors(newErrors);

  if (Object.keys(newErrors).length === 0) {
    console.log("Form is valid, submitting:", formData);
     onLoginSuccess();
     navigate("/dashboard");
  }
}

  return (
    <div className="page auth-page">
      <form className="auth-form" onSubmit={handleSubmit} noValidate>
        <h2>Login</h2>

        <label htmlFor="email">Email</label>
        <input
          type="email"
          id="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
        />
    {errors.email && <span className="error-text">{errors.email}</span>}
        <label htmlFor="password">Password</label>
        <input
          type="password"
          id="password"
          name="password"
          value={formData.password}
          onChange={handleChange}
        />
   {errors.password && <span className="error-text">{errors.password}</span>}
        <button type="submit">Login</button>

        <p className="auth-switch">
          Don't have an account? <a href="/register">Register</a>
        </p>
      </form>
    </div>
  );
}

export default Login;