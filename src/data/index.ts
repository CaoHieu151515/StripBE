import authReducer from '@/data/auth/auth.slice';
import globalReducer from '@/data/global/global.slice';
import { configureStore } from '@reduxjs/toolkit';
import { setupListeners } from '@reduxjs/toolkit/query';
import { CRMApi } from './CRMApi.api';
import { authApi } from './auth/auth.api';

export const store = configureStore({
  reducer: {
    global: globalReducer,
    auth: authReducer,
    [authApi.reducerPath]: authApi.reducer,
    [CRMApi.reducerPath]: CRMApi.reducer,
  },
  middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(authApi.middleware, CRMApi.middleware),
  devTools: true,
});

setupListeners(store.dispatch);

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
