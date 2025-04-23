import InfoItem from '@/components/common/InfoItem';
import CustomDatePickerWithLabel from '@/components/form-related/CustomDatePickerWithLabel';
import CustomSelectWithLabel from '@/components/form-related/CustomSelectWithLabel';
import CustomTextFieldWithLabel from '@/components/form-related/CustomTextFieldWithLabel';
import { RootState } from '@/data';
import { GlobalState } from '@/data/global/global.slice';
import { UpdateUserInfoREQ } from '@/data/user/request/user-info.request';
import { usePutUpdateUserInfoMutation } from '@/data/user/user.api';
import { UpdateProfileInput } from '@/helpers/form-schemas/user/user.input';
import { updateProfileSchema } from '@/helpers/form-schemas/user/user.schema';
import { useAppSelector } from '@/hooks/reduxHook';
import DefaultContainer from '@/layouts/DefaultContainer';
import { DATE_FORMAT } from '@/utils/constants/date.constant';
import { GENDER } from '@/utils/enum/common.enum';
import { yupResolver } from '@hookform/resolvers/yup';
import { Button } from 'antd';
import dayjs from 'dayjs';
import { enqueueSnackbar } from 'notistack';
import { useEffect, useState } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';

export default function ProfilePage() {
  const { userInfo }: GlobalState = useAppSelector((state: RootState) => state.global);
  const [isUpdate, setIsUpdate] = useState(false);

  // handle form
  const { handleSubmit, control, setValue } = useForm<UpdateProfileInput>({
    resolver: yupResolver(updateProfileSchema),
    defaultValues: updateProfileSchema.getDefault(),
  });

  const [putUpdateInfo, { isLoading: isLoadingUpdateInfo }] = usePutUpdateUserInfoMutation();

  useEffect(() => {
    if (!userInfo) return;
    if (userInfo) {
      setValue('firstName', userInfo.firstName);
      setValue('lastName', userInfo.lastName);
      setValue('email', userInfo.email);
      setValue('phone', userInfo.phone);
      setValue('address', userInfo.address);
      setValue('dob', userInfo.dob);
      setValue('gender', userInfo.gender);
    }
  }, [setValue, userInfo]);

  const onSubmit: SubmitHandler<UpdateProfileInput> = async (data: UpdateProfileInput) => {
    const dataUpdateInfo: UpdateUserInfoREQ = {
      firstName: data.firstName,
      lastName: data.lastName,
      phone: data.phone,
      address: data.address,
      dob: dayjs(data.dob),
      gender: data.gender,
    };
    putUpdateInfo(dataUpdateInfo)
      .unwrap()
      .then(() => {
        enqueueSnackbar('Cập nhật thông tin thành công', {
          variant: 'success',
        });
        setIsUpdate(false);
      })
      .catch(() => {
        enqueueSnackbar('Cập nhật thông tin thất bại', {
          variant: 'error',
        });
      });
  };

  return (
    <DefaultContainer title={isUpdate ? 'Thông tin cá nhân' : 'Cập nhật thông tin cá nhân'}>
      <div className='flex max-w-screen-md flex-col justify-center rounded-lg bg-white p-6 py-8 shadow'>
        {isUpdate ? (
          <form>
            <CustomTextFieldWithLabel
              control={control}
              name='firstName'
              label='Họ và tên đệm'
              className='mb-4'
              required
              placeholder='Họ và tên đệm'
            />
            <CustomTextFieldWithLabel
              control={control}
              name='lastName'
              label='Tên'
              className='mb-4'
              required
              placeholder='Tên'
            />
            <CustomTextFieldWithLabel
              control={control}
              name='email'
              label='Email'
              className='mb-4'
              required
              placeholder='Email'
            />
            <CustomTextFieldWithLabel
              control={control}
              name='phone'
              label='Số điện thoại'
              className='mb-4'
              required
              placeholder='Số điện thoại'
            />
            <CustomTextFieldWithLabel
              control={control}
              name='address'
              label='Địa chỉ'
              className='mb-4'
              placeholder='Địa chỉ'
            />
            <CustomDatePickerWithLabel
              control={control}
              name='dob'
              label='Ngày sinh'
              className='mb-4'
              required
              placeholder='Ngày sinh'
              format={DATE_FORMAT}
              showTime={false}
              use12Hours={false}
            />
            <CustomSelectWithLabel
              name='gender'
              control={control}
              label='Giới tính'
              className='mb-4'
              placeholder='Giới tính'
              options={[
                {
                  label: 'Nam',
                  value: GENDER.MALE,
                },
                {
                  label: 'Nữ',
                  value: GENDER.FEMALE,
                },
              ]}
            />
            <div className='flex justify-end'>
              <Button
                className='mt-4 bg-primary-500 py-6 text-white ease-linear'
                onClick={handleSubmit(onSubmit)}
                loading={isLoadingUpdateInfo}>
                Cập nhật thông tin
              </Button>
            </div>
          </form>
        ) : (
          <>
            <div className='mb-5 flex flex-col'>
              <img src={userInfo?.userImageUrl} className='mb-4 h-32 w-32 shrink-0 rounded-full bg-gray-300' />
              <h1 className='text-xl font-bold'>{userInfo?.firstName + ' ' + userInfo?.lastName}</h1>
              <p className='text-gray-700'>{userInfo?.email}</p>
            </div>
            <div className='grid grid-cols-2 gap-6'>
              <InfoItem label='Name' value={userInfo?.firstName + ' ' + userInfo?.lastName} />
              <InfoItem label='Phone' value={userInfo?.phone} />
              <InfoItem label='Address' value={userInfo?.address} />
              <InfoItem label='Date of Birth' value={dayjs(userInfo?.dob).format(DATE_FORMAT)} />
              <InfoItem label='Gender' value={userInfo?.gender === GENDER.MALE ? 'Nam' : 'Nữ'} />
              <InfoItem
                label='Role'
                value={
                  userInfo?.roles
                    .map((role) => {
                      if (role === 'ROLE_ADMIN') {
                        return 'Admin';
                      }
                      if (role === 'ROLE_USER') {
                        return 'User';
                      }
                      if (role === 'ROLE_MODERATOR') {
                        return 'Moderator';
                      }
                      return role;
                    })
                    ?.join(' | ') ?? ''
                }
              />
            </div>
            <div className='flex justify-end'>
              <Button
                className='mt-4 bg-primary-500 py-6 text-white ease-linear'
                onClick={() => setIsUpdate((prev) => !prev)}>
                Cập nhật thông tin
              </Button>
            </div>
          </>
        )}
      </div>
    </DefaultContainer>
  );
}
