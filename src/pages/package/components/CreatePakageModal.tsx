import CustomTextFieldWithLabel from '@/components/form-related/CustomTextFieldWithLabel';
import CustomModal from '@/components/modal/CustomModal';
import { CreatePackageInput } from '@/helpers/form-schemas/package/package.input';
import { createPackageSchema } from '@/helpers/form-schemas/package/package.schema';
import { yupResolver } from '@hookform/resolvers/yup';
import { Button } from 'antd';
import { SubmitHandler, useForm } from 'react-hook-form';

type CreatePakageModalProps = {
  open: boolean;
  setOpen: (open: boolean) => void;
};

export default function CreatePakageModal({ open, setOpen }: CreatePakageModalProps) {
  const { handleSubmit, control } = useForm<CreatePackageInput>({
    resolver: yupResolver(createPackageSchema),
    defaultValues: createPackageSchema.getDefault(),
  });

  const onSubmit: SubmitHandler<CreatePackageInput> = (data) => {
    console.log(data);
  };

  return (
    <CustomModal
      title='Create Package'
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
          label='Package Name'
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
          label='Package Price (VND)'
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
