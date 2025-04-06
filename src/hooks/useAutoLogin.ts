import { useLoginMutation } from '@/data/auth/auth.api';
import { loginThunk } from '@/data/auth/auth.thunk';
import { resetState } from '@/data/global/global.slice';
import { MY_ROUTE } from '@/helpers/router/route.constant';
import { enqueueSnackbar } from 'notistack';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { useAppDispatch } from './reduxHook';

export default function useAutoLogin() {
  const [login] = useLoginMutation();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { t: tSignUp } = useTranslation('sign_up');

  const autoLogin = ({ email, password }: { email: string; password: string }) => {
    dispatch(resetState());
    login({ email, password })
      .unwrap()
      .then((dto) => {
        if (dto) {
          const loginData = { ...dto, keepLoggedIn: true };
          dispatch(loginThunk(loginData));
        } else throw Error();
      })
      .catch(() => {
        enqueueSnackbar({
          message: tSignUp('toast.error.auto_login'),
          variant: 'error',
        });
        navigate(MY_ROUTE.LOGIN);
      });
  };

  return autoLogin;
}
