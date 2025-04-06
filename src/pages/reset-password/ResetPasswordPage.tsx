import CustomTextFieldWithLabel from '@/components/form-related/CustomTextFieldWithLabel';
import { usePutAdminPasswordMutation } from '@/data/admin/admin.api';
import { PutAdminPasswordREQ } from '@/data/admin/request/admin.request';
import { ResetPasswordInput } from '@/helpers/form-schemas/reset-password/reset-password.input';
import { resetPasswordSchema } from '@/helpers/form-schemas/reset-password/reset-password.schema';
import { MY_ROUTE } from '@/helpers/router/route.constant';
import DefaultContainer from '@/layouts/DefaultContainer';
import { yupResolver } from '@hookform/resolvers/yup';
import { Button } from 'antd';
import { enqueueSnackbar } from 'notistack';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate, useParams } from 'react-router-dom';

export default function ResetPasswordPage() {
  const { t: tCommon } = useTranslation('common');
  const { t: tForm } = useTranslation('form');
  const { t: tLogin } = useTranslation('login');
  const { t: tAdmin } = useTranslation('admin');
  const { id } = useParams();

  const navigate = useNavigate();

  //handle form
  const { handleSubmit, control } = useForm<ResetPasswordInput>({
    resolver: yupResolver(resetPasswordSchema),
    defaultValues: resetPasswordSchema.getDefault(),
  });

  const [putAdminPassword, { isLoading: isLoadingPutAdminPassword }] = usePutAdminPasswordMutation();

  //handle submit
  const onSubmit: SubmitHandler<ResetPasswordInput> = async (data) => {
    const editAdminPasswordREQ: PutAdminPasswordREQ = {
      password: data.password,
    };
    putAdminPassword({ id: Number(id), data: editAdminPasswordREQ })
      .unwrap()
      .then(() => {
        enqueueSnackbar({
          message: tAdmin('toast.admin_updated_success'),
          variant: 'success',
        });
        navigate(MY_ROUTE.ADMINISTRATOR_ACCOUNT.self);
      })
      .catch(() => {
        enqueueSnackbar({
          message: tAdmin('toast.admin_updated_fail'),
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
          <Button
            type='primary'
            htmlType='submit'
            className='mt-5 w-full py-6 text-base font-semibold'
            loading={isLoadingPutAdminPassword}>
            {tCommon('button.change_password')}
          </Button>
        </form>
      </div>
    </DefaultContainer>
  );
}
