import { LoginData } from '@/pages/login/types/LoginData.type';
import { STORAGE } from '@/utils/constants/shared.constant.ts';
import { getFreshdeskParams } from '@/utils/param.helper.ts';
import { createAsyncThunk } from '@reduxjs/toolkit';
import Cookie from 'js-cookie';
import { CRMApi } from '../CRMApi.api';
import { resetState as resetGlobalState } from '../global/global.slice';
import { authApi } from './auth.api.ts';
import { removeJwtData, setJwtData } from './auth.service.ts';
import { resetState as resetAuthState, setStatus } from './auth.slice';

export const loginThunk = createAsyncThunk<void, LoginData>('auth/login', async ({ accessToken }) => {
  setJwtData({ accessToken });
});

export const logoutThunk = createAsyncThunk('auth/logout', async (_, { dispatch }) => {
  removeJwtData();

  dispatch(resetAuthState());
  dispatch(resetGlobalState());

  dispatch(CRMApi.util.resetApiState());
  dispatch(authApi.util.resetApiState());
});

export const syncAccessToken = createAsyncThunk<void, void>('auth/sync-access-token', async (_, { dispatch }) => {
  if (getFreshdeskParams()) {
    dispatch(setStatus('freshdesk'));
    return;
  }

  const accessToken = Cookie.get(STORAGE.ACCESS_TOKEN);

  if (accessToken) {
    dispatch(setStatus('in-app'));
  } else {
    const keepLoggedIn = localStorage.getItem(STORAGE.KEEP_LOGGED_IN);
    if (keepLoggedIn === '0') {
      dispatch(setStatus('out-session'));
    }
  }
});
