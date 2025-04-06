import { AUTH_CODE_TYPE } from '@/components/types/AuthCodeType';

export type AuthVerifyPasswordREQ = {
  email: string;
  authCode: string;
  authCodeType: AUTH_CODE_TYPE;
};
