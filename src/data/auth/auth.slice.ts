import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { loginThunk, logoutThunk } from './auth.thunk';

type AuthStatus = 'before-login' | 'in-app' | 'after-logout' | 'out-session' | 'freshdesk';

export interface AuthState {
  status: AuthStatus;
  // userInfo?: UserGetMeDTO;
}

const initialState: AuthState = {
  status: 'after-logout',
};

export const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    resetState: () => initialState,

    setStatus: (state, { payload }: PayloadAction<AuthStatus>) => {
      state.status = payload;
    },

    // setUserInfo: (state, { payload }: PayloadAction<UserGetMeDTO>) => {
    //   state.userInfo = payload;
    // },
  },
  extraReducers: (builder) => {
    builder.addCase(loginThunk.fulfilled, (state) => {
      state.status = 'in-app';
    });

    builder.addCase(logoutThunk.fulfilled, (state) => {
      state.status = 'after-logout';
      // state.userInfo = undefined;
    });
  },
});

export const {
  resetState,
  setStatus,
  // setUserInfo
} = authSlice.actions;

export default authSlice.reducer;
