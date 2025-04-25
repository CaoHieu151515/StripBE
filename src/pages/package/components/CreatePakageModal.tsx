import CustomTextFieldWithLabel from '@/components/form-related/CustomTextFieldWithLabel';
import CustomModal from '@/components/modal/CustomModal';
import { packageApi } from '@/data/services/api/package/package.api';
import queryClient from '@/data/services/queryClient';
import { CreatePackageInput } from '@/helpers/form-schemas/package/package.input';
import { createPackageSchema } from '@/helpers/form-schemas/package/package.schema';
import { yupResolver } from '@hookform/resolvers/yup';
import { Button, message } from 'antd';
import { SubmitHandler, useForm } from 'react-hook-form';

type CreatePakageModalProps = {
  open: boolean;
  setOpen: (open: boolean) => void;
};

export default function CreatePakageModal({ open, setOpen }: CreatePakageModalProps) {
  // const { handleSubmit, control } = useForm<CreatePackageInput>({
  //   resolver: yupResolver(createPackageSchema),
  //   defaultValues: createPackageSchema.getDefault(),
  // });

  const { handleSubmit, control, reset } = useForm<CreatePackageInput>({
    resolver: yupResolver(createPackageSchema),
    defaultValues: createPackageSchema.getDefault(),
  });

  const onSubmit: SubmitHandler<CreatePackageInput> = async (data) => {
    try {
      await packageApi.create({
        name: data.packageName,
        price: data.packagePrice,
        time: data.packageTime,
        bonus: data.packageBonusTime,
        description: data.packageDescription || '',
      });
      message.success('Tạo gói thành công!');
      setOpen(false);
      reset();
      queryClient.invalidateQueries(['packages']);
    } catch (error) {
      message.error('Tạo gói thất bại!');
      console.error(error);
    }
  };

  return (
    <CustomModal
      title='Tạo Gói'
      open={open}
      className='!w-[520px]'
      setOpen={setOpen}
      footer={
        <div className='mt-6 flex justify-end gap-3'>
          <Button onClick={() => setOpen(false)}>Cancel</Button>
          <Button type='primary' className='border-none' onClick={handleSubmit(onSubmit)}>
            Create
          </Button>
        </div>
      }>
      <form className='mt-10 space-y-4'>
        <CustomTextFieldWithLabel
          control={control}
          name='packageName'
          label='Tên Gói'
          placeholder='Enter package name'
          className='w-full'
        />
        <CustomTextFieldWithLabel
          control={control}
          name='packageTime'
          label='Package Time (Month)'
          placeholder='Enter package time'
          type='number'
        />
        <CustomTextFieldWithLabel
          control={control}
          name='packageBonusTime'
          label='Package Bonus Time (Month)'
          placeholder='Enter package bonus time'
          type='number'
        />
        <CustomTextFieldWithLabel
          control={control}
          name='packagePrice'
          label='Giá Gói (VND)'
          placeholder='Enter package price'
          type='number'
          min={1000}
          max={1000000000}
          step={1000}
        />
      </form>
    </CustomModal>
  );
}
