import CustomTextFieldWithLabel from '@/components/form-related/CustomTextFieldWithLabel';
import { useSendAuthCodeMutation } from '@/data/auth/auth.api';
import { ForgotPasswordInput } from '@/helpers/form-schemas/reset-password/forgot-password.input';
import { forgotPasswordSchema } from '@/helpers/form-schemas/reset-password/forgot-password.schema';
import { AUTH_CODE_TYPE } from '@/utils/enum/auth/auth-code-type.enum';
import { yupResolver } from '@hookform/resolvers/yup';
import { Button } from 'antd';
import { enqueueSnackbar } from 'notistack';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import InputOTPCode from './components/InputOTPCode';
import ResetPasswordForm from './components/ResetPasswordForm';

export default function ForgotPasswordPage() {
  //i18n
  const { t: tLogin } = useTranslation('login');
  const { t: tForm } = useTranslation('form');

  const navigate = useNavigate();

  // apis
  const [sendAuthCode, { isLoading: isLoadingForgotPassword }] = useSendAuthCodeMutation();

  //handle form
  const { handleSubmit, control } = useForm<ForgotPasswordInput>({
    resolver: yupResolver(forgotPasswordSchema),
    defaultValues: forgotPasswordSchema.getDefault(),
  });

  // state
  const [step, setStep] = useState<'input-email' | 'input-otp' | 'reset-password'>('input-email');
  const [email, setEmail] = useState('');
  const [otp, setOtp] = useState('');

  const onSubmit = (data: ForgotPasswordInput) => {
    sendAuthCode({ email: data.email, authCodeType: AUTH_CODE_TYPE.RESET_PASSWORD })
      .unwrap()
      .then(() => {
        setEmail(data.email);
        enqueueSnackbar({
          message: tLogin('text.email_sent'),
          variant: 'success',
        });
        setStep('input-otp');
      })
      .catch(() => {
        enqueueSnackbar({
          message: tLogin('toast.bad_credential'),
          variant: 'error',
        });
      });
  };

  const handleOTPVerificationSuccess = (otpValue: string) => {
    setOtp(otpValue);
    setStep('reset-password');
  };

  return (
    <div className='flex h-screen w-full items-center justify-center bg-[url("/src/assets/images/background.png")] bg-cover bg-center bg-no-repeat'>
      <div className='w-full max-w-lg rounded-2xl bg-white p-8 shadow'>
        <h2 className='text-center text-2xl font-bold uppercase text-black'>
          {step === 'reset-password'
            ? tLogin('text.reset_password')
            : step === 'input-otp'
              ? tLogin('text.verify_password')
              : tLogin('text.forgot_password')}
        </h2>
        <div className='mt-10 sm:mx-auto sm:w-full sm:max-w-sm'>
          {step === 'reset-password' ? (
            <ResetPasswordForm email={email} otp={otp} />
          ) : step === 'input-otp' ? (
            <InputOTPCode email={email} onSuccess={handleOTPVerificationSuccess} />
          ) : (
            <form className='flex flex-col gap-4' onSubmit={handleSubmit(onSubmit)}>
              <CustomTextFieldWithLabel
                name='email'
                control={control}
                label={tForm('label.email')}
                required
                placeholder={tForm('placeholder.email')}
              />
              <div className='flex flex-col gap-4'>
                <Button
                  htmlType='submit'
                  className='w-full bg-[#F18D45CF] py-6 text-sm font-semibold text-white ease-linear'
                  loading={isLoadingForgotPassword}>
                  {tLogin('button.send_email')}
                </Button>
                <Button
                  htmlType='button'
                  className='w-full border-[#F18D45CF] bg-white py-6 text-primary-500 ease-linear'
                  onClick={() => navigate(-1)}>
                  {tLogin('button.back')}
                </Button>
              </div>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}
