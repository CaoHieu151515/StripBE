import { UserInfo } from '@/components/types/UserInfo.type';
import { PayloadAction, createSlice } from '@reduxjs/toolkit';

export type GlobalState = {
  userInfo: UserInfo | null;
  // userPermission: UserPermission | null;
};

const initialState: GlobalState = {
  userInfo: null,
  // userPermission: null,
};

export const globalSlice = createSlice({
  name: 'global',
  initialState,
  reducers: {
    resetState: () => initialState,

    setUserInfo: (state, { payload }: PayloadAction<UserInfo>) => {
      state.userInfo = payload;
    },

    // setUserPermission: (state, { payload }: PayloadAction<UserPermission>) => {
    //   state.userPermission = payload;
    // },
  },
});

export const { resetState, setUserInfo } = globalSlice.actions;

export default globalSlice.reducer;
