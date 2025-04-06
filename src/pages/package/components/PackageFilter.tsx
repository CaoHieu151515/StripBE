import CustomSearchInputQueryWithLabel from '@/components/form-related/CustomSearchInputQueryWithLabel';
import CustomSelectQueryWithLabel from '@/components/form-related/CustomSelectQueryWithLabel';
import CirclePlusIcon from '@/components/icons/CirclePlusIcon';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { Button } from 'antd';
import { useState } from 'react';
import CreatePakageModal from './CreatePakageModal';

export default function PackageFilter() {
  const [isModalCreatePackageOpen, setIsModalCreatePackageOpen] = useState(false);

  return (
    <>
      <div className='flex items-center justify-between gap-5'>
        <div className='flex flex-wrap items-center justify-start gap-5'>
          <CustomSearchInputQueryWithLabel label={'Search'} searchParamName={PARAM_FIELD.SEARCH_KEYWORD} />
          <CustomSelectQueryWithLabel
            label={'Search by'}
            queryKey={PARAM_FIELD.SEARCH_BY}
            options={[
              {
                label: 'ID',
                value: 'id',
              },
              {
                label: 'Package Name',
                value: 'packageName',
              },
              {
                label: 'Package Price',
                value: 'packagePrice',
              },
            ]}
            className='w-40'
          />
          <CustomSelectQueryWithLabel
            label={'Package Time'}
            queryKey={'packageTime'}
            options={[
              {
                label: '1 month',
                value: '1',
              },
              {
                label: '3 months',
                value: '3',
              },
              {
                label: '6 months',
                value: '6',
              },
              {
                label: '1 year',
                value: '12',
              },
            ]}
            className='w-40'
          />
          <CustomSelectQueryWithLabel
            label={'Bonus Time'}
            queryKey={'packageBonusTime'}
            options={[
              {
                label: '1 month',
                value: '1',
              },
              {
                label: '3 months',
                value: '3',
              },
              {
                label: '6 months',
                value: '6',
              },
              {
                label: '1 year',
                value: '12',
              },
            ]}
            className='w-40'
          />
        </div>
        <Button
          type='primary'
          icon={<CirclePlusIcon className='size-5 text-white' />}
          className='flex items-center gap-2'
          onClick={() => setIsModalCreatePackageOpen(true)}>
          Create Package
        </Button>
      </div>
      {isModalCreatePackageOpen && (
        <CreatePakageModal open={isModalCreatePackageOpen} setOpen={setIsModalCreatePackageOpen} />
      )}
    </>
  );
}
