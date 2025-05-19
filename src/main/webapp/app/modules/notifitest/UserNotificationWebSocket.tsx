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

type NotificationListResponseDTO = {
  notifications: NotificationDTO[];
  unreadCount: number;
};

const NotificationList: React.FC = () => {
  const [notifications, setNotifications] = useState<NotificationDTO[]>([]);
  const [unreadCount, setUnreadCount] = useState<number>(0);

  useEffect(() => {
    // Kết nối WebSocket khi component mount
    connectNotificationWebSocket();

    // Đăng ký nhận dữ liệu realtime
    const subscription = receiveNotifications().subscribe((data: NotificationListResponseDTO) => {
      console.log('📥 Received notifications:', data);
      setNotifications(data.notifications || []);
      setUnreadCount(data.unreadCount || 0);
    });

    // Hủy đăng ký và ngắt kết nối khi component unmount
    return () => {
      subscription.unsubscribe();
      disconnectNotificationWebSocket();
    };
  }, []);

  return (
    <div>
      <h2>🔔 Danh sách thông báo realtime ({unreadCount} chưa đọc)</h2>
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
