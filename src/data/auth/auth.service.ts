import { LoginData } from '@/pages/login/types/LoginData.type';
import { STORAGE } from '@/utils/constants/shared.constant';
import Cookie from 'js-cookie';

type JwtData = Pick<LoginData, 'accessToken'>;

export function setJwtData({ accessToken }: JwtData) {
  Cookie.set(STORAGE.ACCESS_TOKEN, accessToken, {
    path: '/',
    expires: new Date(Date.now() + STORAGE.EXP_TIME_LONG),
  });
}

export function removeJwtData() {
  Cookie.remove(STORAGE.ACCESS_TOKEN);

  localStorage.removeItem(STORAGE.KEEP_LOGGED_IN);
}

export function getJwtData(): JwtData | null {
  const accessToken = Cookie.get(STORAGE.ACCESS_TOKEN);
  if (!accessToken) return null;

  return { accessToken };
}
