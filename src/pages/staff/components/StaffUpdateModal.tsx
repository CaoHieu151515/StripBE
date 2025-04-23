import CustomDatePickerWithLabel from '@/components/form-related/CustomDatePickerWithLabel';
import CustomSelectWithLabel from '@/components/form-related/CustomSelectWithLabel';
import CustomTextFieldWithLabel from '@/components/form-related/CustomTextFieldWithLabel';
import CustomModal from '@/components/modal/CustomModal';
import { StaffDTO } from '@/data/staff/dto/staff.dto';
import { StaffUpdateREQ } from '@/data/staff/request/staff.request';
import { usePutUpdateStaffMutation } from '@/data/staff/staff.api';
import { StaffInput } from '@/helpers/form-schemas/staff/staff.input';
import { staffSchema } from '@/helpers/form-schemas/staff/staff.schema';
import { DATE_FORMAT } from '@/utils/constants/date.constant';
import { GENDER } from '@/utils/enum/common.enum';
import { yupResolver } from '@hookform/resolvers/yup';
import dayjs from 'dayjs';
import { enqueueSnackbar } from 'notistack';
import { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';

type StaffUpdateModalProps = {
  open: boolean;
  setOpen: (open: boolean) => void;
  data: StaffDTO;
};

export const StaffUpdateModal = ({ open, setOpen, data }: StaffUpdateModalProps) => {
  const { handleSubmit, control, reset, setValue } = useForm<StaffInput>({
    resolver: yupResolver(staffSchema),
    defaultValues: staffSchema.getDefault(),
  });

  const [putUpdateStaff, { isLoading: isLoadingUpdateStaff }] = usePutUpdateStaffMutation();

  useEffect(() => {
    if (!data) return;
    if (data) {
      setValue('firstName', data.firstName);
      setValue('lastName', data.lastName);
      setValue('phone', data.phone);
      setValue('email', data.email);
      setValue('address', data.address);
      setValue('dob', data.dob);
      setValue('gender', data.gender);
    }

    return () => reset();
  }, [data, reset, setValue]);

  const onSubmit: SubmitHandler<StaffInput> = async (dataInput: StaffInput) => {
    const dataUpdateInfo: StaffUpdateREQ = {
      userId: data.userId,
      firstName: dataInput.firstName,
      lastName: dataInput.lastName,
      email: dataInput.email,
      phone: dataInput.phone,
      address: dataInput.address,
      dob: dayjs(dataInput.dob),
      gender: dataInput.gender,
    };

    putUpdateStaff(dataUpdateInfo)
      .unwrap()
      .then(() => {
        enqueueSnackbar('Cập nhật thông tin thành công', {
          variant: 'success',
        });
        setOpen(false);
      })
      .catch(() => {
        enqueueSnackbar('Cập nhật thông tin thất bại', {
          variant: 'error',
        });
      });
  };

  return (
    <CustomModal
      title='Cập nhật thông tin nhân viên'
      open={open}
      setOpen={setOpen}
      className='!w-[850px]'
      onConfirm={handleSubmit(onSubmit)}
      loading={isLoadingUpdateStaff}
      okText='Cập nhật'>
      <form className='mt-10 space-y-4'>
        <div className='grid grid-cols-2 gap-4'>
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
            name='email'
            label='Email'
            className='mb-4'
            required
            placeholder='Email'
          />
        </div>
        <CustomTextFieldWithLabel
          control={control}
          name='address'
          label='Địa chỉ'
          className='mb-4'
          placeholder='Địa chỉ'
        />
      </form>
    </CustomModal>
  );
};
