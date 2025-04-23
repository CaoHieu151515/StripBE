import CustomTextFieldWithLabel from '@/components/form-related/CustomTextFieldWithLabel';
import { RootState } from '@/data';
import { AuthResetPasswordREQ } from '@/data/auth/request/auth-reset-password.request';
import { GlobalState } from '@/data/global/global.slice';
import { usePutChangePasswordMutation } from '@/data/user/user.api';
import { ResetPasswordInput } from '@/helpers/form-schemas/reset-password/reset-password.input';
import { resetPasswordSchema } from '@/helpers/form-schemas/reset-password/reset-password.schema';
import { useAppSelector } from '@/hooks/reduxHook';
import DefaultContainer from '@/layouts/DefaultContainer';
import { yupResolver } from '@hookform/resolvers/yup';
import { Button } from 'antd';
import { enqueueSnackbar } from 'notistack';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';

export default function ChangePasswordPage() {
  const { t: tCommon } = useTranslation('common');
  const { t: tLogin } = useTranslation('login');

  const { userInfo }: GlobalState = useAppSelector((state: RootState) => state.global);

  const navigate = useNavigate();

  // api
  const [putChangePassword, { isLoading: isLoadingPutChangePassword }] = usePutChangePasswordMutation();

  //handle form
  const { handleSubmit, control, reset } = useForm<ResetPasswordInput>({
    resolver: yupResolver(resetPasswordSchema),
    defaultValues: resetPasswordSchema.getDefault(),
  });

  //handle submit
  const onSubmit: SubmitHandler<ResetPasswordInput> = async (data) => {
    const editAdminPasswordREQ: AuthResetPasswordREQ = {
      currentPassword: data.password,
      newPassword: data.newPassword,
    };
    putChangePassword(editAdminPasswordREQ)
      .unwrap()
      .then(() => {
        enqueueSnackbar({
          message: 'Đổi mật khẩu thành công',
          variant: 'success',
        });
        reset();
      })
      .catch(() => {
        enqueueSnackbar({
          message: 'Đổi mật khẩu thất bại',
          variant: 'error',
        });
      });
  };

  return (
    <DefaultContainer title={tLogin('text.change_password')} className='bg-white'>
      <div className='text-left text-black'>
        <p className='text-base font-normal text-gray-400'>{tLogin('text.reset_password_description')}</p>
      </div>
      <div className='w-full max-w-lg'>
        <form onSubmit={handleSubmit(onSubmit)} className='flex flex-col gap-4'>
          <CustomTextFieldWithLabel
            name='password'
            control={control}
            label={'Mật khẩu hiện tại'}
            required
            type='password'
            placeholder={'Mật khẩu hiện tại'}
            autoComplete='false'
          />
          <CustomTextFieldWithLabel
            name='newPassword'
            control={control}
            label={'Mật khẩu mới'}
            required
            type='password'
            placeholder={'Mật khẩu mới'}
            autoComplete='false'
          />
          <CustomTextFieldWithLabel
            name='confirmPassword'
            control={control}
            label={'Xác nhận mật khẩu mới'}
            required
            type='password'
            placeholder={'Xác nhận mật khẩu mới'}
            autoComplete='false'
          />
          <Button
            type='primary'
            htmlType='submit'
            className='mt-5 w-full py-6 text-base font-semibold'
            loading={isLoadingPutChangePassword}>
            {tCommon('button.change_password')}
          </Button>
        </form>
      </div>
    </DefaultContainer>
  );
}
