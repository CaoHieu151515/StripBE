import { AuthVerifyPasswordREQ } from '@/data/auth/request/auth-verify-password.request';
import { Button } from 'antd';
import { enqueueSnackbar } from 'notistack';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import OTPInput from 'react-otp-input';
import { useNavigate } from 'react-router-dom';
import { twMerge } from 'tailwind-merge';

const VALIDATION_CODE_OTP_LENGTH = 6;

export type InputOTPCodeInputProps = {
  email: string;
  onSuccess: (otp: string) => void;
};
export default function InputOTPCode({ email, onSuccess }: InputOTPCodeInputProps) {
  const { t: tLogin } = useTranslation('login');

  const navigate = useNavigate();

  // api
  // const [verifyAuthCode, { isLoading: isVerifyAuthCodeLoading }] = useVerifyAuthCodeMutation();

  const [otp, setOtp] = useState('');

  const onSubmit = () => {
    if (otp.length !== VALIDATION_CODE_OTP_LENGTH || !/^\d+$/.test(otp)) {
      enqueueSnackbar({
        message: tLogin('toast.otp_digit'),
        variant: 'error',
      });
      return;
    }

    const verifyAuthCodeREQ: AuthVerifyPasswordREQ = {
      email: email,
      authCode: otp,
    };
    // verifyAuthCode(verifyAuthCodeREQ)
    //   .unwrap()
    //   .then(() => {
    //     enqueueSnackbar({
    //       message: tLogin('toast.otp_verified_successfully'),
    //       variant: 'success',
    //     });
    //     onSuccess(otp);
    //   })
    //   .catch(() => {
    //     enqueueSnackbar({
    //       message: tLogin('toast.invalid_otp'),
    //       variant: 'error',
    //     });
    //   });
  };

  return (
    <div className='flex flex-col items-center justify-center'>
      <div className='mb-5 flex flex-col items-center gap-3'>
        <label className='mb-1 block text-sm text-gray-900' htmlFor='otp-0'>
          Please Input OTP Code from your email
        </label>
        <OTPInput
          value={otp}
          onChange={(value: string) => setOtp(value)}
          numInputs={VALIDATION_CODE_OTP_LENGTH}
          inputType='tel'
          renderInput={(props, index) => {
            return (
              <input
                {...props}
                id={`otp-${index}`}
                style={undefined}
                className={twMerge(
                  index !== VALIDATION_CODE_OTP_LENGTH - 1 && 'mr-2.5 max-[440px]:mr-2',
                  'otp-container relative flex w-full items-center gap-2.5 rounded-lg border border-gray-300 bg-gray-50 p-4 ps-4 text-center text-sm font-semibold focus:border-primary-500 focus:ring-primary-500 dark:border-gray-600 dark:text-primary-700 dark:placeholder-gray-400 dark:focus:border-primary-500 dark:focus:ring-primary-500',
                )}
              />
            );
          }}
        />
      </div>
      <Button
        htmlType='submit'
        className='mt-4 w-full bg-primary-500 py-6 text-white ease-linear'
        onClick={onSubmit}
        loading={false}>
        {tLogin('button.confirm')}
      </Button>
      <Button
        htmlType='button'
        className='mt-4 w-full border-primary-500 bg-white py-6 text-primary-500 ease-linear'
        onClick={() => navigate(-1)}>
        {tLogin('button.back')}
      </Button>
    </div>
  );
}
