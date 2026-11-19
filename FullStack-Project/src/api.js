const API_BASE = "http://localhost:8080/api";

export async function authFetch(path, options = {}) {
  const token = localStorage.getItem("token");

  return fetch(`${API_BASE}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...options.headers,
      Authorization: `Bearer ${token}`,
    },
  });
}