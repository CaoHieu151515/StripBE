import CustomTextFieldWithLabel from '@/components/form-related/CustomTextFieldWithLabel';
import { RootState } from '@/data';
import { GlobalState } from '@/data/global/global.slice';
import { ResetPasswordInput } from '@/helpers/form-schemas/reset-password/reset-password.input';
import { resetPasswordSchema } from '@/helpers/form-schemas/reset-password/reset-password.schema';
import { useAppSelector } from '@/hooks/reduxHook';
import DefaultContainer from '@/layouts/DefaultContainer';
import { yupResolver } from '@hookform/resolvers/yup';
import { Button } from 'antd';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';

export default function ChangePasswordPage() {
  const { t: tCommon } = useTranslation('common');
  const { t: tForm } = useTranslation('form');
  const { t: tLogin } = useTranslation('login');

  const { userInfo }: GlobalState = useAppSelector((state: RootState) => state.global);

  const navigate = useNavigate();

  //handle form
  const { handleSubmit, control } = useForm<ResetPasswordInput>({
    resolver: yupResolver(resetPasswordSchema),
    defaultValues: resetPasswordSchema.getDefault(),
  });

  //handle submit
  const onSubmit: SubmitHandler<ResetPasswordInput> = async (data) => {
    const editAdminPasswordREQ = {
      password: data.password,
    };
    console.log('editAdminPasswordREQ', editAdminPasswordREQ);
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
            label={tForm('label.new_password')}
            required
            type='password'
            placeholder={tForm('placeholder.new_password')}
            autoComplete='false'
          />
          <CustomTextFieldWithLabel
            name='confirmPassword'
            control={control}
            label={tForm('label.confirm_password')}
            required
            type='password'
            placeholder={tForm('placeholder.confirm_password')}
            autoComplete='false'
          />
          <Button type='primary' htmlType='submit' className='mt-5 w-full py-6 text-base font-semibold' loading={false}>
            {tCommon('button.change_password')}
          </Button>
        </form>
      </div>
    </DefaultContainer>
  );
}
