export const STORAGE = {
  ACCESS_TOKEN: 'accessToken',
  KEEP_LOGGED_IN: 'keepLoggedIn',
  EXP_TIME_SHORT: 1000 * 60 * 30, // 30 min
  EXP_TIME_LONG: 1000 * 60 * 60 * 24 * 30, // 30 days
  RECENTLY_LOGGED_IN: 'recentlyLoggedIn',
};

export const PAGE_SIZE = 10;

export const NOTISTACK_DURATION = 5000;

export const TAG_TYPES = {
  USER: 'user',
  CURRENCY: 'currency',
  NOTIFICATION_SETTING: 'setting_notification',
  ADMIN: 'admin',
};

export const apiBaseUrl = import.meta.env.VITE_API_ENDPOINT;

export const FRESHDESK_PARAMS = {
  nonce: 'nonce',
  state: 'state',
};
