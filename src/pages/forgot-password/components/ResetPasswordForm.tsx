import CustomTextFieldWithLabel from '@/components/form-related/CustomTextFieldWithLabel';
import { useResetPasswordMutation } from '@/data/auth/auth.api';
import { AuthResetPasswordREQ } from '@/data/auth/request/auth-reset-password.request';
import { ResetPasswordInput } from '@/helpers/form-schemas/reset-password/reset-password.input';
import { resetPasswordSchema } from '@/helpers/form-schemas/reset-password/reset-password.schema';
import { MY_ROUTE } from '@/helpers/router/route.constant';
import { getErrorMessage } from '@/utils/common.helper';
import { yupResolver } from '@hookform/resolvers/yup';
import { Button } from 'antd';
import { enqueueSnackbar } from 'notistack';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';

export type InputResetPasswordFormProps = {
  email: string;
  otp: string;
};

export default function ResetPasswordForm({ email, otp }: InputResetPasswordFormProps) {
  //i18n
  const { t: tLogin } = useTranslation('login');
  const { t: tForm } = useTranslation('form');

  // navigate
  const navigate = useNavigate();

  //handle form
  const { handleSubmit, control } = useForm<ResetPasswordInput>({
    resolver: yupResolver(resetPasswordSchema),
    defaultValues: resetPasswordSchema.getDefault(),
  });

  //handle api
  const [resetPassword, { isLoading: isResetPasswordLoading }] = useResetPasswordMutation();

  const onSubmit = (data: ResetPasswordInput) => {
    const verifyAuthCodeREQ: AuthResetPasswordREQ = {
      email: email,
      password: data.password,
      authCode: otp,
    };

    resetPassword(verifyAuthCodeREQ)
      .unwrap()
      .then(() => {
        enqueueSnackbar({
          message: tLogin('toast.password_change'),
          variant: 'success',
        });
        navigate(MY_ROUTE.FORGOT_PASSWORD.success);
      })
      .catch((error) => {
        const message = getErrorMessage(error);
        enqueueSnackbar({ message, variant: 'error' });
      });
  };

  return (
    <div className='flex w-full items-center justify-center bg-gray-50'>
      <div className='w-full max-w-lg rounded-2xl bg-white'>
        <div className='sm:mx-auto sm:w-full sm:max-w-sm'>
          <form onSubmit={handleSubmit(onSubmit)} className='flex flex-col gap-4'>
            <CustomTextFieldWithLabel
              name='password'
              control={control}
              label={tForm('label.new_password')}
              required
              placeholder={tForm('placeholder.confirm_password')}
              autoComplete='false'
              type='password'
            />
            <CustomTextFieldWithLabel
              name='confirmPassword'
              control={control}
              label={tForm('label.confirm_password')}
              required
              type='password'
              placeholder={tForm('placeholder.password')}
              autoComplete='false'
            />
            <div className='flex flex-col gap-4'>
              <Button
                htmlType='submit'
                className='w-full bg-primary-500 py-6 text-sm font-semibold text-white ease-linear'
                loading={isResetPasswordLoading}>
                {tLogin('button.reset_password')}
              </Button>
              <Button
                htmlType='button'
                className='w-full border-primary-500 bg-white py-6 text-primary-500 ease-linear'
                onClick={() => navigate(-1)}>
                {tLogin('button.back')}
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
