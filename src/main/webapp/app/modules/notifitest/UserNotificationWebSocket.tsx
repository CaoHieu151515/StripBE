import React, { useEffect, useState } from 'react';
import {
  connectNotificationWebSocket,
  disconnectNotificationWebSocket,
  receiveNotifications,
} from 'app/config/notification-websocket-middleware';

type NotificationDTO = {
  id: number;
  title: string;
  content: string;
  isRead: boolean;
  createdDate: string;
};

const NotificationList: React.FC = () => {
  const [notifications, setNotifications] = useState<NotificationDTO[]>([]);

  useEffect(() => {
    connectNotificationWebSocket();

    const subscription = receiveNotifications().subscribe(data => {
      console.log('📥 Received notifications:', data);
      setNotifications(data);
    });

    return () => {
      subscription.unsubscribe();
      disconnectNotificationWebSocket();
    };
  }, []);

  return (
    <div>
      <h2>🔔 Danh sách thông báo realtime</h2>
      {notifications.length === 0 ? (
        <p>Không có thông báo.</p>
      ) : (
        <ul>
          {notifications.map(n => (
            <li key={n.id}>
              <strong>{n.title}</strong> - {n.content}
              <br />
              <small>{new Date(n.createdDate).toLocaleString()}</small>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default NotificationList;
