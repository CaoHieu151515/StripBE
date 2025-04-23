import CustomSearchInputQueryWithLabel from '@/components/form-related/CustomSearchInputQueryWithLabel';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';

export default function UserAccountFilter() {
  return (
    <div>
      <div className='flex flex-wrap items-center justify-start gap-5'>
        <CustomSearchInputQueryWithLabel label='Họ' searchParamName={PARAM_FIELD.FIRST_NAME} placeholder='Nhập họ' />
        <CustomSearchInputQueryWithLabel label='Tên' searchParamName={PARAM_FIELD.LAST_NAME} placeholder='Nhập tên' />
        {/* <CustomSearchInputQueryWithLabel label={'Full Name'} searchParamName={PARAM_FIELD.FULL_NAME} />
        <CustomSearchInputQueryWithLabel label={'Phone'} searchParamName={PARAM_FIELD.PHONE} />
        <CustomSearchInputQueryWithLabel label={'Email'} searchParamName={PARAM_FIELD.EMAIL} /> */}
        {/* <CustomSelectQueryWithLabel
          label={'Gender'}
          queryKey={PARAM_FIELD.SEARCH_BY}
          options={[
            {
              label: 'Male',
              value: GENDER.MALE,
            },
            {
              label: 'Female',
              value: GENDER.FEMALE,
            },
          ]}
          className='w-40'
        />
        <CustomSelectQueryWithLabel
          label={'Status'}
          queryKey={PARAM_FIELD.SEARCH_BY}
          options={[
            {
              label: 'Active',
              value: USER_ACCOUNT_STATUS.ACTIVE,
            },
            {
              label: 'Inactive',
              value: USER_ACCOUNT_STATUS.INACTIVE,
            },
          ]}
          className='w-40'
        />
        <CustomSelectQueryWithLabel
          label={'Role'}
          queryKey={PARAM_FIELD.SEARCH_BY}
          options={[
            {
              label: 'Driver',
              value: 'DRIVER',
            },
            {
              label: 'Passenger',
              value: 'PASSENGER',
            },
          ]}
          className='w-40'
        /> */}
      </div>
    </div>
  );
}
