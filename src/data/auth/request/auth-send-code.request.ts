import { AUTH_CODE_TYPE } from '@/utils/enum/auth/auth-code-type.enum';

export type AuthSendCodeREQ = {
  email: string;
  authCodeType: AUTH_CODE_TYPE;
};
