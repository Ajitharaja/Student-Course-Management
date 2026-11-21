import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import { useAuth } from "./AuthContext";
import "./NotificationCenter.css";

const WS_URL = "ws://localhost:8080/ws";

const LABELS = {
  ENROLLMENT: "Enrollment",
  COMPLETED: "Course completed",
  NEW_COURSE: "New course",
};

function NotificationCenter() {
  const { token, isLoggedIn } = useAuth();
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    if (!isLoggedIn || !token) return;

    function addNotification(data) {
      const item = { ...data, id: Date.now() + Math.random() };
      setNotifications((prev) => [item, ...prev].slice(0, 5));
      setTimeout(() => {
        setNotifications((prev) => prev.filter((n) => n.id !== item.id));
      }, 6000);
    }

    const client = new Client({
      brokerURL: WS_URL,
      connectHeaders: { Authorization: `Bearer ${token}` },
      reconnectDelay: 5000,
      onConnect: () => {
        client.subscribe("/user/queue/notifications", (frame) => {
          addNotification(JSON.parse(frame.body));
        });
        client.subscribe("/topic/announcements", (frame) => {
          addNotification(JSON.parse(frame.body));
        });
      },
      onStompError: (frame) => {
        console.warn("Notification connection refused:", frame.headers["message"]);
      },
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, [isLoggedIn, token]);

  function dismiss(id) {
    setNotifications((prev) => prev.filter((n) => n.id !== id));
  }

  return (
    <div className="toast-container">
      {notifications.map((n) => (
        <div className={`toast toast-${n.type}`} key={n.id}>
          <div>
            <div className="toast-title">{LABELS[n.type] || "Notification"}</div>
            <div>{n.message}</div>
          </div>
          <button onClick={() => dismiss(n.id)} aria-label="Dismiss">
            ×
          </button>
        </div>
      ))}
    </div>
  );
}

export default NotificationCenter;