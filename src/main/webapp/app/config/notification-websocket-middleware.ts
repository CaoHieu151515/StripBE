import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import { Observable } from 'rxjs';
import { Storage } from 'react-jhipster';

type NotificationDTO = {
  id: number;
  title: string;
  content: string;
  isRead: boolean;
  createdDate: string;
};

let stompClient = null;
let subscriber = null;
let connection: Promise<any>;
let connectedPromise: any = null;
let listener: Observable<NotificationDTO[]>;
let listenerObserver: any;
let alreadyConnectedOnce = false;

const createConnection = (): Promise<any> => new Promise(resolve => (connectedPromise = resolve));

const createListener = (): Observable<NotificationDTO[]> =>
  new Observable(observer => {
    listenerObserver = observer;
  });

export const sendNotificationListRequest = () => {
  connection?.then(() => {
    stompClient?.send('/app/notification/list', '', '');
  });
};

const subscribe = () => {
  connection.then(() => {
    subscriber = stompClient.subscribe('/user/queue/notification-list', data => {
      const notifications: NotificationDTO[] = JSON.parse(data.body);
      listenerObserver.next(notifications);
    });

    // Gửi yêu cầu lấy danh sách
    sendNotificationListRequest();
  });
};

export const connectNotificationWebSocket = () => {
  if (connectedPromise !== null || alreadyConnectedOnce) {
    return;
  }

  connection = createConnection();
  listener = createListener();

  const loc = window.location;
  const baseHref = document.querySelector('base')?.getAttribute('href')?.replace(/\/$/, '') || '';
  let url = '//' + loc.host + baseHref + '/websocket/notifications';
  const authToken = Storage.local.get('jhi-authenticationToken') || Storage.session.get('jhi-authenticationToken');
  if (authToken) {
    url += '?access_token=' + authToken;
  }

  const socket = new SockJS(url);
  stompClient = Stomp.over(socket, { protocols: ['v12.stomp'] });

  stompClient.connect({}, () => {
    connectedPromise('success');
    connectedPromise = null;
    alreadyConnectedOnce = true;
    subscribe();
  });
};

export const disconnectNotificationWebSocket = () => {
  if (stompClient !== null && stompClient.connected) {
    stompClient.disconnect();
  }
  stompClient = null;
  alreadyConnectedOnce = false;
};

export const receiveNotifications = () => listener;

export const unsubscribeNotificationWebSocket = () => {
  if (subscriber !== null) {
    subscriber.unsubscribe();
  }
  listener = createListener();
};
