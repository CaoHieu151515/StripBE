export enum ROUTE_NAME {
  LOGIN = 'login',
  SIGN_UP = 'sign-up',
  MY_PAGE = 'my-page',
  NOT_FOUND = 'not-found',
  RESET_PASSWORD = 'reset-password',
  FORGOT_PASSWORD = 'forgot-password',
  CHANGE_PASSWORD = 'change-password',
  PROFILE = 'profile',
  DASHBOARD = 'dashboard',
  USER = 'user',
  VIEW_USER_ACCOUNT = 'view-user-account',
  CENSOR_DRIVER_REQUEST = 'censor-driver-request',
  TRIP = 'trip',
  FEEDBACK = 'feedback',
  PACKAGE = 'package',
  WALLET = 'wallet',
  REPORT = 'report',
  TRANSACTION_HISTORY = 'transaction-history',
  CENSOR_VEHICLE = 'censor-vehicle',
  RATE_DRIVER = 'rate-driver',
  STAFF = 'staff',
}

export const MY_ROUTE = {
  HOME: '/',
  DASHBOARD: {
    self: ROUTE_NAME.DASHBOARD,
  },
  LOGIN: ROUTE_NAME.LOGIN,
  SIGNUP: ROUTE_NAME.SIGN_UP,
  NOT_FOUND: ROUTE_NAME.NOT_FOUND,
  CHANGE_PASSWORD: ROUTE_NAME.CHANGE_PASSWORD,
  PROFILE: ROUTE_NAME.PROFILE,
  FORGOT_PASSWORD: {
    self: `/${ROUTE_NAME.FORGOT_PASSWORD}`,
    success: `/${ROUTE_NAME.FORGOT_PASSWORD}/success`,
  },
  USER: {
    self: `/${ROUTE_NAME.USER}`,
    VIEW_USER_ACCOUNT: {
      self: `/${ROUTE_NAME.USER}/${ROUTE_NAME.VIEW_USER_ACCOUNT}`,
      detail: (id: string) => `/${ROUTE_NAME.USER}/${ROUTE_NAME.VIEW_USER_ACCOUNT}/${id}`,
    },
    CENSOR_DRIVER_REQUEST: {
      self: `/${ROUTE_NAME.USER}/${ROUTE_NAME.CENSOR_DRIVER_REQUEST}`,
      detail: (id: string) => `/${ROUTE_NAME.USER}/${ROUTE_NAME.CENSOR_DRIVER_REQUEST}/${id}`,
    },
    STAFF: {
      self: `/${ROUTE_NAME.USER}/${ROUTE_NAME.STAFF}`,
      detail: (id: string) => `/${ROUTE_NAME.USER}/${ROUTE_NAME.STAFF}/${id}`,
    },
  },
  TRIP: {
    self: `/${ROUTE_NAME.TRIP}`,
    detail: (id: string) => `/${ROUTE_NAME.TRIP}/${id}`,
  },
  FEEDBACK: {
    self: `/${ROUTE_NAME.FEEDBACK}`,
    detail: (id: string) => `/${ROUTE_NAME.FEEDBACK}/${id}`,
  },
  PACKAGE: {
    self: `/${ROUTE_NAME.PACKAGE}`,
  },
  WALLET: {
    self: `/${ROUTE_NAME.WALLET}`,
  },
  REPORT: {
    self: `/${ROUTE_NAME.REPORT}`,
  },
  TRANSACTION_HISTORY: {
    self: `/${ROUTE_NAME.TRANSACTION_HISTORY}`,
  },
  CENSOR_VEHICLE: {
    self: `/${ROUTE_NAME.CENSOR_VEHICLE}`,
    detail: (id: string) => `/${ROUTE_NAME.CENSOR_VEHICLE}/${id}`,
  },
  RATE_DRIVER: {
    self: `/${ROUTE_NAME.RATE_DRIVER}`,
    detail: (id: string) => `/${ROUTE_NAME.RATE_DRIVER}/${id}`,
  },
};

export const DEFAULT_ROUTE = MY_ROUTE.LOGIN;
